@file:Suppress("DEPRECATION")

package com.example.telephases.ui.components

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manager para manejar la reconexión automática de dispositivos BLE
 */
class BLEReconnectionManager(
    private val context: Context,
    private val deviceAddress: String,
    private val deviceName: String
) {
    private val tag = "BLEReconnectionManager"
    
    // Estados observables
    private val _isReconnecting = MutableStateFlow(false)
    val isReconnecting: StateFlow<Boolean> = _isReconnecting.asStateFlow()
    
    private val _reconnectionAttempts = MutableStateFlow(0)
    val reconnectionAttempts: StateFlow<Int> = _reconnectionAttempts.asStateFlow()
    
    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()
    
    // Configuración de reconexión
    private val maxReconnectionAttempts = 5
    private val baseDelayMs = 2000L // 2 segundos
    private val maxDelayMs = 30000L // 30 segundos máximo
    
    private var reconnectionJob: Job? = null
    private var isEnabled = true
    
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    
    /**
     * Inicia el proceso de reconexión automática
     */
    fun startReconnection(
        onReconnect: suspend () -> Boolean,
        onMaxAttemptsReached: () -> Unit = {}
    ) {
        if (!isEnabled) return
        
        stopReconnection()
        
        reconnectionJob = CoroutineScope(Dispatchers.IO).launch {
            _isReconnecting.value = true
            _reconnectionAttempts.value = 0
            _lastError.value = null
            
            Log.d(tag, "🔄 Iniciando reconexión automática para $deviceName ($deviceAddress)")
            
            while (_reconnectionAttempts.value < maxReconnectionAttempts && isEnabled) {
                _reconnectionAttempts.value++
                val currentAttempt = _reconnectionAttempts.value
                
                try {
                    Log.d(tag, "🔄 Intento de reconexión $currentAttempt/$maxReconnectionAttempts para $deviceName")
                    
                    // Verificar que Bluetooth esté habilitado
                    if (!bluetoothAdapter.isEnabled) {
                        _lastError.value = "Bluetooth no está habilitado"
                        Log.w(tag, "⚠️ Bluetooth no está habilitado")
                        break
                    }
                    
                    // Intentar reconectar
                    val success = onReconnect()
                    
                    if (success) {
                        Log.d(tag, "✅ Reconexión exitosa en intento $currentAttempt para $deviceName")
                        _isReconnecting.value = false
                        _lastError.value = null
                        return@launch
                    } else {
                        Log.w(tag, "❌ Reconexión falló en intento $currentAttempt para $deviceName")
                        _lastError.value = "Intento $currentAttempt falló"
                    }
                    
                    // Calcular delay exponencial con jitter
                    val delay = calculateDelay(currentAttempt)
                    Log.d(tag, "⏳ Esperando ${delay}ms antes del siguiente intento")
                    delay(delay)
                    
                } catch (e: Exception) {
                    Log.e(tag, "❌ Error en intento de reconexión $currentAttempt", e)
                    _lastError.value = "Error: ${e.message}"
                    
                    // Delay más largo en caso de error
                    delay(calculateDelay(currentAttempt) * 2)
                }
            }
            
            if (_reconnectionAttempts.value >= maxReconnectionAttempts) {
                Log.e(tag, "❌ Máximo de intentos de reconexión alcanzado para $deviceName")
                _lastError.value = "Máximo de intentos alcanzado ($maxReconnectionAttempts)"
                onMaxAttemptsReached()
            }
            
            _isReconnecting.value = false
        }
    }
    
    /**
     * Detiene el proceso de reconexión
     */
    fun stopReconnection() {
        reconnectionJob?.cancel()
        reconnectionJob = null
        _isReconnecting.value = false
        Log.d(tag, "🛑 Reconexión detenida para $deviceName")
    }
    
    /**
     * Habilita o deshabilita la reconexión automática
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (!enabled) {
            stopReconnection()
        }
        Log.d(tag, "🔧 Reconexión automática ${if (enabled) "habilitada" else "deshabilitada"} para $deviceName")
    }
    
    /**
     * Reinicia el contador de intentos
     */
    fun resetAttempts() {
        _reconnectionAttempts.value = 0
        _lastError.value = null
        Log.d(tag, "🔄 Contador de intentos reiniciado para $deviceName")
    }
    
    /**
     * Calcula el delay exponencial con jitter para evitar thundering herd
     */
    private fun calculateDelay(attempt: Int): Long {
        val exponentialDelay = baseDelayMs * (1L shl (attempt - 1)) // 2^attempt
        val delay = minOf(exponentialDelay, maxDelayMs)
        
        // Agregar jitter aleatorio (±25%)
        val jitter = (delay * 0.25 * (Math.random() - 0.5)).toLong()
        return maxOf(delay + jitter, 1000L) // Mínimo 1 segundo
    }
    
    /**
     * Obtiene el estado actual de reconexión
     */
    fun getStatus(): ReconnectionStatus {
        return ReconnectionStatus(
            isReconnecting = _isReconnecting.value,
            attempts = _reconnectionAttempts.value,
            maxAttempts = maxReconnectionAttempts,
            lastError = _lastError.value,
            isEnabled = isEnabled
        )
    }
    
    /**
     * Limpia recursos
     */
    fun cleanup() {
        stopReconnection()
        Log.d(tag, "🧹 BLEReconnectionManager limpiado para $deviceName")
    }
}

/**
 * Estado de reconexión
 */
data class ReconnectionStatus(
    val isReconnecting: Boolean,
    val attempts: Int,
    val maxAttempts: Int,
    val lastError: String?,
    val isEnabled: Boolean
) {
    val progress: Float = attempts.toFloat() / maxAttempts.toFloat()
    val canRetry: Boolean = attempts < maxAttempts && isEnabled
    val isMaxAttemptsReached: Boolean = attempts >= maxAttempts
}

