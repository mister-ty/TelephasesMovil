package com.example.telephases.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telephases.data.repository.AuthRepository
import com.example.telephases.data.local.entities.MaletaEntity
import com.example.telephases.data.local.dao.MaletaDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MaletaViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val maletaDao: MaletaDao
) : ViewModel() {

    private val tag = "MaletaViewModel"

    // Estados principales
    private val _uiState = MutableStateFlow(MaletaUiState())
    val uiState: StateFlow<MaletaUiState> = _uiState.asStateFlow()

    private val _currentMaleta = MutableStateFlow<MaletaEntity?>(null)
    val currentMaleta: StateFlow<MaletaEntity?> = _currentMaleta.asStateFlow()

    private val _allMaletas = MutableStateFlow<List<MaletaEntity>>(emptyList())
    val allMaletas: StateFlow<List<MaletaEntity>> = _allMaletas.asStateFlow()

    init {
        // Cargar maleta del usuario actual al inicializar
        Log.d(tag, "🔄 Inicializando MaletaViewModel")
        loadCurrentUserMaleta()
    }

    /**
     * Verificar si el usuario tiene una maleta configurada
     */
    suspend fun checkMaletaSetup(): Boolean {
        val currentUser = authRepository.getCurrentUser()
        return if (currentUser != null) {
            // SOLO verificar maletas asignadas específicamente al usuario
            val maleta = maletaDao.getMaletaByUsuarioId(currentUser.id)
            Log.d(tag, "🔍 checkMaletaSetup: Usuario ${currentUser.username} -> Maleta: ${maleta?.nombreMaleta ?: "NINGUNA"}")
            maleta != null
        } else {
            Log.d(tag, "🔍 checkMaletaSetup: No hay usuario actual")
            false
        }
    }

    /**
     * Cargar la maleta del usuario actual
     */
    private fun loadCurrentUserMaleta() {
        viewModelScope.launch {
            try {
                Log.d(tag, "🔄 Cargando maleta del usuario actual...")
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val currentUser = authRepository.getCurrentUser()
                Log.d(tag, "📋 Usuario actual: ${currentUser?.username ?: "null"} (ID: ${currentUser?.id ?: "null"})")
                
                if (currentUser != null) {
                    // SOLO buscar maletas asignadas específicamente al usuario
                    val maleta = maletaDao.getMaletaByUsuarioId(currentUser.id)
                    
                    Log.d(tag, "🔍 Buscando maleta específica del usuario: ${currentUser.id}")
                    Log.d(tag, "📦 Maleta asignada al usuario: ${maleta?.nombreMaleta ?: "NINGUNA"}")
                    Log.d(tag, "📦 Maleta encontrada: ${maleta?.nombreMaleta ?: "null"} (ID: ${maleta?.id ?: "null"})")
                    
                    _currentMaleta.value = maleta
                    
                    _uiState.value = _uiState.value.copy(
                        hasMaletaConfigured = maleta != null,
                        isLoading = false
                    )
                    
                    Log.d(tag, "✅ Estado actualizado - hasMaletaConfigured: ${maleta != null}")
                } else {
                    Log.w(tag, "⚠️ No hay usuario actual para cargar maleta")
                    _uiState.value = _uiState.value.copy(
                        hasMaletaConfigured = false,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e(tag, "❌ Error cargando maleta del usuario", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasMaletaConfigured = false,
                    error = "Error cargando configuración de maleta: ${e.message}"
                )
            }
        }
    }

    /**
     * Configurar nueva maleta para el usuario actual
     */
    fun configurarMaleta(
        nombreMaleta: String,
        identificadorInvima: String,
        descripcion: String? = null
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val currentUser = authRepository.getCurrentUser()
                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No hay usuario logueado"
                    )
                    return@launch
                }
                // Upsert por identificadorInvima: si ya existe local, asociarla al usuario; si no, crear nueva
                val existente = maletaDao.getByIdentificadorInvima(identificadorInvima)
                val maletaFinal = if (existente != null) {
                    val actualizada = existente.copy(
                        nombreMaleta = nombreMaleta,
                        descripcion = descripcion,
                        asignadaAUsuarioId = currentUser.id,
                        entidadSaludId = currentUser.entidadSaludId,
                        activa = true,
                        ultimaRevision = LocalDate.now().toString()
                    )
                    maletaDao.updateMaleta(actualizada)
                    actualizada
                } else {
                    val nuevaMaleta = MaletaEntity(
                        id = 0,
                        maletaUid = java.util.UUID.randomUUID().toString(),
                        identificadorInvima = identificadorInvima,
                        nombreMaleta = nombreMaleta,
                        descripcion = descripcion,
                        asignadaAUsuarioId = currentUser.id,
                        entidadSaludId = currentUser.entidadSaludId,
                        ultimaRevision = LocalDate.now().toString(),
                        activa = true,
                        serverId = null
                    )
                    val maletaId = maletaDao.upsertMaleta(nuevaMaleta)
                    maletaDao.getMaletaById(maletaId.toInt()) ?: nuevaMaleta
                }
                val maletaCreada = maletaFinal
                
                _currentMaleta.value = maletaCreada
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasMaletaConfigured = true,
                    lastConfiguredMaleta = maletaCreada
                )

                Log.d(tag, "✅ Maleta configurada/asociada: ${maletaCreada.nombreMaleta} (ID: ${maletaCreada.id})")
                
            } catch (e: Exception) {
                Log.e(tag, "❌ Error configurando maleta", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error configurando maleta: ${e.message}"
                )
            }
        }
    }

    /**
     * Actualizar información de la maleta actual
     */
    fun actualizarMaleta(
        nombreMaleta: String? = null,
        descripcion: String? = null
    ) {
        viewModelScope.launch {
            try {
                val maleta = _currentMaleta.value
                if (maleta == null) {
                    _uiState.value = _uiState.value.copy(error = "No hay maleta para actualizar")
                    return@launch
                }

                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val maletaActualizada = maleta.copy(
                    nombreMaleta = nombreMaleta ?: maleta.nombreMaleta,
                    descripcion = descripcion ?: maleta.descripcion,
                    ultimaRevision = LocalDate.now().toString()
                )

                maletaDao.updateMaleta(maletaActualizada)
                _currentMaleta.value = maletaActualizada
                
                _uiState.value = _uiState.value.copy(isLoading = false)
                
                Log.d(tag, "✅ Maleta actualizada: ${maletaActualizada.nombreMaleta}")
                
            } catch (e: Exception) {
                Log.e(tag, "❌ Error actualizando maleta", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error actualizando maleta: ${e.message}"
                )
            }
        }
    }

    /**
     * Obtener información de la maleta actual
     */
    fun getMaletaInfo(): MaletaInfo? {
        val maleta = _currentMaleta.value ?: return null
        return MaletaInfo(
            id = maleta.id,
            nombre = maleta.nombreMaleta ?: "Maleta sin nombre",
            identificadorInvima = maleta.identificadorInvima,
            descripcion = maleta.descripcion,
            ultimaRevision = maleta.ultimaRevision,
            activa = maleta.activa
        )
    }

    /**
     * Marcar maleta como revisada
     */
    fun marcarRevision() {
        viewModelScope.launch {
            try {
                val maleta = _currentMaleta.value ?: return@launch
                
                val maletaRevisada = maleta.copy(
                    ultimaRevision = LocalDate.now().toString()
                )
                
                maletaDao.updateMaleta(maletaRevisada)
                _currentMaleta.value = maletaRevisada
                
                Log.d(tag, "✅ Revisión de maleta registrada")
                
            } catch (e: Exception) {
                Log.e(tag, "❌ Error registrando revisión", e)
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
     * Cargar todas las maletas activas de la entidad del usuario actual
     */
    fun loadAllMaletas() {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser != null && currentUser.entidadSaludId != null) {
                    val maletas = maletaDao.getMaletasByEntidadId(currentUser.entidadSaludId)
                    _allMaletas.value = maletas
                    Log.d(tag, "✅ Cargadas ${maletas.size} maletas de la entidad ${currentUser.entidadSaludId}")
                } else {
                    Log.w(tag, "⚠️ Usuario sin entidad de salud, mostrando maletas vacías")
                    _allMaletas.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e(tag, "❌ Error cargando maletas de la entidad", e)
                _allMaletas.value = emptyList()
            }
        }
    }

    /**
     * Recargar configuración de maleta
     */
    fun reloadMaletaConfig() {
        loadCurrentUserMaleta()
        loadAllMaletas()
    }
}

/**
 * Estado de UI para maletas
 */
data class MaletaUiState(
    val isLoading: Boolean = true,
    val hasMaletaConfigured: Boolean = false,
    val error: String? = null,
    val lastConfiguredMaleta: MaletaEntity? = null
) {
    val needsSetup: Boolean get() = !hasMaletaConfigured && !isLoading
    val showSetupFlow: Boolean get() = needsSetup && error == null
}

/**
 * Información resumida de maleta
 */
data class MaletaInfo(
    val id: Int,
    val nombre: String,
    val identificadorInvima: String,
    val descripcion: String?,
    val ultimaRevision: String?,
    val activa: Boolean
)
