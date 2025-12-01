package com.example.telephases.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.telephases.data.repository.SyncManager
import com.example.telephases.workers.WorkScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor de conectividad de red
 * Proporciona información en tiempo real sobre el estado de la red
 */
@Singleton
class NetworkConnectivityManager @Inject constructor(
    private val context: Context,
    private val syncManager: SyncManager,
    private val workScheduler: WorkScheduler
) {

    private val tag = "NetworkConnectivityManager"
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Estados de conectividad
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    private val _networkType = MutableLiveData<NetworkType>()
    val networkType: LiveData<NetworkType> = _networkType

    private val _connectionQuality = MutableLiveData<ConnectionQuality>()
    val connectionQuality: LiveData<ConnectionQuality> = _connectionQuality

    // StateFlow para Compose
    private val _connectivityState = MutableStateFlow(
        ConnectivityState(
            isConnected = false,
            networkType = NetworkType.NONE,
            connectionQuality = ConnectionQuality.POOR
        )
    )
    val connectivityState: StateFlow<ConnectivityState> = _connectivityState.asStateFlow()

    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var currentNetwork: Network? = null

    /**
     * Inicializa el monitoreo de conectividad
     */
    fun startMonitoring() {
        Log.d(tag, "📡 Iniciando monitoreo de conectividad...")

        try {
            // Verificar estado inicial
            updateConnectivityStatus()

            // Crear callback de red
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.d(tag, "🟢 Red disponible: $network")
                    currentNetwork = network
                    updateConnectivityStatus()
                    onNetworkAvailable()
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Log.d(tag, "🔴 Red perdida: $network")
                    currentNetwork = null
                    updateConnectivityStatus()
                    onNetworkLost()
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    Log.d(tag, "🔄 Capacidades de red cambiadas")
                    updateConnectivityStatus(networkCapabilities)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Log.d(tag, "❌ Red no disponible")
                    updateConnectivityStatus()
                }
            }

            // Registrar callback
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .build()

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)
            Log.d(tag, "✅ Monitoreo de conectividad iniciado")

        } catch (e: Exception) {
            Log.e(tag, "❌ Error iniciando monitoreo de conectividad", e)
        }
    }

    /**
     * Detiene el monitoreo de conectividad
     */
    fun stopMonitoring() {
        Log.d(tag, "📴 Deteniendo monitoreo de conectividad...")

        try {
            networkCallback?.let { callback ->
                connectivityManager.unregisterNetworkCallback(callback)
                networkCallback = null
            }
            currentNetwork = null
            Log.d(tag, "✅ Monitoreo de conectividad detenido")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error deteniendo monitoreo", e)
        }
    }

    /**
     * Verifica si hay conectividad actualmente
     */
    fun isCurrentlyConnected(): Boolean {
        return try {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } catch (e: Exception) {
            Log.e(tag, "Error verificando conectividad actual", e)
            false
        }
    }

    /**
     * Obtiene el tipo de red actual
     */
    fun getCurrentNetworkType(): NetworkType {
        return try {
            val network = connectivityManager.activeNetwork ?: return NetworkType.NONE
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.NONE
            
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
                else -> NetworkType.OTHER
            }
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo tipo de red", e)
            NetworkType.NONE
        }
    }

    /**
     * Estima la calidad de la conexión
     */
    fun getConnectionQuality(): ConnectionQuality {
        return try {
            val network = connectivityManager.activeNetwork ?: return ConnectionQuality.POOR
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return ConnectionQuality.POOR

            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    // Para WiFi, asumir buena calidad si está validada
                    if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                        ConnectionQuality.EXCELLENT
                    } else {
                        ConnectionQuality.GOOD
                    }
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    // Para datos móviles, calidad moderada por defecto
                    ConnectionQuality.GOOD
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    ConnectionQuality.EXCELLENT
                }
                else -> ConnectionQuality.POOR
            }
        } catch (e: Exception) {
            Log.e(tag, "Error estimando calidad de conexión", e)
            ConnectionQuality.POOR
        }
    }

    /**
     * Actualiza el estado de conectividad
     */
    private fun updateConnectivityStatus(networkCapabilities: NetworkCapabilities? = null) {
        try {
            val isConnected = isCurrentlyConnected()
            val networkType = getCurrentNetworkType()
            val connectionQuality = getConnectionQuality()

            // Actualizar LiveData
            _isConnected.postValue(isConnected)
            _networkType.postValue(networkType)
            _connectionQuality.postValue(connectionQuality)

            // Actualizar StateFlow
            _connectivityState.value = ConnectivityState(
                isConnected = isConnected,
                networkType = networkType,
                connectionQuality = connectionQuality
            )

            // Actualizar SyncManager
            // Comentamos temporalmente
            // kotlinx.coroutines.GlobalScope.launch {
            //     syncManager.updateNetworkStatus(isConnected)
            // }

            Log.d(tag, "📊 Estado actualizado - Conectado: $isConnected, Tipo: $networkType, Calidad: $connectionQuality")

        } catch (e: Exception) {
            Log.e(tag, "Error actualizando estado de conectividad", e)
        }
    }

    /**
     * Maneja cuando la red se vuelve disponible
     */
    private fun onNetworkAvailable() {
        Log.d(tag, "🟢 Red disponible - verificando sincronización...")

        try {
            // Programar sincronización inmediata si hay datos pendientes
            // Comentamos temporalmente para compilar
            // kotlinx.coroutines.GlobalScope.launch {
            //     val syncStats = syncManager.getSyncStats()
            //     if (syncStats.needsSync && syncStats.autoSyncEnabled) {
            //         Log.d(tag, "🔄 Datos pendientes detectados - programando sincronización")
            //         workScheduler.scheduleImmediateSync()
            //     }
            // }
        } catch (e: Exception) {
            Log.e(tag, "Error manejando red disponible", e)
        }
    }

    /**
     * Maneja cuando se pierde la red
     */
    private fun onNetworkLost() {
        Log.d(tag, "🔴 Red perdida - modo offline activado")
        
        // Aquí se podrían añadir notificaciones al usuario si es necesario
    }

    /**
     * Fuerza una verificación de conectividad
     */
    fun forceConnectivityCheck() {
        Log.d(tag, "🔄 Forzando verificación de conectividad...")
        updateConnectivityStatus()
    }

    // ========== CLASES DE DATOS ==========

    /**
     * Tipos de red
     */
    enum class NetworkType {
        NONE,
        WIFI,
        CELLULAR,
        ETHERNET,
        OTHER
    }

    /**
     * Calidad de conexión
     */
    enum class ConnectionQuality {
        POOR,       // Conectividad limitada
        GOOD,       // Conectividad estable
        EXCELLENT   // Conectividad óptima
    }

    /**
     * Estado completo de conectividad
     */
    data class ConnectivityState(
        val isConnected: Boolean,
        val networkType: NetworkType,
        val connectionQuality: ConnectionQuality
    ) {
        val canSync: Boolean get() = isConnected && connectionQuality != ConnectionQuality.POOR
        val isHighQuality: Boolean get() = connectionQuality == ConnectionQuality.EXCELLENT
        val displayText: String get() = when {
            !isConnected -> "Sin conexión"
            networkType == NetworkType.WIFI -> "WiFi conectado"
            networkType == NetworkType.CELLULAR -> "Datos móviles"
            networkType == NetworkType.ETHERNET -> "Ethernet"
            else -> "Conectado"
        }
    }
}

