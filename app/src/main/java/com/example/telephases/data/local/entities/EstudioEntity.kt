package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

/**
 * Entidad para los estudios (agrupación de exámenes)
 */
@Entity(
    tableName = "estudios",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["pacienteId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["creadoPorUsuarioId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CitaEntity::class,
            parentColumns = ["id"],
            childColumns = ["citaId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class EstudioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pacienteId: String, // UUID del paciente
    val creadoPorUsuarioId: String, // UUID del usuario que creó el estudio
    val citaId: Int? = null, // Opcional, si el estudio viene de una cita
    val fechaEstudio: String = Instant.now().toString(),
    val resumen: String? = null, // Conclusión del médico para el conjunto de exámenes
    val serverId: Int? = null // ID en el servidor para sincronización
)
