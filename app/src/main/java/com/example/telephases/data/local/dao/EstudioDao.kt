package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.EstudioEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con estudios
 */
@Dao
interface EstudioDao {
    
    @Query("SELECT * FROM estudios ORDER BY fechaEstudio DESC")
    fun getAll(): Flow<List<EstudioEntity>>
    
    @Query("SELECT * FROM estudios WHERE pacienteId = :pacienteId ORDER BY fechaEstudio DESC")
    fun getByPaciente(pacienteId: String): Flow<List<EstudioEntity>>
    
    @Query("SELECT * FROM estudios WHERE creadoPorUsuarioId = :usuarioId ORDER BY fechaEstudio DESC")
    fun getByUsuario(usuarioId: String): Flow<List<EstudioEntity>>
    
    @Query("SELECT * FROM estudios WHERE citaId = :citaId")
    fun getByCita(citaId: Int): Flow<List<EstudioEntity>>
    
    @Query("SELECT * FROM estudios WHERE id = :id")
    suspend fun getById(id: Int): EstudioEntity?
    
    @Query("SELECT * FROM estudios WHERE serverId = :serverId")
    suspend fun getByServerId(serverId: Int): EstudioEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(estudio: EstudioEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(estudios: List<EstudioEntity>)
    
    @Update
    suspend fun update(estudio: EstudioEntity)
    
    @Delete
    suspend fun delete(estudio: EstudioEntity)
    
    @Query("SELECT COUNT(*) FROM estudios")
    suspend fun getTotalCount(): Int
    
    @Query("SELECT COUNT(*) FROM estudios WHERE serverId IS NULL")
    suspend fun getUnsyncedCount(): Int
    
    @Query("SELECT * FROM estudios WHERE serverId IS NULL")
    suspend fun getUnsynced(): List<EstudioEntity>
    
    @Query("SELECT * FROM estudios")
    suspend fun getAllSync(): List<EstudioEntity>
}
