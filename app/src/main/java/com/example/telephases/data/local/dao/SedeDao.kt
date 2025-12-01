package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.SedeEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con sedes
 */
@Dao
interface SedeDao {
    
    @Query("SELECT * FROM sedes WHERE activa = 1 ORDER BY nombreSede ASC")
    fun getAllActive(): Flow<List<SedeEntity>>
    
    @Query("SELECT * FROM sedes WHERE entidadSaludId = :entidadSaludId AND activa = 1 ORDER BY nombreSede ASC")
    fun getByEntidadSalud(entidadSaludId: Int): Flow<List<SedeEntity>>
    
    @Query("SELECT * FROM sedes WHERE id = :id")
    suspend fun getById(id: Int): SedeEntity?
    
    @Query("SELECT * FROM sedes WHERE serverId = :serverId")
    suspend fun getByServerId(serverId: Int): SedeEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sede: SedeEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sedes: List<SedeEntity>)
    
    @Update
    suspend fun update(sede: SedeEntity)
    
    @Delete
    suspend fun delete(sede: SedeEntity)
    
    @Query("UPDATE sedes SET activa = 0 WHERE id = :id")
    suspend fun softDelete(id: Int)
    
    @Query("SELECT COUNT(*) FROM sedes WHERE activa = 1")
    suspend fun getActiveCount(): Int
    
    @Query("SELECT COUNT(*) FROM sedes WHERE serverId IS NULL AND activa = 1")
    suspend fun getUnsyncedCount(): Int
    
    @Query("SELECT * FROM sedes WHERE serverId IS NULL AND activa = 1")
    suspend fun getUnsynced(): List<SedeEntity>
    
    @Query("SELECT * FROM sedes WHERE activa = 1")
    suspend fun getAllActiveSync(): List<SedeEntity>
}
