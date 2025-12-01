package com.example.telephases.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.telephases.data.local.dao.PatientDao
import com.example.telephases.data.local.dao.SyncMetadataDao
import com.example.telephases.data.local.entities.PatientEntity
import com.example.telephases.network.ApiInterface
import com.example.telephases.network.Patient
import com.example.telephases.network.PatientRegistrationRequest
import com.example.telephases.network.PatientRegistrationResponse
import com.example.telephases.network.PatientSearchResponse
import com.example.telephases.network.PatientSyncRequest
import com.example.telephases.network.PatientSyncData
import com.example.telephases.network.PatientSyncResponse
import com.example.telephases.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del Repository para manejo de pacientes
 * Implementa patrón Offline-First con sincronización automática
 */
@Singleton
class PatientRepositoryImpl @Inject constructor(
    private val patientDao: PatientDao,
    private val syncMetadataDao: SyncMetadataDao,
    private val apiService: ApiInterface,
    private val context: Context,
    private val authRepository: AuthRepository
) : PatientRepository {

    private val tag = "PatientRepository"

    // ========== OPERACIONES LOCALES (OFFLINE) ==========

    override suspend fun getLocalPatients(): List<PatientEntity> = withContext(Dispatchers.IO) {
        try {
            patientDao.getAllPatients()
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo pacientes locales", e)
            emptyList()
        }
    }

    override fun getLocalPatientsFlow(): Flow<List<PatientEntity>> {
        return patientDao.getAllPatientsFlow()
    }

    override suspend fun getLocalPatientById(patientId: String): PatientEntity? = withContext(Dispatchers.IO) {
        try {
            patientDao.getPatientById(patientId)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo paciente por ID: $patientId", e)
            null
        }
    }

    override suspend fun getLocalPatientByDocument(numeroDocumento: String): PatientEntity? = withContext(Dispatchers.IO) {
        try {
            patientDao.getPatientByDocument(numeroDocumento)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo paciente por documento: $numeroDocumento", e)
            null
        }
    }

    override suspend fun searchLocalPatientsByName(searchTerm: String): List<PatientEntity> = withContext(Dispatchers.IO) {
        try {
            patientDao.searchPatientsByName(searchTerm)
        } catch (e: Exception) {
            Log.e(tag, "Error buscando pacientes por nombre: $searchTerm", e)
            emptyList()
        }
    }

    override suspend fun insertLocalPatient(patient: PatientEntity): String = withContext(Dispatchers.IO) {
        try {
            patientDao.insertPatient(patient)
            updateSyncCounters()
            Log.d(tag, "✅ Paciente insertado localmente: ${patient.nombreCompleto}")
            patient.id
        } catch (e: Exception) {
            Log.e(tag, "❌ Error insertando paciente local", e)
            throw PatientRepository.RepositoryError.UnknownError(e)
        }
    }

    override suspend fun updateLocalPatient(patient: PatientEntity): Unit = withContext(Dispatchers.IO) {
        try {
            val updatedPatient = patient.copy(
                modificadoLocalmente = true,
                fechaModificacionLocal = java.time.Instant.now().toString(),
                sincronizado = false
            )
            patientDao.updatePatient(updatedPatient)
            updateSyncCounters()
            Log.d(tag, "✅ Paciente actualizado localmente: ${patient.nombreCompleto}")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error actualizando paciente local", e)
            throw PatientRepository.RepositoryError.UnknownError(e)
        }
    }

    override suspend fun deleteLocalPatient(patientId: String): Unit = withContext(Dispatchers.IO) {
        try {
            patientDao.deletePatient(patientId)
            updateSyncCounters()
            Log.d(tag, "✅ Paciente eliminado localmente: $patientId")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error eliminando paciente local", e)
            throw PatientRepository.RepositoryError.UnknownError(e)
        }
    }

    // ========== OPERACIONES REMOTAS (ONLINE) ==========

    override suspend fun registerPatientRemote(
        token: String,
        request: PatientRegistrationRequest
    ): Result<PatientRegistrationResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.registerPatient("Bearer $token", request)
            Log.d(tag, "✅ Paciente registrado en servidor: ${response.paciente.nombre_completo}")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error registrando paciente en servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun searchPatientRemote(
        token: String,
        numeroDocumento: String
    ): Result<PatientSearchResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.searchPatient("Bearer $token", numeroDocumento)
            Log.d(tag, "✅ Paciente encontrado en servidor: ${response.paciente.nombre_completo}")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error buscando paciente en servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun getAllPatientsRemote(token: String): Result<List<Patient>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllPatients("Bearer $token")
            Log.d(tag, "✅ Obtenidos ${response.pacientes.size} pacientes del servidor")
            Result.success(response.pacientes)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo pacientes del servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    // ========== OPERACIONES UNIFICADAS (OFFLINE-FIRST) ==========

    override suspend fun registerPatient(
        token: String?,
        request: PatientRegistrationRequest
    ): Result<PatientEntity> = withContext(Dispatchers.IO) {
        try {
            // Verificar si ya existe localmente
            val existingPatient = patientDao.getPatientByDocument(request.numero_documento)
            if (existingPatient != null) {
                return@withContext Result.failure(PatientRepository.RepositoryError.DuplicateError)
            }

            // Verificar si el token es válido para llamadas al servidor
            val isValidServerToken = token != null && 
                !token.startsWith("OFFLINE_TOKEN") && 
                !token.startsWith("TEST_TOKEN_") && 
                isNetworkAvailable()

            if (isValidServerToken) {
                // MODO ONLINE: Registrar en servidor primero
                val remoteResult = registerPatientRemote(token!!, request)
                if (remoteResult.isSuccess) {
                    // Guardar en local desde respuesta del servidor
                    val serverPatient = remoteResult.getOrThrow().paciente
                    val localPatient = PatientEntity.fromApiModel(serverPatient)
                    
                    // CORRECCIÓN: Preservar datos del request que el servidor no devuelve
                    val finalLocalPatient = localPatient.copy(
                        // Preservar entidad de salud si el servidor no la devolvió
                        entidadSaludId = localPatient.entidadSaludId ?: request.entidad_salud_id,
                        // Preservar fecha de nacimiento si el servidor no la devolvió
                        fechaNacimiento = localPatient.fechaNacimiento ?: request.fecha_nacimiento,
                        // Preservar género si el servidor no lo devolvió
                        genero = localPatient.genero ?: request.genero,
                        // Preservar teléfono si el servidor no lo devolvió
                        telefono = localPatient.telefono ?: request.telefono,
                        // Preservar dirección si el servidor no la devolvió
                        direccion = localPatient.direccion ?: request.direccion,
                        // Preservar estado civil si el servidor no lo devolvió
                        estadoCivil = localPatient.estadoCivil ?: request.estado_civil,
                        // Preservar país si el servidor no lo devolvió
                        pais = localPatient.pais ?: request.pais,
                        // Preservar departamento si el servidor no lo devolvió
                        departamento = localPatient.departamento ?: request.departamento,
                        // Preservar municipio si el servidor no lo devolvió
                        municipio = localPatient.municipio ?: request.municipio,
                        // Preservar tipo de usuario si el servidor no lo devolvió
                        tipoUsuario = localPatient.tipoUsuario ?: request.tipo_usuario,
                        // Preservar tipo de identificación si el servidor no lo devolvió
                        tipoIdentificacion = localPatient.tipoIdentificacion ?: request.tipo_identificacion
                    )
                    
                    patientDao.insertPatient(finalLocalPatient)
                    
                    Log.d(tag, "✅ Paciente registrado online y guardado localmente")
                    return@withContext Result.success(finalLocalPatient)
                } else {
                    Log.w(tag, "⚠️ Fallo registro online, guardando offline")
                }
            } else {
                Log.d(tag, "ℹ️ Token offline o sin red, guardando localmente para sincronizar después")
            }

            // MODO OFFLINE: Guardar solo localmente
            val offlinePatient = PatientEntity.createForOffline(
                primerNombre = request.primer_nombre,
                primerApellido = request.primer_apellido,
                numeroDocumento = request.numero_documento,
                segundoNombre = request.segundo_nombre,
                segundoApellido = request.segundo_apellido,
                tipoDocumentoId = request.tipo_documento_id,
                email = request.email,
                telefono = request.telefono,
                direccion = request.direccion,
                ciudadId = request.ciudad_id,
                fechaNacimiento = request.fecha_nacimiento,
                genero = request.genero,
                entidadSaludId = request.entidad_salud_id, // CORRECCIÓN: Pasar entidad del request
                tipoUsuario = request.tipo_usuario
            )

            patientDao.insertPatient(offlinePatient)
            updateSyncCounters()
            
            Log.d(tag, "✅ Paciente registrado offline: ${offlinePatient.nombreCompleto}")
            Result.success(offlinePatient)

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en registerPatient", e)
            Result.failure(PatientRepository.RepositoryError.UnknownError(e))
        }
    }

    override suspend fun searchPatientByDocument(
        token: String?,
        numeroDocumento: String
    ): Result<PatientEntity> = withContext(Dispatchers.IO) {
        try {
            // PRIMERO: Buscar en local (OFFLINE-FIRST)
            val localPatient = patientDao.getPatientByDocument(numeroDocumento)
            if (localPatient != null) {
                Log.d(tag, "✅ Paciente encontrado localmente: ${localPatient.nombreCompleto}")
                
                // Si hay red, intentar actualizar en background
                val isValidServerToken = token != null && 
                    !token.startsWith("OFFLINE_TOKEN") && 
                    !token.startsWith("TEST_TOKEN_") && 
                    isNetworkAvailable()
                
                if (isValidServerToken) {
                    tryUpdatePatientFromServer(token!!, localPatient)
                }
                
                return@withContext Result.success(localPatient)
            }

            // SEGUNDO: Si no existe local y hay red, buscar en servidor
            val isValidServerToken = token != null && 
                !token.startsWith("OFFLINE_TOKEN") && 
                !token.startsWith("TEST_TOKEN_") && 
                isNetworkAvailable()
            
            if (isValidServerToken) {
                val remoteResult = searchPatientRemote(token!!, numeroDocumento)
                if (remoteResult.isSuccess) {
                    // Guardar en local desde servidor
                    val serverPatient = remoteResult.getOrThrow().paciente
                    val localPatient = PatientEntity.fromApiModel(serverPatient)
                    patientDao.insertPatient(localPatient)
                    
                    Log.d(tag, "✅ Paciente encontrado en servidor y guardado localmente")
                    return@withContext Result.success(localPatient)
                }
            }

            // NO ENCONTRADO
            Log.d(tag, "❌ Paciente no encontrado: $numeroDocumento")
            Result.failure(PatientRepository.RepositoryError.NotFoundError)

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en searchPatientByDocument", e)
            Result.failure(PatientRepository.RepositoryError.UnknownError(e))
        }
    }

    override suspend fun getAllPatients(token: String?): List<PatientEntity> = withContext(Dispatchers.IO) {
        try {
            // PRIMERO: Obtener datos locales
            val localPatients = patientDao.getAllPatients()
            Log.d(tag, "📱 ${localPatients.size} pacientes en BD local")

            // Si hay red, sincronizar PRIMERO y luego devolver datos actualizados
            val isValidServerToken = token != null && 
                !token.startsWith("OFFLINE_TOKEN") && 
                !token.startsWith("TEST_TOKEN_") && 
                isNetworkAvailable()
            
            if (isValidServerToken) {
                Log.d(tag, "🌐 Red disponible, sincronizando pacientes del servidor...")
                try {
                    val remoteResult = getAllPatientsRemote(token!!)
                    if (remoteResult.isSuccess) {
                        val serverPatients = remoteResult.getOrThrow()
                        Log.d(tag, "✅ ${serverPatients.size} pacientes obtenidos del servidor")
                        
                        val syncResult = syncPatientsFromServer(serverPatients)
                        Log.d(tag, "✅ Sincronización completada: $syncResult pacientes sincronizados")
                        
                        // Obtener los datos actualizados DESPUÉS de la sincronización
                        val updatedLocalPatients = patientDao.getAllPatients()
                        Log.d(tag, "✅ Después de sincronización: ${updatedLocalPatients.size} pacientes en BD local")
                        
                        return@withContext updatedLocalPatients
                    } else {
                        Log.w(tag, "⚠️ Error obteniendo datos remotos: ${remoteResult.exceptionOrNull()?.message}")
                    }
                } catch (e: Exception) {
                    Log.w(tag, "⚠️ Error en sincronización, usando datos locales", e)
                    // Continuar con datos locales si falla la sync
                }
            } else {
                Log.d(tag, "📱 Sin red o sin token, usando solo datos locales")
            }

            // Devolver datos locales (originales o sin cambios si falló la sync)
            localPatients
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en getAllPatients", e)
            emptyList()
        }
    }

    override suspend fun updatePatient(
        token: String?,
        patient: PatientEntity
    ): Result<PatientEntity> = withContext(Dispatchers.IO) {
        try {
            // Actualizar localmente primero
            updateLocalPatient(patient)

            // Si hay red, intentar sincronizar
            val isValidServerToken = token != null && 
                !token.startsWith("OFFLINE_TOKEN") && 
                !token.startsWith("TEST_TOKEN_") && 
                isNetworkAvailable()
            
            if (isValidServerToken) {
                // TODO: Implementar updatePatientRemote cuando esté disponible en la API
                Log.d(tag, "ℹ️ Actualización remota pendiente - no implementado en API")
            }

            Result.success(patient)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error actualizando paciente", e)
            Result.failure(PatientRepository.RepositoryError.UnknownError(e))
        }
    }

    // ========== SINCRONIZACIÓN ==========

    override suspend fun syncPatients(token: String): Result<PatientRepository.SyncResult> = withContext(Dispatchers.IO) {
        try {
            if (!isNetworkAvailable()) {
                return@withContext Result.failure(PatientRepository.RepositoryError.NetworkError)
            }

            // Verificar si el token es válido para llamadas al servidor
            if (token.startsWith("OFFLINE_TOKEN") || token.startsWith("TEST_TOKEN_")) {
                Log.d(tag, "ℹ️ Token offline o de prueba detectado, cancelando sincronización de pacientes")
                return@withContext Result.success(PatientRepository.SyncResult(
                    success = false,
                    syncedCount = 0,
                    failedCount = 0,
                    totalCount = 0,
                    errors = listOf("Token offline - no se puede sincronizar con servidor")
                ))
            }

            val unsyncedPatients = patientDao.getUnsyncedPatients()
            
            if (unsyncedPatients.isEmpty()) {
                Log.d(tag, "ℹ️ No hay pacientes pendientes de sincronización")
                return@withContext Result.success(PatientRepository.SyncResult(
                    success = true,
                    syncedCount = 0,
                    failedCount = 0,
                    totalCount = 0,
                    errors = emptyList()
                ))
            }

            Log.d(tag, "🔄 Iniciando sincronización masiva de ${unsyncedPatients.size} pacientes...")

            // Convertir pacientes a formato de API
            val patientsSyncData = unsyncedPatients.map { patient ->
                PatientSyncData(
                    id = patient.id,
                    username = patient.email ?: "paciente_${patient.numeroDocumento}",
                    primer_nombre = patient.primerNombre,
                    segundo_nombre = patient.segundoNombre,
                    primer_apellido = patient.primerApellido,
                    segundo_apellido = patient.segundoApellido,
                    tipo_documento_id = patient.tipoDocumentoId,
                    numero_documento = patient.numeroDocumento,
                    email = patient.email,
                    telefono = patient.telefono,
                    direccion = patient.direccion,
                    ciudad_id = patient.ciudadId,
                    fecha_nacimiento = patient.fechaNacimiento,
                    genero = patient.genero,
                    tipo_identificacion = patient.tipoIdentificacion,
                    estado_civil = patient.estadoCivil,
                    pais = patient.pais,
                    municipio = patient.municipio,
                    departamento = patient.departamento,
                    entidad_salud_id = patient.entidadSaludId,
                    rol_id = 2 // Los pacientes siempre tienen rol_id = 2 (Paciente)
                )
            }

            val request = PatientSyncRequest(patients = patientsSyncData)

            try {
                // Llamar al endpoint masivo
                val response = apiService.syncPatients("Bearer $token", request)
                
                Log.d(tag, "📊 Respuesta de sincronización: ${response.results.synced}/${response.results.total} exitosos")

                // Marcar pacientes sincronizados como sincronizados
                response.results.created.forEach { patientId ->
                    try {
                        patientDao.markPatientAsSynced(patientId)
                        // Log.d(tag, "✅ Paciente marcado como sincronizado: $patientId")
                    } catch (e: Exception) {
                        Log.e(tag, "Error marcando paciente $patientId como sincronizado", e)
                    }
                }

                val syncedCount = response.results.synced
                val failedCount = response.results.failed
                val errors = response.results.errors.map { "${it.id}: ${it.error}" }

                updateSyncCounters()
                
                val syncResult = PatientRepository.SyncResult(
                    success = failedCount == 0,
                    syncedCount = syncedCount,
                    failedCount = failedCount,
                    totalCount = unsyncedPatients.size,
                    errors = errors
                )

                Log.d(tag, "🔄 Sincronización completada: ${syncResult.syncedCount}/${syncResult.totalCount} exitosos")
                return@withContext Result.success(syncResult)

            } catch (apiException: Exception) {
                Log.e(tag, "❌ Error en sincronización masiva de pacientes", apiException)
                return@withContext Result.failure(PatientRepository.RepositoryError.NetworkError)
            }

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización de pacientes", e)
            Result.failure(PatientRepository.RepositoryError.UnknownError(e))
        }
    }

    override suspend fun syncPatient(token: String, patientId: String): Result<PatientEntity> = withContext(Dispatchers.IO) {
        try {
            val patient = patientDao.getPatientById(patientId)
                ?: return@withContext Result.failure(PatientRepository.RepositoryError.NotFoundError)

            if (patient.sincronizado) {
                return@withContext Result.success(patient)
            }

            val syncResult = syncPatients(token)
            if (syncResult.isSuccess && syncResult.getOrThrow().syncedCount > 0) {
                // Obtener paciente actualizado
                val updatedPatient = patientDao.getPatientById(patientId)
                    ?: return@withContext Result.failure(PatientRepository.RepositoryError.NotFoundError)
                Result.success(updatedPatient)
            } else {
                Result.failure(PatientRepository.RepositoryError.NetworkError)
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error sincronizando paciente individual", e)
            Result.failure(PatientRepository.RepositoryError.UnknownError(e))
        }
    }

    override suspend fun getUnsyncedPatients(): List<PatientEntity> = withContext(Dispatchers.IO) {
        try {
            patientDao.getUnsyncedPatients()
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo pacientes no sincronizados", e)
            emptyList()
        }
    }

    override suspend fun markPatientAsSynced(patientId: String, serverId: String?) {
        try {
            patientDao.markPatientAsSynced(patientId)
            updateSyncCounters()
            // Log.d(tag, "✅ Paciente marcado como sincronizado: $patientId")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error marcando paciente como sincronizado", e)
        }
    }

    override suspend fun forceSyncFromServer(token: String): Result<PatientRepository.SyncResult> = withContext(Dispatchers.IO) {
        try {
            // Verificar si el token es válido para llamadas al servidor
            if (token.startsWith("OFFLINE_TOKEN") || token.startsWith("TEST_TOKEN_")) {
                Log.d(tag, "ℹ️ Token offline o de prueba detectado, cancelando sincronización forzada")
                return@withContext Result.success(PatientRepository.SyncResult(
                    success = false,
                    syncedCount = 0,
                    failedCount = 0,
                    totalCount = 0,
                    errors = listOf("Token offline - no se puede sincronizar con servidor")
                ))
            }

            val remoteResult = getAllPatientsRemote(token)
            if (remoteResult.isSuccess) {
                val serverPatients = remoteResult.getOrThrow()
                val syncedCount = syncPatientsFromServer(serverPatients)
                
                val syncResult = PatientRepository.SyncResult(
                    success = true,
                    syncedCount = syncedCount,
                    failedCount = 0,
                    totalCount = serverPatients.size
                )
                
                Result.success(syncResult)
            } else {
                Result.failure(remoteResult.exceptionOrNull() ?: PatientRepository.RepositoryError.NetworkError)
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización forzada", e)
            Result.failure(PatientRepository.RepositoryError.UnknownError(e))
        }
    }

    // ========== UTILIDADES ==========

    override suspend fun existsPatientWithDocument(numeroDocumento: String): Boolean = withContext(Dispatchers.IO) {
        try {
            patientDao.existsPatientWithDocument(numeroDocumento)
        } catch (e: Exception) {
            Log.e(tag, "Error verificando existencia de paciente", e)
            false
        }
    }

    override suspend fun getPatientStats(): PatientRepository.PatientStats = withContext(Dispatchers.IO) {
        try {
            val totalPatients = patientDao.getTotalPatientsCount()
            val unsyncedPatients = patientDao.getUnsyncedPatientsCount()
            val syncedPatients = totalPatients - unsyncedPatients
            val todayRegistrations = patientDao.getTodayPatientsCount()
            val recentPatients = patientDao.getRecentPatients(5)

            PatientRepository.PatientStats(
                totalPatients = totalPatients,
                syncedPatients = syncedPatients,
                unsyncedPatients = unsyncedPatients,
                todayRegistrations = todayRegistrations,
                recentPatients = recentPatients
            )
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo estadísticas de pacientes", e)
            PatientRepository.PatientStats(0, 0, 0, 0, emptyList())
        }
    }

    override suspend fun performMaintenance(): Unit = withContext(Dispatchers.IO) {
        try {
            patientDao.cleanupOldDeletedPatients()
            updateSyncCounters()
            Log.d(tag, "✅ Mantenimiento de pacientes completado")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en mantenimiento de pacientes", e)
        }
    }

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

    // ========== MÉTODOS PRIVADOS ==========

    private suspend fun updateSyncCounters() {
        try {
            val unsyncedCount = patientDao.getUnsyncedPatientsCount()
            syncMetadataDao.updatePendingPatientsCount(unsyncedCount)
        } catch (e: Exception) {
            Log.e(tag, "Error actualizando contadores de sync", e)
        }
    }

    private suspend fun syncPatientsFromServer(serverPatients: List<Patient>): Int {
        var syncedCount = 0
        try {
            Log.d(tag, "🔄 Iniciando sincronización de ${serverPatients.size} pacientes desde servidor...")
            
            // PRIMERO: Eliminar todos los administradores de la tabla de pacientes
            try {
                val deletedCount = patientDao.deleteAdminPatients()
                Log.d(tag, "🗑️ Eliminados $deletedCount administradores de la tabla de pacientes")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error eliminando administradores de pacientes", e)
            }
            
            // Obtener entidad del usuario activo para rellenar pacientes sin entidad
            val currentUserEntidadId = getCurrentUserEntidadId()
            
            // FILTRAR: Solo procesar pacientes reales (rol_id = 2)
            val realPatients = serverPatients.filter { patient ->
                // Verificar que sea un paciente real (no administrador)
                val email = patient.email ?: ""
                val isRealPatient = !email.startsWith("contacto@") && 
                                  !email.startsWith("admin@") &&
                                  !email.startsWith("medico@")
                
                if (!isRealPatient) {
                    Log.d(tag, "⚠️ Saltando administrador: ${patient.nombre_completo} ($email)")
                }
                
                isRealPatient
            }
            
            Log.d(tag, "📊 Procesando ${realPatients.size} pacientes reales de ${serverPatients.size} usuarios totales")
            
            for (serverPatient in realPatients) {
                try {
                    Log.d(tag, "🔄 Procesando paciente: ${serverPatient.nombre_completo} (ID: ${serverPatient.id})")
                    
                    // VERIFICAR si ya existe el paciente localmente
                    val existingPatient = patientDao.getPatientByDocument(serverPatient.numero_documento)
                    
                    if (existingPatient != null) {
                        Log.d(tag, "🔄 Paciente ya existe localmente: ${serverPatient.nombre_completo} - actualizando ID del servidor")
                        
                        // Actualizar el ID del servidor y marcarlo como sincronizado
                        patientDao.updatePatientServerId(existingPatient.id, serverPatient.id)
                        patientDao.markPatientAsSynced(serverPatient.id)
                        syncedCount++
                        
                        Log.d(tag, "✅ Paciente actualizado exitosamente: ${serverPatient.nombre_completo}")
                    } else {
                        // Crear nuevo paciente solo si no existe
                        val localPatient = PatientEntity.fromApiModel(serverPatient)
                        
                        // CORRECCIÓN: Si el servidor devolvió null y tenemos entidad del usuario, asignarla
                        val finalLocalPatient = if (localPatient.entidadSaludId == null && currentUserEntidadId != null) {
                            localPatient.copy(entidadSaludId = currentUserEntidadId)
                        } else {
                            localPatient
                        }
                        
                        Log.d(tag, "✅ Entidad local creada: ${finalLocalPatient.nombreCompleto}")
                        
                        val insertResult = patientDao.insertPatient(finalLocalPatient)
                        Log.d(tag, "✅ Paciente insertado en BD local con resultado: $insertResult")
                        
                        syncedCount++
                        Log.d(tag, "✅ Paciente sincronizado exitosamente: ${serverPatient.nombre_completo}")
                    }
                } catch (e: Exception) {
                    Log.e(tag, "❌ Error sincronizando paciente desde servidor: ${serverPatient.nombre_completo}", e)
                    Log.e(tag, "❌ Detalles del error: ${e.message}")
                    Log.e(tag, "❌ Stack trace: ${e.stackTraceToString()}")
                }
            }
            
            updateSyncCounters()
            Log.d(tag, "✅ Sincronización completada: $syncedCount pacientes sincronizados desde servidor")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error general en sincronización desde servidor", e)
            Log.e(tag, "❌ Detalles del error: ${e.message}")
            Log.e(tag, "❌ Stack trace: ${e.stackTraceToString()}")
        }
        return syncedCount
    }

    private suspend fun tryUpdatePatientFromServer(token: String, localPatient: PatientEntity) {
        try {
            // Esta funcionalidad requeriría un endpoint específico en la API
            // Por ahora solo loggeamos la intención
            Log.d(tag, "ℹ️ Actualización desde servidor pendiente para: ${localPatient.nombreCompleto}")
        } catch (e: Exception) {
            Log.w(tag, "Error actualizando paciente desde servidor", e)
        }
    }

    private suspend fun getCurrentUserEntidadId(): Int? {
        return try {
            val currentUser = authRepository.getCurrentUser()
            currentUser?.entidadSaludId
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo entidad del usuario activo", e)
            null
        }
    }

    override suspend fun getExamCountForPatient(patientId: String): Int = withContext(Dispatchers.IO) {
        try {
            // Por ahora retornamos 0, esto se implementaría con un DAO de exámenes
            // TODO: Implementar con ExamDao.getExamCountForPatient(patientId)
            Log.d(tag, "📊 Obteniendo cantidad de exámenes para paciente: $patientId")
            0
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo cantidad de exámenes", e)
            0
        }
    }

    override suspend fun getEntidadSaludName(entidadId: Int): String = withContext(Dispatchers.IO) {
        try {
            // Por ahora retornamos un nombre genérico, esto se implementaría con un DAO de entidades
            // TODO: Implementar con EntidadSaludDao.getEntidadSaludName(entidadId)
            Log.d(tag, "🏥 Obteniendo nombre de entidad: $entidadId")
            "Entidad de Salud $entidadId"
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo nombre de entidad", e)
            "No especificada"
        }
    }

    private fun mapNetworkException(exception: Throwable): PatientRepository.RepositoryError {
        return when (exception) {
            is java.net.UnknownHostException, 
            is java.net.ConnectException,
            is java.net.SocketTimeoutException -> PatientRepository.RepositoryError.NetworkError
            is retrofit2.HttpException -> {
                when (exception.code()) {
                    401 -> PatientRepository.RepositoryError.AuthenticationError
                    404 -> PatientRepository.RepositoryError.NotFoundError
                    409 -> PatientRepository.RepositoryError.DuplicateError
                    422 -> PatientRepository.RepositoryError.ValidationError
                    else -> PatientRepository.RepositoryError.ServerError(exception.code(), exception.message())
                }
            }
            else -> PatientRepository.RepositoryError.UnknownError(exception)
        }
    }
}

