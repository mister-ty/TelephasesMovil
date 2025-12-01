package com.example.telephases.data.repository

import android.content.Context
import android.util.Log
import com.example.telephases.data.local.dao.SyncMetadataDao
import com.example.telephases.data.local.entities.SyncMetadataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor centralizado de sincronización
 * Coordina la sincronización entre diferentes repositories
 */
@Singleton
class SyncManager @Inject constructor(
    private val patientRepository: PatientRepository,
    private val examRepository: ExamRepository,
    private val authRepository: AuthRepository,
    private val maletaRepository: MaletaRepository,
    private val syncMetadataDao: SyncMetadataDao,
    private val context: Context
) {

    private val tag = "SyncManager"

    /**
     * Ejecuta sincronización completa de todos los datos
     */
    suspend fun syncAll(token: String? = null): SyncAllResult = withContext(Dispatchers.IO) {
        Log.d(tag, "🔄 Iniciando sincronización completa...")
        
        val startTime = System.currentTimeMillis()
        var hasErrors = false
        val results = mutableMapOf<String, Any>()
        val errors = mutableListOf<String>()

        try {
            // Verificar conectividad
            if (!isNetworkAvailable()) {
                Log.w(tag, "⚠️ Sin conectividad - cancelando sincronización")
                return@withContext SyncAllResult(
                    success = false,
                    totalTime = 0,
                    results = emptyMap(),
                    errors = listOf("Sin conectividad de red")
                )
            }

            // Obtener token si no se proporcionó
            val authToken = token ?: authRepository.getCurrentToken()
            if (authToken == null) {
                Log.w(tag, "⚠️ Sin token válido - cancelando sincronización")
                return@withContext SyncAllResult(
                    success = false,
                    totalTime = 0,
                    results = emptyMap(),
                    errors = listOf("Sin token de autenticación válido")
                )
            }

        // Verificar si el token es válido para llamadas al servidor
        val isValidServerToken = !authToken.startsWith("OFFLINE_TOKEN") && !authToken.startsWith("TEST_TOKEN_")
        if (!isValidServerToken) {
            Log.w(tag, "⚠️ Token offline o de prueba - cancelando sincronización con servidor")
            return@withContext SyncAllResult(
                success = false,
                totalTime = 0,
                results = emptyMap(),
                errors = listOf("Token offline - no se puede sincronizar con servidor")
            )
        }
        
        Log.d(tag, "🌐 Token JWT válido detectado - iniciando sincronización con servidor")

            // Marcar inicio de sincronización
            syncMetadataDao.updateMetadataValue(
                SyncMetadataEntity.LAST_SYNC_TIMESTAMP,
                java.time.Instant.now().toString()
            )

            // PASO 1: Sincronizar entidades PRIMERO (necesario para mapeo de IDs)
            Log.d(tag, "📍 Sincronizando entidades de salud...")
            try {
                val entidadesResult = syncEntidades(authToken)
                results["entidades"] = entidadesResult
                Log.d(tag, "✅ Sincronización de entidades completada: ${entidadesResult.mapping.size} mapeadas")
            } catch (e: Exception) {
                hasErrors = true
                errors.add("Entidades: ${e.message}")
                Log.e(tag, "❌ Error sincronizando entidades", e)
            }

            // PASO 2: Ejecutar sincronizaciones en paralelo
            val syncJobs = listOf(
                async { 
                    try {
                        val result = patientRepository.syncPatients(authToken)
                        results["patients"] = result.getOrThrow()
                        Log.d(tag, "✅ Sincronización de pacientes completada")
                    } catch (e: Exception) {
                        hasErrors = true
                        errors.add("Pacientes: ${e.message}")
                        Log.e(tag, "❌ Error sincronizando pacientes", e)
                    }
                },
                async { 
                    try {
                        val result = examRepository.syncExams(authToken)
                        results["exams"] = result.getOrThrow()
                        Log.d(tag, "✅ Sincronización de exámenes completada")
                    } catch (e: Exception) {
                        hasErrors = true
                        errors.add("Exámenes: ${e.message}")
                        Log.e(tag, "❌ Error sincronizando exámenes", e)
                    }
                },
                async { 
                    try {
                        val result = authRepository.syncUsers()
                        results["users"] = result.getOrThrow()
                        Log.d(tag, "✅ Sincronización de usuarios completada")
                    } catch (e: Exception) {
                        hasErrors = true
                        errors.add("Usuarios: ${e.message}")
                        Log.e(tag, "❌ Error sincronizando usuarios", e)
                    }
                },
                async { 
                    try {
                        val result = maletaRepository.syncMaletas(authToken)
                        results["maletas"] = result.getOrThrow()
                        Log.d(tag, "✅ Sincronización de maletas completada")
                    } catch (e: Exception) {
                        hasErrors = true
                        errors.add("Maletas: ${e.message}")
                        Log.e(tag, "❌ Error sincronizando maletas", e)
                    }
                },
                async { 
                    try {
                        val result = maletaRepository.syncEntidadesSalud(authToken)
                        results["entidades_salud"] = result.getOrThrow()
                        Log.d(tag, "✅ Sincronización de entidades de salud completada")
                    } catch (e: Exception) {
                        hasErrors = true
                        errors.add("Entidades de Salud: ${e.message}")
                        Log.e(tag, "❌ Error sincronizando entidades de salud", e)
                    }
                }
            )

            // Esperar a que todas las sincronizaciones terminen
            syncJobs.awaitAll()

            val endTime = System.currentTimeMillis()
            val totalTime = endTime - startTime

            // Marcar sincronización exitosa si no hay errores
            if (!hasErrors) {
                syncMetadataDao.markSuccessfulSync()
                Log.d(tag, "✅ Sincronización completa exitosa en ${totalTime}ms")
            } else {
                syncMetadataDao.logSyncError("Errores en sincronización: ${errors.joinToString(", ")}")
                Log.w(tag, "⚠️ Sincronización completada con errores en ${totalTime}ms")
            }

            SyncAllResult(
                success = !hasErrors,
                totalTime = totalTime,
                results = results,
                errors = errors
            )

        } catch (e: Exception) {
            val endTime = System.currentTimeMillis()
            val totalTime = endTime - startTime
            
            Log.e(tag, "❌ Error crítico en sincronización completa", e)
            syncMetadataDao.logSyncError("Error crítico: ${e.message}")
            
            SyncAllResult(
                success = false,
                totalTime = totalTime,
                results = results,
                errors = errors + "Error crítico: ${e.message}"
            )
        }
    }

    /**
     * Sincroniza solo datos pendientes (optimizado)
     */
    suspend fun syncPendingOnly(token: String? = null): SyncAllResult = withContext(Dispatchers.IO) {
        Log.d(tag, "🔄 Iniciando sincronización de datos pendientes...")

        try {
            val authToken = token ?: authRepository.getCurrentToken()
            if (authToken == null || !isNetworkAvailable()) {
                return@withContext SyncAllResult(
                    success = false,
                    totalTime = 0,
                    results = emptyMap(),
                    errors = listOf("Sin conectividad o token")
                )
            }

            // Verificar si el token es válido para llamadas al servidor
            if (authToken.startsWith("OFFLINE_TOKEN") || authToken.startsWith("TEST_TOKEN_")) {
                return@withContext SyncAllResult(
                    success = false,
                    totalTime = 0,
                    results = emptyMap(),
                    errors = listOf("Token offline o de prueba - no se puede sincronizar con servidor")
                )
            }

            val startTime = System.currentTimeMillis()
            val results = mutableMapOf<String, Any>()
            val errors = mutableListOf<String>()

            // Obtener conteos de datos pendientes
            val pendingPatients = patientRepository.getUnsyncedPatients()
            val pendingExams = examRepository.getUnsyncedExams()
            val pendingUsers = authRepository.getUnsyncedUsers()

            Log.d(tag, "📊 Datos pendientes: ${pendingPatients.size} pacientes, ${pendingExams.size} exámenes, ${pendingUsers.size} usuarios")

            // Solo sincronizar si hay datos pendientes
            val syncJobs = mutableListOf<suspend () -> Unit>()

            if (pendingPatients.isNotEmpty()) {
                syncJobs.add {
                    try {
                        val result = patientRepository.syncPatients(authToken)
                        results["patients"] = result.getOrThrow()
                    } catch (e: Exception) {
                        errors.add("Pacientes: ${e.message}")
                    }
                }
            }

            if (pendingExams.isNotEmpty()) {
                syncJobs.add {
                    try {
                        val result = examRepository.syncExams(authToken)
                        results["exams"] = result.getOrThrow()
                    } catch (e: Exception) {
                        errors.add("Exámenes: ${e.message}")
                    }
                }
            }

            if (pendingUsers.isNotEmpty()) {
                syncJobs.add {
                    try {
                        val result = authRepository.syncUsers()
                        results["users"] = result.getOrThrow()
                    } catch (e: Exception) {
                        errors.add("Usuarios: ${e.message}")
                    }
                }
            }

            // Ejecutar sincronizaciones
            if (syncJobs.isNotEmpty()) {
                syncJobs.forEach { job ->
                    job()
                }
            }

            val endTime = System.currentTimeMillis()
            val totalTime = endTime - startTime

            Log.d(tag, "✅ Sincronización de pendientes completada en ${totalTime}ms")

            SyncAllResult(
                success = errors.isEmpty(),
                totalTime = totalTime,
                results = results,
                errors = errors
            )

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización de pendientes", e)
            SyncAllResult(
                success = false,
                totalTime = 0,
                results = emptyMap(),
                errors = listOf("Error: ${e.message}")
            )
        }
    }

    /**
     * Fuerza descarga completa desde el servidor
     */
    suspend fun forceSyncFromServer(token: String): SyncAllResult = withContext(Dispatchers.IO) {
        Log.d(tag, "🔄 Iniciando sincronización forzada desde servidor...")

        try {
            if (!isNetworkAvailable()) {
                return@withContext SyncAllResult(
                    success = false,
                    totalTime = 0,
                    results = emptyMap(),
                    errors = listOf("Sin conectividad")
                )
            }

            // Verificar si el token es válido para llamadas al servidor
            if (token.startsWith("OFFLINE_TOKEN")) {
                return@withContext SyncAllResult(
                    success = false,
                    totalTime = 0,
                    results = emptyMap(),
                    errors = listOf("Token offline - no se puede sincronizar con servidor")
                )
            }

            val startTime = System.currentTimeMillis()
            val results = mutableMapOf<String, Any>()
            val errors = mutableListOf<String>()

            // Sincronizar desde servidor en paralelo
            val syncJobs = listOf(
                async {
                    try {
                        val result = patientRepository.forceSyncFromServer(token)
                        results["patients"] = result.getOrThrow()
                    } catch (e: Exception) {
                        errors.add("Pacientes: ${e.message}")
                    }
                },
                async {
                    try {
                        val result = examRepository.forceSyncFromServer(token)
                        results["exams"] = result.getOrThrow()
                    } catch (e: Exception) {
                        errors.add("Exámenes: ${e.message}")
                    }
                },
                async {
                    try {
                        val result = authRepository.forceSyncFromServer()
                        results["users"] = result.getOrThrow()
                    } catch (e: Exception) {
                        errors.add("Usuarios: ${e.message}")
                    }
                }
            )

            syncJobs.awaitAll()

            val endTime = System.currentTimeMillis()
            val totalTime = endTime - startTime

            Log.d(tag, "✅ Sincronización forzada completada en ${totalTime}ms")

            SyncAllResult(
                success = errors.isEmpty(),
                totalTime = totalTime,
                results = results,
                errors = errors
            )

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización forzada", e)
            SyncAllResult(
                success = false,
                totalTime = 0,
                results = emptyMap(),
                errors = listOf("Error: ${e.message}")
            )
        }
    }

    /**
     * Obtiene estadísticas de sincronización
     */
    suspend fun getSyncStats(): SyncStats = withContext(Dispatchers.IO) {
        try {
            val patientStats = patientRepository.getPatientStats()
            val examStats = examRepository.getExamStats()
            val authStats = authRepository.getAuthStats()

            val lastSync = syncMetadataDao.getLastSyncTimestamp()
            val lastSuccessfulSync = syncMetadataDao.getLastSuccessfulSync()
            val networkStatus = syncMetadataDao.getNetworkStatus()
            val autoSyncEnabled = syncMetadataDao.isAutoSyncEnabled()

            SyncStats(
                lastSyncTime = lastSync,
                lastSuccessfulSyncTime = lastSuccessfulSync,
                pendingPatients = patientStats.unsyncedPatients,
                pendingExams = examStats.unsyncedExams,
                pendingUsers = authStats.unsyncedUsers,
                totalPendingItems = patientStats.unsyncedPatients + examStats.unsyncedExams + authStats.unsyncedUsers,
                networkAvailable = networkStatus,
                autoSyncEnabled = autoSyncEnabled,
                hasValidToken = authStats.hasActiveSession
            )
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo estadísticas de sync", e)
            SyncStats(
                lastSyncTime = null,
                lastSuccessfulSyncTime = null,
                pendingPatients = 0,
                pendingExams = 0,
                pendingUsers = 0,
                totalPendingItems = 0,
                networkAvailable = false,
                autoSyncEnabled = false,
                hasValidToken = false
            )
        }
    }

    /**
     * Actualiza el estado de conectividad
     */
    suspend fun updateNetworkStatus(isConnected: Boolean) {
        try {
            syncMetadataDao.updateNetworkStatus(isConnected)
            Log.d(tag, "📡 Estado de red actualizado: ${if (isConnected) "CONECTADO" else "DESCONECTADO"}")
        } catch (e: Exception) {
            Log.e(tag, "Error actualizando estado de red", e)
        }
    }

    /**
     * Configura sincronización automática
     */
    suspend fun setAutoSyncEnabled(enabled: Boolean) {
        try {
            syncMetadataDao.updateAutoSyncEnabled(enabled)
            Log.d(tag, "⚙️ Sincronización automática: ${if (enabled) "HABILITADA" else "DESHABILITADA"}")
        } catch (e: Exception) {
            Log.e(tag, "Error configurando auto-sync", e)
        }
    }

    /**
     * Ejecuta mantenimiento en todos los repositories
     */
    suspend fun performMaintenance() = withContext(Dispatchers.IO) {
        try {
            Log.d(tag, "🧹 Iniciando mantenimiento...")

            // Ejecutar mantenimiento en paralelo
            val maintenanceJobs = listOf(
                async { patientRepository.performMaintenance() },
                async { examRepository.performMaintenance() },
                async { authRepository.performMaintenance() },
                async { maletaRepository.performMaintenance() }
            )

            maintenanceJobs.awaitAll()

            Log.d(tag, "✅ Mantenimiento completado")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en mantenimiento", e)
        }
    }

    /**
     * Verifica conectividad de red
     */
    private suspend fun isNetworkAvailable(): Boolean {
        return patientRepository.isNetworkAvailable()
    }

    /**
     * Sincroniza las entidades de salud con el servidor y retorna el mapeo de IDs
     */
    private suspend fun syncEntidades(token: String): EntidadesSyncResult {
        Log.d(tag, "🏥 Sincronizando entidades de salud...")
        
        // TODO: Implementar sincronización de entidades
        // Por ahora, retornar un mapeo vacío
        return EntidadesSyncResult(
            success = true,
            synced = 0,
            failed = 0,
            mapping = emptyMap()
        )
    }

    // ========== CLASES DE DATOS ==========

    /**
     * Resultado de sincronización completa
     */
    data class SyncAllResult(
        val success: Boolean,
        val totalTime: Long,
        val results: Map<String, Any>,
        val errors: List<String>
    ) {
        val hasPartialSuccess: Boolean get() = results.isNotEmpty() && errors.isNotEmpty()
        val totalSyncedItems: Int get() {
            var total = 0
            results.values.forEach { result ->
                when (result) {
                    is PatientRepository.SyncResult -> total += result.syncedCount
                    is ExamRepository.SyncResult -> total += result.syncedCount
                    is AuthRepository.SyncResult -> total += result.syncedCount
                }
            }
            return total
        }
    }

    /**
     * Estadísticas de sincronización
     */
    data class SyncStats(
        val lastSyncTime: String?,
        val lastSuccessfulSyncTime: String?,
        val pendingPatients: Int,
        val pendingExams: Int,
        val pendingUsers: Int,
        val totalPendingItems: Int,
        val networkAvailable: Boolean,
        val autoSyncEnabled: Boolean,
        val hasValidToken: Boolean
    ) {
        val needsSync: Boolean get() = totalPendingItems > 0
        val canSync: Boolean get() = networkAvailable && hasValidToken
        val syncReadiness: SyncReadiness get() = when {
            !hasValidToken -> SyncReadiness.NO_TOKEN
            !networkAvailable -> SyncReadiness.NO_NETWORK
            totalPendingItems == 0 -> SyncReadiness.UP_TO_DATE
            else -> SyncReadiness.READY
        }
    }

    /**
     * Estados de disponibilidad para sincronización
     */
    enum class SyncReadiness {
        READY,          // Listo para sincronizar
        NO_NETWORK,     // Sin conexión de red
        NO_TOKEN,       // Sin token válido
        UP_TO_DATE      // Sin datos pendientes
    }

    /**
     * Resultado de sincronización de entidades
     */
    data class EntidadesSyncResult(
        val success: Boolean,
        val synced: Int,
        val failed: Int,
        val mapping: Map<Int, Int> // localId -> serverId
    )
}


