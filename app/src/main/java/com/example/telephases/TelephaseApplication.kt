package com.example.telephases

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.telephases.data.local.database.DatabaseInitializer
import com.example.telephases.utils.NetworkConnectivityManager
import com.example.telephases.workers.WorkScheduler
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Aplicación principal con Hilt configurado
 */
@HiltAndroidApp
class TelephaseApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var databaseInitializer: DatabaseInitializer

    @Inject
    lateinit var networkConnectivityManager: NetworkConnectivityManager

    @Inject
    lateinit var workScheduler: WorkScheduler

    private val tag = "TelephaseApplication"

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "🚀 Iniciando aplicación Telephase...")

        // Crear canales de notificación
        createNotificationChannels()

        // Inicializar servicios en background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Inicializar base de datos
                databaseInitializer.initialize()
                databaseInitializer.performInitialMaintenance()

                // Configurar monitoreo de red
                networkConnectivityManager.startMonitoring()

                // Configurar trabajos de sincronización
                workScheduler.setupAllWork()

                Log.d(tag, "✅ Aplicación inicializada correctamente")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error inicializando aplicación", e)
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(tag, "🔄 Terminando aplicación...")

        try {
            // Detener monitoreo de red
            networkConnectivityManager.stopMonitoring()
            
            // Cancelar trabajos si es necesario
            // workScheduler.cancelAllSyncWork()
            
            Log.d(tag, "✅ Aplicación terminada correctamente")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error terminando aplicación", e)
        }
    }

    /**
     * Configuración para WorkManager con Hilt
     */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(if (BuildConfig.DEBUG) Log.DEBUG else Log.INFO)
            .build()
    
    fun getWorkManagerConfigurationLegacy(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    }

    /**
     * Crear canales de notificación para sincronización
     */
    private fun createNotificationChannels() {
        try {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Canal para sincronización
            val syncChannel = NotificationChannel(
                "sync_channel",
                "Sincronización",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notificaciones de sincronización de datos"
                setShowBadge(false)
                enableLights(false)
                enableVibration(false)
            }

            // Canal para alertas importantes
            val alertChannel = NotificationChannel(
                "alert_channel",
                "Alertas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alertas importantes de la aplicación"
                setShowBadge(true)
                enableLights(true)
                enableVibration(true)
            }

            notificationManager.createNotificationChannels(listOf(syncChannel, alertChannel))
            Log.d(tag, "✅ Canales de notificación creados")

        } catch (e: Exception) {
            Log.e(tag, "❌ Error creando canales de notificación", e)
        }
    }
}

