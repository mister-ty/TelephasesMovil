package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.ExamEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de exámenes médicos en la base de datos local
 */
@Dao
interface ExamDao {

    // ========== OPERACIONES CRUD BÁSICAS ==========

    /**
     * Inserta un nuevo examen
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: ExamEntity): Long

    /**
     * Inserta múltiples exámenes
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExams(exams: List<ExamEntity>)

    /**
     * Actualiza un examen existente
     */
    @Update
    suspend fun updateExam(exam: ExamEntity)

    /**
     * Elimina un examen (soft delete)
     */
    @Query("UPDATE exams SET activo = 0, modificado_localmente = 1, fecha_modificacion_local = :timestamp WHERE id = :examId")
    suspend fun deleteExam(examId: String, timestamp: String = java.time.Instant.now().toString())

    /**
     * Elimina un examen completamente (hard delete)
     */
    @Delete
    suspend fun deleteExamPermanently(exam: ExamEntity)

    // ========== CONSULTAS DE BÚSQUEDA ==========

    /**
     * Obtiene un examen por ID
     */
    @Query("SELECT * FROM exams WHERE id = :examId AND activo = 1")
    suspend fun getExamById(examId: String): ExamEntity?

    /**
     * Obtiene todos los exámenes de un paciente
     */
    @Query("SELECT * FROM exams WHERE patient_id = :patientId AND activo = 1 ORDER BY fecha_creacion DESC")
    suspend fun getExamsByPatient(patientId: String): List<ExamEntity>

    /**
     * Obtiene exámenes de un paciente como Flow
     */
    @Query("SELECT * FROM exams WHERE patient_id = :patientId AND activo = 1 ORDER BY fecha_creacion DESC")
    fun getExamsByPatientFlow(patientId: String): Flow<List<ExamEntity>>

    /**
     * Obtiene exámenes por tipo
     */
    @Query("SELECT * FROM exams WHERE tipo_examen_nombre = :tipoExamen AND activo = 1 ORDER BY fecha_creacion DESC")
    suspend fun getExamsByType(tipoExamen: String): List<ExamEntity>

    /**
     * Obtiene un examen por título, valor y paciente (para evitar duplicados)
     */
    @Query("SELECT * FROM exams WHERE patient_id = :patientId AND titulo = :titulo AND valor = :valor AND activo = 1 LIMIT 1")
    suspend fun getExamByTitleAndValue(patientId: String, titulo: String, valor: String): ExamEntity?

    /**
     * [DEBUG] Obtiene TODOS los exámenes de un paciente (incluyendo inactivos)
     */
    @Query("SELECT * FROM exams WHERE patient_id = :patientId ORDER BY fecha_creacion DESC")
    suspend fun getAllExamsByPatientDebug(patientId: String): List<ExamEntity>

    /**
     * Obtiene el último examen de cada tipo para un paciente
     */
    @Query("""
        SELECT * FROM exams e1
        WHERE e1.patient_id = :patientId 
        AND e1.activo = 1
        AND e1.fecha_creacion = (
            SELECT MAX(e2.fecha_creacion) 
            FROM exams e2 
            WHERE e2.patient_id = :patientId 
            AND e2.tipo_examen_nombre = e1.tipo_examen_nombre 
            AND e2.activo = 1
        )
        ORDER BY e1.fecha_creacion DESC
        LIMIT 5
    """)
    suspend fun getLatestExamsByTypeForPatient(patientId: String): List<ExamEntity>

    /**
     * Flow para obtener los últimos exámenes por tipo para un paciente específico
     */
    @Query("""
        SELECT * FROM exams e1
        WHERE e1.patient_id = :patientId
        AND e1.activo = 1
        AND e1.fecha_creacion = (
            SELECT MAX(e2.fecha_creacion) 
            FROM exams e2 
            WHERE e2.tipo_examen_nombre = e1.tipo_examen_nombre 
            AND e2.patient_id = :patientId
            AND e2.activo = 1
        )
        ORDER BY e1.fecha_creacion DESC
        LIMIT 5
    """)
    fun getLatestExamsByTypeForPatientFlow(patientId: String): Flow<List<ExamEntity>>

    /**
     * Obtiene todos los exámenes más recientes por tipo (global)
     */
    @Query("""
        SELECT * FROM exams e1
        WHERE e1.activo = 1
        AND e1.fecha_creacion = (
            SELECT MAX(e2.fecha_creacion) 
            FROM exams e2 
            WHERE e2.tipo_examen_nombre = e1.tipo_examen_nombre 
            AND e2.activo = 1
        )
        ORDER BY e1.fecha_creacion DESC
    """)
    suspend fun getLatestExamsByType(): List<ExamEntity>

