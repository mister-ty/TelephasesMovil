package com.example.telephases.ui.screens

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build // <<<<<<<<<<<<<<<<<<< ¡AQUÍ ESTÁ EL ARREGLO!
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*





class BLETemperatureManager(private val context: Context) {

    // UUIDs del servicio y características del termómetro BLE
    private val serviceUUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")
    private val notifyUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
    private val writeUUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb")
    private val clientCharacteristicConfigUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")


    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    private var bluetoothGatt: BluetoothGatt? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    // Estados observables
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private val _temperature = MutableStateFlow<TemperatureReading?>(null)
    val temperature: StateFlow<TemperatureReading?> = _temperature

    private val _devicesFound = MutableStateFlow<List<BLEDevice>>(emptyList())
    val devicesFound: StateFlow<List<BLEDevice>> = _devicesFound

    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs

    // Función para agregar logs con timestamp
    private fun log(message: String) {
        val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
            .format(Date())
        mainHandler.post {
            _logs.value = listOf("[$timestamp] $message") + _logs.value
        }
        android.util.Log.d("BLETemp", message)
    }

    // Verificar si Bluetooth está habilitado
    fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true

    // Verificar permisos
    fun hasBluetoothPermissions(): Boolean {
        val hasScanPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        } else {
            true // No se requiere para APIs < 31
        }
        val hasConnectPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            true // No se requiere para APIs < 31
        }
        // El permiso de ubicación es necesario para el escaneo en APIs < 31
        val hasLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        return hasScanPermission && hasConnectPermission && hasLocationPermission
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION])
    fun startScan() {
        if (!isBluetoothEnabled()){
            log("❌ Bluetooth no está activado.")
            return
        }
        if (!hasBluetoothPermissions()) {
            log("❌ Faltan permisos de Bluetooth o Ubicación.")
            return
        }

        if (_isScanning.value) {
            log("⚠️ El escaneo ya está en progreso.")
            return
        }

        log("🧼 Limpiando lista de dispositivos encontrados...")
        _devicesFound.value = emptyList()
        _isScanning.value = true

        try {
            val scanner = bluetoothAdapter?.bluetoothLeScanner
            if (scanner == null) {
                log("❌ Scanner BLE no disponible.")
                _isScanning.value = false
                return
            }

            log("🎯 Iniciando escaneo con filtro para el servicio: $serviceUUID")

            // 1. Crear un filtro para buscar el servicio específico del termómetro.
            val scanFilter = ScanFilter.Builder()
                .setServiceUuid(ParcelUuid(serviceUUID))
                .build()
            val filters = listOf(scanFilter)

            // 2. Configuración de escaneo agresiva para encontrar el dispositivo rápidamente.
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

            // 3. Iniciar el escaneo CON EL FILTRO.
            scanner.startScan(filters, settings, scanCallback)

            // Detener escaneo después de 15 segundos para ahorrar batería.
            mainHandler.postDelayed({
                if (_isScanning.value) {
                    stopScan()
                }
            }, 15000)

        } catch (e: Exception) {
            log("❌ Error al iniciar escaneo: ${e.message}")
            _isScanning.value = false
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScan() {
        if (!_isScanning.value) return
        log("🛑 Deteniendo escaneo...")
        try {
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
        } catch (e: Exception) {
            log("❌ Error al detener escaneo: ${e.message}")
        } finally {
            _isScanning.value = false
            log("📊 Escaneo finalizado. Dispositivos encontrados: ${_devicesFound.value.size}")
        }
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            try {
                val deviceName = result.device.name ?: "Dispositivo sin nombre"
                val deviceAddress = result.device.address

                // Evitar duplicados en la lista
                if (_devicesFound.value.none { it.address == deviceAddress }) {
                    log("📡 Encontrado: $deviceName ($deviceAddress) RSSI: ${result.rssi}")
                    _devicesFound.value += BLEDevice(deviceName, deviceAddress, result.rssi)
                    
                    // Conectar automáticamente al primer dispositivo encontrado
                    if (!_isConnected.value) {
                        log("🔗 Conectando automáticamente a $deviceName...")
                        stopScan()
                        connectToDevice(deviceAddress)
                    }
                }
            } catch (e: SecurityException) {
                log("❌ Error de seguridad en onScanResult: ${e.message}")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            val errorMsg = when (errorCode) {
                SCAN_FAILED_ALREADY_STARTED -> "Escaneo ya iniciado"
                SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> "Error de registro de aplicación"
                SCAN_FAILED_INTERNAL_ERROR -> "Error interno del sistema Bluetooth"
                SCAN_FAILED_FEATURE_UNSUPPORTED -> "Característica no soportada en este dispositivo"
                else -> "Error desconocido (código $errorCode)"
            }
            log("❌ Escaneo falló: $errorMsg")
            _isScanning.value = false
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN])
    fun connectToDevice(address: String) {
        if (!hasBluetoothPermissions()) {
            log("❌ No hay permisos para conectar.")
            return
        }
        val device = bluetoothAdapter?.getRemoteDevice(address)
        if (device == null) {
            log("❌ Dispositivo no encontrado con la dirección: $address")
            return
        }

        log("🔗 Conectando a ${device.name ?: device.address}...")

        // Detener el escaneo antes de conectar es una buena práctica
        if (_isScanning.value) {
            stopScan()
        }

        // Cerrar conexión anterior y conectar de nuevo
        mainHandler.post {
            bluetoothGatt?.close()
            bluetoothGatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            val deviceName = gatt.device.name ?: gatt.device.address
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    _isConnected.value = true
                    log("✅ Conectado a $deviceName")
                    mainHandler.postDelayed({ gatt.discoverServices() }, 600)
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    _isConnected.value = false
                    log("🔌 Desconectado de $deviceName")
                    bluetoothGatt?.close()
                    bluetoothGatt = null
                }
            } else {
                // Manejar errores de conexión
                log("❌ Error de conexión. Status: $status, Desconectando de $deviceName")
                _isConnected.value = false
                bluetoothGatt?.close()
                bluetoothGatt = null
            }
        }

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                log("✅ Servicios descubiertos.")
                val service = gatt.getService(serviceUUID)
                if (service == null) {
                    log("❌ Servicio $serviceUUID no encontrado.")
                    return
                }
                val notifyChar = service.getCharacteristic(notifyUUID)
                if (notifyChar == null) {
                    log("❌ Característica de notificación $notifyUUID no encontrada.")
                    return
                }
                enableNotifications(gatt, notifyChar)
            } else {
                log("❌ Descubrimiento de servicios falló con status: $status")
            }
        }

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        private fun enableNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            gatt.setCharacteristicNotification(characteristic, true)
            val descriptor = characteristic.getDescriptor(clientCharacteristicConfigUUID)
            if (descriptor != null) {
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
                log("🔔 Habilitando notificaciones para ${characteristic.uuid}")
            } else {
                log("❌ Descriptor de configuración no encontrado.")
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS) {
                log("👍 Notificaciones habilitadas correctamente.")
                log("🕐 Esperando datos de temperatura...")
            } else {
                log("❌ Error al escribir el descriptor. Status: $status")
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            decodeNotification(characteristic.value)
        }
    }

    private fun decodeNotification(data: ByteArray) {
        val hex = data.joinToString(" ") { "%02X".format(it) }
        log("📦 Datos recibidos (hex): $hex")

        if (data.size >= 7 && data[2] == 0xC1.toByte()) {
            val tempRaw = ((data[4].toInt() and 0xFF) shl 8) or (data[5].toInt() and 0xFF)
            val tempC = tempRaw / 100.0
            val mode = data[6].toInt() and 0xFF
            val modeStr = when (mode) {
                0x01 -> "Adulto frente"
                0x02 -> "Niño frente"
                0x03 -> "Conducto auditivo"
                0x04 -> "Objeto"
                else -> "Desconocido (0x${Integer.toHexString(mode)})"
            }
            _temperature.value = TemperatureReading(tempC, modeStr)
            log("🌡 Temperatura: %.2f °C — Modo: %s".format(tempC, modeStr))
        } else {
            log("❓ Formato de datos no reconocido.")
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnect() {
        if (bluetoothGatt == null) return
        log("🔌 Intentando desconectar...")
        bluetoothGatt?.disconnect()
        // La llamada a gatt.close() se maneja en onConnectionStateChange
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun cleanup() {
        mainHandler.removeCallbacksAndMessages(null)
        bluetoothGatt?.close()
        bluetoothGatt = null
    }
}