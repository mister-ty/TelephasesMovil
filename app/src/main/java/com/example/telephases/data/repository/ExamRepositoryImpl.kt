package com.example.telephases.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.telephases.data.local.dao.ExamDao
import com.example.telephases.data.local.dao.PatientDao
import com.example.telephases.data.local.dao.SyncMetadataDao
import com.example.telephases.data.local.dao.TipoExamenDao
import com.example.telephases.data.local.dao.SedeDao
import com.example.telephases.data.local.dao.MaletaDao
import com.example.telephases.data.local.entities.ExamEntity
import com.example.telephases.network.ApiInterface
import com.example.telephases.network.ExamRequest
import com.example.telephases.network.ExamResponse
import com.example.telephases.network.ExamData
import com.example.telephases.network.ExamsResponse
import com.example.telephases.network.StudyExam
import com.example.telephases.network.StudyRequest
import com.example.telephases.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del Repository para manejo de exámenes médicos
 * Implementa patrón Offline-First con sincronización automática
 */
@Singleton
class ExamRepositoryImpl @Inject constructor(
    private val examDao: ExamDao,
    private val patientDao: PatientDao,
    private val syncMetadataDao: SyncMetadataDao,
    private val tipoExamenDao: TipoExamenDao,
    private val sedeDao: SedeDao,
    private val maletaDao: MaletaDao,
    private val apiService: ApiInterface,
    private val authRepository: AuthRepository,
    private val context: Context
) : ExamRepository {

    private val tag = "ExamRepository"
    
    // Mecanismo para evitar sincronizaciones múltiples simultáneas
    private var isSyncing = false
    private val syncMutex = Any()

    // ========== FUNCIONES AUXILIARES ==========
    
    private suspend fun getFirstSedeByEntidad(entidadSaludId: Int): com.example.telephases.data.local.entities.SedeEntity? {
        return try {
            val sedes = sedeDao.getByEntidadSalud(entidadSaludId)
            val sedesList = sedes.firstOrNull()
            sedesList?.firstOrNull()
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo sede para entidad $entidadSaludId", e)
            null
        }
    }

    // ========== OPERACIONES LOCALES (OFFLINE) ==========

    override suspend fun getLocalExams(): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            examDao.getRecentExams(50) // Limitar para performance
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo exámenes locales", e)
            emptyList()
        }
    }

    override fun getLocalExamsFlow(): Flow<List<ExamEntity>> {
        return examDao.getExamsByPatientFlow("") // TODO: Ajustar según necesidades
    }

    override suspend fun getLocalExamById(examId: String): ExamEntity? = withContext(Dispatchers.IO) {
        try {
            examDao.getExamById(examId)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo examen por ID: $examId", e)
            null
        }
    }

    override suspend fun getLocalExamsByPatient(patientId: String): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            Log.d(tag, "🔍 Consultando exámenes directos para paciente: $patientId")
            val exams = examDao.getExamsByPatient(patientId)
            Log.d(tag, "📊 Exámenes directos encontrados: ${exams.size}")
            if (exams.isNotEmpty()) {
                exams.forEach { exam ->
                    Log.d(tag, "📋 Examen directo: ${exam.titulo} = ${exam.valor} (Sync: ${exam.sincronizado}, ID: ${exam.id})")
                }
            } else {
                Log.d(tag, "❌ NO hay exámenes directos para el paciente $patientId")
            }
            exams
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo exámenes del paciente: $patientId", e)
            emptyList()
        }
    }

    override fun getLocalExamsByPatientFlow(patientId: String): Flow<List<ExamEntity>> {
        return examDao.getExamsByPatientFlow(patientId).map { exams: List<ExamEntity> ->
            Log.d(tag, "🔍 Flow actualizado: ${exams.size} exámenes para paciente $patientId")
            if (exams.isNotEmpty()) {
                exams.forEach { exam: ExamEntity ->
                    Log.d(tag, "📋 Examen en flow: ${exam.titulo} = ${exam.valor} (Sync: ${exam.sincronizado}, ID: ${exam.id})")
                }
            } else {
                Log.d(tag, "❌ NO hay exámenes en el flow para el paciente $patientId")
            }
            exams
        }
    }

    override fun getLatestExamsByTypeForPatientFlow(patientId: String): Flow<List<ExamEntity>> {
        return examDao.getLatestExamsByTypeForPatientFlow(patientId).map { exams: List<ExamEntity> ->
            Log.d(tag, "🔍 Flow ÚLTIMOS por tipo: ${exams.size} exámenes para paciente $patientId")
            if (exams.isNotEmpty()) {
                exams.forEach { exam: ExamEntity ->
                    Log.d(tag, "📋 Último examen: ${exam.titulo} = ${exam.valor} (Sync: ${exam.sincronizado}, ID: ${exam.id})")
                }
            } else {
                Log.d(tag, "❌ NO hay últimos exámenes por tipo para el paciente $patientId")
            }
            exams
        }
    }

    override suspend fun getLatestLocalExamsByTypeForPatient(patientId: String): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            examDao.getLatestExamsByTypeForPatient(patientId)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo últimos exámenes por tipo del paciente: $patientId", e)
            emptyList()
        }
    }

    override suspend fun getLatestLocalExamsByType(): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            examDao.getLatestExamsByType()
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo últimos exámenes por tipo", e)
            emptyList()
        }
    }

    override suspend fun getLocalExamsByType(tipoExamen: String): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            examDao.getExamsByType(tipoExamen)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo exámenes por tipo: $tipoExamen", e)
            emptyList()
        }
    }

    override suspend fun getLocalExamsByDateRange(startDate: String, endDate: String): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            examDao.getExamsByDateRange(startDate, endDate)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo exámenes por rango de fechas", e)
            emptyList()
        }
    }

    override suspend fun insertLocalExam(exam: ExamEntity): String = withContext(Dispatchers.IO) {
        try {
            examDao.insertExam(exam)
            updateSyncCounters()
            Log.d(tag, "✅ Examen insertado localmente: ${exam.titulo}")
            exam.id
        } catch (e: Exception) {
            Log.e(tag, "❌ Error insertando examen local", e)
            throw ExamRepository.RepositoryError.UnknownError(e)
        }
    }

    override suspend fun updateLocalExam(exam: ExamEntity): Unit = withContext(Dispatchers.IO) {
        try {
            val updatedExam = exam.copy(
                modificadoLocalmente = true,
                fechaModificacionLocal = java.time.Instant.now().toString(),
                sincronizado = false
            )
            examDao.updateExam(updatedExam)
            updateSyncCounters()
            Log.d(tag, "✅ Examen actualizado localmente: ${exam.titulo}")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error actualizando examen local", e)
            throw ExamRepository.RepositoryError.UnknownError(e)
        }
    }

    override suspend fun deleteLocalExam(examId: String): Unit = withContext(Dispatchers.IO) {
        try {
            examDao.deleteExam(examId)
            updateSyncCounters()
            Log.d(tag, "✅ Examen eliminado localmente: $examId")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error eliminando examen local", e)
            throw ExamRepository.RepositoryError.UnknownError(e)
        }
    }

    // ========== OPERACIONES REMOTAS (ONLINE) ==========

    override suspend fun createExamRemote(
        token: String,
        examRequest: ExamRequest
    ): Result<ExamResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createExam("Bearer $token", examRequest)
            Log.d(tag, "✅ Examen creado en servidor: ${response.examen.titulo}")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error creando examen en servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun getExamsRemote(token: String): Result<ExamsResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getExams("Bearer $token")
            Log.d(tag, "✅ Obtenidos ${response.examenes.size} exámenes del servidor")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo exámenes del servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun getLatestExamsRemote(token: String): Result<ExamsResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLatestExams("Bearer $token")
            Log.d(tag, "✅ Obtenidos ${response.examenes.size} últimos exámenes del servidor")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo últimos exámenes del servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun getPatientExamsRemote(
        token: String,
        patientId: String
    ): Result<ExamsResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPatientExams("Bearer $token", patientId)
            Log.d(tag, "✅ Obtenidos ${response.examenes.size} exámenes del paciente $patientId del servidor")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo exámenes del paciente del servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    // ========== OPERACIONES UNIFICADAS (OFFLINE-FIRST) ==========

    override suspend fun createExam(
        token: String?,
        patientId: String,
        tipoExamenNombre: String,
        titulo: String,
        valor: String,
        unidad: String?,
        observaciones: String?,
        datosAdicionales: Map<String, Any>?
    ): Result<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            // Validar que el tipo de examen existe
            val tipoExamen = tipoExamenDao.getTipoExamenByNombre(tipoExamenNombre)
            if (tipoExamen == null) {
                Log.e(tag, "❌ Tipo de examen no válido: $tipoExamenNombre")
                return@withContext Result.failure(ExamRepository.RepositoryError.InvalidExamTypeError)
            }

            Log.d(tag, "🌐 Estado de red: ${if (isNetworkAvailable()) "DISPONIBLE" else "NO DISPONIBLE"}")
            Log.d(tag, "🔑 Token disponible: ${if (token != null) "SÍ" else "NO"}")
            
            // SIEMPRE guardar localmente primero (OFFLINE-FIRST)
            // Esto permite agrupar exámenes antes de sincronizar
            Log.d(tag, "📱 Guardando examen en BD local primero")
            val offlineExam = ExamEntity.createForOffline(
                patientId = patientId,
                tipoExamenNombre = tipoExamenNombre,
                titulo = titulo,
                valor = valor,
                unidad = unidad,
                observaciones = observaciones,
                datosAdicionales = datosAdicionales
            )

            Log.d(tag, "📝 Guardando examen en BD local: $titulo = $valor ${unidad ?: ""}")
            val insertResult = examDao.insertExam(offlineExam)
            Log.d(tag, "💾 Resultado de inserción en BD local: $insertResult")
            
            // Verificar que se guardó correctamente
            val savedExam = examDao.getExamById(offlineExam.id)
            if (savedExam != null) {
                Log.d(tag, "✅ Examen guardado exitosamente en BD local: ${savedExam.titulo}")
            } else {
                Log.e(tag, "❌ ERROR: Examen NO se guardó en BD local")
            }
            
            updateSyncCounters()
            
            // NO sincronizar inmediatamente - dejar que se agrupen los exámenes
            // La sincronización se hará por lote cuando:
            // 1. El usuario termine de agregar todos los exámenes
            // 2. El WorkManager ejecute la sincronización periódica
            // 3. El usuario presione "Sincronizar" manualmente
            Log.d(tag, "📦 Examen guardado localmente - se sincronizará en lote con otros exámenes")
            
            Log.d(tag, "✅ Examen creado: $titulo")
            Result.success(offlineExam)

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en createExam", e)
            Result.failure(ExamRepository.RepositoryError.UnknownError(e))
        }
    }

    override suspend fun getPatientExams(
        token: String?,
        patientId: String
    ): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            // SIEMPRE devolver datos locales inmediatamente (OFFLINE-FIRST)
            Log.d(tag, "🔍 Consultando BD local para paciente: $patientId")
            
            // DEBUG: Verificar TODOS los exámenes (incluyendo inactivos)
            val allExamsDebug = examDao.getAllExamsByPatientDebug(patientId)
            Log.d(tag, "🐛 [DEBUG] TODOS los exámenes (activos e inactivos): ${allExamsDebug.size}")
            allExamsDebug.forEach { exam ->
                Log.d(tag, "🐛 [DEBUG] Examen: ${exam.titulo} = ${exam.valor} | Activo: ${exam.activo} | Sync: ${exam.sincronizado}")
            }
            
            val localExams = examDao.getExamsByPatient(patientId)
            Log.d(tag, "📱 Obteniendo exámenes del paciente $patientId desde BD local")
            Log.d(tag, "📊 Exámenes encontrados en BD local (activos): ${localExams.size}")
            
            if (localExams.isNotEmpty()) {
                localExams.forEach { exam ->
                    Log.d(tag, "📋 Examen local: ${exam.titulo} = ${exam.valor} (ID: ${exam.id})")
                }
            } else {
                Log.d(tag, "📭 No hay exámenes en BD local para el paciente $patientId")
            }
            
            Log.d(tag, "✅ Devolviendo ${localExams.size} exámenes locales del paciente $patientId")

            // SINCRONIZACIÓN AUTOMÁTICA DESHABILITADA - PRESERVAR DATOS OFFLINE
            // Solo sincronizar cuando el usuario lo solicite manualmente
            Log.d(tag, "🔒 Sincronización automática deshabilitada - preservando datos offline")
            
            /* COMENTADO - Causaba pérdida de exámenes offline
            if (isNetworkAvailable() && token != null) {
                try {
                    Log.d(tag, "🌐 Red disponible, iniciando sincronización desde servidor...")
                    val remoteResult = getPatientExamsRemote(token, patientId)
                    if (remoteResult.isSuccess) {
                        val serverExams = remoteResult.getOrThrow().examenes
                        Log.d(tag, "📡 Servidor devolvió ${serverExams.size} exámenes para paciente $patientId")
                        val syncedCount = syncExamsFromServer(serverExams, patientId)
                        Log.d(tag, "🔄 Sincronización completada: $syncedCount exámenes nuevos desde servidor")
                        
                        // Volver a consultar BD local después de sincronización
                        val localExamsAfterSync = examDao.getExamsByPatient(patientId)
                        Log.d(tag, "📊 Después de sync: ${localExamsAfterSync.size} exámenes en BD local")
                    } else {
                        Log.w(tag, "⚠️ Error en respuesta del servidor para exámenes del paciente")
                    }
                } catch (e: Exception) {
                    Log.w(tag, "⚠️ Error en sincronización background de exámenes", e)
                    // No fallar la operación por error de sync
                }
            }
            */

            localExams
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en getPatientExams", e)
            emptyList()
        }
    }

    override suspend fun getLatestExamsByType(
        token: String?,
        patientId: String?
    ): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            val localExams = if (patientId != null) {
                examDao.getLatestExamsByTypeForPatient(patientId)
            } else {
                examDao.getLatestExamsByType()
            }
            
            Log.d(tag, "✅ Devolviendo ${localExams.size} últimos exámenes por tipo")

            // SINCRONIZACIÓN AUTOMÁTICA DESHABILITADA - PRESERVAR DATOS OFFLINE
            Log.d(tag, "🔒 Sincronización automática deshabilitada para getLatestExams")
            
            /* COMENTADO - Causaba pérdida de exámenes offline
            if (isNetworkAvailable() && token != null) {
                try {
                    val remoteResult = getLatestExamsRemote(token)
                    if (remoteResult.isSuccess) {
                        syncExamsFromServer(remoteResult.getOrThrow().examenes)
                    }
                } catch (e: Exception) {
                    Log.w(tag, "⚠️ Error en sincronización background", e)
                }
            }
            */

            localExams
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en getLatestExamsByType", e)
            emptyList()
        }
    }

    override suspend fun getAllExams(token: String?): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            val localExams = examDao.getRecentExams(100)
            Log.d(tag, "✅ Devolviendo ${localExams.size} exámenes locales")

            // SINCRONIZACIÓN AUTOMÁTICA DESHABILITADA - PRESERVAR DATOS OFFLINE
            Log.d(tag, "🔒 Sincronización automática deshabilitada para getAllExams")
            
            /* COMENTADO - Causaba pérdida de exámenes offline
            if (isNetworkAvailable() && token != null) {
                try {
                    val remoteResult = getExamsRemote(token)
                    if (remoteResult.isSuccess) {
                        syncExamsFromServer(remoteResult.getOrThrow().examenes)
                    }
                } catch (e: Exception) {
                    Log.w(tag, "⚠️ Error en sincronización background", e)
                }
            }
            */

            localExams
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en getAllExams", e)
            emptyList()
        }
    }

    override suspend fun updateExam(
        token: String?,
        exam: ExamEntity
    ): Result<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            // Actualizar localmente primero
            updateLocalExam(exam)

            // Si hay red, intentar sincronizar
            if (isNetworkAvailable() && token != null) {
                // TODO: Implementar updateExamRemote cuando esté disponible en la API
                Log.d(tag, "ℹ️ Actualización remota de examen pendiente - no implementado en API")
            }

            Result.success(exam)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error actualizando examen", e)
            Result.failure(ExamRepository.RepositoryError.UnknownError(e))
        }
    }

    // ========== SINCRONIZACIÓN ==========

    override suspend fun syncExams(token: String): Result<ExamRepository.SyncResult> = withContext(Dispatchers.IO) {
        synchronized(syncMutex) {
            if (isSyncing) {
                Log.d(tag, "🔄 Sincronización ya en progreso, saltando...")
                return@withContext Result.success(ExamRepository.SyncResult(
                    success = true,
                    syncedCount = 0,
                    failedCount = 0,
                    totalCount = 0,
                    errors = listOf("Sincronización ya en progreso")
                ))
            }
            
            isSyncing = true
        }
        
        try {
            if (!isNetworkAvailable()) {
                Log.d(tag, "❌ No hay red disponible para sincronización")
                return@withContext Result.failure(ExamRepository.RepositoryError.NetworkError)
            }

            val unsyncedExams = examDao.getUnsyncedExams()
            Log.d(tag, "🔍 Exámenes no sincronizados encontrados: ${unsyncedExams.size}")
            
            if (unsyncedExams.isNotEmpty()) {
                unsyncedExams.forEach { exam ->
                    Log.d(tag, "📋 Examen pendiente: ${exam.titulo} = ${exam.valor} (ID: ${exam.id})")
                }
            }

            // Agrupar exámenes por paciente
            val examsByPatient = unsyncedExams.groupBy { it.patientId }
            Log.d(tag, "📊 Exámenes agrupados por ${examsByPatient.size} pacientes")
            
            if (examsByPatient.isEmpty()) {
                Log.d(tag, "📭 No hay exámenes pendientes para sincronizar")
                return@withContext Result.success(ExamRepository.SyncResult(
                    success = true,
                    syncedCount = 0,
                    failedCount = 0,
                    totalCount = 0,
                    errors = emptyList()
                ))
            }

            // NUEVA LÓGICA: Solo sincronizar si hay 2+ exámenes del mismo paciente
            val patientsWithMultipleExams = examsByPatient.filter { it.value.size >= 2 }
            val patientsWithSingleExam = examsByPatient.filter { it.value.size == 1 }
            
            if (patientsWithMultipleExams.isEmpty()) {
                Log.d(tag, "📦 No hay pacientes con 2+ exámenes pendientes - esperando más exámenes para agrupar")
                Log.d(tag, "📋 Pacientes con 1 examen: ${patientsWithSingleExam.size}")
                return@withContext Result.success(ExamRepository.SyncResult(
                    success = true,
                    syncedCount = 0,
                    failedCount = 0,
                    totalCount = unsyncedExams.size,
                    errors = emptyList()
                ))
            }
            
            Log.d(tag, "📊 Pacientes con múltiples exámenes: ${patientsWithMultipleExams.size}")
            
            var syncedCount = 0
            var failedCount = 0
            val errors = mutableListOf<String>()

            Log.d(tag, "🔄 Iniciando sincronización de pacientes con múltiples exámenes...")
            Log.d(tag, "📊 Pacientes a sincronizar: ${patientsWithMultipleExams.size}")

            for ((patientId, patientExams) in patientsWithMultipleExams) {
                try {
                    // Obtener el server_id del paciente para sincronizar
                    val patient = patientDao.getPatientById(patientId)
                    if (patient?.serverId == null) {
                        Log.w(tag, "⚠️ Paciente $patientId no tiene server_id, saltando ${patientExams.size} exámenes")
                        failedCount += patientExams.size
                        errors.add("Paciente no sincronizado para ${patientExams.size} exámenes")
                        continue
                    }

                    // Obtener sede y maleta del paciente
                    val entidadSaludId = patient.entidadSaludId
                    val sedeId = if (entidadSaludId != null) {
                        val sede = getFirstSedeByEntidad(entidadSaludId)
                        sede?.serverId?.toString()
                    } else null
                    
                    val maletaId = if (entidadSaludId != null) {
                        val maleta = maletaDao.getMaletaByEntidadId(entidadSaludId)
                        maleta?.serverId?.toString()
                    } else null
                    
                    Log.d(tag, "🔍 Sede ID: $sedeId, Maleta ID: $maletaId para entidad $entidadSaludId")

                    // Crear exámenes del estudio
                    val studyExams = patientExams.map { exam ->
                        StudyExam(
                            tipo_examen_nombre = exam.tipoExamenNombre,
                            titulo = exam.titulo,
                            valor = exam.valor,
                            unidad = exam.unidad,
                            observaciones = exam.observaciones,
                            datos_adicionales = exam.datosAdicionales?.let {
                                try { com.google.gson.Gson().fromJson(it, Map::class.java) as Map<String, Any> } catch (_: Exception) { null }
                            }
                        )
                    }

                    // Enviar como estudio conjunto
                    val study = StudyRequest(
                        paciente_id = patient.serverId,
                        fecha_estudio = null,
                        observaciones = "Estudio médico completo de ${patientExams.size} exámenes",
                        examenes = studyExams,
                        maleta_id = maletaId,
                        sede_id = sedeId
                    )

                    try {
                        val studyResp = apiService.createStudy("Bearer $token", study)
                        Log.d(tag, "✅ Estudio creado con ID ${studyResp.estudio_id} y ${studyResp.exam_ids.size} exámenes")
                        
                        // Marcar todos los exámenes como sincronizados
                        patientExams.forEachIndexed { index, exam ->
                            val serverExamId = studyResp.exam_ids.getOrNull(index) ?: 0L
                            examDao.markExamAsSynced(exam.id, serverExamId)
                        }
                        
                        syncedCount += patientExams.size
                        Log.d(tag, "✅ ${patientExams.size} exámenes sincronizados como estudio conjunto")
                    } catch (e: Exception) {
                        failedCount += patientExams.size
                        errors.add("Error sincronizando estudio de ${patientExams.size} exámenes: ${e.message}")
                        Log.e(tag, "❌ Error sincronizando estudio conjunto", e)
                    }
                } catch (e: Exception) {
                    failedCount += patientExams.size
                    errors.add("Error procesando ${patientExams.size} exámenes del paciente $patientId: ${e.message}")
                    Log.e(tag, "❌ Excepción procesando exámenes del paciente", e)
                }
            }

            updateSyncCounters()
            
            val syncResult = ExamRepository.SyncResult(
                success = failedCount == 0,
                syncedCount = syncedCount,
                failedCount = failedCount,
                totalCount = unsyncedExams.size,
                errors = errors
            )

            Log.d(tag, "🔄 Sincronización completada: ${syncResult.syncedCount}/${syncResult.totalCount} exitosos")
            Result.success(syncResult)

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización de exámenes", e)
            Result.failure(ExamRepository.RepositoryError.UnknownError(e))
        } finally {
            synchronized(syncMutex) {
                isSyncing = false
                Log.d(tag, "🔓 Bloqueo de sincronización liberado")
            }
        }
    }

    // Método para forzar sincronización de exámenes pendientes (incluso si hay solo 1)
    override suspend fun forceSyncExams(token: String): Result<ExamRepository.SyncResult> = withContext(Dispatchers.IO) {
        synchronized(syncMutex) {
            if (isSyncing) {
                Log.d(tag, "🔄 [FORZADA] Sincronización ya en progreso, saltando...")
                return@withContext Result.success(ExamRepository.SyncResult(
                    success = true,
                    syncedCount = 0,
                    failedCount = 0,
                    totalCount = 0,
                    errors = listOf("Sincronización ya en progreso")
                ))
            }
            
            isSyncing = true
        }
        
        try {
            if (!isNetworkAvailable()) {
                Log.d(tag, "❌ No hay red disponible para sincronización forzada")
                return@withContext Result.failure(ExamRepository.RepositoryError.NetworkError)
            }

            val unsyncedExams = examDao.getUnsyncedExams()
            Log.d(tag, "🔍 [FORZADA] Exámenes no sincronizados encontrados: ${unsyncedExams.size}")
            
            if (unsyncedExams.isEmpty()) {
                Log.d(tag, "📭 No hay exámenes pendientes para sincronizar")
                return@withContext Result.success(ExamRepository.SyncResult(
                    success = true,
                    syncedCount = 0,
                    failedCount = 0,
                    totalCount = 0,
                    errors = emptyList()
                ))
            }
            
            var syncedCount = 0
            var failedCount = 0
            val errors = mutableListOf<String>()

            // Agrupar exámenes por paciente
            val examsByPatient = unsyncedExams.groupBy { it.patientId }
            Log.d(tag, "📊 [FORZADA] Exámenes agrupados por ${examsByPatient.size} pacientes")

            Log.d(tag, "🔄 [FORZADA] Iniciando sincronización de ${unsyncedExams.size} exámenes...")

            for ((patientId, patientExams) in examsByPatient) {
                try {
                    // Obtener el server_id del paciente para sincronizar
                    val patient = patientDao.getPatientById(patientId)
                    if (patient?.serverId == null) {
                        Log.w(tag, "⚠️ Paciente $patientId no tiene server_id, saltando ${patientExams.size} exámenes")
                        failedCount += patientExams.size
                        errors.add("Paciente no sincronizado para ${patientExams.size} exámenes")
                        continue
                    }

                    // Obtener sede y maleta del paciente
                    val entidadSaludId = patient.entidadSaludId
                    val sedeId = if (entidadSaludId != null) {
                        val sede = getFirstSedeByEntidad(entidadSaludId)
                        sede?.serverId?.toString()
                    } else null
                    
                    val maletaId = if (entidadSaludId != null) {
                        val maleta = maletaDao.getMaletaByEntidadId(entidadSaludId)
                        maleta?.serverId?.toString()
                    } else null
                    
                    Log.d(tag, "🔍 [FORZADA] Sede ID: $sedeId, Maleta ID: $maletaId para entidad $entidadSaludId")

                    // Crear exámenes del estudio
                    val studyExams = patientExams.map { exam ->
                        StudyExam(
                            tipo_examen_nombre = exam.tipoExamenNombre,
                            titulo = exam.titulo,
                            valor = exam.valor,
                            unidad = exam.unidad,
                            observaciones = exam.observaciones,
                            datos_adicionales = exam.datosAdicionales?.let {
                                try { com.google.gson.Gson().fromJson(it, Map::class.java) as Map<String, Any> } catch (_: Exception) { null }
                            }
                        )
                    }

                    // Enviar como estudio conjunto
                    val study = StudyRequest(
                        paciente_id = patient.serverId,
                        fecha_estudio = null,
                        observaciones = "Estudio conjunto de ${patientExams.size} exámenes (sincronización forzada)",
                        examenes = studyExams,
                        maleta_id = maletaId,
                        sede_id = sedeId
                    )

                    try {
                        val studyResp = apiService.createStudy("Bearer $token", study)
                        Log.d(tag, "✅ [FORZADA] Estudio creado con ID ${studyResp.estudio_id} y ${studyResp.exam_ids.size} exámenes")
                        
                        // Marcar todos los exámenes como sincronizados
                        patientExams.forEachIndexed { index, exam ->
                            val serverExamId = studyResp.exam_ids.getOrNull(index) ?: 0L
                            examDao.markExamAsSynced(exam.id, serverExamId)
                        }
                        
                        syncedCount += patientExams.size
                        Log.d(tag, "✅ [FORZADA] ${patientExams.size} exámenes sincronizados como estudio conjunto")
                    } catch (e: Exception) {
                        failedCount += patientExams.size
                        errors.add("Error sincronizando estudio de ${patientExams.size} exámenes: ${e.message}")
                        Log.e(tag, "❌ [FORZADA] Error sincronizando estudio conjunto", e)
                    }
                } catch (e: Exception) {
                    failedCount += patientExams.size
                    errors.add("Error procesando ${patientExams.size} exámenes del paciente $patientId: ${e.message}")
                    Log.e(tag, "❌ [FORZADA] Excepción procesando exámenes del paciente", e)
                }
            }

            updateSyncCounters()
            
            val syncResult = ExamRepository.SyncResult(
                success = failedCount == 0,
                syncedCount = syncedCount,
                failedCount = failedCount,
                totalCount = unsyncedExams.size,
                errors = errors
            )

            Log.d(tag, "🔄 [FORZADA] Sincronización completada: ${syncResult.syncedCount}/${syncResult.totalCount} exitosos")
            Result.success(syncResult)

        } catch (e: Exception) {
            Log.e(tag, "❌ [FORZADA] Error en sincronización forzada de exámenes", e)
            Result.failure(ExamRepository.RepositoryError.UnknownError(e))
        } finally {
            synchronized(syncMutex) {
                isSyncing = false
                Log.d(tag, "🔓 [FORZADA] Bloqueo de sincronización liberado")
            }
        }
    }

    override suspend fun syncExam(token: String, examId: String): Result<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            val exam = examDao.getExamById(examId)
                ?: return@withContext Result.failure(ExamRepository.RepositoryError.NotFoundError)

            if (exam.sincronizado) {
                return@withContext Result.success(exam)
            }

            // Obtener el server_id del paciente para sincronizar
            val patient = patientDao.getPatientById(exam.patientId)
            if (patient?.serverId == null) {
                Log.w(tag, "⚠️ Paciente ${exam.patientId} no tiene server_id, no se puede sincronizar examen ${exam.titulo}")
                return@withContext Result.failure(ExamRepository.RepositoryError.NetworkError)
            }

            val examRequest = exam.toApiRequestWithServerId(patient.serverId)
            val result = createExamRemote(token, examRequest)
            
            if (result.isSuccess) {
                val serverResponse = result.getOrThrow()
                examDao.markExamAsSynced(exam.id, serverResponse.examen.id)
                updateSyncCounters()
                
                val updatedExam = examDao.getExamById(examId)
                    ?: return@withContext Result.failure(ExamRepository.RepositoryError.NotFoundError)
                Result.success(updatedExam)
            } else {
                Result.failure(result.exceptionOrNull() ?: ExamRepository.RepositoryError.NetworkError)
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error sincronizando examen individual", e)
            Result.failure(ExamRepository.RepositoryError.UnknownError(e))
        }
    }

    override suspend fun getUnsyncedExams(): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            val unsyncedExams = examDao.getUnsyncedExams()
            Log.d(tag, "🔍 getUnsyncedExams: ${unsyncedExams.size} exámenes pendientes")
            if (unsyncedExams.isNotEmpty()) {
                unsyncedExams.forEach { exam ->
                    Log.d(tag, "📋 Examen pendiente: ${exam.titulo} = ${exam.valor} (ID: ${exam.id})")
                }
            }
            unsyncedExams
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo exámenes no sincronizados", e)
            emptyList()
        }
    }

    override suspend fun markExamAsSynced(examId: String, serverId: Long) {
        try {
            examDao.markExamAsSynced(examId, serverId)
            updateSyncCounters()
            Log.d(tag, "✅ Examen marcado como sincronizado: $examId")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error marcando examen como sincronizado", e)
        }
    }


    override suspend fun forceSyncFromServer(token: String): Result<ExamRepository.SyncResult> = withContext(Dispatchers.IO) {
        try {
            val remoteResult = getExamsRemote(token)
            if (remoteResult.isSuccess) {
                val serverExams = remoteResult.getOrThrow().examenes
                val syncedCount = syncExamsFromServer(serverExams)
                
                val syncResult = ExamRepository.SyncResult(
                    success = true,
                    syncedCount = syncedCount,
                    failedCount = 0,
                    totalCount = serverExams.size
                )
                
                Result.success(syncResult)
            } else {
                Result.failure(remoteResult.exceptionOrNull() ?: ExamRepository.RepositoryError.NetworkError)
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización forzada", e)
            Result.failure(ExamRepository.RepositoryError.UnknownError(e))
        }
    }

    override suspend fun syncPatientExams(token: String, patientId: String): Result<ExamRepository.SyncResult> = withContext(Dispatchers.IO) {
        try {
            val remoteResult = getPatientExamsRemote(token, patientId)
            if (remoteResult.isSuccess) {
                val serverExams = remoteResult.getOrThrow().examenes
                val syncedCount = syncExamsFromServer(serverExams, patientId)
                
                val syncResult = ExamRepository.SyncResult(
                    success = true,
                    syncedCount = syncedCount,
                    failedCount = 0,
                    totalCount = serverExams.size
                )
                
                Result.success(syncResult)
            } else {
                Result.failure(remoteResult.exceptionOrNull() ?: ExamRepository.RepositoryError.NetworkError)
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error sincronizando exámenes del paciente", e)
            Result.failure(ExamRepository.RepositoryError.UnknownError(e))
        }
    }

    // ========== BÚSQUEDA Y FILTROS ==========

    override suspend fun searchExamsWithFilters(
        patientId: String?,
        tipoExamen: String?,
        startDate: String?,
        endDate: String?,
        limit: Int,
        offset: Int
    ): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            examDao.getExamsWithFilters(patientId, tipoExamen, startDate, endDate, limit, offset)
        } catch (e: Exception) {
            Log.e(tag, "Error buscando exámenes con filtros", e)
            emptyList()
        }
    }

    override suspend fun searchExamsByValue(searchValue: String): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            examDao.searchExamsByValue(searchValue)
        } catch (e: Exception) {
            Log.e(tag, "Error buscando exámenes por valor", e)
            emptyList()
        }
    }

    override suspend fun getCriticalExams(minUrgencyLevel: Int): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            examDao.getCriticalExams(minUrgencyLevel)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo exámenes críticos", e)
            emptyList()
        }
    }

    override suspend fun getRecentExams(limit: Int): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            examDao.getRecentExams(limit)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo exámenes recientes", e)
            emptyList()
        }
    }

    // ========== ESTADÍSTICAS ==========

    override suspend fun getExamStatsByType(): List<ExamRepository.ExamTypeStats> = withContext(Dispatchers.IO) {
        try {
            val stats = examDao.getExamStatsByType()
            stats.map { stat ->
                ExamRepository.ExamTypeStats(
                    tipoExamen = stat.tipo_examen_nombre,
                    totalCount = stat.total,
                    averageValue = stat.promedio,
                    minValue = stat.minimo,
                    maxValue = stat.maximo,
                    lastExamDate = null // TODO: Añadir fecha al query si se necesita
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo estadísticas por tipo", e)
            emptyList()
        }
    }

    override suspend fun getExamStats(): ExamRepository.ExamStats = withContext(Dispatchers.IO) {
        try {
            val totalExams = examDao.getTotalExamsCount()
            val unsyncedExams = examDao.getUnsyncedExamsCount()
            val syncedExams = totalExams - unsyncedExams
            val todayExams = examDao.getTodayExamsCount()
            val criticalExams = examDao.getCriticalExams().size
            
            // TODO: Implementar examsByType si se necesita
            val examsByType = emptyMap<String, Int>()

            ExamRepository.ExamStats(
                totalExams = totalExams,
                syncedExams = syncedExams,
                unsyncedExams = unsyncedExams,
                todayExams = todayExams,
                criticalExams = criticalExams,
                examsByType = examsByType
            )
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo estadísticas de exámenes", e)
            ExamRepository.ExamStats(0, 0, 0, 0, 0, emptyMap())
        }
    }

    override suspend fun getExamCountByPatient(patientId: String): Int = withContext(Dispatchers.IO) {
        try {
            examDao.getPatientExamsCount(patientId)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo conteo de exámenes del paciente", e)
            0
        }
    }

    override suspend fun getExamCountByType(tipoExamen: String): Int = withContext(Dispatchers.IO) {
        try {
            examDao.getExamsCountByType(tipoExamen)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo conteo de exámenes por tipo", e)
            0
        }
    }

    // ========== UTILIDADES ==========

    override suspend fun isNetworkAvailable(): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } catch (e: Exception) {
            Log.e(tag, "Error verificando conectividad", e)
            false
        }
    }

    override suspend fun performMaintenance(): Unit = withContext(Dispatchers.IO) {
        try {
            examDao.cleanupOldDeletedExams()
            examDao.cleanupOrphanedExams()
            updateSyncCounters()
            Log.d(tag, "✅ Mantenimiento de exámenes completado")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en mantenimiento de exámenes", e)
        }
    }

    override suspend fun getAvailableExamTypes(): List<String> = withContext(Dispatchers.IO) {
        try {
            tipoExamenDao.getAllTiposExamenNames()
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo tipos de examen disponibles", e)
            emptyList()
        }
    }

    // ========== MÉTODOS PRIVADOS ==========

    private suspend fun updateSyncCounters() {
        try {
            val unsyncedCount = examDao.getUnsyncedExamsCount()
            syncMetadataDao.updatePendingExamsCount(unsyncedCount)
        } catch (e: Exception) {
            Log.e(tag, "Error actualizando contadores de sync", e)
        }
    }

    private suspend fun syncExamsFromServer(serverExams: List<ExamData>, specificPatientId: String? = null): Int {
        var syncedCount = 0
        try {
            Log.d(tag, "🔄 Iniciando sincronización de ${serverExams.size} exámenes desde servidor...")
            
            for (serverExam in serverExams) {
                try {
                    // Solo sincronizar si tenemos el patientId correcto
                    val patientId = specificPatientId
                    if (patientId == null) {
                        Log.w(tag, "⚠️ Saltando examen ${serverExam.titulo} - sin patientId específico")
                        continue
                    }
                    
                    // VERIFICAR si ya existe un examen local con el mismo título y valor para este paciente
                    val existingLocalExam = examDao.getExamByTitleAndValue(patientId, serverExam.titulo, serverExam.valor)
                    
                    if (existingLocalExam != null) {
                        Log.d(tag, "🔄 Examen local ya existe: ${serverExam.titulo} = ${serverExam.valor} - NO sobrescribiendo")
                        continue
                    }
                    
                    val localExam = ExamEntity.fromApiData(serverExam, patientId)
                    examDao.insertExam(localExam)
                    syncedCount++
                    Log.d(tag, "✅ Examen sincronizado: ${serverExam.titulo} para paciente $patientId")
                } catch (e: Exception) {
                    Log.e(tag, "❌ Error sincronizando examen desde servidor: ${serverExam.titulo}", e)
                }
            }
            updateSyncCounters()
            Log.d(tag, "✅ Sincronizados $syncedCount exámenes desde servidor")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización desde servidor", e)
        }
        return syncedCount
    }

    private fun mapNetworkException(exception: Throwable): ExamRepository.RepositoryError {
        return when (exception) {
            is java.net.UnknownHostException, 
            is java.net.ConnectException,
            is java.net.SocketTimeoutException -> ExamRepository.RepositoryError.NetworkError
            is retrofit2.HttpException -> {
                when (exception.code()) {
                    401 -> ExamRepository.RepositoryError.AuthenticationError
                    404 -> ExamRepository.RepositoryError.NotFoundError
                    422 -> ExamRepository.RepositoryError.ValidationError
                    else -> ExamRepository.RepositoryError.ServerError(exception.code(), exception.message())
                }
            }
            else -> ExamRepository.RepositoryError.UnknownError(exception)
        }
    }
    
    override suspend fun getExamsByCitaId(citaId: Int): List<ExamEntity> = withContext(Dispatchers.IO) {
        try {
            // TODO: Implementar consulta por cita_id cuando esté disponible en la BD local
            // Por ahora, devolver lista vacía
            Log.d(tag, "🔍 getExamsByCitaId: Consultando exámenes para cita $citaId")
            emptyList()
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo exámenes por cita", e)
            emptyList()
        }
    }
    
    override suspend fun createStudy(study: com.example.telephases.network.StudyRequest): Result<com.example.telephases.network.StudyCreateResponse> = withContext(Dispatchers.IO) {
        try {
            if (!isNetworkAvailable()) {
                Log.d(tag, "❌ No hay red disponible para crear estudio")
                return@withContext Result.failure(ExamRepository.RepositoryError.NetworkError)
            }
            
            val token = authRepository.getCurrentToken()
            if (token == null) {
                Log.d(tag, "❌ No hay token disponible para crear estudio")
                return@withContext Result.failure(ExamRepository.RepositoryError.AuthenticationError)
            }
            
            Log.d(tag, "🎯 Creando estudio con ${study.examenes.size} exámenes para paciente ${study.paciente_id}")
            
            val result = apiService.createStudy("Bearer $token", study)
            Log.d(tag, "✅ Estudio creado exitosamente con ID ${result.estudio_id}")
            
            Result.success(result)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error creando estudio", e)
            Result.failure(mapNetworkException(e))
        }
    }

}

