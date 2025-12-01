package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.CitaEntity
import com.example.telephases.data.local.entities.CitaExamenPrevistoEntity
import com.example.telephases.data.local.entities.EstadoCitaEntity
// import com.example.telephases.data.local.entities.TipoExamenEntity // Temporalmente comentado
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {
    // EstadoCita
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEstados(estados: List<EstadoCitaEntity>)

    @Query("SELECT * FROM estado_cita ORDER BY id ASC")
    fun getEstadosFlow(): Flow<List<EstadoCitaEntity>>
    
    @Query("SELECT * FROM estado_cita ORDER BY id ASC")
    suspend fun getEstados(): List<EstadoCitaEntity>

    // Citas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCita(cita: CitaEntity): Long

    @Update
    suspend fun updateCita(cita: CitaEntity)

    @Delete
    suspend fun deleteCita(cita: CitaEntity)

    @Query("SELECT * FROM citas WHERE fecha_cita LIKE :yyyyMmDd || '%' ORDER BY fecha_cita ASC")
    fun getCitasDeDiaFlow(yyyyMmDd: String): Flow<List<CitaEntity>>

    @Query("SELECT * FROM citas ORDER BY fecha_cita ASC")
    suspend fun getCitasHoy(): List<CitaEntity>
    
    @Query("DELETE FROM citas")
    suspend fun clearCitasDelDia()

    // Pivot
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrevistos(previstos: List<CitaExamenPrevistoEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCitaExamenPrevisto(previsto: CitaExamenPrevistoEntity)

    @Query("DELETE FROM cita_examenes_previstos WHERE cita_id = :citaId")
    suspend fun clearPrevistos(citaId: Int)

    @Query("SELECT * FROM cita_examenes_previstos WHERE cita_id = :citaId")
    suspend fun getCitaExamenesPrevistos(citaId: Long): List<CitaExamenPrevistoEntity>
    
    // Método para obtener usuarios con token (para AuthRepository temporal)
    @Query("SELECT * FROM users WHERE token_actual IS NOT NULL AND token_actual != '' LIMIT 1")
    suspend fun getUsersWithToken(): List<com.example.telephases.data.local.entities.UserEntity>
    
    // Método para buscar paciente por ID del servidor
    @Query("SELECT * FROM patients WHERE server_id = :serverId LIMIT 1")
    suspend fun getPatientByServerId(serverId: String): com.example.telephases.data.local.entities.PatientEntity?
    
    // Método para buscar paciente por ID local
    @Query("SELECT * FROM patients WHERE id = :patientId LIMIT 1")
    suspend fun getPatientById(patientId: String): com.example.telephases.data.local.entities.PatientEntity?
    
    // Método para buscar estado de cita por ID
    @Query("SELECT * FROM estado_cita WHERE id = :estadoId LIMIT 1")
    suspend fun getEstadoById(estadoId: Int): com.example.telephases.data.local.entities.EstadoCitaEntity?
    
    // Método para buscar usuario por ID
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): com.example.telephases.data.local.entities.UserEntity?
    
    // Método para obtener todos los usuarios
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getAllUsers(): List<com.example.telephases.data.local.entities.UserEntity>
    
    // Método para obtener una cita específica por ID
    @Query("SELECT * FROM citas WHERE id = :citaId LIMIT 1")
    suspend fun getCitaById(citaId: Int): com.example.telephases.data.local.entities.CitaEntity?
    
    // Método para obtener tipo de examen por ID (temporalmente comentado)
    // @Query("SELECT * FROM tipo_examen WHERE id = :tipoExamenId LIMIT 1")
    // suspend fun getTipoExamenById(tipoExamenId: Int): com.example.telephases.data.local.entities.TipoExamenEntity?
    
    // Método para verificar si la tabla tipo_examen existe y tiene datos (temporalmente comentado)
    // @Query("SELECT COUNT(*) FROM tipo_examen")
    // suspend fun getTipoExamenCount(): Int
    
    // Método para obtener todos los tipos de examen (temporalmente comentado)
    // @Query("SELECT * FROM tipo_examen LIMIT 10")
    // suspend fun getAllTiposExamen(): List<com.example.telephases.data.local.entities.TipoExamenEntity>
}


