@file:Suppress("DEPRECATION")
@file:SuppressLint("MissingPermission")

package com.example.telephases.ui.screens

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.util.Log
import com.example.telephases.ui.components.BLEReconnectionManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

/**
 * Manager mejorado para glucómetro BLE con reconexión automática
 */
class ImprovedBLEGlucometerManager(private val context: Context) {
    
    private val tag = "ImprovedBLEGlucometer"
    
    // UUIDs del glucómetro
    private val SERVICE_UUID = UUID.fromString("00001808-0000-1000-8000-00805f9b34fb")
    private val MEASUREMENT_UUID = UUID.fromString("00002a18-0000-1000-8000-00805f9b34fb")
    private val CONTROL_POINT_UUID = UUID.fromString("00002a52-0000-1000-8000-00805f9b34fb")
    private val CCC_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    
    private var bluetoothGatt: BluetoothGatt? = null
    private var glucoseControlPoint: BluetoothGattCharacteristic? = null
    private var scanner: BluetoothLeScanner? = null
    private var scanCallback: ScanCallback? = null
    private var currentDevice: BluetoothDevice? = null
    
    // Variable para almacenar el código del dispositivo
    private var deviceCode: String = "5019999886"
    private var pairingPassword: String = ""
    
    // Manager de reconexión automática
    private var reconnectionManager: BLEReconnectionManager? = null
    
    // Estados observables adicionales para emparejamiento
    private val _isPairing = MutableStateFlow(false)
    val isPairing: StateFlow<Boolean> = _isPairing.asStateFlow()
    
    private val _pairingRequired = MutableStateFlow(false)
    val pairingRequired: StateFlow<Boolean> = _pairingRequired.asStateFlow()
    
    private val _devicePassword = MutableStateFlow("")
    val devicePassword: StateFlow<String> = _devicePassword.asStateFlow()
    
    // Estados observables
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    
    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting.asStateFlow()
    
    private val _glucometerReading = MutableStateFlow<GlucometerReading?>(null)
    val glucometerReading: StateFlow<GlucometerReading?> = _glucometerReading.asStateFlow()
    
    private val _statusMessage = MutableStateFlow("Listo para conectar")
    val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()
    
    private val _deviceFound = MutableStateFlow(false)
    val deviceFound: StateFlow<Boolean> = _deviceFound.asStateFlow()
    
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()
    
    // Estados de reconexión
    private val _isReconnecting = MutableStateFlow(false)
    val isReconnecting: StateFlow<Boolean> = _isReconnecting.asStateFlow()
    
    private val _reconnectionAttempts = MutableStateFlow(0)
    val reconnectionAttempts: StateFlow<Int> = _reconnectionAttempts.asStateFlow()
    
    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()
    
    // Configuración
    private var autoReconnectionEnabled = true
    private var deviceName = "Glucómetro BLE"
    
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        _isConnected.value = true
                        _isConnecting.value = false
                        _isPairing.value = false
                        _pairingRequired.value = false
                        _isReconnecting.value = false
                        _lastError.value = null
                        _statusMessage.value = "✅ Conectado exitosamente"
                        
                        Log.d(tag, "✅ Conectado exitosamente al glucómetro")
                        
