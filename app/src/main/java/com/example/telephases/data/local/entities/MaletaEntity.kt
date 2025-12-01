package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index
import androidx.room.ForeignKey

/**
 * Entidad Room para representar una maleta en la base de datos local
 */
@Entity(
    tableName = "maletas",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["asignadaAUsuarioId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = EntidadSaludEntity::class,
            parentColumns = ["id"],
            childColumns = ["entidadSaludId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["entidadSaludId"], name = "index_maletas_entidadSaludId"),
        Index(value = ["maletaUid"], name = "index_maletas_maletaUid", unique = true),
        Index(value = ["identificadorInvima"], name = "index_maletas_identificadorInvima"),
        Index(value = ["activa"], name = "index_maletas_activa"),
        Index(value = ["asignadaAUsuarioId"], name = "index_maletas_asignadaAUsuarioId")
    ]
)
data class MaletaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "maletaUid")
    val maletaUid: String? = null,

    @ColumnInfo(name = "identificadorInvima")
    val identificadorInvima: String,

    @ColumnInfo(name = "nombreMaleta")
    val nombreMaleta: String? = null,

    @ColumnInfo(name = "descripcion")
    val descripcion: String? = null,

    @ColumnInfo(name = "asignadaAUsuarioId")
    val asignadaAUsuarioId: String? = null,

    @ColumnInfo(name = "entidadSaludId")
    val entidadSaludId: Int? = null,

    @ColumnInfo(name = "ultimaRevision")
    val ultimaRevision: String? = null,

    @ColumnInfo(name = "activa")
    val activa: Boolean = true,

    @ColumnInfo(name = "serverId")
    val serverId: Int? = null
) {
    companion object {
        /**
         * Crea una entidad desde el modelo de la API
         */
        fun fromApiModel(maleta: com.example.telephases.network.Maleta): MaletaEntity {
            return MaletaEntity(
                id = 0, // Se asignará automáticamente
                maletaUid = null,
                identificadorInvima = maleta.id, // Usar el ID como identificador
                nombreMaleta = maleta.nombre,
                descripcion = maleta.descripcion,
                asignadaAUsuarioId = maleta.usuario_id,
                entidadSaludId = maleta.entidad_salud_id,
                ultimaRevision = maleta.fecha_modificacion,
                activa = maleta.estado == "activa",
                serverId = maleta.id.toIntOrNull()
            )
        }

        /**
         * Crea una nueva entidad para datos creados offline
         */
        fun createForOffline(
            identificadorInvima: String,
            nombreMaleta: String? = null,
            descripcion: String? = null,
            asignadaAUsuarioId: String? = null,
            entidadSaludId: Int? = null
        ): MaletaEntity {
            return MaletaEntity(
                id = 0, // Se asignará automáticamente
                maletaUid = null,
                identificadorInvima = identificadorInvima,
                nombreMaleta = nombreMaleta,
                descripcion = descripcion,
                asignadaAUsuarioId = asignadaAUsuarioId,
                entidadSaludId = entidadSaludId,
                ultimaRevision = null,
                activa = true,
                serverId = null
            )
        }
    }
}