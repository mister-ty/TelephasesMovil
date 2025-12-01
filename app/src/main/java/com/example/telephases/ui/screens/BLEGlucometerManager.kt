@file:Suppress("DEPRECATION")
@file:SuppressLint("MissingPermission")

package com.example.telephases.ui.screens

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.os.Handler
import android.os.Looper

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class BLEGlucometerManager(private val context: Context) {
    
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
    
    // Variable para almacenar el código del dispositivo
    private var deviceCode: String = "5019999886"
    private var pairingPassword: String = ""
    
    // Estados observables adicionales para emparejamiento
    private val _isPairing = MutableStateFlow(false)
    val isPairing: StateFlow<Boolean> = _isPairing
    
    private val _pairingRequired = MutableStateFlow(false)
    val pairingRequired: StateFlow<Boolean> = _pairingRequired
    
    private val _devicePassword = MutableStateFlow("")
    val devicePassword: StateFlow<String> = _devicePassword
    
    // Estados observables
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected
    
    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting
    
    private val _glucometerReading = MutableStateFlow<GlucometerReading?>(null)
    val glucometerReading: StateFlow<GlucometerReading?> = _glucometerReading
    
    private val _statusMessage = MutableStateFlow("Listo para conectar")
    val statusMessage: StateFlow<String> = _statusMessage
    
    private val _deviceFound = MutableStateFlow(false)
    val deviceFound: StateFlow<Boolean> = _deviceFound
    
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning
    
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        _isConnected.value = true
                        _isConnecting.value = false
                        _isPairing.value = false
                        _pairingRequired.value = false
                        _statusMessage.value = "✅ Conectado exitosamente (bLE OnL)"
                        
                        // Esperar un poco antes de descubrir servicios para estabilizar conexión
                        Handler(Looper.getMainLooper()).postDelayed({
                            gatt.discoverServices()
                        }, 1000)
                    } else {
                        _statusMessage.value = "❌ Error de conexión (E62 - Emparejamiento falló)"
                        _isConnected.value = false
                        _isConnecting.value = false
                        _isPairing.value = false
                        handlePairingFailure()
                    }
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    _isConnected.value = false
                    _isConnecting.value = false
                    _isPairing.value = false
                    if (status != BluetoothGatt.GATT_SUCCESS) {
                        _statusMessage.value = "❌ Desconectado por error (E62 - Verificar emparejamiento)"
                        handlePairingFailure()
                    } else {
                        _statusMessage.value = "📴 Desconectado"
                    }
                }
                BluetoothProfile.STATE_CONNECTING -> {
                    _isConnecting.value = true
                    _statusMessage.value = "🔗 Estableciendo conexión..."
                }
            }
        }
        
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                _statusMessage.value = "🔍 Servicios descubiertos correctamente"
                val glucoseService = gatt.getService(SERVICE_UUID)
                val measurement = glucoseService?.getCharacteristic(MEASUREMENT_UUID)
                glucoseControlPoint = glucoseService?.getCharacteristic(CONTROL_POINT_UUID)
                
                if (measurement != null) {
                    enableNotifications(gatt, measurement)
                    _statusMessage.value = "📡 Listo para recibir mediciones"
                } else {
                    _statusMessage.value = "⚠️ Servicio de glucosa no encontrado"
                }
                
                if (glucoseControlPoint == null) {
                    _statusMessage.value = "⚠️ Control de glucosa no disponible"
                }
            } else {
                _statusMessage.value = "❌ Error descubriendo servicios: $status"
            }
        }
        
        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            val value = characteristic.value
            decodeGlucoseMeasurement(value)
        }
        
        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                _statusMessage.value = "✅ Comando enviado exitosamente"
            } else {
                _statusMessage.value = "❌ Error enviando comando: $status"
            }
        }
    }
    
    // Función para manejar fallos de emparejamiento
    private fun handlePairingFailure() {
        _pairingRequired.value = true
        _statusMessage.value = """
            ❌ Error E62 - Emparejamiento falló
            
            Verifique:
            1. Glucómetro en modo 'bLE' (Set + Mem)
            2. Distancia menor a 10 metros
            3. Contraseña correcta (3 dígitos en pantalla)
            4. Reinicie el glucómetro si persiste
        """.trimIndent()
    }
    
    // Función mejorada para establecer el código del dispositivo
    fun setDeviceCode(code: String) {
        // Manejar formato específico del GlucoLeader (00501-9999886)
        val cleanCode = when {
            // Si viene con guiones como "00501-9999886", concatenar sin guiones
            code.contains("-") -> code.replace("-", "").replace(Regex("[^0-9]"), "")
            // Si no tiene guiones, extraer solo números
            else -> code.replace(Regex("[^0-9]"), "")
        }
        
        // Para tu glucómetro: 00501-9999886 → Meter5019999886 (se usan 10 dígitos del final)
        deviceCode = when {
            cleanCode.length >= 10 -> {
                // Si viene "005019999886" (12 dígitos), tomar los últimos 10
                if (cleanCode.length == 12 && cleanCode.startsWith("00501")) {
                    cleanCode.substring(2) // "5019999886"
                } else {
                    cleanCode.take(10)
                }
            }
            cleanCode.length > 0 -> cleanCode.padStart(10, '0')
            else -> "5019999886" // Tu glucómetro específico
        }
        
        val deviceName = getDeviceName()
        _statusMessage.value = "Dispositivo configurado: $deviceName"
        _statusMessage.value = "Número de serie completo: $cleanCode"
    }
    
    // Función para obtener el nombre del dispositivo basado en el código
    private fun getDeviceName(): String {
        return "Meter$deviceCode"
    }
    
    // Función para establecer la contraseña de emparejamiento
    fun setPairingPassword(password: String) {
        // Limpiar y validar contraseña
        val cleanPassword = password.replace(Regex("[^0-9]"), "")
        
        pairingPassword = when {
            cleanPassword.length == 3 -> "000$cleanPassword"
            cleanPassword.length == 6 -> cleanPassword
            cleanPassword.length > 0 -> cleanPassword.padStart(6, '0').take(6)
            else -> ""
        }
        
        _devicePassword.value = pairingPassword
        
        if (pairingPassword.isNotEmpty()) {
            _statusMessage.value = "Contraseña lista: ${pairingPassword.takeLast(3)} (mostrada como ${pairingPassword})"
        }
    }
    
    // Función para iniciar emparejamiento con mejor guía
    fun startPairing() {
        _isPairing.value = true
        _pairingRequired.value = true
        _statusMessage.value = """
            📋 Pasos para emparejar:
            
            1. Apague el glucómetro completamente
            2. Presione 'Set' + 'Mem' simultáneamente 
            3. Mantenga hasta ver símbolo 'bLE' parpadeando
            4. Aparecerá contraseña de 3 dígitos
            5. Ingrese esa contraseña en la app
        """.trimIndent()
    }
    
    fun startScan() {
        if (_isScanning.value) return
        
        _isScanning.value = true
        _deviceFound.value = false
        val targetDevice = getDeviceName()
        _statusMessage.value = "🔍 Buscando: $targetDevice..."
        
        scanner = bluetoothAdapter.bluetoothLeScanner
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        val filter = ScanFilter.Builder()
            .setDeviceName(targetDevice)
            .build()
        
        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.device?.let { device ->
                    _statusMessage.value = "✅ Glucómetro encontrado: ${device.name}"
                    _deviceFound.value = true
                    stopScan()
                    
                    // Verificar si ya está emparejado
                    if (device.bondState == BluetoothDevice.BOND_BONDED) {
                        _statusMessage.value = "🔗 Dispositivo ya emparejado, conectando..."
                        connectToDevice(device)
                    } else {
                        _statusMessage.value = "🔐 Dispositivo nuevo, iniciando emparejamiento..."
                        _pairingRequired.value = true
                        startPairing()
                        
                        // Intentar conexión que iniciará el proceso de emparejamiento
                        Handler(Looper.getMainLooper()).postDelayed({
                            connectToDevice(device)
                        }, 2000)
                    }
                }
            }
            
            override fun onScanFailed(errorCode: Int) {
                _statusMessage.value = "❌ Error en escaneo: $errorCode"
                _isScanning.value = false
            }
        }
        
        scanner?.startScan(listOf(filter), settings, scanCallback)
        
        // Timeout después de 8 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            if (_isScanning.value && !_deviceFound.value) {
                stopScan()
                _statusMessage.value = "⏱️ No se encontró: $targetDevice"
            }
        }, 8000)
    }
    
    fun stopScan() {
        if (_isScanning.value) {
            scanCallback?.let { scanner?.stopScan(it) }
            _isScanning.value = false
        }
    }
    
    private fun connectToDevice(device: BluetoothDevice) {
        _isConnecting.value = true
        _statusMessage.value = "🔗 Conectando a ${device.name}..."
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }
    
    private fun enableNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        gatt.setCharacteristicNotification(characteristic, true)
        val descriptor = characteristic.getDescriptor(CCC_DESCRIPTOR_UUID)
        descriptor?.let {
            it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(it)
        }
    }
    
    fun requestGlucoseRecords() {
        val gatt = bluetoothGatt ?: return
        val controlPoint = glucoseControlPoint ?: run {
            _statusMessage.value = "❌ Punto de control no disponible"
            return
        }
        
        controlPoint.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        controlPoint.value = byteArrayOf(0x01, 0x01)
        gatt.writeCharacteristic(controlPoint)
        _statusMessage.value = "📤 Solicitando registros de glucosa..."
    }
    
    private fun decodeGlucoseMeasurement(data: ByteArray) {
        try {
            val seq = (data[2].toInt() shl 8) or (data[1].toInt() and 0xFF)
            val year = (data[4].toInt() shl 8) or (data[3].toInt() and 0xFF)
            val month = data[5].toInt()
            val day = data[6].toInt()
            val hour = data[7].toInt()
            val minute = data[8].toInt()
            
            val glucose = data[10].toInt() and 0xFF // Valor de glucosa real
            
            val typeLoc = data.getOrNull(11)?.toInt() ?: 0
            val type = (typeLoc ushr 4) and 0xF
            val loc = typeLoc and 0xF
            
            val reading = GlucometerReading(
                glucose = glucose,
                sequenceNumber = seq,
                year = year,
                month = month,
                day = day,
                hour = hour,
                minute = minute,
                type = type,
                location = loc
            )
            
            _glucometerReading.value = reading
            _statusMessage.value = "📊 Lectura recibida: ${glucose} mg/dL"
            
        } catch (e: Exception) {
            _statusMessage.value = "⚠️ Error interpretando datos: ${e.message}"
        }
    }
    
    fun disconnect() {
        stopScan()
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        glucoseControlPoint = null
        _isConnected.value = false
        _isConnecting.value = false
        _isPairing.value = false
        _pairingRequired.value = false
        _glucometerReading.value = null
        _statusMessage.value = "Desconectado"
    }
    
    fun cleanup() {
        disconnect()
    }
    
    // Función para reiniciar el proceso cuando hay error E62
    fun retryPairing() {
        disconnect()
        _pairingRequired.value = true
        _statusMessage.value = """
            🔄 Reintentando emparejamiento...
            
            Pasos importantes:
            1. ¡APAGUE completamente el glucómetro!
            2. Presione 'Set' + 'Mem' hasta ver 'bLE'
            3. El glucómetro mostrará 3 dígitos
            4. Ingrese esos 3 dígitos aquí
            5. Presione conectar nuevamente
        """.trimIndent()
    }
    
    // Función para limpiar emparejamiento problemático
    fun clearPairing() {
        disconnect()
        _pairingRequired.value = false
        _isPairing.value = false
        pairingPassword = ""
        _devicePassword.value = ""
        _statusMessage.value = """
            🧹 Emparejamiento limpiado
            
            Si el problema persiste:
            1. Vaya a Configuración de Android
            2. Bluetooth → Dispositivos emparejados
            3. Olvide el dispositivo Meter$deviceCode
            4. Reinicie Bluetooth y reintente
        """.trimIndent()
    }
    
    // Función para obtener comentario inteligente sobre glucosa
    fun getGlucoseComment(glucose: Int?): String {
        return when {
            glucose == null -> "Esperando lectura del glucómetro..."
            glucose < 70 -> "⚠️ Glucosa baja. Consulte a su médico"
            glucose > 125 -> "⚠️ Glucosa alta. Consulte a su médico"  
            glucose in 100..125 -> "⚡ Glucosa en límite normal. Monitoree regularmente"
            else -> "✅ Glucosa normal. ¡Excelente!"
        }
    }
} 