package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.PatientEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de pacientes en la base de datos local
 */
@Dao
interface PatientDao {

    // ========== OPERACIONES CRUD BÁSICAS ==========

    /**
     * Inserta un nuevo paciente
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity): Long

    /**
     * Inserta múltiples pacientes
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatients(patients: List<PatientEntity>)

    /**
     * Actualiza un paciente existente
     */
    @Update
    suspend fun updatePatient(patient: PatientEntity)

    /**
     * Elimina un paciente (soft delete)
     */
    @Query("UPDATE patients SET activo = 0, modificado_localmente = 1, fecha_modificacion_local = :timestamp WHERE id = :patientId")
    suspend fun deletePatient(patientId: String, timestamp: String = java.time.Instant.now().toString())

    /**
     * Elimina un paciente completamente (hard delete)
     */
    @Delete
    suspend fun deletePatientPermanently(patient: PatientEntity)

    // ========== CONSULTAS DE BÚSQUEDA ==========

    /**
     * Obtiene un paciente por ID
     */
    @Query("SELECT * FROM patients WHERE id = :patientId AND activo = 1")
    suspend fun getPatientById(patientId: String): PatientEntity?

    /**
     * Obtiene un paciente por número de documento
     */
    @Query("SELECT * FROM patients WHERE numero_documento = :numeroDocumento AND activo = 1")
    suspend fun getPatientByDocument(numeroDocumento: String): PatientEntity?

    /**
     * Busca pacientes por nombre (parcial)
     */
    @Query("""
        SELECT * FROM patients 
        WHERE activo = 1 
        AND (primer_nombre LIKE '%' || :searchTerm || '%' 
             OR primer_apellido LIKE '%' || :searchTerm || '%'
             OR segundo_nombre LIKE '%' || :searchTerm || '%'
             OR segundo_apellido LIKE '%' || :searchTerm || '%')
        ORDER BY primer_nombre ASC, primer_apellido ASC
    """)
    suspend fun searchPatientsByName(searchTerm: String): List<PatientEntity>

    /**
     * Obtiene todos los pacientes activos
     */
    @Query("SELECT * FROM patients WHERE activo = 1 ORDER BY fecha_registro DESC")
    suspend fun getAllPatients(): List<PatientEntity>

    /**
     * Obtiene todos los pacientes como Flow (para observar cambios)
     */
    @Query("SELECT * FROM patients WHERE activo = 1 ORDER BY fecha_registro DESC")
    fun getAllPatientsFlow(): Flow<List<PatientEntity>>

    /**
     * Obtiene pacientes recientes (últimos N)
     */
    @Query("SELECT * FROM patients WHERE activo = 1 ORDER BY fecha_registro DESC LIMIT :limit")
    suspend fun getRecentPatients(limit: Int = 10): List<PatientEntity>

    // ========== CONSULTAS DE SINCRONIZACIÓN ==========

    /**
     * Obtiene pacientes que necesitan sincronización
     */
    @Query("SELECT * FROM patients WHERE sincronizado = 0 AND activo = 1")
    suspend fun getUnsyncedPatients(): List<PatientEntity>

    /**
     * Obtiene pacientes modificados localmente
     */
    @Query("SELECT * FROM patients WHERE modificado_localmente = 1 AND activo = 1")
    suspend fun getLocallyModifiedPatients(): List<PatientEntity>

    /**
     * Marca un paciente como sincronizado
     */
    @Query("""
        UPDATE patients 
        SET sincronizado = 1, 
            fecha_ultima_sincronizacion = :timestamp,
            modificado_localmente = 0,
            fecha_modificacion_local = NULL
        WHERE id = :patientId
    """)
    suspend fun markPatientAsSynced(patientId: String, timestamp: String = java.time.Instant.now().toString())

    /**
     * Marca múltiples pacientes como sincronizados
     */
    @Query("""
        UPDATE patients 
        SET sincronizado = 1, 
            fecha_ultima_sincronizacion = :timestamp,
            modificado_localmente = 0,
            fecha_modificacion_local = NULL
        WHERE id IN (:patientIds)
    """)
    suspend fun markPatientsAsSynced(patientIds: List<String>, timestamp: String = java.time.Instant.now().toString())

