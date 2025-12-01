package com.example.telephases.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telephases.data.repository.ExamRepository
import com.example.telephases.data.repository.AuthRepository
import com.example.telephases.data.local.entities.ExamEntity
import com.example.telephases.utils.NetworkConnectivityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val examRepository: ExamRepository,
    private val authRepository: AuthRepository,
    private val networkConnectivityManager: NetworkConnectivityManager
) : ViewModel() {

    private val tag = "ExamViewModel"

    // Estados principales
    private val _uiState = MutableStateFlow(ExamUiState())
    
    // Método eliminado - duplicado
    val uiState: StateFlow<ExamUiState> = _uiState.asStateFlow()

    private val _exams = MutableStateFlow<List<ExamEntity>>(emptyList())
    val exams: StateFlow<List<ExamEntity>> = _exams.asStateFlow()

    private val _patientExams = MutableStateFlow<List<ExamEntity>>(emptyList())
    val patientExams: StateFlow<List<ExamEntity>> = _patientExams.asStateFlow()

    private val _latestExamsByType = MutableStateFlow<List<ExamEntity>>(emptyList())
    val latestExamsByType: StateFlow<List<ExamEntity>> = _latestExamsByType.asStateFlow()

    private val _selectedExam = MutableStateFlow<ExamEntity?>(null)
    val selectedExam: StateFlow<ExamEntity?> = _selectedExam.asStateFlow()

    private val _availableExamTypes = MutableStateFlow<List<String>>(emptyList())
    val availableExamTypes: StateFlow<List<String>> = _availableExamTypes.asStateFlow()

    // Estado combinado
    val combinedState = combine(
        _uiState,
        _exams,
        networkConnectivityManager.connectivityState
    ) { uiState, examsList, connectivity ->
        uiState.copy(
            exams = examsList,
            isOnline = connectivity.isConnected,
            canSync = connectivity.canSync
        )
    }

    init {
        // Cargar tipos de examen disponibles
        loadAvailableExamTypes()
        
        // Cargar exámenes recientes
        loadRecentExams()
        
        // Observar cambios de conectividad
        viewModelScope.launch {
            networkConnectivityManager.connectivityState.collect { connectivity ->
                if (connectivity.isConnected) {
                    Log.d(tag, "📡 Conectividad detectada - verificando exámenes pendientes...")
                    // Siempre verificar si hay exámenes pendientes cuando se conecta WiFi
                    updateStats()
                    if (_uiState.value.hasPendingSync) {
                        Log.d(tag, "🔄 Exámenes pendientes detectados - iniciando sync automático")
                        syncExamsInBackground()
                    } else {
                        Log.d(tag, "✅ No hay exámenes pendientes de sincronización")
                    }
                }
            }
        }
    }

    // ========== OPERACIONES DE EXÁMENES ==========

    /**
     * Crear nuevo examen (offline-first)
     */
    fun createExam(
        patientId: String,
        tipoExamenNombre: String,
        titulo: String,
        valor: String,
        unidad: String? = null,
        observaciones: String? = null,
        datosAdicionales: Map<String, Any>? = null,
        onSuccess: ((ExamEntity) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isCreating = true, error = null)
                
                val token = authRepository.getCurrentToken()
                val result = examRepository.createExam(
                    token = token,
                    patientId = patientId,
                    tipoExamenNombre = tipoExamenNombre,
                    titulo = titulo,
                    valor = valor,
                    unidad = unidad,
                    observaciones = observaciones,
                    datosAdicionales = datosAdicionales
                )
                
                if (result.isSuccess) {
                    val exam = result.getOrThrow()
                    
                    // Actualizar listas locales
                    val updatedExams = _exams.value + exam
                    _exams.value = updatedExams
                    
                    // Si es del paciente actual, actualizar también esa lista
                    if (_uiState.value.currentPatientId == patientId) {
                        val updatedPatientExams = _patientExams.value + exam
                        _patientExams.value = updatedPatientExams
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isCreating = false,
                        totalExams = updatedExams.size,
                        lastCreatedExam = exam
                    )
                    
                    // Actualizar estadísticas inmediatamente después de crear el examen
                    updateStats()
                    
                    // Verificar si hay exámenes pendientes después de actualizar stats
                    if (_uiState.value.hasPendingSync) {
                        Log.d(tag, "🔄 Exámenes pendientes detectados después de crear examen - iniciando sync automático")
                        syncExamsInBackground()
                    }
                    
                    onSuccess?.invoke(exam)
                    
                    Log.d(tag, "✅ Examen creado: ${exam.titulo}")
                } else {
                    val error = result.exceptionOrNull()
                    val errorMessage = when (error) {
                        is ExamRepository.RepositoryError.InvalidExamTypeError -> "Tipo de examen inválido"
                        is ExamRepository.RepositoryError.PatientNotFoundError -> "Paciente no encontrado"
                        is ExamRepository.RepositoryError.ValidationError -> "Datos inválidos"
                        is ExamRepository.RepositoryError.NetworkError -> "Sin conexión - guardado offline"
                        else -> error?.message ?: "Error creando examen"
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isCreating = false,
                        error = errorMessage
                    )
                    
                    Log.e(tag, "❌ Error creando examen: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e(tag, "❌ Excepción creando examen", e)
                _uiState.value = _uiState.value.copy(
                    isCreating = false,
                    error = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    /**
     * Cargar exámenes de un paciente específico usando Flow para observar cambios en tiempo real
     */
    fun loadPatientExams(patientId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true, 
                    error = null, 
                    currentPatientId = patientId
                )
                
                Log.d(tag, "🔍 Iniciando observación de exámenes del paciente: $patientId")
                
                // Observar cambios en tiempo real usando Flow - SOLO ÚLTIMOS POR TIPO
                examRepository.getLatestExamsByTypeForPatientFlow(patientId).collect { exams ->
                    _patientExams.value = exams
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        patientExamsCount = exams.size
                    )
                    
                    Log.d(tag, "🔄 Actualización en tiempo real: ${exams.size} ÚLTIMOS exámenes por tipo del paciente $patientId")
                }
                
                // Iniciar sincronización en background (no bloquear UI)
                val token = authRepository.getCurrentToken()
                if (token != null) {
                    launch {
                        try {
                            examRepository.getPatientExams(token, patientId)
                            Log.d(tag, "🌐 Sincronización en background completada para paciente $patientId")
                        } catch (e: Exception) {
                            Log.w(tag, "⚠️ Error en sincronización background: ${e.message}")
                        }
                    }
                }
                
            } catch (e: Exception) {
                Log.e(tag, "❌ Error cargando exámenes del paciente", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error cargando exámenes: ${e.message}"
                )
            }
        }
    }

    /**
     * Cargar últimos exámenes por tipo
     */
    fun loadLatestExamsByType(patientId: String? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val token = authRepository.getCurrentToken()
                val exams = examRepository.getLatestExamsByType(token, patientId)
                
                _latestExamsByType.value = exams
                _uiState.value = _uiState.value.copy(isLoading = false)
                
                Log.d(tag, "✅ Cargados ${exams.size} últimos exámenes por tipo")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error cargando últimos exámenes", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error cargando exámenes: ${e.message}"
                )
            }
        }
    }

    /**
     * Cargar exámenes recientes usando Flow para observar cambios en tiempo real
     */
    fun loadRecentExams(limit: Int = 20) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                Log.d(tag, "🔍 Iniciando observación de exámenes recientes")
                
                // Observar cambios en tiempo real usando Flow
                examRepository.getLocalExamsFlow().collect { allExams ->
                    // Tomar solo los más recientes
                    val recentExams = allExams.take(limit)
                    _exams.value = recentExams
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        totalExams = recentExams.size
                    )
                    
                    updateStats()
                    
                    Log.d(tag, "🔄 Actualización en tiempo real: ${recentExams.size} exámenes recientes")
                }
                
                // Iniciar sincronización en background (no bloquear UI)
                val token = authRepository.getCurrentToken()
                if (token != null) {
                    launch {
                        try {
                            examRepository.getAllExams(token)
                            Log.d(tag, "🌐 Sincronización en background completada para exámenes recientes")
                        } catch (e: Exception) {
                            Log.w(tag, "⚠️ Error en sincronización background: ${e.message}")
                        }
                    }
                }
                
            } catch (e: Exception) {
                Log.e(tag, "❌ Error cargando exámenes recientes", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error cargando exámenes: ${e.message}"
                )
            }
        }
    }

    /**
     * Buscar exámenes con filtros
     */
    fun searchExams(
        patientId: String? = null,
        tipoExamen: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSearching = true, error = null)
                
                val results = examRepository.searchExamsWithFilters(
                    patientId = patientId,
                    tipoExamen = tipoExamen,
                    startDate = startDate,
                    endDate = endDate
                )
                
                _exams.value = results
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    searchResultsCount = results.size
                )
                
                Log.d(tag, "✅ Búsqueda completada: ${results.size} resultados")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error en búsqueda", e)
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    error = "Error en búsqueda: ${e.message}"
                )
            }
        }
    }

    /**
     * Obtener exámenes críticos
     */
    fun loadCriticalExams() {
        viewModelScope.launch {
            try {
                val criticalExams = examRepository.getCriticalExams()
                        _uiState.value = _uiState.value.copy(
            exams = criticalExams
        )
                
                Log.d(tag, "⚠️ ${criticalExams.size} exámenes críticos encontrados")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error cargando exámenes críticos", e)
            }
        }
    }

    // ========== TIPOS DE EXAMEN ==========

    /**
     * Cargar tipos de examen disponibles
     */
    private fun loadAvailableExamTypes() {
        viewModelScope.launch {
            try {
                val types = examRepository.getAvailableExamTypes()
                _availableExamTypes.value = types
                
                Log.d(tag, "✅ Cargados ${types.size} tipos de examen")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error cargando tipos de examen", e)
            }
        }
    }

    // ========== SINCRONIZACIÓN ==========

    /**
     * Sincronizar exámenes manualmente
     */
    fun syncExams() {
        viewModelScope.launch {
            try {
                val token = authRepository.getCurrentToken()
                if (token == null) {
                    _uiState.value = _uiState.value.copy(error = "No hay sesión válida")
                    return@launch
                }
                
                _uiState.value = _uiState.value.copy(isSyncing = true, error = null)
                
                val result = examRepository.syncExams(token)
                if (result.isSuccess) {
                    val syncResult = result.getOrThrow()
                    
                    // Recargar exámenes después de sync
                    loadRecentExams()
                    if (_uiState.value.currentPatientId != null) {
                        loadPatientExams(_uiState.value.currentPatientId!!)
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        lastSyncResult = "Sincronizados ${syncResult.syncedCount}/${syncResult.totalCount} exámenes"
                    )
                    
                    Log.d(tag, "✅ Sincronización exitosa: ${syncResult.syncedCount}/${syncResult.totalCount}")
                } else {
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        error = "Error en sincronización: ${result.exceptionOrNull()?.message}"
                    )
                }
            } catch (e: Exception) {
                Log.e(tag, "❌ Error en sincronización", e)
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    error = "Error en sincronización: ${e.message}"
                )
            }
        }
    }

    /**
     * Sincronización en background (silenciosa)
     */
    private fun syncExamsInBackground() {
        viewModelScope.launch {
            try {
                Log.d(tag, "🔄 Iniciando sincronización automática en background...")
                
                val token = authRepository.getCurrentToken()
                if (token == null) {
                    Log.w(tag, "⚠️ No hay token válido para sincronización")
                    return@launch
                }
                
                Log.d(tag, "🔑 Token obtenido, verificando exámenes pendientes...")
                
                // Verificar exámenes pendientes ANTES de sincronizar
                val unsyncedExams = examRepository.getUnsyncedExams()
                Log.d(tag, "📊 Exámenes pendientes de sincronización: ${unsyncedExams.size}")
                
                if (unsyncedExams.isNotEmpty()) {
                    unsyncedExams.forEach { exam ->
                        Log.d(tag, "📋 Examen pendiente: ${exam.titulo} = ${exam.valor} (ID: ${exam.id})")
                    }
                    
                    Log.d(tag, "🔄 Iniciando sincronización de ${unsyncedExams.size} exámenes...")
                    val result = examRepository.syncExams(token)
                    
                    if (result.isSuccess) {
                        val syncResult = result.getOrThrow()
                        Log.d(tag, "✅ Sync en background exitoso: ${syncResult.syncedCount}/${syncResult.totalCount} exámenes")
                        
                        // Recargar datos después de sincronización
                        loadRecentExams()
                        if (_uiState.value.currentPatientId != null) {
                            loadPatientExams(_uiState.value.currentPatientId!!)
                        }
                    } else {
                        Log.e(tag, "❌ Error en sync background: ${result.exceptionOrNull()?.message}")
                    }
                } else {
                    Log.d(tag, "✅ No hay exámenes pendientes de sincronización")
                }
            } catch (e: Exception) {
                Log.e(tag, "❌ Excepción en sync background", e)
            }
        }
    }

    /**
     * Sincronización manual de exámenes
     */
    fun syncExamsManually() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSyncing = true, error = null)
                
                val token = authRepository.getCurrentToken()
                if (token == null) {
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        error = "No hay sesión válida"
                    )
                    return@launch
                }
                
                Log.d(tag, "🔄 Iniciando sincronización manual FORZADA...")
                
                // Usar forceSyncExams para sincronizar incluso si hay solo 1 examen
                val result = examRepository.forceSyncExams(token)
                if (result.isSuccess) {
                    val syncResult = result.getOrThrow()
                    
                    // Recargar exámenes después de sync
                    loadRecentExams()
                    if (_uiState.value.currentPatientId != null) {
                        loadPatientExams(_uiState.value.currentPatientId!!)
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        lastSyncResult = "Sincronizados ${syncResult.syncedCount}/${syncResult.totalCount} exámenes"
                    )
                    
                    Log.d(tag, "✅ Sincronización manual FORZADA exitosa: ${syncResult.syncedCount}/${syncResult.totalCount}")
                } else {
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        error = "Error en sincronización: ${result.exceptionOrNull()?.message}"
                    )
                    
                    Log.e(tag, "❌ Error en sincronización manual FORZADA: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(tag, "❌ Excepción en sincronización manual FORZADA", e)
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    error = "Error en sincronización: ${e.message}"
                )
            }
        }
    }

    // ========== SELECCIÓN Y NAVEGACIÓN ==========

    /**
     * Seleccionar examen
     */
    fun selectExam(exam: ExamEntity) {
        _selectedExam.value = exam
        Log.d(tag, "📋 Examen seleccionado: ${exam.titulo}")
    }

    /**
     * Limpiar selección
     */
    fun clearSelection() {
        _selectedExam.value = null
    }

    /**
     * Cambiar paciente actual
     */
    fun setCurrentPatient(patientId: String?) {
        _uiState.value = _uiState.value.copy(currentPatientId = patientId)
        if (patientId != null) {
            loadPatientExams(patientId)
            loadLatestExamsByType(patientId)
        } else {
            _patientExams.value = emptyList()
            loadLatestExamsByType()
        }
    }

    // ========== UTILIDADES ==========

    /**
     * Actualizar estadísticas
     */
    private fun updateStats() {
        viewModelScope.launch {
            try {
                val stats = examRepository.getExamStats()
                val hasPendingSync = stats.unsyncedExams > 0
                
                Log.d(tag, "📊 Estadísticas actualizadas: Total=${stats.totalExams}, Sincronizados=${stats.syncedExams}, Pendientes=${stats.unsyncedExams}, hasPendingSync=$hasPendingSync")
                
                _uiState.value = _uiState.value.copy(
                    totalExams = stats.totalExams,
                    syncedExams = stats.syncedExams,
                    unsyncedExams = stats.unsyncedExams,
                    todayExams = stats.todayExams,
                    criticalExamsCount = stats.criticalExams,
                    hasPendingSync = hasPendingSync
                )
            } catch (e: Exception) {
                Log.e(tag, "❌ Error actualizando estadísticas", e)
            }
        }
    }

    /**
     * Limpiar errores
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Obtener conteo de exámenes por paciente
     */
    suspend fun getExamCountForPatient(patientId: String): Int {
        return try {
            examRepository.getExamCountByPatient(patientId)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo conteo de exámenes", e)
            0
        }
    }

    /**
     * Obtener conteo de exámenes por tipo
     */
    suspend fun getExamCountByType(tipoExamen: String): Int {
        return try {
            examRepository.getExamCountByType(tipoExamen)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo conteo por tipo", e)
            0
        }
    }

    /**
     * Guardar un examen localmente
     */
    suspend fun saveExam(exam: ExamEntity) {
        try {
            Log.d(tag, "💾 Iniciando guardado de examen: ${exam.titulo}")
            Log.d(tag, "📊 Datos del examen:")
            Log.d(tag, "  - PatientId: ${exam.patientId}")
            Log.d(tag, "  - Tipo: ${exam.tipoExamenNombre}")
            Log.d(tag, "  - Valor: ${exam.valor}")
            Log.d(tag, "  - Unidad: ${exam.unidad}")
            
            val token = authRepository.getCurrentToken()
            Log.d(tag, "🔑 Token obtenido: ${if (token != null) "SÍ" else "NO"}")
            
            val result = examRepository.createExam(
                token = token,
                patientId = exam.patientId,
                tipoExamenNombre = exam.tipoExamenNombre,
                titulo = exam.titulo,
                valor = exam.valor,
                unidad = exam.unidad,
                observaciones = exam.observaciones,
                datosAdicionales = if (exam.datosAdicionales != null) {
                    try {
                        com.google.gson.Gson().fromJson(exam.datosAdicionales, Map::class.java) as Map<String, Any>
                    } catch (e: Exception) {
                        null
                    }
                } else null
            )
            
            if (result.isSuccess) {
                Log.d(tag, "✅ Examen guardado exitosamente: ${exam.titulo}")
            } else {
                Log.e(tag, "❌ Error guardando examen: ${result.exceptionOrNull()?.message}")
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error guardando examen", e)
            _uiState.value = _uiState.value.copy(error = "Error guardando examen: ${e.message}")
        }
    }
}

