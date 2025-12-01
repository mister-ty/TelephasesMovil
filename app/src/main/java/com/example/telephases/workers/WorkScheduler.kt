package com.example.telephases.workers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.util.Log
import androidx.work.*
import com.example.telephases.data.repository.SyncManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Programador de trabajos de sincronización
 * Maneja la configuración y programación de todos los Workers
 */
@Singleton
class WorkScheduler @Inject constructor(
    private val context: Context,
    private val syncManager: SyncManager
) {

    private val tag = "WorkScheduler"
    private val workManager = WorkManager.getInstance(context)
    private var networkCallback: NetworkCallback? = null

    /**
     * Configura todos los trabajos de sincronización
     */
    fun setupPeriodicSync() {
        Log.d(tag, "⚙️ Configurando sincronización periódica...")

        // Trabajo periódico de sincronización (cada 2 horas)
        val periodicSyncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            2, TimeUnit.HOURS,
            30, TimeUnit.MINUTES // Ventana de flexibilidad
        )
            .setInputData(
                Data.Builder()
                    .putString(SyncWorker.KEY_SYNC_TYPE, SyncWorker.SYNC_TYPE_PENDING_ONLY)
                    .putBoolean(SyncWorker.KEY_FORCE_SYNC, false)
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .addTag("periodic_sync")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "periodic_sync_work",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncRequest
        )

        Log.d(tag, "✅ Sincronización periódica configurada")
        
        // Trabajo periódico de sincronización de USUARIOS (cada 5 minutos)
        val userSyncRequest = PeriodicWorkRequestBuilder<UserSyncWorker>(
            15, TimeUnit.MINUTES,  // Mínimo permitido por Android
            5, TimeUnit.MINUTES // Ventana de flexibilidad
        )
            .setInputData(
                Data.Builder()
                    .putBoolean(UserSyncWorker.KEY_FORCE_SYNC, false)
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .addTag("user_sync_periodic")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "user_sync_periodic_work",
            ExistingPeriodicWorkPolicy.KEEP,
            userSyncRequest
        )

        Log.d(tag, "✅ Sincronización periódica de usuarios configurada (cada 15 min con flex de 5 min)")
    }

    /**
     * Configura monitoreo de red en tiempo real
     */
    fun setupNetworkMonitoring() {
        Log.d(tag, "📡 Configurando monitoreo de red...")

        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            
            // Crear callback de red
            networkCallback = NetworkCallback(context, syncManager)
            
            // Registrar callback
            val networkRequest = NetworkRequest.Builder()
                .addCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .build()

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)
            
            Log.d(tag, "✅ Monitoreo de red configurado")
            
        } catch (e: Exception) {
            Log.e(tag, "❌ Error configurando monitoreo de red", e)
        }
    }

    /**
     * Programa sincronización inmediata
     */
    fun scheduleImmediateSync(
        syncType: String = SyncWorker.SYNC_TYPE_PENDING_ONLY,
        forceSync: Boolean = false
    ) {
        Log.d(tag, "🔄 Programando sincronización inmediata: $syncType")

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInputData(
                Data.Builder()
                    .putString(SyncWorker.KEY_SYNC_TYPE, syncType)
                    .putBoolean(SyncWorker.KEY_FORCE_SYNC, forceSync)
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .addTag("immediate_sync")
            .build()

        workManager.enqueueUniqueWork(
            "immediate_sync_work",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    /**
     * Programa sincronización inmediata de usuarios
     */
    fun scheduleImmediateUserSync(forceSync: Boolean = false) {
        Log.d(tag, "👥 Programando sincronización inmediata de usuarios...")

        val userSyncRequest = OneTimeWorkRequestBuilder<UserSyncWorker>()
            .setInputData(
                Data.Builder()
                    .putBoolean(UserSyncWorker.KEY_FORCE_SYNC, forceSync)
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .addTag("immediate_user_sync")
            .build()

        workManager.enqueueUniqueWork(
            "immediate_user_sync_work",
            ExistingWorkPolicy.REPLACE,
            userSyncRequest
        )
        
        Log.d(tag, "✅ Sincronización inmediata de usuarios programada")
    }

    /**
     * Programa sincronización con retraso
     */
    fun scheduleDelayedSync(
        delayMinutes: Long = 5,
        syncType: String = SyncWorker.SYNC_TYPE_PENDING_ONLY
    ) {
        Log.d(tag, "⏰ Programando sincronización con retraso: ${delayMinutes}min")

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .setInputData(
                Data.Builder()
                    .putString(SyncWorker.KEY_SYNC_TYPE, syncType)
                    .putBoolean(SyncWorker.KEY_FORCE_SYNC, false)
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .addTag("delayed_sync")
            .build()

        workManager.enqueueUniqueWork(
            "delayed_sync_work",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    /**
     * Programa sincronización de respaldo (cuando falla la inmediata)
     */
    fun scheduleBackupSync() {
        Log.d(tag, "🔄 Programando sincronización de respaldo...")

        val backupSyncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInitialDelay(30, TimeUnit.MINUTES)
            .setInputData(
                Data.Builder()
                    .putString(SyncWorker.KEY_SYNC_TYPE, SyncWorker.SYNC_TYPE_PENDING_ONLY)
                    .putBoolean(SyncWorker.KEY_FORCE_SYNC, true)
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag("backup_sync")
            .build()

        workManager.enqueueUniqueWork(
            "backup_sync_work",
            ExistingWorkPolicy.REPLACE,
            backupSyncRequest
        )
    }

    /**
     * Programa trabajo de mantenimiento
     */
    fun scheduleMaintenanceWork() {
        Log.d(tag, "🧹 Programando trabajo de mantenimiento...")

        val maintenanceRequest = PeriodicWorkRequestBuilder<MaintenanceWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresDeviceIdle(true)
                    .setRequiresCharging(true)
                    .build()
            )
            .addTag("maintenance")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "maintenance_work",
            ExistingPeriodicWorkPolicy.KEEP,
            maintenanceRequest
        )

        Log.d(tag, "✅ Trabajo de mantenimiento programado")
    }

    /**
     * Cancela todos los trabajos de sincronización
     */
    fun cancelAllSyncWork() {
        Log.d(tag, "❌ Cancelando todos los trabajos de sincronización...")

        workManager.cancelAllWorkByTag("periodic_sync")
        workManager.cancelAllWorkByTag("immediate_sync")
        workManager.cancelAllWorkByTag("delayed_sync")
        workManager.cancelAllWorkByTag("backup_sync")
        workManager.cancelAllWorkByTag("auto_sync")

        Log.d(tag, "✅ Trabajos de sincronización cancelados")
    }

    /**
     * Cancela monitoreo de red
     */
    fun cancelNetworkMonitoring() {
        Log.d(tag, "📴 Cancelando monitoreo de red...")

        try {
            networkCallback?.let { callback ->
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                connectivityManager.unregisterNetworkCallback(callback)
                networkCallback = null
            }
            
            workManager.cancelAllWorkByTag("network_change")
            workManager.cancelAllWorkByTag("network_capabilities_change")
            
            Log.d(tag, "✅ Monitoreo de red cancelado")
            
        } catch (e: Exception) {
            Log.e(tag, "❌ Error cancelando monitoreo de red", e)
        }
    }

    /**
     * Obtiene información sobre trabajos activos
     */
    suspend fun getWorkInfo(): WorkInfo {
        return try {
            val workInfos = workManager.getWorkInfosByTag("periodic_sync").await()
            val periodicWork = workInfos.firstOrNull()
            
            val immediateInfos = workManager.getWorkInfosByTag("immediate_sync").await()
            val immediateWork = immediateInfos.firstOrNull()

            WorkInfo(
                hasPeriodicSync = periodicWork?.state == androidx.work.WorkInfo.State.RUNNING || periodicWork?.state == androidx.work.WorkInfo.State.ENQUEUED,
                hasImmediateSync = immediateWork?.state == androidx.work.WorkInfo.State.RUNNING,
                periodicSyncState = periodicWork?.state?.name ?: "NONE",
                immediateSyncState = immediateWork?.state?.name ?: "NONE",
                lastSyncResult = periodicWork?.outputData?.getString("message"),
                networkMonitoringActive = networkCallback != null
            )
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo información de trabajos", e)
            WorkInfo(
                hasPeriodicSync = false,
                hasImmediateSync = false,
                periodicSyncState = "ERROR",
                immediateSyncState = "ERROR",
                lastSyncResult = "Error: ${e.message}",
                networkMonitoringActive = false
            )
        }
    }

    /**
     * Configura todos los trabajos iniciales
     */
    fun setupAllWork() {
        Log.d(tag, "🚀 Configurando todos los trabajos...")
        
        setupPeriodicSync()
        setupNetworkMonitoring()
        scheduleMaintenanceWork()
        
        // Programar primera sincronización con retraso
        scheduleDelayedSync(2, SyncWorker.SYNC_TYPE_PENDING_ONLY)
        
        Log.d(tag, "✅ Todos los trabajos configurados")
    }

    /**
     * Limpia y reconfigura todos los trabajos
     */
    fun resetAllWork() {
        Log.d(tag, "🔄 Reiniciando todos los trabajos...")
        
        cancelAllSyncWork()
        cancelNetworkMonitoring()
        
        // Esperar un poco antes de reconfigurar
        // Comentamos temporalmente
        // kotlinx.coroutines.GlobalScope.launch {
        //     kotlinx.coroutines.delay(1000)
        //     setupAllWork()
        // }
    }

    /**
     * Información sobre el estado de los trabajos
     */
    data class WorkInfo(
        val hasPeriodicSync: Boolean,
        val hasImmediateSync: Boolean,
        val periodicSyncState: String,
        val immediateSyncState: String,
        val lastSyncResult: String?,
        val networkMonitoringActive: Boolean
    )
}

