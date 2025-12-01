package com.example.telephases.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.Data
import com.example.telephases.data.repository.SyncManager
import com.example.telephases.data.repository.AuthRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Worker para sincronización en background
 * Ejecuta sincronización automática cuando hay conectividad
 */
class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val tag = "SyncWorker"
    
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DependenciesEntryPoint {
        fun syncManager(): SyncManager
        fun authRepository(): AuthRepository
    }
    
    private val deps: DependenciesEntryPoint by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            DependenciesEntryPoint::class.java
        )
    }

    companion object {
        const val WORK_NAME = "sync_work"
        const val KEY_SYNC_TYPE = "sync_type"
        const val KEY_FORCE_SYNC = "force_sync"
        
        // Tipos de sincronización
        const val SYNC_TYPE_FULL = "full"
        const val SYNC_TYPE_PENDING_ONLY = "pending_only"
        const val SYNC_TYPE_FROM_SERVER = "from_server"
    }

    override suspend fun doWork(): Result {
        Log.d(tag, "🔄 Iniciando trabajo de sincronización...")

        try {
            // Verificar si hay sesión válida
            val token = deps.authRepository().getCurrentToken()
            if (token == null) {
                Log.w(tag, "⚠️ Sin token válido - saltando sincronización")
                return Result.success(createOutputData(false, "Sin token válido"))
            }

            // Obtener tipo de sincronización
            val syncType = inputData.getString(KEY_SYNC_TYPE) ?: SYNC_TYPE_PENDING_ONLY
            val forceSync = inputData.getBoolean(KEY_FORCE_SYNC, false)

            Log.d(tag, "📊 Tipo de sincronización: $syncType, Forzar: $forceSync")

            // Ejecutar sincronización según el tipo
            val syncResult = when (syncType) {
                SYNC_TYPE_FULL -> {
                    Log.d(tag, "🔄 Ejecutando sincronización completa...")
                    deps.syncManager().syncAll(token)
                }
                SYNC_TYPE_FROM_SERVER -> {
                    Log.d(tag, "🔄 Descargando desde servidor...")
                    deps.syncManager().forceSyncFromServer(token)
                }
                else -> {
                    Log.d(tag, "🔄 Sincronizando datos pendientes...")
                    deps.syncManager().syncPendingOnly(token)
                }
            }

            // Procesar resultado
            if (syncResult.success) {
                Log.d(tag, "✅ Sincronización exitosa: ${syncResult.totalSyncedItems} elementos en ${syncResult.totalTime}ms")
                
                return Result.success(createOutputData(
                    success = true,
                    message = "Sincronizados ${syncResult.totalSyncedItems} elementos",
                    syncedItems = syncResult.totalSyncedItems,
                    totalTime = syncResult.totalTime
                ))
            } else {
                Log.w(tag, "⚠️ Sincronización con errores: ${syncResult.errors}")
                
                return if (syncResult.hasPartialSuccess) {
                    Result.success(createOutputData(
                        success = false,
                        message = "Sincronización parcial: ${syncResult.errors.size} errores",
                        syncedItems = syncResult.totalSyncedItems,
                        errors = syncResult.errors.joinToString(", ")
                    ))
                } else {
                    Result.retry()
                }
            }

        } catch (e: Exception) {
            Log.e(tag, "❌ Error crítico en sincronización", e)
            
            return Result.failure(createOutputData(
                success = false,
                message = "Error: ${e.message}",
                errors = e.message ?: "Error desconocido"
            ))
        }
    }


    /**
     * Crea datos de salida del worker
     */
    private fun createOutputData(
        success: Boolean,
        message: String,
        syncedItems: Int = 0,
        totalTime: Long = 0,
        errors: String = ""
    ): Data {
        return Data.Builder()
            .putBoolean("success", success)
            .putString("message", message)
            .putInt("synced_items", syncedItems)
            .putLong("total_time", totalTime)
            .putString("errors", errors)
            .putLong("timestamp", System.currentTimeMillis())
            .build()
    }
}


