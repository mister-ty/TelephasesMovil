package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.MaletaEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con maletas
 */
@Dao
interface MaletaDao {
    
    @Query("SELECT * FROM maletas WHERE activa = 1 ORDER BY nombreMaleta ASC")
    fun getAllActive(): Flow<List<MaletaEntity>>
    
    @Query("SELECT * FROM maletas WHERE asignadaAUsuarioId = :usuarioId AND activa = 1 ORDER BY nombreMaleta ASC")
    fun getByUsuario(usuarioId: String): Flow<List<MaletaEntity>>
    
    @Query("SELECT * FROM maletas WHERE id = :id")
    suspend fun getById(id: Int): MaletaEntity?
    
    @Query("SELECT * FROM maletas WHERE id = :id")
    suspend fun getMaletaById(id: Int): MaletaEntity?
    
    @Query("SELECT * FROM maletas WHERE asignadaAUsuarioId = :usuarioId AND activa = 1 LIMIT 1")
    suspend fun getMaletaByUsuarioId(usuarioId: String): MaletaEntity?
    
    @Query("SELECT * FROM maletas WHERE entidadSaludId = :entidadId AND activa = 1 LIMIT 1")
    suspend fun getMaletaByEntidadId(entidadId: Int): MaletaEntity?
    
    @Query("SELECT * FROM maletas WHERE entidadSaludId = :entidadId AND activa = 1 ORDER BY nombreMaleta ASC")
    suspend fun getMaletasByEntidadId(entidadId: Int): List<MaletaEntity>
    
    @Query("SELECT * FROM maletas WHERE serverId = :serverId")
    suspend fun getByServerId(serverId: Int): MaletaEntity?
    
    @Query("SELECT * FROM maletas WHERE identificadorInvima = :identificadorInvima")
    suspend fun getByIdentificadorInvima(identificadorInvima: String): MaletaEntity?
    
    // Usar Upsert para evitar REPLACE (que puede romper FKs y limpiar asignaciones)
    @Upsert
    suspend fun upsert(maleta: MaletaEntity): Long
    
    @Upsert
    suspend fun upsertMaleta(maleta: MaletaEntity): Long
    
    @Upsert
    suspend fun upsertAll(maletas: List<MaletaEntity>)
    
    @Update
    suspend fun update(maleta: MaletaEntity)
    
    @Update
    suspend fun updateMaleta(maleta: MaletaEntity)

    @Query("""
        UPDATE maletas 
        SET asignadaAUsuarioId = :usuarioId,
            entidadSaludId = :entidadId
        WHERE id = :maletaId
    """)
    suspend fun assignMaletaToUser(
        maletaId: Int,
        usuarioId: String,
        entidadId: Int?
    )
    
    @Delete
    suspend fun delete(maleta: MaletaEntity)
    
    @Query("UPDATE maletas SET activa = 0 WHERE id = :id")
    suspend fun softDelete(id: Int)
    
    @Query("SELECT COUNT(*) FROM maletas WHERE activa = 1")
    suspend fun getActiveCount(): Int
    
    @Query("SELECT COUNT(*) FROM maletas WHERE serverId IS NULL AND activa = 1")
    suspend fun getUnsyncedCount(): Int
    
    @Query("SELECT * FROM maletas WHERE serverId IS NULL AND activa = 1")
    suspend fun getUnsynced(): List<MaletaEntity>
    
    @Query("SELECT * FROM maletas WHERE activa = 1")
    suspend fun getAllActiveSync(): List<MaletaEntity>
}
