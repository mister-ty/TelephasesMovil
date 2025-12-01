package com.example.telephases.data.local.dao

import android.util.Log
import androidx.room.*
import com.example.telephases.data.local.entities.SyncMetadataEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de metadata de sincronización
 */
@Dao
interface SyncMetadataDao {

    /**
     * Inserta o actualiza un metadata
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: SyncMetadataEntity)

    /**
     * Inserta múltiples metadatas
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadatas(metadatas: List<SyncMetadataEntity>)

    /**
     * Actualiza un metadata existente
     */
    @Update
    suspend fun updateMetadata(metadata: SyncMetadataEntity)

    /**
     * Elimina un metadata
     */
    @Delete
    suspend fun deleteMetadata(metadata: SyncMetadataEntity)

    /**
     * Elimina metadata por clave
     */
    @Query("DELETE FROM sync_metadata WHERE key = :key")
    suspend fun deleteMetadataByKey(key: String)

    /**
     * Obtiene un metadata por clave
     */
    @Query("SELECT * FROM sync_metadata WHERE key = :key")
    suspend fun getMetadataByKey(key: String): SyncMetadataEntity?

    /**
     * Obtiene todos los metadatas
     */
    @Query("SELECT * FROM sync_metadata ORDER BY last_updated DESC")
    suspend fun getAllMetadata(): List<SyncMetadataEntity>

    /**
     * Obtiene metadatas como Flow
     */
    @Query("SELECT * FROM sync_metadata ORDER BY last_updated DESC")
    fun getAllMetadataFlow(): Flow<List<SyncMetadataEntity>>

    /**
     * Obtiene metadatas por estado de sincronización
     */
    @Query("SELECT * FROM sync_metadata WHERE sync_status = :status ORDER BY last_updated DESC")
    suspend fun getMetadataByStatus(status: String): List<SyncMetadataEntity>

    /**
     * Actualiza el valor de un metadata por clave
     */
    @Query("UPDATE sync_metadata SET value = :value, last_updated = :timestamp WHERE key = :key")
    suspend fun updateMetadataValue(
        key: String, 
        value: String, 
        timestamp: String = java.time.Instant.now().toString()
    )

    /**
     * Actualiza el estado de sincronización
     */
    @Query("UPDATE sync_metadata SET sync_status = :status, last_updated = :timestamp WHERE key = :key")
    suspend fun updateSyncStatus(
        key: String, 
        status: String, 
        timestamp: String = java.time.Instant.now().toString()
    )

    /**
     * Obtiene el valor de un metadata como string
     */
    @Query("SELECT value FROM sync_metadata WHERE key = :key")
    suspend fun getMetadataValue(key: String): String?

    /**
     * Verifica si existe un metadata con la clave dada
     */
    @Query("SELECT EXISTS(SELECT 1 FROM sync_metadata WHERE key = :key)")
    suspend fun existsMetadataWithKey(key: String): Boolean

    /**
     * Limpia metadatas antiguos (más de X días)
     */
    @Query("DELETE FROM sync_metadata WHERE datetime(last_updated) < datetime('now', '-30 days')")
    suspend fun cleanupOldMetadata()

    /**
     * Obtiene metadatas de error (status = 'failed')
     */
    @Query("SELECT * FROM sync_metadata WHERE sync_status = 'failed' ORDER BY last_updated DESC")
    suspend fun getErrorMetadata(): List<SyncMetadataEntity>

    /**
     * Cuenta metadatas por estado
     */
    @Query("SELECT COUNT(*) FROM sync_metadata WHERE sync_status = :status")
    suspend fun getMetadataCountByStatus(status: String): Int

    // ========== MÉTODOS ESPECÍFICOS PARA SINCRONIZACIÓN ==========

    /**
     * Actualiza el timestamp de última sincronización
     */
    suspend fun updateLastSyncTimestamp(timestamp: String = java.time.Instant.now().toString()) {
        insertMetadata(
            SyncMetadataEntity.createTimestampMetadata(
                SyncMetadataEntity.LAST_SYNC_TIMESTAMP,
                timestamp
            )
        )
    }

