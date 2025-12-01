package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.UsuarioSedeEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con asignaciones usuario-sede
 */
@Dao
interface UsuarioSedeDao {
    
    @Query("SELECT * FROM usuario_sedes WHERE activa = 1")
    fun getAllActive(): Flow<List<UsuarioSedeEntity>>
    
    @Query("SELECT * FROM usuario_sedes WHERE usuarioId = :usuarioId AND activa = 1")
    fun getByUsuario(usuarioId: String): Flow<List<UsuarioSedeEntity>>
    
    @Query("SELECT * FROM usuario_sedes WHERE sedeId = :sedeId AND activa = 1")
    fun getBySede(sedeId: Int): Flow<List<UsuarioSedeEntity>>
    
    @Query("SELECT * FROM usuario_sedes WHERE usuarioId = :usuarioId AND sedeId = :sedeId")
    suspend fun getByUsuarioAndSede(usuarioId: String, sedeId: Int): UsuarioSedeEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuarioSede: UsuarioSedeEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(usuarioSedes: List<UsuarioSedeEntity>)
    
    @Update
    suspend fun update(usuarioSede: UsuarioSedeEntity)
    
    @Delete
    suspend fun delete(usuarioSede: UsuarioSedeEntity)
    
    @Query("UPDATE usuario_sedes SET activa = 0 WHERE usuarioId = :usuarioId AND sedeId = :sedeId")
    suspend fun softDelete(usuarioId: String, sedeId: Int)
    
    @Query("UPDATE usuario_sedes SET activa = 0 WHERE usuarioId = :usuarioId")
    suspend fun softDeleteByUsuario(usuarioId: String)
    
    @Query("UPDATE usuario_sedes SET activa = 0 WHERE sedeId = :sedeId")
    suspend fun softDeleteBySede(sedeId: Int)
    
    @Query("SELECT COUNT(*) FROM usuario_sedes WHERE activa = 1")
    suspend fun getActiveCount(): Int
    
    @Query("SELECT COUNT(*) FROM usuario_sedes WHERE usuarioId = :usuarioId AND activa = 1")
    suspend fun getCountByUsuario(usuarioId: String): Int
    
    @Query("SELECT COUNT(*) FROM usuario_sedes WHERE sedeId = :sedeId AND activa = 1")
    suspend fun getCountBySede(sedeId: Int): Int
}
