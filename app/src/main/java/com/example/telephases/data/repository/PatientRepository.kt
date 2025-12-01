package com.example.telephases.data.repository

import com.example.telephases.data.local.entities.PatientEntity
import com.example.telephases.network.Patient
import com.example.telephases.network.PatientRegistrationRequest
import com.example.telephases.network.PatientRegistrationResponse
import com.example.telephases.network.PatientSearchResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface del Repository para manejo de pacientes
 * Implementa el patrón Repository con fuente dual (local + remoto)
 */
interface PatientRepository {

    // ========== OPERACIONES LOCALES (OFFLINE) ==========

    /**
     * Obtiene todos los pacientes locales
     */
    suspend fun getLocalPatients(): List<PatientEntity>

    /**
     * Obtiene pacientes locales como Flow para observar cambios
     */
    fun getLocalPatientsFlow(): Flow<List<PatientEntity>>

    /**
     * Obtiene un paciente local por ID
     */
    suspend fun getLocalPatientById(patientId: String): PatientEntity?

    /**
     * Obtiene un paciente local por número de documento
     */
    suspend fun getLocalPatientByDocument(numeroDocumento: String): PatientEntity?

    /**
     * Busca pacientes locales por nombre
     */
    suspend fun searchLocalPatientsByName(searchTerm: String): List<PatientEntity>

    /**
     * Inserta un paciente local (modo offline)
     */
    suspend fun insertLocalPatient(patient: PatientEntity): String

    /**
     * Actualiza un paciente local
     */
    suspend fun updateLocalPatient(patient: PatientEntity)

    /**
     * Elimina un paciente local (soft delete)
     */
    suspend fun deleteLocalPatient(patientId: String)

    // ========== OPERACIONES REMOTAS (ONLINE) ==========

    /**
     * Registra un paciente en el servidor remoto
     */
    suspend fun registerPatientRemote(
        token: String,
        request: PatientRegistrationRequest
    ): Result<PatientRegistrationResponse>

    /**
     * Busca un paciente en el servidor remoto por documento
     */
    suspend fun searchPatientRemote(
        token: String,
        numeroDocumento: String
    ): Result<PatientSearchResponse>

    /**
     * Obtiene todos los pacientes del servidor remoto
     */
    suspend fun getAllPatientsRemote(token: String): Result<List<Patient>>

    // ========== OPERACIONES UNIFICADAS (OFFLINE-FIRST) ==========

    /**
     * Registra un paciente (offline-first)
     * - Si hay conexión: registra en servidor y guarda local
     * - Si no hay conexión: guarda local para sincronizar después
     */
    suspend fun registerPatient(
        token: String?,
        request: PatientRegistrationRequest
    ): Result<PatientEntity>

    /**
     * Busca un paciente por documento (offline-first)
     * - Primero busca en base de datos local
     * - Si no existe y hay conexión, busca en servidor
     * - Guarda el resultado en local si lo encuentra
     */
    suspend fun searchPatientByDocument(
        token: String?,
        numeroDocumento: String
    ): Result<PatientEntity>

    /**
     * Obtiene todos los pacientes (offline-first)
     * - Devuelve pacientes locales inmediatamente
     * - Si hay conexión, sincroniza en background
     */
    suspend fun getAllPatients(token: String? = null): List<PatientEntity>

    /**
     * Actualiza un paciente (offline-first)
     */
    suspend fun updatePatient(
        token: String?,
        patient: PatientEntity
    ): Result<PatientEntity>

    // ========== SINCRONIZACIÓN ==========

    /**
     * Sincroniza todos los pacientes pendientes con el servidor
     */
    suspend fun syncPatients(token: String): Result<SyncResult>

    /**
     * Sincroniza un paciente específico
     */
    suspend fun syncPatient(token: String, patientId: String): Result<PatientEntity>

    /**
     * Obtiene pacientes que necesitan sincronización
     */
    suspend fun getUnsyncedPatients(): List<PatientEntity>

    /**
     * Marca un paciente como sincronizado
     */
    suspend fun markPatientAsSynced(patientId: String, serverId: String? = null)

    /**
     * Fuerza la sincronización desde el servidor (descarga todo)
     */
    suspend fun forceSyncFromServer(token: String): Result<SyncResult>

    // ========== UTILIDADES ==========

    /**
     * Verifica si existe un paciente con el documento dado
     */
    suspend fun existsPatientWithDocument(numeroDocumento: String): Boolean

    /**
     * Obtiene estadísticas de pacientes
     */
    suspend fun getPatientStats(): PatientStats

    /**
     * Limpia datos antiguos y ejecuta mantenimiento
     */
    suspend fun performMaintenance()

    /**
     * Verifica el estado de conectividad
     */
    suspend fun isNetworkAvailable(): Boolean

    /**
     * Obtiene la cantidad de exámenes para un paciente
     */
    suspend fun getExamCountForPatient(patientId: String): Int

    /**
     * Obtiene el nombre de una entidad de salud por ID
     */
    suspend fun getEntidadSaludName(entidadId: Int): String

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
     * Estadísticas de pacientes
     */
    data class PatientStats(
        val totalPatients: Int,
        val syncedPatients: Int,
        val unsyncedPatients: Int,
        val todayRegistrations: Int,
        val recentPatients: List<PatientEntity>
    ) {
        val syncPercentage: Float get() = if (totalPatients > 0) (syncedPatients.toFloat() / totalPatients) * 100 else 0f
    }

    /**
     * Estados de resultado de operaciones
     */
    sealed class RepositoryError : Exception() {
        object NetworkError : RepositoryError()
        object AuthenticationError : RepositoryError()
        object ValidationError : RepositoryError()
        object NotFoundError : RepositoryError()
        object DuplicateError : RepositoryError()
        data class ServerError(val code: Int, override val message: String) : RepositoryError()
        data class UnknownError(override val cause: Throwable) : RepositoryError()
    }
}

