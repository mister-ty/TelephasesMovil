package com.example.telephases.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*



@SuppressLint("MissingPermission")
class BLEOximeterManager(private val context: Context) {
    private val TAG = "BLEOximeterManager"
    private val OXIMETER_NAMES = listOf("PC-60F_SN83919f", "PC-60F_SN83919b")
    
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    
    private var bluetoothGatt: BluetoothGatt? = null
    private var scanning = false
    private var measurementHandler: Handler? = null
    
    // Estados observables
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    
    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting.asStateFlow()
    
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()
    
    private val _isMeasuring = MutableStateFlow(false)
    val isMeasuring: StateFlow<Boolean> = _isMeasuring.asStateFlow()
    
    private val _oximeterReading = MutableStateFlow<OximeterReading?>(null)
    val oximeterReading: StateFlow<OximeterReading?> = _oximeterReading.asStateFlow()
    
    // Para el sistema de promediado
    private val _currentReading = MutableStateFlow<OximeterReading?>(null)
    val currentReading: StateFlow<OximeterReading?> = _currentReading.asStateFlow()
    
    private val _readingsList = MutableStateFlow<List<OximeterReading>>(emptyList())
    val readingsList: StateFlow<List<OximeterReading>> = _readingsList.asStateFlow()
    
    private val _statusMessage = MutableStateFlow("Listo para conectar")
    val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()
    
    private val _devicesFound = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devicesFound: StateFlow<List<BluetoothDevice>> = _devicesFound.asStateFlow()
    
    private val _measurementProgress = MutableStateFlow(0)
    val measurementProgress: StateFlow<Int> = _measurementProgress.asStateFlow()

    fun hasBluetoothPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.device?.let { device ->
                if (device.name != null && OXIMETER_NAMES.contains(device.name)) {
                    Log.i(TAG, "✅ Oxímetro encontrado: ${device.name}")
                    val currentDevices = _devicesFound.value.toMutableList()
                    if (!currentDevices.any { it.address == device.address }) {
                        currentDevices.add(device)
                        _devicesFound.value = currentDevices
                    }
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e(TAG, "❌ Falló el escaneo BLE: $errorCode")
            _statusMessage.value = "Error en escaneo BLE"
            _isScanning.value = false
        }
    }

    fun startScan() {
        if (!hasBluetoothPermissions()) {
            _statusMessage.value = "Permisos de Bluetooth requeridos"
            return
        }

        val scanner = bluetoothAdapter?.bluetoothLeScanner
        if (scanner == null) {
            _statusMessage.value = "Bluetooth no disponible"
            return
        }

        val filters = listOf<ScanFilter>()
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        _isScanning.value = true
        _statusMessage.value = "Buscando oxímetro..."
        _devicesFound.value = emptyList()
        
        scanner.startScan(filters, settings, scanCallback)

        Handler(Looper.getMainLooper()).postDelayed({
            if (_isScanning.value) {
                stopScan()
                if (_devicesFound.value.isEmpty()) {
                    _statusMessage.value = "No se encontró oxímetro"
                }
            }
        }, 10000) // 10 segundos de escaneo
    }

    fun stopScan() {
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
        _isScanning.value = false
    }

    fun connectToDevice(device: BluetoothDevice) {
        stopScan()
        _isConnecting.value = true
        _statusMessage.value = "Conectando a ${device.name ?: "oxímetro"}..."
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }

