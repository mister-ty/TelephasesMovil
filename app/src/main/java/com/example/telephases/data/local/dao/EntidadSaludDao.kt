package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.EntidadSaludEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con entidades de salud
 */
@Dao
interface EntidadSaludDao {
    
    @Query("SELECT * FROM entidades_salud WHERE activa = 1 ORDER BY nombreEntidad ASC")
    fun getAllActive(): Flow<List<EntidadSaludEntity>>
    
    @Query("SELECT * FROM entidades_salud WHERE id = :id")
    suspend fun getById(id: Int): EntidadSaludEntity?
    
    @Query("SELECT * FROM entidades_salud WHERE serverId = :serverId")
    suspend fun getByServerId(serverId: Int): EntidadSaludEntity?
    
    @Query("SELECT * FROM entidades_salud WHERE nit = :nit")
    suspend fun getByNit(nit: String): EntidadSaludEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entidad: EntidadSaludEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entidades: List<EntidadSaludEntity>)
    
    @Update
    suspend fun update(entidad: EntidadSaludEntity)
    
    @Delete
    suspend fun delete(entidad: EntidadSaludEntity)
    
    @Query("UPDATE entidades_salud SET activa = 0 WHERE id = :id")
    suspend fun softDelete(id: Int)
    
    @Query("SELECT COUNT(*) FROM entidades_salud WHERE activa = 1")
    suspend fun getActiveCount(): Int
    
    @Query("SELECT COUNT(*) FROM entidades_salud WHERE serverId IS NULL AND activa = 1")
    suspend fun getUnsyncedCount(): Int
    
    @Query("SELECT * FROM entidades_salud WHERE serverId IS NULL AND activa = 1")
    suspend fun getUnsynced(): List<EntidadSaludEntity>
    
    @Query("SELECT * FROM entidades_salud WHERE activa = 1")
    suspend fun getAllActiveSync(): List<EntidadSaludEntity>
}
