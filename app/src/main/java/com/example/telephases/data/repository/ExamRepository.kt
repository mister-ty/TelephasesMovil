package com.example.telephases.data.repository

import com.example.telephases.data.local.entities.ExamEntity
import com.example.telephases.network.ExamRequest
import com.example.telephases.network.ExamResponse
import com.example.telephases.network.ExamData
import com.example.telephases.network.ExamsResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface del Repository para manejo de exámenes médicos
 * Implementa el patrón Repository con fuente dual (local + remoto)
 */
interface ExamRepository {

    // ========== OPERACIONES LOCALES (OFFLINE) ==========

    /**
     * Obtiene todos los exámenes locales
     */
    suspend fun getLocalExams(): List<ExamEntity>

    /**
     * Obtiene exámenes locales como Flow
     */
    fun getLocalExamsFlow(): Flow<List<ExamEntity>>

    /**
     * Obtiene un examen local por ID
     */
    suspend fun getLocalExamById(examId: String): ExamEntity?

    /**
     * Obtiene exámenes locales de un paciente
     */
    suspend fun getLocalExamsByPatient(patientId: String): List<ExamEntity>

    /**
     * Obtiene exámenes locales de un paciente como Flow
     */
    fun getLocalExamsByPatientFlow(patientId: String): Flow<List<ExamEntity>>

    /**
     * Obtiene los últimos exámenes por tipo para un paciente como Flow
     */
    fun getLatestExamsByTypeForPatientFlow(patientId: String): Flow<List<ExamEntity>>

    /**
     * Obtiene los últimos exámenes por tipo para un paciente
     */
    suspend fun getLatestLocalExamsByTypeForPatient(patientId: String): List<ExamEntity>

    /**
     * Obtiene los últimos exámenes por tipo (global)
     */
    suspend fun getLatestLocalExamsByType(): List<ExamEntity>

    /**
     * Obtiene exámenes locales por tipo
     */
    suspend fun getLocalExamsByType(tipoExamen: String): List<ExamEntity>

    /**
     * Obtiene exámenes locales por rango de fechas
     */
    suspend fun getLocalExamsByDateRange(startDate: String, endDate: String): List<ExamEntity>

    /**
     * Inserta un examen local (modo offline)
     */
    suspend fun insertLocalExam(exam: ExamEntity): String

    /**
     * Actualiza un examen local
     */
    suspend fun updateLocalExam(exam: ExamEntity)

    /**
     * Elimina un examen local (soft delete)
     */
    suspend fun deleteLocalExam(examId: String)

    // ========== OPERACIONES REMOTAS (ONLINE) ==========

    /**
     * Crea un examen en el servidor remoto
     */
    suspend fun createExamRemote(
        token: String,
        examRequest: ExamRequest
    ): Result<ExamResponse>

    /**
     * Obtiene exámenes del servidor remoto
     */
    suspend fun getExamsRemote(token: String): Result<ExamsResponse>

    /**
     * Obtiene últimos exámenes del servidor remoto
     */
    suspend fun getLatestExamsRemote(token: String): Result<ExamsResponse>

    /**
     * Obtiene exámenes de un paciente del servidor remoto
     */
    suspend fun getPatientExamsRemote(
        token: String,
        patientId: String
    ): Result<ExamsResponse>

    // ========== OPERACIONES UNIFICADAS (OFFLINE-FIRST) ==========

    /**
     * Crea un examen (offline-first)
     * - Si hay conexión: crea en servidor y guarda local
     * - Si no hay conexión: guarda local para sincronizar después
     */
    suspend fun createExam(
        token: String?,
        patientId: String,
        tipoExamenNombre: String,
        titulo: String,
        valor: String,
        unidad: String? = null,
        observaciones: String? = null,
        datosAdicionales: Map<String, Any>? = null
    ): Result<ExamEntity>

    /**
     * Obtiene exámenes de un paciente (offline-first)
     * - Devuelve exámenes locales inmediatamente
     * - Si hay conexión, sincroniza en background
     */
    suspend fun getPatientExams(
        token: String?,
        patientId: String
    ): List<ExamEntity>

    /**
     * Obtiene últimos exámenes por tipo (offline-first)
     */
    suspend fun getLatestExamsByType(
        token: String?,
        patientId: String? = null
    ): List<ExamEntity>

    /**
     * Obtiene todos los exámenes (offline-first)
     */
    suspend fun getAllExams(token: String? = null): List<ExamEntity>

    /**
     * Actualiza un examen (offline-first)
     */
    suspend fun updateExam(
        token: String?,
        exam: ExamEntity
    ): Result<ExamEntity>

    // ========== SINCRONIZACIÓN ==========

    /**
     * Sincroniza todos los exámenes pendientes con el servidor
     */
    suspend fun syncExams(token: String): Result<SyncResult>

