package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Entidad principal de citas
 */
@Entity(
    tableName = "citas",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["paciente_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["creado_por_usuario_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = EstadoCitaEntity::class,
            parentColumns = ["id"],
            childColumns = ["estado_cita_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["paciente_id"]),
        Index(value = ["creado_por_usuario_id"]),
        Index(value = ["estado_cita_id"])
    ]
)
data class CitaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "paciente_id")
    val pacienteId: String,

    @ColumnInfo(name = "creado_por_usuario_id")
    val creadoPorUsuarioId: String,

    @ColumnInfo(name = "estado_cita_id")
    val estadoCitaId: Int = 1,

    // ISO-8601 con zona horaria como texto local
    @ColumnInfo(name = "fecha_cita")
    val fechaCita: String,

    @ColumnInfo(name = "observaciones_admin")
    val observacionesAdmin: String? = null,

    @ColumnInfo(name = "observaciones_paciente")
    val observacionesPaciente: String? = null,

    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: String,

    @ColumnInfo(name = "fecha_modificacion")
    val fechaModificacion: String
)


