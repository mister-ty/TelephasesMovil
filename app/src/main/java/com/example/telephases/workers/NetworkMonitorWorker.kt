package com.example.telephases.workers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.telephases.data.repository.SyncManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Worker para monitoreo de conectividad de red
 * Dispara sincronización automática cuando se detecta conectividad
 */
class NetworkMonitorWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val tag = "NetworkMonitorWorker"
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SyncManagerEntryPoint {
        fun syncManager(): SyncManager
    }

    private val syncManager: SyncManager by lazy {
        val ep = EntryPointAccessors.fromApplication(
            applicationContext,
            SyncManagerEntryPoint::class.java
        )
        ep.syncManager()
    }

    companion object {
        const val WORK_NAME = "network_monitor_work"
    }

    override suspend fun doWork(): Result {
        Log.d(tag, "📡 Iniciando monitoreo de red...")

        try {
            // Verificar estado actual de red
            val isConnected = isNetworkAvailable()
            
            // Actualizar estado en SyncManager
            syncManager.updateNetworkStatus(isConnected)
            
            Log.d(tag, "📊 Estado de red: ${if (isConnected) "CONECTADO" else "DESCONECTADO"}")

            if (isConnected) {
                // Si hay conexión, verificar si hay datos pendientes
                val syncStats = syncManager.getSyncStats()
                
                // DEBUG: Log detallado de las estadísticas
                Log.d(tag, "🔍 DEBUG - Estadísticas de sincronización:")
                Log.d(tag, "  - pendingPatients: ${syncStats.pendingPatients}")
                Log.d(tag, "  - pendingExams: ${syncStats.pendingExams}")
                Log.d(tag, "  - pendingUsers: ${syncStats.pendingUsers}")
                Log.d(tag, "  - totalPendingItems: ${syncStats.totalPendingItems}")
                Log.d(tag, "  - needsSync: ${syncStats.needsSync}")
                Log.d(tag, "  - autoSyncEnabled: ${syncStats.autoSyncEnabled}")
                Log.d(tag, "  - hasValidToken: ${syncStats.hasValidToken}")
                
                if (syncStats.needsSync && syncStats.autoSyncEnabled) {
                    Log.d(tag, "🔄 Conectividad detectada con datos pendientes - programando sincronización")
                    
                    // Programar sincronización automática
                    val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
                        .setInputData(
                            Data.Builder()
                                .putString(SyncWorker.KEY_SYNC_TYPE, SyncWorker.SYNC_TYPE_PENDING_ONLY)
                                .putBoolean(SyncWorker.KEY_FORCE_SYNC, false)
                                .build()
                        )
                        .addTag("auto_sync")
                        .build()

                    WorkManager.getInstance(applicationContext)
                        .enqueue(syncRequest)

                    return Result.success(createOutputData(
                        networkAvailable = true,
                        syncTriggered = true,
                        pendingItems = syncStats.totalPendingItems
                    ))
                } else {
                    Log.d(tag, "ℹ️ Conectividad detectada pero sin datos pendientes o auto-sync deshabilitado")
                    return Result.success(createOutputData(
                        networkAvailable = true,
                        syncTriggered = false,
                        pendingItems = syncStats.totalPendingItems
                    ))
                }
            } else {
                Log.d(tag, "📴 Sin conectividad de red")
                return Result.success(createOutputData(
                    networkAvailable = false,
                    syncTriggered = false,
                    pendingItems = 0
                ))
            }

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en monitoreo de red", e)
            return Result.failure(createOutputData(
                networkAvailable = false,
                syncTriggered = false,
                pendingItems = 0,
                error = e.message ?: "Error desconocido"
            ))
        }
    }

    /**
     * Verifica si hay conectividad de red disponible
     */
    private fun isNetworkAvailable(): Boolean {
        return try {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } catch (e: Exception) {
            Log.e(tag, "Error verificando conectividad", e)
            false
        }
    }

    /**
     * Crea datos de salida del worker
     */
    private fun createOutputData(
        networkAvailable: Boolean,
        syncTriggered: Boolean,
        pendingItems: Int,
        error: String = ""
    ): Data {
        return Data.Builder()
            .putBoolean("network_available", networkAvailable)
            .putBoolean("sync_triggered", syncTriggered)
            .putInt("pending_items", pendingItems)
            .putString("error", error)
            .putLong("timestamp", System.currentTimeMillis())
            .build()
    }
}

/**
 * Callback para monitoreo de red en tiempo real
 */
class NetworkCallback(
    private val context: Context,
    private val syncManager: SyncManager
) : ConnectivityManager.NetworkCallback() {

    private val tag = "NetworkCallback"

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.d(tag, "📡 Red disponible detectada")
        
        // Programar trabajo de monitoreo
        val workRequest = OneTimeWorkRequestBuilder<NetworkMonitorWorker>()
            .addTag("network_change")
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Log.d(tag, "📴 Pérdida de red detectada")
        
        // Actualizar estado inmediatamente
        // Comentamos temporalmente
        // kotlinx.coroutines.GlobalScope.launch {
        //     try {
        //         syncManager.updateNetworkStatus(false)
        //     } catch (e: Exception) {
        //         Log.e(tag, "Error actualizando estado de red", e)
        //     }
        // }
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        
        val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val isValidated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        
        Log.d(tag, "📊 Capacidades de red cambiadas - Internet: $hasInternet, Validada: $isValidated")
        
        if (hasInternet && isValidated) {
            // Programar verificación
            val workRequest = OneTimeWorkRequestBuilder<NetworkMonitorWorker>()
                .addTag("network_capabilities_change")
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}

