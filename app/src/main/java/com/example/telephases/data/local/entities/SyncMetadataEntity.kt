package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

/**
 * Entidad para manejar metadatos de sincronización
 * Mantiene información sobre el estado de sincronización general de la aplicación
 */
@Entity(tableName = "sync_metadata")
data class SyncMetadataEntity(
    @PrimaryKey
    @ColumnInfo(name = "key")
    val key: String, // Clave única para identificar el tipo de metadata

    @ColumnInfo(name = "value")
    val value: String, // Valor del metadata (puede ser JSON)

    @ColumnInfo(name = "last_updated")
    val lastUpdated: String, // ISO datetime string

    @ColumnInfo(name = "sync_status")
    val syncStatus: String = "pending" // pending, syncing, completed, failed
) {
    companion object {
        // Claves predefinidas para diferentes tipos de metadata
        const val LAST_SYNC_TIMESTAMP = "last_sync_timestamp"
        const val PENDING_PATIENTS_COUNT = "pending_patients_count"
        const val PENDING_EXAMS_COUNT = "pending_exams_count"
        const val SYNC_CONFLICTS_COUNT = "sync_conflicts_count"
        const val NETWORK_STATUS = "network_status"
        const val AUTO_SYNC_ENABLED = "auto_sync_enabled"
        const val LAST_SUCCESSFUL_SYNC = "last_successful_sync"
        const val SYNC_ERROR_LOG = "sync_error_log"

        /**
         * Crear metadata de conteo
         */
        fun createCountMetadata(key: String, count: Int): SyncMetadataEntity {
            return SyncMetadataEntity(
                key = key,
                value = count.toString(),
                lastUpdated = java.time.Instant.now().toString(),
                syncStatus = "completed"
            )
        }

        /**
         * Crear metadata de timestamp
         */
        fun createTimestampMetadata(key: String, timestamp: String = java.time.Instant.now().toString()): SyncMetadataEntity {
            return SyncMetadataEntity(
                key = key,
                value = timestamp,
                lastUpdated = java.time.Instant.now().toString(),
                syncStatus = "completed"
            )
        }

        /**
         * Crear metadata de estado booleano
         */
        fun createBooleanMetadata(key: String, value: Boolean): SyncMetadataEntity {
            return SyncMetadataEntity(
                key = key,
                value = value.toString(),
                lastUpdated = java.time.Instant.now().toString(),
                syncStatus = "completed"
            )
        }

        /**
         * Crear metadata de error
         */
        fun createErrorMetadata(key: String, errorMessage: String): SyncMetadataEntity {
            return SyncMetadataEntity(
                key = key,
                value = errorMessage,
                lastUpdated = java.time.Instant.now().toString(),
                syncStatus = "failed"
            )
        }
    }

    /**
     * Obtiene el valor como entero
     */
    fun getIntValue(): Int? {
        return try {
            value.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    /**
     * Obtiene el valor como booleano
     */
    fun getBooleanValue(): Boolean? {
        return try {
            value.toBoolean()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Obtiene el valor como timestamp
     */
    fun getTimestampValue(): java.time.Instant? {
        return try {
            java.time.Instant.parse(value)
        } catch (e: Exception) {
            null
        }
    }
}


