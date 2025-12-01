package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Entidad para la asignación de usuarios a sedes (relación many-to-many)
 */
@Entity(
    tableName = "usuario_sedes",
    primaryKeys = ["usuarioId", "sedeId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SedeEntity::class,
            parentColumns = ["id"],
            childColumns = ["sedeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UsuarioSedeEntity(
    val usuarioId: String, // UUID del usuario
    val sedeId: Int,
    val fechaAsignacion: String = java.time.Instant.now().toString(),
    val activa: Boolean = true
)
