package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ColumnInfo
import java.time.Instant

/**
 * Entidad para las entidades de salud (hospitales, clínicas, etc.)
 */
@Entity(
    tableName = "entidades_salud",
    indices = [
        Index(value = ["nit"]),
        Index(value = ["activa"]),
        Index(value = ["serverId"])
    ]
)
data class EntidadSaludEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreEntidad: String,
    val nit: String? = null,
    val contactoPrincipalNombre: String? = null,
    val contactoPrincipalEmail: String? = null,
    val contactoPrincipalTelefono: String? = null,
    val configuracionJson: String? = null, // JSON para personalizaciones (logo, colores, etc.)
    val fechaRegistro: String = Instant.now().toString(),
    @ColumnInfo(defaultValue = "1")
    val activa: Boolean = true,
    val serverId: Int? = null // ID en el servidor para sincronización
) {
    companion object {
        /**
         * Crear entidad de salud para uso offline
         */
        fun createForOffline(
            nombre: String,
            codigo: String? = null,
            direccion: String? = null,
            telefono: String? = null,
            email: String? = null
        ): EntidadSaludEntity {
            return EntidadSaludEntity(
                nombreEntidad = nombre,
                nit = codigo,
                contactoPrincipalNombre = null,
                contactoPrincipalEmail = email,
                contactoPrincipalTelefono = telefono,
                configuracionJson = null,
                activa = true
            )
        }

        /**
         * Crear entidad de salud desde modelo de API
         */
        fun fromApiModel(entidad: com.example.telephases.network.EntidadSalud): EntidadSaludEntity {
            return EntidadSaludEntity(
                id = entidad.id,
                nombreEntidad = entidad.nombre,
                nit = entidad.codigo,
                contactoPrincipalNombre = null,
                contactoPrincipalEmail = entidad.email,
                contactoPrincipalTelefono = entidad.telefono,
                configuracionJson = null,
                activa = true,
                serverId = entidad.id
            )
        }
    }
}