    fun findAndConnect() {
        if (_isConnected.value || _isConnecting.value) return
        
        startScan()
        
        // Auto-conectar al primer dispositivo encontrado después de 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            if (_devicesFound.value.isNotEmpty() && !_isConnected.value) {
                connectToDevice(_devicesFound.value.first())
            }
        }, 3000)
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.i(TAG, "✅ Conectado a GATT")
                    _isConnecting.value = false
                    _isConnected.value = true
                    _statusMessage.value = "Conectado. Descubriendo servicios..."
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.w(TAG, "📴 Desconectado del GATT")
                    _isConnecting.value = false
                    _isConnected.value = false
                    _isMeasuring.value = false
                    _statusMessage.value = "Desconectado"
                    measurementHandler?.removeCallbacksAndMessages(null)
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                for (service in gatt.services) {
                    for (characteristic in service.characteristics) {
                        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                            enableNotification(gatt, characteristic)
                            return
                        }
                    }
                }
                _statusMessage.value = "No se encontró característica con notify"
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            val data = characteristic.value
            parseOximeterData(data)
        }
    }

    private fun enableNotification(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        gatt.setCharacteristicNotification(characteristic, true)
        val descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
        descriptor?.let {
            it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(it)
        }
        
        startMeasurement()
    }

    private fun startMeasurement() {
        _isMeasuring.value = true
        _statusMessage.value = "Midiendo durante 20 segundos..."
        _measurementProgress.value = 0
        
        // Limpiar lecturas anteriores
        _readingsList.value = emptyList()
        _oximeterReading.value = null
        
        measurementHandler = Handler(Looper.getMainLooper())
        
        // Actualizar progreso cada segundo
        val progressRunnable = object : Runnable {
            override fun run() {
                val currentProgress = _measurementProgress.value
                if (currentProgress < 20 && _isMeasuring.value) {
                    _measurementProgress.value = currentProgress + 1
                    measurementHandler?.postDelayed(this, 1000)
                }
            }
        }
        measurementHandler?.post(progressRunnable)
        
        // Finalizar medición después de 20 segundos
        measurementHandler?.postDelayed({
            finishMeasurement()
        }, 20000)
    }

    private fun finishMeasurement() {
        _isMeasuring.value = false
        _measurementProgress.value = 20
        
        // Calcular promedio de todas las lecturas
        val readings = _readingsList.value
        if (readings.isNotEmpty()) {
            val avgSpo2 = readings.map { it.spo2 }.average().toInt()
            val avgPulse = readings.map { it.pulseRate }.average().toInt()
            val avgPi = readings.map { it.pi }.average()
            
            val averageReading = OximeterReading(avgSpo2, avgPulse, avgPi)
            _oximeterReading.value = averageReading
            
            Log.i(TAG, "📊 Promedio final - SpO₂: $avgSpo2% | Pulso: $avgPulse bpm | PI: ${String.format("%.1f", avgPi)}%")
            _statusMessage.value = "Medición completada - Promedio calculado"
        } else {
            _statusMessage.value = "No se recibieron datos del oxímetro"
        }
        
        // Desconectar automáticamente
        Handler(Looper.getMainLooper()).postDelayed({
            disconnect()
        }, 2000)
    }

    private fun parseOximeterData(data: ByteArray) {
        if (data.size >= 10 && data[0] == 0xAA.toByte() && data[1] == 0x55.toByte() && data[4] == 0x01.toByte()) {
            val spo2 = data[5].toInt() and 0xFF
            val pr = (data[6].toInt() and 0xFF) + ((data[7].toInt() and 0xFF) shl 8)
            val pi = (data[8].toInt() and 0xFF) / 10.0
            
            // Solo actualizar si los valores son válidos
            if (spo2 > 0 && spo2 <= 100 && pr > 0 && pr <= 300) {
                val reading = OximeterReading(spo2, pr, pi)
                
                // Actualizar lectura actual para mostrar en tiempo real
                _currentReading.value = reading
                
                // Agregar a la lista de lecturas para promediar
                val currentList = _readingsList.value.toMutableList()
                currentList.add(reading)
                _readingsList.value = currentList
                
                Log.i(TAG, "📊 SpO₂: $spo2% | Pulso: $pr bpm | PI: $pi% (Lectura ${currentList.size})")
            }
        }
    }

    fun disconnect() {
        measurementHandler?.removeCallbacksAndMessages(null)
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        stopScan()
    }

    fun resetReading() {
        _oximeterReading.value = null
        _currentReading.value = null
        _readingsList.value = emptyList()
        _measurementProgress.value = 0
        _statusMessage.value = "Listo para conectar"
    }
} 