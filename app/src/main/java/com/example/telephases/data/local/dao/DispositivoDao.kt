package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.DispositivoEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con dispositivos
 */
@Dao
interface DispositivoDao {
    
    @Query("SELECT * FROM dispositivos WHERE activo = 1 ORDER BY tipoDispositivo ASC, marca ASC")
    fun getAllActive(): Flow<List<DispositivoEntity>>
    
    @Query("SELECT * FROM dispositivos WHERE tipoDispositivo = :tipoDispositivo AND activo = 1 ORDER BY marca ASC")
    fun getByTipo(tipoDispositivo: String): Flow<List<DispositivoEntity>>
    
    @Query("SELECT * FROM dispositivos WHERE id = :id")
    suspend fun getById(id: Int): DispositivoEntity?
    
    @Query("SELECT * FROM dispositivos WHERE serverId = :serverId")
    suspend fun getByServerId(serverId: Int): DispositivoEntity?
    
    @Query("SELECT * FROM dispositivos WHERE serial = :serial")
    suspend fun getBySerial(serial: String): DispositivoEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dispositivo: DispositivoEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dispositivos: List<DispositivoEntity>)
    
    @Update
    suspend fun update(dispositivo: DispositivoEntity)
    
    @Delete
    suspend fun delete(dispositivo: DispositivoEntity)
    
    @Query("UPDATE dispositivos SET activo = 0 WHERE id = :id")
    suspend fun softDelete(id: Int)
    
    @Query("SELECT COUNT(*) FROM dispositivos WHERE activo = 1")
    suspend fun getActiveCount(): Int
    
    @Query("SELECT COUNT(*) FROM dispositivos WHERE serverId IS NULL AND activo = 1")
    suspend fun getUnsyncedCount(): Int
    
    @Query("SELECT * FROM dispositivos WHERE serverId IS NULL AND activo = 1")
    suspend fun getUnsynced(): List<DispositivoEntity>
    
    @Query("SELECT * FROM dispositivos WHERE activo = 1")
    suspend fun getAllActiveSync(): List<DispositivoEntity>
}
