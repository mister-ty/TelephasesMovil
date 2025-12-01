package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

/**
 * Entidad Room para representar un paciente en la base de datos local
 * Corresponde a la tabla 'usuario' con rol_id = 2 en PostgreSQL
 */
@Entity(
    tableName = "patients",
    indices = [
        Index(value = ["numero_documento"], unique = true),
        Index(value = ["email"], unique = true),
        Index(value = ["entidad_salud_id"]) // Alinear con índice creado en migración 7->8
    ]
)
data class PatientEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String, // UUID string compatible con PostgreSQL

    @ColumnInfo(name = "primer_nombre")
    val primerNombre: String,

    @ColumnInfo(name = "segundo_nombre")
    val segundoNombre: String? = null,

    @ColumnInfo(name = "primer_apellido")
    val primerApellido: String,

    @ColumnInfo(name = "segundo_apellido")
    val segundoApellido: String? = null,

    @ColumnInfo(name = "tipo_documento_id")
    val tipoDocumentoId: Int,

    @ColumnInfo(name = "numero_documento")
    val numeroDocumento: String,

    @ColumnInfo(name = "email")
    val email: String? = null,

    @ColumnInfo(name = "telefono")
    val telefono: String? = null,

    @ColumnInfo(name = "direccion")
    val direccion: String? = null,

    @ColumnInfo(name = "ciudad_id")
    val ciudadId: Int? = null,

    @ColumnInfo(name = "fecha_nacimiento")
    val fechaNacimiento: String? = null, // ISO date string

    @ColumnInfo(name = "genero")
    val genero: String? = null, // M, F, O

    @ColumnInfo(name = "tipo_identificacion")
    val tipoIdentificacion: String? = null, // Registro Civil, Tarjeta Identidad, etc.

    @ColumnInfo(name = "estado_civil")
    val estadoCivil: String? = null, // Casado(a), Soltero(a), Divorciado(a), etc.

    @ColumnInfo(name = "pais")
    val pais: String? = null,

    @ColumnInfo(name = "municipio")
    val municipio: String? = null,

    @ColumnInfo(name = "departamento")
    val departamento: String? = null,

    @ColumnInfo(name = "tipo_usuario")
    val tipoUsuario: String? = null, // Capita, Contributivo, etc.

    @ColumnInfo(name = "entidad_salud_id")
    val entidadSaludId: Int? = null, // Relación con entidades_salud

    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: String, // ISO datetime string

    @ColumnInfo(name = "activo")
    val activo: Boolean = true,

    // Campos para sincronización
    @ColumnInfo(name = "sincronizado")
    val sincronizado: Boolean = false,

    @ColumnInfo(name = "server_id")
    val serverId: String? = null, // ID del servidor cuando se sincroniza

    @ColumnInfo(name = "fecha_ultima_sincronizacion")
    val fechaUltimaSincronizacion: String? = null,

    @ColumnInfo(name = "modificado_localmente")
    val modificadoLocalmente: Boolean = false,

    @ColumnInfo(name = "fecha_modificacion_local")
    val fechaModificacionLocal: String? = null
) {
    /**
     * Propiedades computadas para compatibilidad con la API
     */
    val nombreCompleto: String
        get() = "$primerNombre $primerApellido".trim()

    /**
     * Convierte esta entidad a la estructura esperada por la API
     */
    fun toApiModel(): com.example.telephases.network.Patient {
        return com.example.telephases.network.Patient(
            id = id,
            primer_nombre = primerNombre,
            segundo_nombre = segundoNombre,
            primer_apellido = primerApellido,
            segundo_apellido = segundoApellido,
            numero_documento = numeroDocumento,
            email = email,
            telefono = telefono,
            direccion = direccion,
            ciudad_id = ciudadId,
            fecha_nacimiento = fechaNacimiento,
            genero = genero,
            nombre_completo = nombreCompleto,
            tipo_identificacion = tipoIdentificacion,
            estado_civil = estadoCivil,
            pais = pais,
            municipio = municipio,
            departamento = departamento,
            tipo_usuario = tipoUsuario,
            entidad_salud_id = entidadSaludId
        )
    }

    companion object {
        /**
         * Crea una entidad desde el modelo de la API
         */
        fun fromApiModel(patient: com.example.telephases.network.Patient): PatientEntity {
            val now = java.time.Instant.now().toString()
            return PatientEntity(
                id = patient.id,
                primerNombre = patient.primer_nombre,
                segundoNombre = patient.segundo_nombre,
                primerApellido = patient.primer_apellido,
                segundoApellido = patient.segundo_apellido,
                tipoDocumentoId = 1, // Default: Cédula de ciudadanía
                numeroDocumento = patient.numero_documento,
                email = patient.email,
                telefono = patient.telefono,
                direccion = patient.direccion,
                ciudadId = patient.ciudad_id,
                fechaNacimiento = convertDateFormat(patient.fecha_nacimiento),
                genero = patient.genero,
                tipoIdentificacion = patient.tipo_identificacion,
                estadoCivil = patient.estado_civil,
                pais = patient.pais,
                municipio = patient.municipio,
                departamento = patient.departamento,
                tipoUsuario = patient.tipo_usuario,
                entidadSaludId = patient.entidad_salud_id,
                fechaRegistro = now,
                activo = true,
                sincronizado = true, // Viene de la API, está sincronizado
                fechaUltimaSincronizacion = now,
                modificadoLocalmente = false,
                fechaModificacionLocal = null
            )
        }

        /**
         * Convierte fecha de formato dd/MM/yyyy a yyyy-MM-dd para la base de datos
         */
        private fun convertDateFormat(dateString: String?): String? {
            if (dateString.isNullOrBlank()) return null
            return try {
                val inputFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                val outputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val date = inputFormat.parse(dateString)
                if (date != null) {
                    outputFormat.format(date)
                } else {
                    null
                }
            } catch (e: Exception) {
                // Si no se puede convertir, retornar el valor original
                dateString
            }
        }

        /**
         * Convierte fecha de formato yyyy-MM-dd a dd/MM/yyyy para visualización
         */
        fun convertDateFormatForDisplay(dateString: String?): String? {
            if (dateString.isNullOrBlank()) return null
            return try {
                val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                val date = inputFormat.parse(dateString)
                if (date != null) {
                    outputFormat.format(date)
                } else {
                    null
                }
            } catch (e: Exception) {
                // Si no se puede convertir, retornar el valor original
                dateString
            }
        }

        /**
         * Crea una nueva entidad para datos creados offline
         */
        fun createForOffline(
            primerNombre: String,
            primerApellido: String,
            numeroDocumento: String,
            segundoNombre: String? = null,
            segundoApellido: String? = null,
            tipoDocumentoId: Int = 1,
            email: String? = null,
            telefono: String? = null,
            direccion: String? = null,
            ciudadId: Int? = null,
            fechaNacimiento: String? = null,
            genero: String? = null,
            tipoIdentificacion: String? = null,
            estadoCivil: String? = null,
            pais: String? = null,
            municipio: String? = null,
            departamento: String? = null,
            tipoUsuario: String? = null,
            entidadSaludId: Int? = null
        ): PatientEntity {
            val now = java.time.Instant.now().toString()
            return PatientEntity(
                id = java.util.UUID.randomUUID().toString(), // ID temporal
                primerNombre = primerNombre,
                segundoNombre = segundoNombre,
                primerApellido = primerApellido,
                segundoApellido = segundoApellido,
                tipoDocumentoId = tipoDocumentoId,
                numeroDocumento = numeroDocumento,
                email = email,
                telefono = telefono,
                direccion = direccion,
                ciudadId = ciudadId,
                fechaNacimiento = convertDateFormat(fechaNacimiento),
                genero = genero,
                tipoIdentificacion = tipoIdentificacion,
                estadoCivil = estadoCivil,
                pais = pais,
                municipio = municipio,
                departamento = departamento,
                tipoUsuario = tipoUsuario,
                entidadSaludId = entidadSaludId,
                fechaRegistro = now,
                activo = true,
                sincronizado = false, // Creado offline, pendiente de sincronización
                fechaUltimaSincronizacion = null,
                modificadoLocalmente = true,
                fechaModificacionLocal = now
            )
        }
    }
}


