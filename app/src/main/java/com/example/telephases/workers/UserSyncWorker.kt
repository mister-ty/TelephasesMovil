package com.example.telephases.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.Data
import com.example.telephases.data.repository.AuthRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Worker dedicado exclusivamente a la sincronización de usuarios
 * Se ejecuta periódicamente cada 15-30 minutos
 */
class UserSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val tag = "UserSyncWorker"
    
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DependenciesEntryPoint {
        fun authRepository(): AuthRepository
    }
    
    private val deps: DependenciesEntryPoint by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            DependenciesEntryPoint::class.java
        )
    }

    companion object {
        const val WORK_NAME = "user_sync_work"
        const val KEY_FORCE_SYNC = "force_sync"
    }

    override suspend fun doWork(): Result {
        Log.d(tag, "👥 Iniciando sincronización de usuarios en background...")
        
        try {
            // Verificar si hay token válido
            val token = deps.authRepository().getCurrentToken()
            if (token == null) {
                Log.w(tag, "⚠️ Sin token válido - saltando sincronización de usuarios")
                return Result.success(createOutputData(false, "Sin token válido"))
            }
            
            // Verificar que no sea token offline
            // TEMPORALMENTE DESHABILITADO PARA FORZAR SINCRONIZACIÓN
            /*if (token.startsWith("OFFLINE_TOKEN") || token.startsWith("TEST_TOKEN_")) {
                Log.w(tag, "⚠️ Token offline detectado - saltando sincronización")
                return Result.success(createOutputData(false, "Token offline"))
            }*/
            Log.d(tag, "✅ Token detectado (validación offline deshabilitada): ${token.take(20)}...")
            
            // Verificar conectividad
            if (!deps.authRepository().isNetworkAvailable()) {
                Log.w(tag, "⚠️ Sin conectividad - saltando sincronización")
                return Result.retry()
            }

            val forceSync = inputData.getBoolean(KEY_FORCE_SYNC, false)
            Log.d(tag, "📊 Forzar sincronización: $forceSync")

            // Obtener usuarios pendientes
            val unsyncedUsers = deps.authRepository().getUnsyncedUsers()
            Log.d(tag, "📊 Usuarios pendientes de sincronización: ${unsyncedUsers.size}")
            
            if (unsyncedUsers.isEmpty() && !forceSync) {
                Log.d(tag, "✅ No hay usuarios pendientes de sincronización")
                return Result.success(createOutputData(
                    success = true,
                    message = "No hay usuarios pendientes",
                    syncedItems = 0
                ))
            }

            // Ejecutar sincronización
            Log.d(tag, "🔄 Sincronizando ${unsyncedUsers.size} usuarios...")
            val startTime = System.currentTimeMillis()
            
            val syncResult = deps.authRepository().syncUsers()
            
            val endTime = System.currentTimeMillis()
            val totalTime = endTime - startTime
            
            // Procesar resultado
            if (syncResult.isSuccess) {
                val result = syncResult.getOrThrow()
                Log.d(tag, "✅ Sincronización de usuarios exitosa:")
                Log.d(tag, "   📤 Sincronizados: ${result.syncedCount}")
                Log.d(tag, "   ❌ Fallidos: ${result.failedCount}")
                Log.d(tag, "   ⏱️ Tiempo: ${totalTime}ms")
                
                return Result.success(createOutputData(
                    success = true,
                    message = "Sincronizados ${result.syncedCount} usuarios",
                    syncedItems = result.syncedCount,
                    totalTime = totalTime
                ))
            } else {
                Log.w(tag, "⚠️ Sincronización de usuarios con errores")
                
                return Result.retry()
            }

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización de usuarios", e)
            return Result.retry()
        }
    }
    
    /**
     * Crea datos de salida para el resultado del Worker
     */
    private fun createOutputData(
        success: Boolean,
        message: String,
        syncedItems: Int = 0,
        totalTime: Long = 0
    ): Data {
        return Data.Builder()
            .putBoolean("success", success)
            .putString("message", message)
            .putInt("synced_items", syncedItems)
            .putLong("total_time", totalTime)
            .putLong("timestamp", System.currentTimeMillis())
            .build()
    }
}

