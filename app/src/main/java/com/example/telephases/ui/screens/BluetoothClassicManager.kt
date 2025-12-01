package com.example.telephases.ui.screens

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothClassicManager(private val context: Context) {

    // UUID estándar para SPP (Serial Port Profile)
    private val SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private val bluetoothAdapter: BluetoothAdapter? =
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    // Estados observables
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _temperature = MutableStateFlow<TemperatureReading?>(null)
    val temperature: StateFlow<TemperatureReading?> = _temperature

    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs

    private val _pairedDevices = MutableStateFlow<List<BLEDevice>>(emptyList())
    val pairedDevices: StateFlow<List<BLEDevice>> = _pairedDevices

    private fun log(message: String) {
        val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
            .format(Date())
        _logs.value = _logs.value + "[$timestamp] $message"
        Log.d("BTClassic", message)
    }

    // Obtener dispositivos emparejados
    fun getPairedDevices() {
        try {
            val devices = bluetoothAdapter?.bondedDevices?.map { device ->
                BLEDevice(
                    name = device.name ?: "Sin nombre",
                    address = device.address
                )
            } ?: emptyList()

            _pairedDevices.value = devices
            log("📱 ${devices.size} dispositivos emparejados encontrados")
            devices.forEach {
                log("   - ${it.name} (${it.address})")
            }
        } catch (e: SecurityException) {
            log("❌ Error al obtener dispositivos: ${e.message}")
        }
    }

    // Conectar a dispositivo Bluetooth clásico
    suspend fun connectToDevice(address: String) = withContext(Dispatchers.IO) {
        try {
            log("🔗 Conectando a $address (Bluetooth Clásico)...")

            val device = bluetoothAdapter?.getRemoteDevice(address)
            if (device == null) {
                log("❌ Dispositivo no encontrado")
                return@withContext
            }

            // Cerrar conexión anterior
            disconnect()

            // Crear socket
            bluetoothSocket = device.createRfcommSocketToServiceRecord(SPP_UUID)

            // Cancelar descubrimiento para mejorar la conexión
            bluetoothAdapter?.cancelDiscovery()

            // Conectar
            bluetoothSocket?.connect()

            // Obtener streams
            inputStream = bluetoothSocket?.inputStream
            outputStream = bluetoothSocket?.outputStream

            _isConnected.value = true
            log("✅ Conectado exitosamente")

            // Iniciar lectura de datos
            startReading()

        } catch (e: IOException) {
            log("❌ Error de conexión: ${e.message}")

            // Intentar método alternativo
            try {
                log("🔄 Intentando conexión alternativa...")
                val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(address)
                val method = bluetoothDevice?.javaClass?.getMethod("createRfcommSocket", Int::class.java)
                bluetoothSocket = method?.invoke(bluetoothDevice, 1) as BluetoothSocket?
                bluetoothSocket?.connect()

                inputStream = bluetoothSocket?.inputStream
                outputStream = bluetoothSocket?.outputStream

                _isConnected.value = true
                log("✅ Conectado con método alternativo")

                startReading()
            } catch (e2: Exception) {
                log("❌ Conexión alternativa falló: ${e2.message}")
                _isConnected.value = false
            }
        } catch (e: SecurityException) {
            log("❌ Error de permisos: ${e.message}")
        }
    }

    // Leer datos continuamente
    private suspend fun startReading() = withContext(Dispatchers.IO) {
        val buffer = ByteArray(1024)
        var bytes: Int

        log("📖 Iniciando lectura de datos...")

        while (_isConnected.value) {
            try {
                bytes = inputStream?.read(buffer) ?: -1
                if (bytes > 0) {
                    val data = buffer.copyOf(bytes)
                    processData(data)
                }
            } catch (e: IOException) {
                log("❌ Error de lectura: ${e.message}")
                _isConnected.value = false
                break
            }
        }
    }

    // Procesar datos recibidos
    private fun processData(data: ByteArray) {
        val hex = data.joinToString(" ") { String.format("%02X", it) }
        log("📦 Datos recibidos: $hex")

        // Intentar decodificar como el protocolo esperado
        if (data.size >= 7 && data[2] == 0xC1.toByte()) {
            // Protocolo original
            val tempRaw = ((data[4].toInt() and 0xFF) shl 8) or (data[5].toInt() and 0xFF)
            val tempC = tempRaw / 100.0

            val mode = when (data[6].toInt() and 0xFF) {
                0x01 -> "Adulto frente"
                0x02 -> "Niño frente"
                0x03 -> "Conducto auditivo"
                0x04 -> "Objeto"
                else -> "Desconocido"
            }

            _temperature.value = TemperatureReading(tempC, mode)
            log("🌡 Temperatura: %.2f °C — Modo: %s".format(tempC, mode))
        } else {
            // Intentar otros formatos comunes
            tryAlternativeFormats(data)
        }
    }

    // Intentar formatos alternativos
    private fun tryAlternativeFormats(data: ByteArray) {
        // Formato ASCII simple (ej: "36.5C\n")
        try {
            val text = String(data).trim()
            log("📝 Texto recibido: $text")

            // Buscar números decimales
            val regex = """(\d+\.?\d*)""".toRegex()
            val match = regex.find(text)
            if (match != null) {
                val temp = match.value.toDoubleOrNull()
                if (temp != null && temp in 20.0..50.0) {
                    _temperature.value = TemperatureReading(temp, "Bluetooth Clásico")
                    log("🌡 Temperatura detectada: $temp °C")
                }
            }
        } catch (e: Exception) {
            log("❌ Error al procesar texto: ${e.message}")
        }
    }

    // Enviar comando al dispositivo
    suspend fun sendCommand(command: ByteArray) = withContext(Dispatchers.IO) {
        try {
            outputStream?.write(command)
            outputStream?.flush()
            log("📤 Comando enviado: ${command.joinToString(" ") { String.format("%02X", it) }}")
        } catch (e: IOException) {
            log("❌ Error al enviar comando: ${e.message}")
        }
    }

    // Función de simulación para pruebas
    fun simulateTemperatureReading() {
        val temp = 36.5 + (Math.random() * 2)
        _temperature.value = TemperatureReading(
            temperature = temp,
            mode = "Bluetooth Clásico (Simulado)"
        )
        _isConnected.value = true
        log("🌡 Temperatura simulada: %.1f°C".format(temp))
    }

    // Desconectar
    fun disconnect() {
        try {
            inputStream?.close()
            outputStream?.close()
            bluetoothSocket?.close()

            inputStream = null
            outputStream = null
            bluetoothSocket = null

            _isConnected.value = false
            _temperature.value = null

            log("🔌 Desconectado")
        } catch (e: Exception) {
            log("❌ Error al desconectar: ${e.message}")
        }
    }

    // Limpiar
    fun cleanup() {
        disconnect()
        _logs.value = emptyList()
        _pairedDevices.value = emptyList()
    }
}