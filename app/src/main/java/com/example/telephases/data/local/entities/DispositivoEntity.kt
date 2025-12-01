package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Entidad para los dispositivos médicos
 */
@Entity(tableName = "dispositivos")
data class DispositivoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipoDispositivo: String, // Ej: 'Tensiómetro', 'Pulsioxímetro'
    val marca: String? = null,
    val modelo: String? = null,
    val serial: String, // Serial único del dispositivo
    val fechaAdquisicion: String? = null, // Fecha de adquisición
    val activo: Boolean = true,
    val serverId: Int? = null // ID en el servidor para sincronización
)