    /**
     * Busca exámenes por rango de fechas
     */
    @Query("""
        SELECT * FROM exams 
        WHERE activo = 1 
        AND datetime(fecha_creacion) BETWEEN datetime(:startDate) AND datetime(:endDate)
        ORDER BY fecha_creacion DESC
    """)
    suspend fun getExamsByDateRange(startDate: String, endDate: String): List<ExamEntity>

    /**
     * Busca exámenes de un paciente por rango de fechas
     */
    @Query("""
        SELECT * FROM exams 
        WHERE patient_id = :patientId 
        AND activo = 1 
        AND datetime(fecha_creacion) BETWEEN datetime(:startDate) AND datetime(:endDate)
        ORDER BY fecha_creacion DESC
    """)
    suspend fun getPatientExamsByDateRange(patientId: String, startDate: String, endDate: String): List<ExamEntity>

    // ========== CONSULTAS DE SINCRONIZACIÓN ==========

    /**
     * Obtiene exámenes que necesitan sincronización
     */
    @Query("SELECT * FROM exams WHERE sincronizado = 0 AND activo = 1 ORDER BY fecha_creacion ASC")
    suspend fun getUnsyncedExams(): List<ExamEntity>

    /**
     * Obtiene exámenes modificados localmente
     */
    @Query("SELECT * FROM exams WHERE modificado_localmente = 1 AND activo = 1")
    suspend fun getLocallyModifiedExams(): List<ExamEntity>

    /**
     * Marca un examen como sincronizado
     */
    @Query("""
        UPDATE exams 
        SET sincronizado = 1, 
            fecha_ultima_sincronizacion = :timestamp,
            modificado_localmente = 0,
            fecha_modificacion_local = NULL,
            server_id = :serverId
        WHERE id = :examId
    """)
    suspend fun markExamAsSynced(
        examId: String, 
        serverId: Long,
        timestamp: String = java.time.Instant.now().toString()
    )

    /**
     * Marca múltiples exámenes como sincronizados
     */
    @Query("""
        UPDATE exams 
        SET sincronizado = 1, 
            fecha_ultima_sincronizacion = :timestamp,
            modificado_localmente = 0,
            fecha_modificacion_local = NULL
        WHERE id IN (:examIds)
    """)
    suspend fun markExamsAsSynced(examIds: List<String>, timestamp: String = java.time.Instant.now().toString())

    /**
     * Actualiza los datos de estado de salud de un examen (desde el servidor)
     */
    @Query("""
        UPDATE exams 
        SET estado_codigo = :estadoCodigo,
            estado_nombre = :estadoNombre,
            estado_emoji = :estadoEmoji,
            estado_color = :estadoColor,
            estado_descripcion = :estadoDescripcion,
            estado_nivel_urgencia = :estadoNivelUrgencia
        WHERE id = :examId
    """)
    suspend fun updateExamHealthStatus(
        examId: String,
        estadoCodigo: String?,
        estadoNombre: String?,
        estadoEmoji: String?,
        estadoColor: String?,
        estadoDescripcion: String?,
        estadoNivelUrgencia: Int?
    )

    // ========== CONSULTAS DE ESTADÍSTICAS ==========

    /**
     * Cuenta total de exámenes activos
     */
    @Query("SELECT COUNT(*) FROM exams WHERE activo = 1")
    suspend fun getTotalExamsCount(): Int

    /**
     * Cuenta exámenes de un paciente
     */
    @Query("SELECT COUNT(*) FROM exams WHERE patient_id = :patientId AND activo = 1")
    suspend fun getPatientExamsCount(patientId: String): Int

    /**
     * Cuenta exámenes por tipo
     */
    @Query("SELECT COUNT(*) FROM exams WHERE tipo_examen_nombre = :tipoExamen AND activo = 1")
    suspend fun getExamsCountByType(tipoExamen: String): Int

    /**
     * Cuenta exámenes no sincronizados
     */
    @Query("SELECT COUNT(*) FROM exams WHERE sincronizado = 0 AND activo = 1")
    suspend fun getUnsyncedExamsCount(): Int

    /**
     * Cuenta exámenes realizados hoy
     */
    @Query("""
        SELECT COUNT(*) FROM exams 
        WHERE activo = 1 
        AND date(fecha_creacion) = date('now', 'localtime')
    """)
    suspend fun getTodayExamsCount(): Int

    /**
     * Cuenta exámenes por paciente en un rango de fechas
     */
    @Query("""
        SELECT COUNT(*) FROM exams 
        WHERE patient_id = :patientId 
        AND activo = 1 
        AND datetime(fecha_creacion) BETWEEN datetime(:startDate) AND datetime(:endDate)
    """)
    suspend fun getPatientExamsCountInRange(patientId: String, startDate: String, endDate: String): Int

    // ========== CONSULTAS AVANZADAS ==========

