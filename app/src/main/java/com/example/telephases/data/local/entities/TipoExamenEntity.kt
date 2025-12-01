package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

/**
 * Entidad para los tipos de examen médico
 * Corresponde a la tabla 'tipo_examen' en PostgreSQL
 */
@Entity(
    tableName = "exam_types",
    indices = [
        Index(value = ["nombre"], unique = true)
    ]
)
data class TipoExamenEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "nombre")
    val nombre: String, // BLOOD_PRESSURE, TEMPERATURE, GLUCOSE, etc.

    @ColumnInfo(name = "descripcion")
    val descripcion: String,

    @ColumnInfo(name = "activo")
    val activo: Boolean = true,

    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: String,

    // Campos para configuración del examen
    @ColumnInfo(name = "unidad_default")
    val unidadDefault: String? = null,

    @ColumnInfo(name = "valor_minimo")
    val valorMinimo: Double? = null,

    @ColumnInfo(name = "valor_maximo")
    val valorMaximo: Double? = null,

    @ColumnInfo(name = "requiere_dispositivo_ble")
    val requiereDispositivoBle: Boolean = false,

    @ColumnInfo(name = "icono")
    val icono: String? = null
) {
    companion object {
        /**
         * Crear tipos de examen por defecto
         */
        fun getDefaultExamTypes(): List<TipoExamenEntity> {
            val now = java.time.Instant.now().toString()
            return listOf(
                TipoExamenEntity(
                    id = 1,
                    nombre = "BLOOD_PRESSURE",
                    descripcion = "Medición de presión arterial",
                    activo = true,
                    fechaCreacion = now,
                    unidadDefault = "mmHg",
                    valorMinimo = 60.0,
                    valorMaximo = 200.0,
                    requiereDispositivoBle = true,
                    icono = "monitor_heart"
                ),
                TipoExamenEntity(
                    id = 2,
                    nombre = "TEMPERATURE",
                    descripcion = "Medición de temperatura corporal",
                    activo = true,
                    fechaCreacion = now,
                    unidadDefault = "°C",
                    valorMinimo = 35.0,
                    valorMaximo = 42.0,
                    requiereDispositivoBle = true,
                    icono = "thermostat"
                ),
                TipoExamenEntity(
                    id = 3,
                    nombre = "GLUCOSE",
                    descripcion = "Medición de glucosa en sangre",
                    activo = true,
                    fechaCreacion = now,
                    unidadDefault = "mg/dL",
                    valorMinimo = 50.0,
                    valorMaximo = 400.0,
                    requiereDispositivoBle = true,
                    icono = "water_drop"
                ),
                TipoExamenEntity(
                    id = 4,
                    nombre = "OXYGEN_SATURATION",
                    descripcion = "Medición de saturación de oxígeno",
                    activo = true,
                    fechaCreacion = now,
                    unidadDefault = "%",
                    valorMinimo = 70.0,
                    valorMaximo = 100.0,
                    requiereDispositivoBle = true,
                    icono = "favorite_border"
                ),
                TipoExamenEntity(
                    id = 5,
                    nombre = "WEIGHT",
                    descripcion = "Medición de peso corporal",
                    activo = true,
                    fechaCreacion = now,
                    unidadDefault = "kg",
                    valorMinimo = 20.0,
                    valorMaximo = 300.0,
                    requiereDispositivoBle = true,
                    icono = "scale"
                ),
                TipoExamenEntity(
                    id = 6,
                    nombre = "HEART_RATE",
                    descripcion = "Medición de frecuencia cardíaca",
                    activo = true,
                    fechaCreacion = now,
                    unidadDefault = "bpm",
                    valorMinimo = 30.0,
                    valorMaximo = 200.0,
                    requiereDispositivoBle = true,
                    icono = "favorite"
                )
            )
        }
    }
}


