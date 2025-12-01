package com.example.telephases.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telephases.data.repository.PatientRepository
import com.example.telephases.data.repository.AuthRepository
import com.example.telephases.data.local.entities.PatientEntity
import com.example.telephases.network.PatientRegistrationRequest
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
class PatientViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val authRepository: AuthRepository,
    private val networkConnectivityManager: NetworkConnectivityManager
) : ViewModel() {

    private val tag = "PatientViewModel"

    // Estados principales
    private val _uiState = MutableStateFlow(PatientUiState())
    val uiState: StateFlow<PatientUiState> = _uiState.asStateFlow()

    private val _patients = MutableStateFlow<List<PatientEntity>>(emptyList())
    val patients: StateFlow<List<PatientEntity>> = _patients.asStateFlow()

    private val _selectedPatient = MutableStateFlow<PatientEntity?>(null)
    val selectedPatient: StateFlow<PatientEntity?> = _selectedPatient.asStateFlow()

    private val _searchResults = MutableStateFlow<List<PatientEntity>>(emptyList())
    val searchResults: StateFlow<List<PatientEntity>> = _searchResults.asStateFlow()

    // Estado combinado
    val combinedState = combine(
        _uiState,
        _patients,
        networkConnectivityManager.connectivityState
    ) { uiState, patientsList, connectivity ->
        uiState.copy(
            patients = patientsList,
            isOnline = connectivity.isConnected,
            canSync = connectivity.canSync
        )
    }

    init {
        // Cargar pacientes al inicializar
        loadPatients()
        
        // Observar cambios de conectividad para sincronización automática
        viewModelScope.launch {
            networkConnectivityManager.connectivityState.collect { connectivity ->
                if (connectivity.isConnected && _uiState.value.hasPendingSync) {
                    Log.d(tag, "📡 Conectividad detectada - iniciando sync automático")
                    syncPatientsInBackground()
                }
            }
        }
    }

    // ========== OPERACIONES DE PACIENTES ==========

    /**
     * Cargar todos los pacientes (offline-first) - solo de la entidad del admin
     */
    fun loadPatients() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val token = authRepository.getCurrentToken()
                val currentUser = authRepository.getCurrentUser()
                
                // Filtrar pacientes por entidad de salud del admin
                val allPatients = patientRepository.getAllPatients(token)
                val filteredPatients = if (currentUser?.entidadSaludId != null) {
                    allPatients.filter { it.entidadSaludId == currentUser.entidadSaludId }
                } else {
                    // Si el admin no tiene entidad asignada, mostrar todos (compatibilidad hacia atrás)
                    allPatients
                }
                
                _patients.value = filteredPatients
                
                Log.d(tag, "👥 Pacientes filtrados para entidad ${currentUser?.entidadSaludId}: ${filteredPatients.size}/${allPatients.size}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    totalPatients = filteredPatients.size
                )
                
                // Actualizar estadísticas
                updateStats()
                
                Log.d(tag, "✅ Cargados ${filteredPatients.size} pacientes de la entidad ${currentUser?.entidadSaludId}")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error cargando pacientes", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error cargando pacientes: ${e.message}"
                )
            }
        }
    }

    /**
     * Registrar nuevo paciente (offline-first)
     */
    fun registerPatient(request: PatientRegistrationRequest, onSuccess: ((PatientEntity) -> Unit)? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isRegistering = true, error = null)
                
                val token = authRepository.getCurrentToken()
                
                // Obtener la entidad de salud del admin actual
                val currentUser = authRepository.getCurrentUser()
                val adminEntidadSaludId = currentUser?.entidadSaludId
                
                // Asignar automáticamente la entidad de salud del admin al paciente
                val updatedRequest = request.copy(entidad_salud_id = adminEntidadSaludId)
                
                Log.d(tag, "👥 Registrando paciente con entidad de salud ID: $adminEntidadSaludId")
                Log.d(tag, "🔍 Token obtenido: ${token?.take(20)}...")
                
                // Verificar si el token es válido para llamadas al servidor
                val isValidServerToken = token != null && !token.startsWith("OFFLINE_TOKEN")
                if (!isValidServerToken) {
                    Log.d(tag, "ℹ️ Token offline detectado, registrando solo localmente")
                }
                
                val result = patientRepository.registerPatient(token, updatedRequest)
                
                if (result.isSuccess) {
                    val patient = result.getOrThrow()
                    
                    // Actualizar lista local
                    val updatedPatients = _patients.value + patient
                    _patients.value = updatedPatients
                    
                    _uiState.value = _uiState.value.copy(
                        isRegistering = false,
                        totalPatients = updatedPatients.size,
                        lastRegisteredPatient = patient
                    )
                    
                    updateStats()
                    onSuccess?.invoke(patient)
                    
                    Log.d(tag, "✅ Paciente registrado: ${patient.nombreCompleto}")
                } else {
                    val error = result.exceptionOrNull()
                    val errorMessage = when (error) {
                        is PatientRepository.RepositoryError.DuplicateError -> "Ya existe un paciente con este documento"
                        is PatientRepository.RepositoryError.ValidationError -> "Datos inválidos"
                        is PatientRepository.RepositoryError.NetworkError -> "Sin conexión - guardado offline"
                        else -> error?.message ?: "Error registrando paciente"
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isRegistering = false,
                        error = errorMessage
                    )
                    
                    Log.e(tag, "❌ Error registrando paciente: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e(tag, "❌ Excepción registrando paciente", e)
                _uiState.value = _uiState.value.copy(
                    isRegistering = false,
                    error = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    /**
     * Buscar paciente por documento
     */
    fun searchPatientByDocument(numeroDocumento: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSearching = true, error = null)
                
                val token = authRepository.getCurrentToken()
                val result = patientRepository.searchPatientByDocument(token, numeroDocumento)
                
                if (result.isSuccess) {
                    val patient = result.getOrThrow()
                    _selectedPatient.value = patient
                    _searchResults.value = listOf(patient)
                    
                    Log.d(tag, "✅ Paciente encontrado: ${patient.nombreCompleto}")
                } else {
                    _searchResults.value = emptyList()
                    val error = result.exceptionOrNull()
                    
                    if (error is PatientRepository.RepositoryError.NotFoundError) {
                        Log.d(tag, "ℹ️ Paciente no encontrado: $numeroDocumento")
                    } else {
                        Log.e(tag, "❌ Error buscando paciente: ${error?.message}")
                    }
                }
                
                _uiState.value = _uiState.value.copy(isSearching = false)
            } catch (e: Exception) {
                Log.e(tag, "❌ Excepción buscando paciente", e)
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    error = "Error buscando paciente: ${e.message}"
                )
            }
        }
    }

    /**
     * Buscar pacientes por nombre - solo en la entidad del admin
     */
    fun searchPatientsByName(searchTerm: String) {
        viewModelScope.launch {
            try {
                if (searchTerm.isBlank()) {
                    _searchResults.value = emptyList()
                    return@launch
                }
                
                _uiState.value = _uiState.value.copy(isSearching = true)
                
                val currentUser = authRepository.getCurrentUser()
                val allResults = patientRepository.searchLocalPatientsByName(searchTerm)
                
                // Filtrar resultados por entidad de salud del admin
                val filteredResults = if (currentUser?.entidadSaludId != null) {
                    allResults.filter { it.entidadSaludId == currentUser.entidadSaludId }
                } else {
                    allResults
                }
                
                _searchResults.value = filteredResults
                
                _uiState.value = _uiState.value.copy(isSearching = false)
                
                Log.d(tag, "✅ Búsqueda completada: ${filteredResults.size} resultados para '$searchTerm' en entidad ${currentUser?.entidadSaludId}")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error en búsqueda por nombre", e)
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    error = "Error en búsqueda: ${e.message}"
                )
            }
        }
    }

    /**
     * Seleccionar paciente
     */
    fun selectPatient(patient: PatientEntity) {
        _selectedPatient.value = patient
        Log.d(tag, "👤 Paciente seleccionado: ${patient.nombreCompleto}")
    }

    /**
     * Limpiar selección
     */
    fun clearSelection() {
        _selectedPatient.value = null
        _searchResults.value = emptyList()
    }

    // ========== SINCRONIZACIÓN ==========

    /**
     * Sincronizar pacientes manualmente
     */
    fun syncPatients() {
        viewModelScope.launch {
            try {
                val token = authRepository.getCurrentToken()
                if (token == null) {
                    _uiState.value = _uiState.value.copy(error = "No hay sesión válida")
                    return@launch
                }
                
                _uiState.value = _uiState.value.copy(isSyncing = true, error = null)
                
                val result = patientRepository.syncPatients(token)
                if (result.isSuccess) {
                    val syncResult = result.getOrThrow()
                    
                    // Recargar pacientes después de sync
                    loadPatients()
                    
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        lastSyncResult = "Sincronizados ${syncResult.syncedCount}/${syncResult.totalCount} pacientes"
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
    private fun syncPatientsInBackground() {
        viewModelScope.launch {
            try {
                Log.d(tag, "🔄 Iniciando sincronización automática de pacientes en background...")
                
                val token = authRepository.getCurrentToken()
                if (token == null) {
                    Log.w(tag, "⚠️ No hay token válido para sincronización de pacientes")
                    return@launch
                }
                
                Log.d(tag, "🔑 Token obtenido, verificando pacientes pendientes...")
                
                // Verificar pacientes pendientes ANTES de sincronizar
                val unsyncedPatients = patientRepository.getUnsyncedPatients()
                Log.d(tag, "📊 Pacientes pendientes de sincronización: ${unsyncedPatients.size}")
                
                if (unsyncedPatients.isNotEmpty()) {
                    unsyncedPatients.forEach { patient ->
                        Log.d(tag, "📋 Paciente pendiente: ${patient.nombreCompleto} (ID: ${patient.id})")
                    }
                    
                    Log.d(tag, "🔄 Iniciando sincronización de ${unsyncedPatients.size} pacientes...")
                    val result = patientRepository.syncPatients(token)
                    
                    if (result.isSuccess) {
                        val syncResult = result.getOrThrow()
                        Log.d(tag, "✅ Sync de pacientes en background exitoso: ${syncResult.syncedCount}/${syncResult.totalCount} pacientes")
                        
                        // Recargar datos después de sincronización
                        loadPatients()
                    } else {
                        Log.e(tag, "❌ Error en sync de pacientes en background: ${result.exceptionOrNull()?.message}")
                    }
                } else {
                    Log.d(tag, "✅ No hay pacientes pendientes de sincronización")
                }
            } catch (e: Exception) {
                Log.e(tag, "❌ Excepción en sync de pacientes en background", e)
            }
        }
    }

    // ========== UTILIDADES ==========

    /**
     * Actualizar estadísticas
     */
    private fun updateStats() {
        viewModelScope.launch {
            try {
                val stats = patientRepository.getPatientStats()
                _uiState.value = _uiState.value.copy(
                    totalPatients = stats.totalPatients,
                    syncedPatients = stats.syncedPatients,
                    unsyncedPatients = stats.unsyncedPatients,
                    todayRegistrations = stats.todayRegistrations,
                    hasPendingSync = stats.unsyncedPatients > 0
                )
            } catch (e: Exception) {
                Log.e(tag, "Error actualizando estadísticas", e)
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
     * Verificar si existe paciente con documento
     */
    suspend fun existsPatientWithDocument(numeroDocumento: String): Boolean {
        return try {
            patientRepository.existsPatientWithDocument(numeroDocumento)
        } catch (e: Exception) {
            Log.e(tag, "Error verificando documento", e)
            false
        }
    }

    /**
     * Obtener paciente por ID
     */
    suspend fun getLocalPatientById(patientId: String): PatientEntity? {
        return try {
            patientRepository.getLocalPatientById(patientId)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo paciente por ID", e)
            null
        }
    }

    /**
     * Obtener cantidad de exámenes para un paciente
     */
    suspend fun getExamCountForPatient(patientId: String): Int {
        return try {
            patientRepository.getExamCountForPatient(patientId)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo cantidad de exámenes", e)
            0
        }
    }

    /**
     * Obtener nombre de entidad de salud
     */
    suspend fun getEntidadSaludName(entidadId: Int?): String {
        return try {
            if (entidadId != null) {
                patientRepository.getEntidadSaludName(entidadId)
            } else {
                "No especificada"
            }
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo nombre de entidad", e)
            "No especificada"
        }
    }
}

/**
 * Estado de UI para pacientes
 */
data class PatientUiState(
    val isLoading: Boolean = false,
    val isRegistering: Boolean = false,
    val isSearching: Boolean = false,
    val isSyncing: Boolean = false,
    val isOnline: Boolean = false,
    val canSync: Boolean = false,
    val error: String? = null,
    val patients: List<PatientEntity> = emptyList(),
    val totalPatients: Int = 0,
    val syncedPatients: Int = 0,
    val unsyncedPatients: Int = 0,
    val todayRegistrations: Int = 0,
    val hasPendingSync: Boolean = false,
    val lastRegisteredPatient: PatientEntity? = null,
    val lastSyncResult: String? = null
) {
    val isEmpty: Boolean get() = patients.isEmpty() && !isLoading
    val hasUnsyncedData: Boolean get() = unsyncedPatients > 0
    val syncPercentage: Float get() = if (totalPatients > 0) (syncedPatients.toFloat() / totalPatients) * 100 else 100f
    val showSyncIndicator: Boolean get() = hasUnsyncedData && isOnline
}


