// Archivo: BLEModels.kt

package com.example.telephases.ui.screens

// Modelo de datos compartido
data class BLEDevice(
    val name: String,
    val address: String,
    val rssi: Int = 0
)

// Modelo de datos para la pesa
data class ScaleReading(
    val weight: Double,
    val impedance: Int,
    val unit: String = "kg",
    val timestamp: Long = System.currentTimeMillis()
)

data class BloodPressureReading(
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
    val hasArrhythmia: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class OximeterReading(
    val spo2: Int,
    val pulseRate: Int,
    val pi: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class TemperatureReading(
    val temperature: Double,
    val mode: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class GlucometerReading(
    val glucose: Int,
    val unit: String = "mg/dL",
    val sequenceNumber: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val type: Int,
    val location: Int
)

