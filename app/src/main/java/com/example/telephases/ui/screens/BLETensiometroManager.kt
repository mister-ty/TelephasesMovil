package com.example.telephases.ui.screens

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class BLETensiometroManager(private val context: Context) {

    private val TARGET_DEVICE_NAME = "Y911-66FC"
    private val SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb")
    private val NOTIFY_UUID = UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb")
    private val CLIENT_CHARACTERISTIC_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    private var bluetoothGatt: BluetoothGatt? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected
    private val _isConnecting = MutableStateFlow(false) // Usaremos este para 'Buscando...'
    val isConnecting: StateFlow<Boolean> = _isConnecting
    private val _bloodPressureReading = MutableStateFlow<BloodPressureReading?>(null)
    val bloodPressureReading: StateFlow<BloodPressureReading?> = _bloodPressureReading
    private val _statusMessage = MutableStateFlow("Listo para iniciar")
    val statusMessage: StateFlow<String> = _statusMessage

    private fun log(message: String) {
        val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(Date())
        mainHandler.post { _statusMessage.value = message }
        android.util.Log.d("BLETensiometro", "[$timestamp] $message")
    }

    fun hasBluetoothPermissions(): Boolean {
        val hasScanPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        } else true
        val hasConnectPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else true
        val hasLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return hasScanPermission && hasConnectPermission && hasLocationPermission
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION])
    fun findAndConnect() {
        if (_isConnecting.value || _isConnected.value) return
        _bloodPressureReading.value = null
        _isConnecting.value = true
        log("Buscando tensiómetro '$TARGET_DEVICE_NAME'...")
        try {
            val scanner = bluetoothAdapter?.bluetoothLeScanner ?: return
            val filter = ScanFilter.Builder().setDeviceName(TARGET_DEVICE_NAME).build()
            val settings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
            scanner.startScan(listOf(filter), settings, scanCallback)
            mainHandler.postDelayed({
                if (_isConnecting.value && !_isConnected.value) {
                    log("No se encontró el tensiómetro. Intente de nuevo.")
                    stopScan()
                }
            }, 20000)
        } catch (e: Exception) {
            log("Error al iniciar escaneo: ${e.message}")
            _isConnecting.value = false
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun stopScan() {
        if (!_isConnecting.value && !_isConnected.value) return
        try {
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
        } finally {
            _isConnecting.value = false
        }
    }

    private val scanCallback = object : ScanCallback() {
        @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (!_isConnecting.value) return
            log("¡Tensiómetro '$TARGET_DEVICE_NAME' encontrado!")
            stopScan()
            connectToDevice(result.device)
        }
        override fun onScanFailed(errorCode: Int) {
            log("Búsqueda falló: código $errorCode")
            _isConnecting.value = false
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun connectToDevice(device: BluetoothDevice) {
        log("Conectando...")
        mainHandler.post {
            bluetoothGatt?.close()
            bluetoothGatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnect() {
        log("Desconectando...")
        _isConnecting.value = false
        bluetoothGatt?.disconnect()
    }

    private val gattCallback = object : BluetoothGattCallback() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    _isConnected.value = true
                    _isConnecting.value = false
                    log("Conectado. Descubriendo servicios...")
                    mainHandler.postDelayed({ gatt.discoverServices() }, 600)
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    _isConnected.value = false
                    _isConnecting.value = false
                    log("Desconectado. Listo para iniciar.")
                    bluetoothGatt?.close()
                }
            } else {
                log("Error de conexión. Status: $status")
                _isConnected.value = false
                _isConnecting.value = false
                bluetoothGatt?.close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                log("Servicios descubiertos.")
                val characteristic = gatt.getService(SERVICE_UUID)?.getCharacteristic(NOTIFY_UUID)
                if (characteristic == null) {
                    log("Característica de notificación no encontrada.")
                    return
                }
                enableNotifications(gatt, characteristic)
            } else {
                log("Descubrimiento de servicios falló: $status")
            }
        }

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        private fun enableNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            gatt.setCharacteristicNotification(characteristic, true)
            val descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
            descriptor?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    gatt.writeDescriptor(it, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                } else {
                    @Suppress("DEPRECATION")
                    it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    @Suppress("DEPRECATION")
                    gatt.writeDescriptor(it)
                }
                log("Listo. Inicie una medición en el tensiómetro.")
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray) {
            decodeBloodPressureData(value)
        }

        @Deprecated("Used for older Android versions")
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            @Suppress("DEPRECATION")
            decodeBloodPressureData(characteristic.value)
        }
    }

    private fun decodeBloodPressureData(data: ByteArray) {
        val hex = data.joinToString(" ") { "%02X".format(it) }
        log("Paquete recibido: $hex")
        if (data.size >= 12 && data[3].toInt() and 0xFF == 0x88) {
            val systolic = (data[5].toInt() and 0xFF) + ((data[6].toInt() and 0xFF) shl 8)
            val diastolic = data[7].toInt() and 0xFF
            val pulse = data[8].toInt() and 0xFF
            val hasArrhythmia = data[9].toInt() != 0
            val reading = BloodPressureReading(systolic, diastolic, pulse, hasArrhythmia)
            _bloodPressureReading.value = reading
            log("Resultado: ${reading.systolic}/${reading.diastolic} mmHg, ${reading.pulse} bpm")
            disconnect()
        }
    }
}