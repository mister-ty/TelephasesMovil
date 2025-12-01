package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index
import androidx.room.ForeignKey

/**
 * Entidad Room para representar un examen médico en la base de datos local
 * Corresponde a la tabla 'examen' en PostgreSQL
 */
@Entity(
    tableName = "exams",
    indices = [
        Index(value = ["patient_id"]),
        Index(value = ["tipo_examen_nombre"]),
        Index(value = ["fecha_creacion"]),
        Index(value = ["sincronizado"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patient_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExamEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String, // UUID string compatible con PostgreSQL

    @ColumnInfo(name = "patient_id")
    val patientId: String, // ID del paciente al que pertenece el examen

    @ColumnInfo(name = "tipo_examen_nombre")
    val tipoExamenNombre: String, // BLOOD_PRESSURE, TEMPERATURE, GLUCOSE, etc.
    
    @ColumnInfo(name = "tipo_examen_id")
    val tipoExamenId: Int? = null, // ID del tipo de examen en PostgreSQL

    @ColumnInfo(name = "titulo")
    val titulo: String,

    @ColumnInfo(name = "valor")
    val valor: String,

    @ColumnInfo(name = "unidad")
    val unidad: String? = null,

    @ColumnInfo(name = "observaciones")
    val observaciones: String? = null,

    @ColumnInfo(name = "datos_adicionales")
    val datosAdicionales: String? = null, // JSON string

    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: String, // ISO datetime string

    @ColumnInfo(name = "fecha_modificacion")
    val fechaModificacion: String? = null,

    @ColumnInfo(name = "activo")
    val activo: Boolean = true,

    // Campos para información de salud (del trigger de PostgreSQL)
    @ColumnInfo(name = "estado_codigo")
    val estadoCodigo: String? = null,

    @ColumnInfo(name = "estado_nombre")
    val estadoNombre: String? = null,

    @ColumnInfo(name = "estado_emoji")
    val estadoEmoji: String? = null,

    @ColumnInfo(name = "estado_color")
    val estadoColor: String? = null,

    @ColumnInfo(name = "estado_descripcion")
    val estadoDescripcion: String? = null,

    @ColumnInfo(name = "estado_nivel_urgencia")
    val estadoNivelUrgencia: Int? = null,

    // Campos para sincronización
    @ColumnInfo(name = "sincronizado")
    val sincronizado: Boolean = false,

    @ColumnInfo(name = "fecha_ultima_sincronizacion")
    val fechaUltimaSincronizacion: String? = null,

    @ColumnInfo(name = "modificado_localmente")
    val modificadoLocalmente: Boolean = false,

    @ColumnInfo(name = "fecha_modificacion_local")
    val fechaModificacionLocal: String? = null,

    // ID temporal para sincronización (si viene de la API tendrá el ID real)
    @ColumnInfo(name = "server_id")
    val serverId: Long? = null
) {
    /**
     * Convierte esta entidad al formato esperado por la API
     */
    fun toApiRequest(): com.example.telephases.network.ExamRequest {
        val additionalData = try {
            if (datosAdicionales != null) {
                com.google.gson.Gson().fromJson(datosAdicionales, Map::class.java) as Map<String, Any>
            } else null
        } catch (e: Exception) {
            null
        }

        return com.example.telephases.network.ExamRequest(
            tipo_examen_nombre = tipoExamenNombre,
            titulo = titulo,
            valor = valor,
            unidad = unidad,
            observaciones = observaciones,
            datos_adicionales = additionalData,
            patientId = patientId,
            paciente_id = patientId
        )
    }

    /**
     * Convierte esta entidad al formato esperado por la API usando el server_id del paciente
     */
    fun toApiRequestWithServerId(patientServerId: String): com.example.telephases.network.ExamRequest {
        val additionalData = try {
            if (datosAdicionales != null) {
                com.google.gson.Gson().fromJson(datosAdicionales, Map::class.java) as Map<String, Any>
            } else null
        } catch (e: Exception) {
            null
        }

        return com.example.telephases.network.ExamRequest(
            tipo_examen_nombre = tipoExamenNombre,
            titulo = titulo,
            valor = valor,
            unidad = unidad,
            observaciones = observaciones,
            datos_adicionales = additionalData,
            patientId = patientServerId, // Usar server_id del paciente
            paciente_id = patientServerId  // Usar server_id del paciente
        )
    }

    /**
     * Convierte esta entidad al formato de respuesta de la API
     */
    fun toApiData(): com.example.telephases.network.ExamData {
        return com.example.telephases.network.ExamData(
            id = serverId ?: id.hashCode().toLong(), // Usar serverId si existe, sino generar uno
            titulo = titulo,
            valor = valor,
            unidad = unidad,
            fecha_creacion = fechaCreacion,
            fecha_modificacion = fechaModificacion ?: fechaCreacion,
            observaciones = observaciones,
            tipo_examen_id = tipoExamenId,
            datos_adicionales = datosAdicionales,
            estado_codigo = estadoCodigo,
            estado_nombre = estadoNombre,
            estado_emoji = estadoEmoji,
            estado_color = estadoColor,
            estado_descripcion = estadoDescripcion,
            estado_nivel_urgencia = estadoNivelUrgencia
        )
    }

    companion object {
        /**
         * Crea una entidad desde la respuesta de la API
         */
        fun fromApiData(examData: com.example.telephases.network.ExamData, patientId: String): ExamEntity {
            val now = java.time.Instant.now().toString()
            
            // Mapear tipo_examen_id a tipoExamenNombre usando la tabla de tipos
            val tipoExamenNombre = when (examData.tipo_examen_id) {
                1 -> "TEMPERATURE"      // Temperatura
                2 -> "BLOOD_PRESSURE"   // Presión arterial
                3 -> "GLUCOSE"          // Glucosa
                4 -> "OXYGEN_SATURATION" // Saturación de oxígeno
                5 -> "WEIGHT"           // Peso
                6 -> "HEART_RATE"       // Frecuencia cardíaca
                else -> when {
                    examData.titulo.contains("Temperatura", ignoreCase = true) -> "TEMPERATURE"
                    examData.titulo.contains("Presión", ignoreCase = true) -> "BLOOD_PRESSURE"
                    examData.titulo.contains("Glucosa", ignoreCase = true) -> "GLUCOSE"
                    examData.titulo.contains("Oxígeno", ignoreCase = true) -> "OXYGEN_SATURATION"
                    examData.titulo.contains("Peso", ignoreCase = true) -> "WEIGHT"
                    examData.titulo.contains("Cardíaca", ignoreCase = true) -> "HEART_RATE"
                    else -> "TEMPERATURE" // Default fallback
                }
            }
            
            return ExamEntity(
                id = java.util.UUID.randomUUID().toString(),
                patientId = patientId,
                tipoExamenNombre = tipoExamenNombre,
                tipoExamenId = examData.tipo_examen_id,
                titulo = examData.titulo,
                valor = examData.valor,
                unidad = examData.unidad,
                observaciones = examData.observaciones,
                datosAdicionales = examData.datos_adicionales?.toString(),
                fechaCreacion = examData.fecha_creacion,
                fechaModificacion = examData.fecha_modificacion,
                activo = true,
                estadoCodigo = examData.estado_codigo,
                estadoNombre = examData.estado_nombre,
                estadoEmoji = examData.estado_emoji,
                estadoColor = examData.estado_color,
                estadoDescripcion = examData.estado_descripcion,
                estadoNivelUrgencia = examData.estado_nivel_urgencia,
                sincronizado = true, // Viene de la API, está sincronizado
                fechaUltimaSincronizacion = now,
                modificadoLocalmente = false,
                fechaModificacionLocal = null,
                serverId = examData.id
            )
        }

        /**
         * Crea una nueva entidad para datos creados offline
         */
        fun createForOffline(
            patientId: String,
            tipoExamenNombre: String,
            titulo: String,
            valor: String,
            unidad: String? = null,
            observaciones: String? = null,
            datosAdicionales: Map<String, Any>? = null
        ): ExamEntity {
            val now = java.time.Instant.now().toString()
            val additionalDataJson = try {
                if (datosAdicionales != null) {
                    com.google.gson.Gson().toJson(datosAdicionales)
                } else null
            } catch (e: Exception) {
                null
            }

            return ExamEntity(
                id = java.util.UUID.randomUUID().toString(), // ID temporal
                patientId = patientId,
                tipoExamenNombre = tipoExamenNombre,
                titulo = titulo,
                valor = valor,
                unidad = unidad,
                observaciones = observaciones,
                datosAdicionales = additionalDataJson,
                fechaCreacion = now,
                fechaModificacion = null,
                activo = true,
                estadoCodigo = null, // Se calculará en el servidor
                estadoNombre = null,
                estadoEmoji = null,
                estadoColor = null,
                estadoDescripcion = null,
                estadoNivelUrgencia = null,
                sincronizado = false, // Creado offline, pendiente de sincronización
                fechaUltimaSincronizacion = null,
                modificadoLocalmente = true,
                fechaModificacionLocal = now,
                serverId = null
            )
        }

        /**
         * Obtiene la descripción para un tipo de examen
         */
        private fun getDescriptionForExamType(tipo: String): String {
            return when (tipo.uppercase()) {
                "BLOOD_PRESSURE" -> "Medición de presión arterial"
                "TEMPERATURE" -> "Medición de temperatura corporal"
                "GLUCOSE" -> "Medición de glucosa en sangre"
                "OXYGEN_SATURATION" -> "Medición de saturación de oxígeno"
                "WEIGHT" -> "Medición de peso corporal"
                "HEART_RATE" -> "Medición de frecuencia cardíaca"
                else -> "Examen médico"
            }
        }
    }
}


