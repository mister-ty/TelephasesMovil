package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.MaletaDispositivoEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con la relación maleta-dispositivo
 */
@Dao
interface MaletaDispositivoDao {
    
    @Query("SELECT * FROM maleta_dispositivos")
    fun getAll(): Flow<List<MaletaDispositivoEntity>>
    
    @Query("SELECT * FROM maleta_dispositivos WHERE maletaId = :maletaId")
    fun getByMaleta(maletaId: Int): Flow<List<MaletaDispositivoEntity>>
    
    @Query("SELECT * FROM maleta_dispositivos WHERE dispositivoId = :dispositivoId")
    fun getByDispositivo(dispositivoId: Int): Flow<List<MaletaDispositivoEntity>>
    
    @Query("SELECT * FROM maleta_dispositivos WHERE maletaId = :maletaId AND dispositivoId = :dispositivoId")
    suspend fun getByMaletaAndDispositivo(maletaId: Int, dispositivoId: Int): MaletaDispositivoEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(maletaDispositivo: MaletaDispositivoEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(maletaDispositivos: List<MaletaDispositivoEntity>)
    
    @Delete
    suspend fun delete(maletaDispositivo: MaletaDispositivoEntity)
    
    @Query("DELETE FROM maleta_dispositivos WHERE maletaId = :maletaId AND dispositivoId = :dispositivoId")
    suspend fun deleteByMaletaAndDispositivo(maletaId: Int, dispositivoId: Int)
    
    @Query("DELETE FROM maleta_dispositivos WHERE maletaId = :maletaId")
    suspend fun deleteByMaleta(maletaId: Int)
    
    @Query("DELETE FROM maleta_dispositivos WHERE dispositivoId = :dispositivoId")
    suspend fun deleteByDispositivo(dispositivoId: Int)
    
    @Query("SELECT COUNT(*) FROM maleta_dispositivos WHERE maletaId = :maletaId")
    suspend fun getCountByMaleta(maletaId: Int): Int
    
    @Query("SELECT COUNT(*) FROM maleta_dispositivos WHERE dispositivoId = :dispositivoId")
    suspend fun getCountByDispositivo(dispositivoId: Int): Int
}
