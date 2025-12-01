package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Tabla pivote cita <-> tipo_examen con información completa del examen
 */
@Entity(
    tableName = "cita_examenes_previstos",
    primaryKeys = ["cita_id", "tipo_examen_id"],
    foreignKeys = [
        ForeignKey(
            entity = CitaEntity::class,
            parentColumns = ["id"],
            childColumns = ["cita_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
        // Foreign Key a TipoExamenEntity comentada temporalmente
        // ForeignKey(
        //     entity = TipoExamenEntity::class,
        //     parentColumns = ["id"],
        //     childColumns = ["tipo_examen_id"],
        //     onDelete = ForeignKey.NO_ACTION,
        //     onUpdate = ForeignKey.NO_ACTION
        // )
    ],
    indices = [
        Index(value = ["tipo_examen_id"])
    ]
)
data class CitaExamenPrevistoEntity(
    @ColumnInfo(name = "cita_id")
    val citaId: Int,

    @ColumnInfo(name = "tipo_examen_id")
    val tipoExamenId: Int,
    
    @ColumnInfo(name = "nombre")
    val nombre: String,
    
    @ColumnInfo(name = "descripcion")
    val descripcion: String
)