/**
 * Estado de UI para exámenes
 */
data class ExamUiState(
    val isLoading: Boolean = false,
    val isCreating: Boolean = false,
    val isSearching: Boolean = false,
    val isSyncing: Boolean = false,
    val isOnline: Boolean = false,
    val canSync: Boolean = false,
    val error: String? = null,
    val exams: List<ExamEntity> = emptyList(),
    val currentPatientId: String? = null,
    val totalExams: Int = 0,
    val patientExamsCount: Int = 0,
    val searchResultsCount: Int = 0,
    val syncedExams: Int = 0,
    val unsyncedExams: Int = 0,
    val todayExams: Int = 0,
    val criticalExamsCount: Int = 0,
    val hasPendingSync: Boolean = false,
    val lastCreatedExam: ExamEntity? = null,
    val lastSyncResult: String? = null
) {
    val isEmpty: Boolean get() = exams.isEmpty() && !isLoading
    val hasUnsyncedData: Boolean get() = unsyncedExams > 0
    val syncPercentage: Float get() = if (totalExams > 0) (syncedExams.toFloat() / totalExams) * 100 else 100f
    val showSyncIndicator: Boolean get() = hasUnsyncedData && isOnline
    val hasCriticalExams: Boolean get() = criticalExamsCount > 0
}

