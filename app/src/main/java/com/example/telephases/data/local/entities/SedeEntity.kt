package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index
import androidx.room.ForeignKey

/**
 * Entidad Room para representar una sede en la base de datos local
 */
@Entity(
    tableName = "sedes",
    foreignKeys = [
        ForeignKey(
            entity = EntidadSaludEntity::class,
            parentColumns = ["id"],
            childColumns = ["entidadSaludId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["entidadSaludId"], name = "index_sedes_entidadSaludId"),
        Index(value = ["nombreSede"], name = "index_sedes_nombreSede")
    ]
)
data class SedeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "entidadSaludId")
    val entidadSaludId: Int,

    @ColumnInfo(name = "nombreSede")
    val nombreSede: String,

    @ColumnInfo(name = "direccion")
    val direccion: String? = null,

    @ColumnInfo(name = "telefono")
    val telefono: String? = null,

    @ColumnInfo(name = "ciudad")
    val ciudad: String? = null,

    @ColumnInfo(name = "responsableSedeNombre")
    val responsableSedeNombre: String? = null,

    @ColumnInfo(name = "activa")
    val activa: Boolean = true,

    @ColumnInfo(name = "serverId")
    val serverId: Int? = null
)