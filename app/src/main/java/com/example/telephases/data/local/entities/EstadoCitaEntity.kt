package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

/**
 * Tabla de referencia de estados de cita
 */
@Entity(
    tableName = "estado_cita",
    indices = [
        Index(value = ["nombre"], unique = true)
    ]
)
data class EstadoCitaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "descripcion")
    val descripcion: String? = null
)