    /**
     * Sincroniza un examen específico
     */
    suspend fun syncExam(token: String, examId: String): Result<ExamEntity>

    /**
     * Obtiene exámenes que necesitan sincronización
     */
    suspend fun getUnsyncedExams(): List<ExamEntity>

    /**
     * Marca un examen como sincronizado
     */
    suspend fun markExamAsSynced(examId: String, serverId: Long)

    /**
     * Fuerza la sincronización desde el servidor (descarga todo)
     */
    suspend fun forceSyncFromServer(token: String): Result<SyncResult>

    /**
     * Sincroniza exámenes de un paciente específico
     */
    suspend fun syncPatientExams(token: String, patientId: String): Result<SyncResult>

    /**
     * Fuerza la sincronización de exámenes pendientes (incluso si hay solo 1)
     */
    suspend fun forceSyncExams(token: String): Result<SyncResult>

    // ========== BÚSQUEDA Y FILTROS ==========

    /**
     * Busca exámenes con filtros múltiples
     */
    suspend fun searchExamsWithFilters(
        patientId: String? = null,
        tipoExamen: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        limit: Int = 20,
        offset: Int = 0
    ): List<ExamEntity>

    /**
     * Busca exámenes por valor
     */
    suspend fun searchExamsByValue(searchValue: String): List<ExamEntity>

    /**
     * Obtiene exámenes críticos (alta urgencia)
     */
    suspend fun getCriticalExams(minUrgencyLevel: Int = 3): List<ExamEntity>

    /**
     * Obtiene exámenes recientes
     */
    suspend fun getRecentExams(limit: Int = 10): List<ExamEntity>

    // ========== ESTADÍSTICAS ==========

    /**
     * Obtiene estadísticas de exámenes por tipo
     */
    suspend fun getExamStatsByType(): List<ExamTypeStats>

    /**
     * Obtiene estadísticas generales de exámenes
     */
    suspend fun getExamStats(): ExamStats

    /**
     * Obtiene conteo de exámenes por paciente
     */
    suspend fun getExamCountByPatient(patientId: String): Int

    /**
     * Obtiene conteo de exámenes por tipo
     */
    suspend fun getExamCountByType(tipoExamen: String): Int

    // ========== UTILIDADES ==========

    /**
     * Verifica el estado de conectividad
     */
    suspend fun isNetworkAvailable(): Boolean

    /**
     * Limpia datos antiguos y ejecuta mantenimiento
     */
    suspend fun performMaintenance()

    /**
     * Obtiene tipos de examen disponibles
     */
    suspend fun getAvailableExamTypes(): List<String>

    // ========== CLASES DE DATOS ==========

    /**
     * Resultado de operación de sincronización
     */
    data class SyncResult(
        val success: Boolean,
        val syncedCount: Int,
        val failedCount: Int,
        val totalCount: Int,
        val errors: List<String> = emptyList()
    ) {
        val isPartialSuccess: Boolean get() = syncedCount > 0 && failedCount > 0
        val isCompleteSuccess: Boolean get() = syncedCount == totalCount && failedCount == 0
        val successPercentage: Float get() = if (totalCount > 0) (syncedCount.toFloat() / totalCount) * 100 else 0f
    }

    /**
     * Estadísticas de exámenes por tipo
     */
    data class ExamTypeStats(
        val tipoExamen: String,
        val totalCount: Int,
        val averageValue: Double?,
        val minValue: Double?,
        val maxValue: Double?,
        val lastExamDate: String?
    )

    /**
     * Estadísticas generales de exámenes
     */
    data class ExamStats(
        val totalExams: Int,
        val syncedExams: Int,
        val unsyncedExams: Int,
        val todayExams: Int,
        val criticalExams: Int,
        val examsByType: Map<String, Int>
    ) {
        val syncPercentage: Float get() = if (totalExams > 0) (syncedExams.toFloat() / totalExams) * 100 else 0f
    }

    /**
     * Obtiene exámenes por ID de cita
     */
    suspend fun getExamsByCitaId(citaId: Int): List<ExamEntity>
    
    /**
     * Crea un estudio en el servidor
     */
    suspend fun createStudy(study: com.example.telephases.network.StudyRequest): Result<com.example.telephases.network.StudyCreateResponse>

    /**
     * Estados de resultado de operaciones
     */
    sealed class RepositoryError : Exception() {
        object NetworkError : RepositoryError()
        object AuthenticationError : RepositoryError()
        object ValidationError : RepositoryError()
        object NotFoundError : RepositoryError()
        object InvalidExamTypeError : RepositoryError()
        object PatientNotFoundError : RepositoryError()
        data class ServerError(val code: Int, override val message: String) : RepositoryError()
        data class UnknownError(override val cause: Throwable) : RepositoryError()
    }
}