    /**
     * Obtiene exámenes con filtros múltiples
     */
    @Query("""
        SELECT * FROM exams 
        WHERE activo = 1
        AND (:patientId IS NULL OR patient_id = :patientId)
        AND (:tipoExamen IS NULL OR tipo_examen_nombre = :tipoExamen)
        AND (:startDate IS NULL OR datetime(fecha_creacion) >= datetime(:startDate))
        AND (:endDate IS NULL OR datetime(fecha_creacion) <= datetime(:endDate))
        ORDER BY fecha_creacion DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getExamsWithFilters(
        patientId: String? = null,
        tipoExamen: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        limit: Int = 20,
        offset: Int = 0
    ): List<ExamEntity>

    /**
     * Obtiene estadísticas de exámenes por tipo
     */
    @Query("""
        SELECT 
            tipo_examen_nombre,
            COUNT(*) as total,
            AVG(CAST(valor AS REAL)) as promedio,
            MIN(CAST(valor AS REAL)) as minimo,
            MAX(CAST(valor AS REAL)) as maximo
        FROM exams 
        WHERE activo = 1 
        AND valor NOT LIKE '%/%' -- Excluir valores como presión arterial
        GROUP BY tipo_examen_nombre
    """)
    suspend fun getExamStatsByType(): List<ExamTypeStats>

    /**
     * Obtiene los últimos N exámenes
     */
    @Query("SELECT * FROM exams WHERE activo = 1 ORDER BY fecha_creacion DESC LIMIT :limit")
    suspend fun getRecentExams(limit: Int = 10): List<ExamEntity>

    /**
     * Busca exámenes por valor (para casos especiales)
     */
    @Query("""
        SELECT * FROM exams 
        WHERE activo = 1 
        AND valor LIKE '%' || :searchValue || '%'
        ORDER BY fecha_creacion DESC
    """)
    suspend fun searchExamsByValue(searchValue: String): List<ExamEntity>

    /**
     * Obtiene exámenes con estados de salud críticos
     */
    @Query("""
        SELECT * FROM exams 
        WHERE activo = 1 
        AND estado_nivel_urgencia IS NOT NULL 
        AND estado_nivel_urgencia >= :minUrgencyLevel
        ORDER BY estado_nivel_urgencia DESC, fecha_creacion DESC
    """)
    suspend fun getCriticalExams(minUrgencyLevel: Int = 3): List<ExamEntity>

    /**
     * Obtiene estadísticas de sincronización para exámenes
     */
    @Query("""
        SELECT 
            COUNT(*) as total,
            SUM(CASE WHEN sincronizado = 1 THEN 1 ELSE 0 END) as sincronizados,
            SUM(CASE WHEN modificado_localmente = 1 THEN 1 ELSE 0 END) as modificados_localmente
        FROM exams 
        WHERE activo = 1
    """)
    suspend fun getSyncStats(): SyncStats

    // ========== OPERACIONES DE MANTENIMIENTO ==========

    /**
     * Limpia exámenes eliminados hace más de X días
     */
    @Query("""
        DELETE FROM exams 
        WHERE activo = 0 
        AND datetime(fecha_modificacion_local) < datetime('now', '-30 days')
    """)
    suspend fun cleanupOldDeletedExams()

    /**
     * Obtiene exámenes huérfanos (sin paciente válido)
     */
    @Query("""
        SELECT e.* FROM exams e
        LEFT JOIN patients p ON e.patient_id = p.id
        WHERE e.activo = 1 AND (p.id IS NULL OR p.activo = 0)
    """)
    suspend fun getOrphanedExams(): List<ExamEntity>

    /**
     * Elimina exámenes huérfanos (pero preserva exámenes offline válidos)
     */
    @Query("""
        UPDATE exams 
        SET activo = 0, modificado_localmente = 1, fecha_modificacion_local = :timestamp
        WHERE id IN (
            SELECT e.id FROM exams e
            LEFT JOIN patients p ON e.patient_id = p.id
            WHERE e.activo = 1 
            AND p.id IS NULL 
            AND e.sincronizado = 1
        )
    """)
    suspend fun cleanupOrphanedExams(timestamp: String = java.time.Instant.now().toString())

    /**
     * Clases auxiliares para estadísticas
     */
    data class ExamTypeStats(
        val tipo_examen_nombre: String,
        val total: Int,
        val promedio: Double?,
        val minimo: Double?,
        val maximo: Double?
    )

    data class SyncStats(
        val total: Int,
        val sincronizados: Int,
        val modificados_localmente: Int
    ) {
        val pendientes: Int get() = total - sincronizados
        val porcentajeSincronizado: Float get() = if (total > 0) (sincronizados.toFloat() / total) * 100 else 0f
    }
}