    /**
     * Actualiza el ID del servidor para un paciente creado offline
     */
    @Query("UPDATE patients SET server_id = :serverId WHERE id = :patientId")
    suspend fun updatePatientServerId(patientId: String, serverId: String)

    // ========== CONSULTAS DE ESTADÍSTICAS ==========

    /**
     * Cuenta total de pacientes activos
     */
    @Query("SELECT COUNT(*) FROM patients WHERE activo = 1")
    suspend fun getTotalPatientsCount(): Int

    /**
     * Cuenta pacientes no sincronizados
     */
    @Query("SELECT COUNT(*) FROM patients WHERE sincronizado = 0 AND activo = 1")
    suspend fun getUnsyncedPatientsCount(): Int

    /**
     * Cuenta pacientes registrados hoy
     */
    @Query("""
        SELECT COUNT(*) FROM patients 
        WHERE activo = 1 
        AND date(fecha_registro) = date('now', 'localtime')
    """)
    suspend fun getTodayPatientsCount(): Int

    /**
     * Obtiene el último paciente registrado
     */
    @Query("SELECT * FROM patients WHERE activo = 1 ORDER BY fecha_registro DESC LIMIT 1")
    suspend fun getLastRegisteredPatient(): PatientEntity?

    // ========== OPERACIONES DE MANTENIMIENTO ==========

    /**
     * Limpia pacientes marcados como eliminados hace más de X días
     */
    @Query("""
        DELETE FROM patients 
        WHERE activo = 0 
        AND datetime(fecha_modificacion_local) < datetime('now', '-30 days')
    """)
    suspend fun cleanupOldDeletedPatients()

    /**
     * Obtiene pacientes duplicados por número de documento
     */
    @Query("""
        SELECT * FROM patients p1
        WHERE EXISTS (
            SELECT 1 FROM patients p2 
            WHERE p2.numero_documento = p1.numero_documento 
            AND p2.id != p1.id 
            AND p2.activo = 1
        ) AND p1.activo = 1
        ORDER BY p1.fecha_registro DESC
    """)
    suspend fun getDuplicatePatients(): List<PatientEntity>

    /**
     * Verifica si existe un paciente con el número de documento dado
     */
    @Query("SELECT EXISTS(SELECT 1 FROM patients WHERE numero_documento = :numeroDocumento AND activo = 1)")
    suspend fun existsPatientWithDocument(numeroDocumento: String): Boolean

    // ========== CONSULTAS ESPECIALES ==========

    /**
     * Busca pacientes con filtros múltiples
     */
    @Query("""
        SELECT * FROM patients 
        WHERE activo = 1
        AND (:searchTerm IS NULL OR 
             primer_nombre LIKE '%' || :searchTerm || '%' OR 
             primer_apellido LIKE '%' || :searchTerm || '%' OR
             numero_documento LIKE '%' || :searchTerm || '%')
        AND (:genero IS NULL OR genero = :genero)
        AND (:ciudadId IS NULL OR ciudad_id = :ciudadId)
        ORDER BY fecha_registro DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun searchPatientsWithFilters(
        searchTerm: String? = null,
        genero: String? = null,
        ciudadId: Int? = null,
        limit: Int = 20,
        offset: Int = 0
    ): List<PatientEntity>

    /**
     * Obtiene estadísticas de sincronización para pacientes
     */
    @Query("""
        SELECT 
            COUNT(*) as total,
            SUM(CASE WHEN sincronizado = 1 THEN 1 ELSE 0 END) as sincronizados,
            SUM(CASE WHEN modificado_localmente = 1 THEN 1 ELSE 0 END) as modificados_localmente
        FROM patients 
        WHERE activo = 1
    """)
    suspend fun getSyncStats(): SyncStats

    /**
     * Elimina todos los administradores de la tabla de pacientes
     */
    @Query("""
        DELETE FROM patients 
        WHERE email LIKE 'contacto@%' 
           OR email LIKE 'admin@%' 
           OR email LIKE 'medico@%'
           OR email IS NULL
    """)
    suspend fun deleteAdminPatients(): Int

    /**
     * Clase para estadísticas de sincronización
     */
    data class SyncStats(
        val total: Int,
        val sincronizados: Int,
        val modificados_localmente: Int
    ) {
        val pendientes: Int get() = total - sincronizados
        val porcentajeSincronizado: Float get() = if (total > 0) (sincronizados.toFloat() / total) * 100 else 0f
    }
}