                        // Esperar un poco antes de descubrir servicios
                        Handler(Looper.getMainLooper()).postDelayed({
                            gatt.discoverServices()
                        }, 1000)
                    } else {
                        handleConnectionError("Error de conexión (E62 - Emparejamiento falló)")
                    }
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    _isConnected.value = false
                    _isConnecting.value = false
                    _isPairing.value = false
                    
                    if (status != BluetoothGatt.GATT_SUCCESS) {
                        val errorMsg = "Desconectado por error (E62 - Verificar emparejamiento)"
                        _statusMessage.value = "❌ $errorMsg"
                        _lastError.value = errorMsg
                        handleConnectionError(errorMsg)
                    } else {
                        _statusMessage.value = "📴 Desconectado"
                        _lastError.value = null
                    }
                }
            }
        }
        
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(tag, "🔍 Servicios descubiertos exitosamente")
                setupGlucoseService(gatt)
            } else {
                handleConnectionError("Error descubriendo servicios: $status")
            }
        }
        
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            when (characteristic.uuid) {
                MEASUREMENT_UUID -> {
                    parseGlucoseMeasurement(characteristic.value)
                }
            }
        }
        
        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                when (characteristic.uuid) {
                    MEASUREMENT_UUID -> {
                        parseGlucoseMeasurement(characteristic.value)
                    }
                }
            } else {
                Log.w(tag, "⚠️ Error leyendo característica: $status")
            }
        }
    }
    
    /**
     * Inicia la búsqueda del dispositivo
     */
    fun startScanning() {
        if (!bluetoothAdapter.isEnabled) {
            _statusMessage.value = "❌ Bluetooth no está habilitado"
            return
        }
        
        if (_isScanning.value) {
            Log.d(tag, "⚠️ Ya se está escaneando")
            return
        }
        
        _isScanning.value = true
        _deviceFound.value = false
        _statusMessage.value = "🔍 Buscando glucómetro..."
        
        scanner = bluetoothAdapter.bluetoothLeScanner
        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device = result.device
                val deviceName = device.name ?: "Dispositivo desconocido"
                
                Log.d(tag, "🔍 Dispositivo encontrado: $deviceName (${device.address})")
                
                // Buscar glucómetro por nombre o características
                if (isGlucoseMeter(device, result.scanRecord)) {
                    _deviceFound.value = true
                    _isScanning.value = false
                    _statusMessage.value = "✅ Glucómetro encontrado: $deviceName"
                    currentDevice = device
                    
                    // Detener escaneo
                    scanner?.stopScan(this)
                    
                    // Conectar automáticamente
                    connectToDevice(device)
                }
            }
            
            override fun onScanFailed(errorCode: Int) {
                _isScanning.value = false
                _statusMessage.value = "❌ Error en escaneo: $errorCode"
                _lastError.value = "Error de escaneo: $errorCode"
                Log.e(tag, "❌ Error en escaneo: $errorCode")
            }
        }
        
        val scanFilter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(SERVICE_UUID))
            .build()
        
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        
        scanner?.startScan(listOf(scanFilter), scanSettings, scanCallback)
        
        // Timeout de escaneo
        Handler(Looper.getMainLooper()).postDelayed({
            if (_isScanning.value) {
                stopScanning()
                _statusMessage.value = "⏰ Tiempo de búsqueda agotado"
            }
        }, 30000) // 30 segundos
    }
    
    /**
     * Conecta al dispositivo especificado
     */
    fun connectToDevice(device: BluetoothDevice) {
        if (_isConnecting.value || _isConnected.value) {
            Log.d(tag, "⚠️ Ya conectado o conectando")
            return
        }
        
        _isConnecting.value = true
        _statusMessage.value = "🔗 Conectando a ${device.name ?: device.address}..."
        
        currentDevice = device
        deviceName = device.name ?: "Glucómetro BLE"
        
        // Configurar reconexión automática
        setupReconnectionManager(device.address, deviceName)
        
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }
    
    /**
     * Configura el manager de reconexión automática
     */
    private fun setupReconnectionManager(deviceAddress: String, deviceName: String) {
        reconnectionManager?.cleanup()
        reconnectionManager = BLEReconnectionManager(context, deviceAddress, deviceName)
        
        // Observar estados de reconexión
        CoroutineScope(Dispatchers.Main).launch {
            reconnectionManager?.isReconnecting?.collect { isReconnecting ->
                _isReconnecting.value = isReconnecting
            }
        }
        
        CoroutineScope(Dispatchers.Main).launch {
            reconnectionManager?.reconnectionAttempts?.collect { attempts ->
                _reconnectionAttempts.value = attempts
            }
        }
        
        CoroutineScope(Dispatchers.Main).launch {
            reconnectionManager?.lastError?.collect { error ->
                _lastError.value = error
            }
        }
    }
    
    /**
     * Inicia reconexión automática
     */
    fun startAutoReconnection() {
        if (!autoReconnectionEnabled || currentDevice == null) return
        
        reconnectionManager?.startReconnection(
            onReconnect = { reconnect() },
            onMaxAttemptsReached = {
                _statusMessage.value = "❌ Máximo de intentos de reconexión alcanzado"
                _isReconnecting.value = false
            }
        )
    }
    
    /**
     * Intenta reconectar al dispositivo
     */
    private suspend fun reconnect(): Boolean {
        return try {
            if (currentDevice == null) return false
            
            _isConnecting.value = true
            _statusMessage.value = "🔄 Reconectando..."
            
            // Cerrar conexión anterior si existe
            bluetoothGatt?.close()
            bluetoothGatt = null
            
            // Esperar un poco antes de reconectar
            delay(2000)
            
            // Intentar reconectar
            bluetoothGatt = currentDevice?.connectGatt(context, false, gattCallback)
            
            // Esperar resultado de conexión
            var attempts = 0
            while (_isConnecting.value && attempts < 10) {
                delay(1000)
                attempts++
            }
            
            _isConnected.value
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en reconexión", e)
            false
        }
    }
    
    /**
     * Desconecta del dispositivo
     */
    fun disconnect() {
        stopAutoReconnection()
        
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        
        _isConnected.value = false
        _isConnecting.value = false
        _isPairing.value = false
        _pairingRequired.value = false
        _statusMessage.value = "📴 Desconectado"
        _lastError.value = null
        
        currentDevice = null
    }
    
    /**
     * Detiene la reconexión automática
     */
    fun stopAutoReconnection() {
        reconnectionManager?.stopReconnection()
        _isReconnecting.value = false
    }
    
    /**
     * Habilita o deshabilita la reconexión automática
     */
    fun setAutoReconnectionEnabled(enabled: Boolean) {
        autoReconnectionEnabled = enabled
        reconnectionManager?.setEnabled(enabled)
        
        if (!enabled) {
            stopAutoReconnection()
        }
    }
    
    /**
     * Maneja errores de conexión
     */
    private fun handleConnectionError(error: String) {
        _isConnected.value = false
        _isConnecting.value = false
        _isPairing.value = false
        _lastError.value = error
        
        Log.e(tag, "❌ Error de conexión: $error")
        
        // Iniciar reconexión automática si está habilitada
        if (autoReconnectionEnabled && currentDevice != null) {
            startAutoReconnection()
        }
    }
    
    /**
     * Verifica si el dispositivo es un glucómetro
     */
    private fun isGlucoseMeter(device: BluetoothDevice, scanRecord: ScanRecord?): Boolean {
        val deviceName = device.name?.lowercase() ?: ""
        val advertisedServices = scanRecord?.serviceUuids ?: emptyList()
        
        return deviceName.contains("glucose") || 
               deviceName.contains("glucometer") ||
               deviceName.contains("glucómetro") ||
               advertisedServices.contains(ParcelUuid(SERVICE_UUID))
    }
    
    /**
     * Configura el servicio de glucosa
     */
    private fun setupGlucoseService(gatt: BluetoothGatt) {
        val glucoseService = gatt.getService(SERVICE_UUID)
        if (glucoseService != null) {
            glucoseControlPoint = glucoseService.getCharacteristic(CONTROL_POINT_UUID)
            val measurementCharacteristic = glucoseService.getCharacteristic(MEASUREMENT_UUID)
            
            if (measurementCharacteristic != null) {
                // Habilitar notificaciones
                gatt.setCharacteristicNotification(measurementCharacteristic, true)
                val descriptor = measurementCharacteristic.getDescriptor(CCC_DESCRIPTOR_UUID)
                descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
            
            _statusMessage.value = "✅ Servicio de glucosa configurado"
            Log.d(tag, "✅ Servicio de glucosa configurado exitosamente")
        } else {
            handleConnectionError("Servicio de glucosa no encontrado")
        }
    }
    
    /**
     * Parsea la medición de glucosa
     */
    private fun parseGlucoseMeasurement(data: ByteArray) {
        try {
            if (data.size >= 2) {
                val glucoseValue = ((data[1].toInt() and 0xFF) shl 8) or (data[0].toInt() and 0xFF)
                val reading = GlucometerReading(
                    glucose = glucoseValue,
                    sequenceNumber = 1,
                    year = 2024,
                    month = 1,
                    day = 1,
                    hour = 12,
                    minute = 0,
                    type = 1,
                    location = 1
                )
                
                _glucometerReading.value = reading
                _statusMessage.value = "📊 Lectura: ${reading.glucose} mg/dL"
                
                Log.d(tag, "📊 Nueva lectura de glucosa: ${reading.glucose} mg/dL")
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error parseando medición de glucosa", e)
        }
    }
    
    /**
     * Detiene el escaneo
     */
    fun stopScanning() {
        scanner?.stopScan(scanCallback)
        _isScanning.value = false
        _statusMessage.value = "🛑 Escaneo detenido"
    }
    
    /**
     * Limpia recursos
     */
    fun cleanup() {
        stopScanning()
        disconnect()
        reconnectionManager?.cleanup()
        reconnectionManager = null
    }
}

