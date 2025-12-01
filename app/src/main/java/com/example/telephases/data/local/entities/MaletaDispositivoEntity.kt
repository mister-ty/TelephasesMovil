package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidad para la relación many-to-many entre maletas y dispositivos
 */
@Entity(
    tableName = "maleta_dispositivos",
    primaryKeys = ["maletaId", "dispositivoId"],
    foreignKeys = [
        ForeignKey(
            entity = MaletaEntity::class,
            parentColumns = ["id"],
            childColumns = ["maletaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DispositivoEntity::class,
            parentColumns = ["id"],
            childColumns = ["dispositivoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MaletaDispositivoEntity(
    val maletaId: Int,
    val dispositivoId: Int
)