    /**
     * Obtiene el timestamp de última sincronización
     */
    suspend fun getLastSyncTimestamp(): String? {
        return getMetadataValue(SyncMetadataEntity.LAST_SYNC_TIMESTAMP)
    }

    /**
     * Actualiza el conteo de pacientes pendientes
     */
    suspend fun updatePendingPatientsCount(count: Int) {
        insertMetadata(
            SyncMetadataEntity.createCountMetadata(
                SyncMetadataEntity.PENDING_PATIENTS_COUNT,
                count
            )
        )
    }

    /**
     * Obtiene el conteo de pacientes pendientes
     */
    suspend fun getPendingPatientsCount(): Int {
        return getMetadataByKey(SyncMetadataEntity.PENDING_PATIENTS_COUNT)?.getIntValue() ?: 0
    }

    /**
     * Actualiza el conteo de exámenes pendientes
     */
    suspend fun updatePendingExamsCount(count: Int) {
        insertMetadata(
            SyncMetadataEntity.createCountMetadata(
                SyncMetadataEntity.PENDING_EXAMS_COUNT,
                count
            )
        )
    }

    /**
     * Obtiene el conteo de exámenes pendientes
     */
    suspend fun getPendingExamsCount(): Int {
        return getMetadataByKey(SyncMetadataEntity.PENDING_EXAMS_COUNT)?.getIntValue() ?: 0
    }

    /**
     * Actualiza el estado de red
     */
    suspend fun updateNetworkStatus(isConnected: Boolean) {
        insertMetadata(
            SyncMetadataEntity.createBooleanMetadata(
                SyncMetadataEntity.NETWORK_STATUS,
                isConnected
            )
        )
    }

    /**
     * Obtiene el estado de red
     */
    suspend fun getNetworkStatus(): Boolean {
        return getMetadataByKey(SyncMetadataEntity.NETWORK_STATUS)?.getBooleanValue() ?: false
    }

    /**
     * Actualiza si la sincronización automática está habilitada
     */
    suspend fun updateAutoSyncEnabled(enabled: Boolean) {
        Log.d("SyncMetadataDao", "🔧 updateAutoSyncEnabled: $enabled")
        insertMetadata(
            SyncMetadataEntity.createBooleanMetadata(
                SyncMetadataEntity.AUTO_SYNC_ENABLED,
                enabled
            )
        )
        Log.d("SyncMetadataDao", "✅ updateAutoSyncEnabled completado: $enabled")
    }

    /**
     * Verifica si la sincronización automática está habilitada
     */
    suspend fun isAutoSyncEnabled(): Boolean {
        val metadata = getMetadataByKey(SyncMetadataEntity.AUTO_SYNC_ENABLED)
        val value = metadata?.getBooleanValue() ?: true
        Log.d("SyncMetadataDao", "🔍 isAutoSyncEnabled: metadata=$metadata, value=$value")
        return value
    }

    /**
     * Registra un error de sincronización
     */
    suspend fun logSyncError(errorMessage: String) {
        insertMetadata(
            SyncMetadataEntity.createErrorMetadata(
                SyncMetadataEntity.SYNC_ERROR_LOG,
                errorMessage
            )
        )
    }

    /**
     * Obtiene el último error de sincronización
     */
    suspend fun getLastSyncError(): String? {
        return getMetadataValue(SyncMetadataEntity.SYNC_ERROR_LOG)
    }

    /**
     * Marca una sincronización exitosa
     */
    suspend fun markSuccessfulSync(timestamp: String = java.time.Instant.now().toString()) {
        insertMetadata(
            SyncMetadataEntity.createTimestampMetadata(
                SyncMetadataEntity.LAST_SUCCESSFUL_SYNC,
                timestamp
            )
        )
    }

    /**
     * Obtiene el timestamp de la última sincronización exitosa
     */
    suspend fun getLastSuccessfulSync(): String? {
        return getMetadataValue(SyncMetadataEntity.LAST_SUCCESSFUL_SYNC)
    }
}


