package com.example.telephases.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telephases.data.repository.AuthRepository
import com.example.telephases.data.local.entities.UserEntity
import com.example.telephases.network.Credentials
import com.example.telephases.network.RegisterRequest
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkConnectivityManager: NetworkConnectivityManager
) : ViewModel() {

    private val tag = "AuthViewModel"

    // Estados principales
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    // Estado combinado para la UI
    val uiState = combine(
        _state,
        _currentUser,
        _isOnline
    ) { authState, user, online ->
        AuthUiState(
            authState = authState,
            currentUser = user,
            isOnline = online,
            hasValidSession = user?.isTokenValid == true
        )
    }

    init {
        // NO verificar sesión automáticamente - solo al hacer login explícito
        // checkCurrentSession() // COMENTADO: No verificar sesión persistente
        
        // Observar cambios de conectividad
        viewModelScope.launch {
            networkConnectivityManager.connectivityState.collect { connectivity ->
                _isOnline.value = connectivity.isConnected
                Log.d(tag, "📡 Estado de conectividad: ${connectivity.displayText}")
            }
        }
    }

    /**
     * Verificar sesión actual
     */
    private fun checkCurrentSession() {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser()
                if (user != null && user.isTokenValid) {
                    _currentUser.value = user
                    _state.value = AuthState.Success(user.tokenActual!!, user)
                    Log.d(tag, "✅ Sesión válida encontrada: ${user.username}")
                } else {
                    _state.value = AuthState.Idle
                    Log.d(tag, "ℹ️ No hay sesión válida")
                }
            } catch (e: Exception) {
                Log.e(tag, "❌ Error verificando sesión", e)
                _state.value = AuthState.Idle
            }
        }
    }

    /**
     * Registro de usuario (offline-first)
     */
    fun register(req: RegisterRequest) = viewModelScope.launch {
        _state.value = AuthState.Loading
        try {
            Log.d(tag, "🔄 Iniciando registro para: ${req.username}")
            
            val result = authRepository.register(req)
            if (result.isSuccess) {
                val user = result.getOrThrow()
                _currentUser.value = user
                _state.value = AuthState.Success(user.tokenActual ?: "", user)
                
                Log.d(tag, "✅ Registro exitoso: ${user.username}")
            } else {
                val error = result.exceptionOrNull()
                val errorMessage = when (error) {
                    is AuthRepository.AuthError.UserAlreadyExists -> "El usuario ya existe"
                    is AuthRepository.AuthError.ValidationError -> "Datos inválidos"
                    is AuthRepository.AuthError.NetworkError -> "Sin conexión - guardado offline"
                    else -> error?.message ?: "Error al registrar"
                }
                
                Log.e(tag, "❌ Error en registro: $errorMessage")
                _state.value = AuthState.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Excepción en registro", e)
            _state.value = AuthState.Error(e.localizedMessage ?: "Error inesperado")
        }
    }

    /**
     * Login de usuario (offline-first)
     */
    fun login(creds: Credentials) = viewModelScope.launch {
        _state.value = AuthState.Loading
        try {
            Log.d(tag, "🔄 Iniciando login para: ${creds.username}")
            
            val result = authRepository.login(creds)
            if (result.isSuccess) {
                val user = result.getOrThrow()
                _currentUser.value = user
                _state.value = AuthState.Success(user.tokenActual ?: "", user)
                
                Log.d(tag, "✅ Login exitoso: ${user.username}")
            } else {
                val error = result.exceptionOrNull()
                val errorMessage = when (error) {
                    is AuthRepository.AuthError.InvalidCredentials -> "Credenciales inválidas"
                    is AuthRepository.AuthError.UserNotFound -> "Usuario no encontrado"
                    is AuthRepository.AuthError.NetworkError -> "Sin conexión"
                    is AuthRepository.AuthError.TokenExpired -> "Sesión expirada"
                    else -> error?.message ?: "Error de autenticación"
                }
                
                Log.e(tag, "❌ Error en login: $errorMessage")
                _state.value = AuthState.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Excepción en login", e)
            _state.value = AuthState.Error(e.localizedMessage ?: "Error inesperado")
        }
    }

    /**
     * Logout del usuario
     */
    fun logout() = viewModelScope.launch {
        try {
            Log.d(tag, "🔄 Iniciando logout...")
            
            val result = authRepository.logout()
            if (result.isSuccess) {
                _currentUser.value = null
                _state.value = AuthState.Idle
                Log.d(tag, "✅ Logout exitoso")
            } else {
                Log.w(tag, "⚠️ Error en logout, pero limpiando sesión local")
                _currentUser.value = null
                _state.value = AuthState.Idle
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en logout", e)
            // Aún así limpiar la sesión local
            _currentUser.value = null
            _state.value = AuthState.Idle
        }
    }
    
    /**
     * Limpiar sesión completamente (para logout forzado)
     */
    fun clearSession() {
        Log.d(tag, "🧹 Limpiando sesión completamente")
        _currentUser.value = null
        _state.value = AuthState.Idle
    }

    /**
     * Refrescar token si es necesario
     */
    fun refreshTokenIfNeeded() = viewModelScope.launch {
        try {
            val user = _currentUser.value
            if (user == null || user.isTokenValid) {
                return@launch // No hay usuario o token aún válido
            }
            
            Log.d(tag, "🔄 Refrescando token...")
            
            val result = authRepository.refreshTokenIfNeeded()
            if (result.isSuccess) {
                // Actualizar usuario con nuevo token
                checkCurrentSession()
                Log.d(tag, "✅ Token refrescado")
            } else {
                Log.w(tag, "⚠️ No se pudo refrescar token - cerrando sesión")
                logout()
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error refrescando token", e)
            logout()
        }
    }

    /**
     * Verificar si hay una sesión válida
     */
    fun hasValidSession(): Boolean {
        return _currentUser.value?.isTokenValid == true
    }

    /**
     * Obtener token actual
     */
    fun getCurrentToken(): String? {
        return if (hasValidSession()) {
            _currentUser.value?.tokenActual
        } else {
            null
        }
    }

    /**
     * Obtener usuario actual
     */
    suspend fun getCurrentUser(): UserEntity? {
        return authRepository.getCurrentUser()
    }

    /**
     * Limpiar errores
     */
    fun clearError() {
        if (_state.value is AuthState.Error) {
            _state.value = AuthState.Idle
        }
    }

    /**
     * Obtener estadísticas de autenticación
     */
    fun getAuthStats() = viewModelScope.launch {
        try {
            val stats = authRepository.getAuthStats()
            Log.d(tag, "📊 Estadísticas de auth: $stats")
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo estadísticas", e)
        }
    }
}

/**
 * Estado de UI combinado
 */
data class AuthUiState(
    val authState: AuthState,
    val currentUser: UserEntity?,
    val isOnline: Boolean,
    val hasValidSession: Boolean
) {
    val showOfflineIndicator: Boolean get() = !isOnline
    val canSync: Boolean get() = isOnline && hasValidSession
    val userDisplayName: String get() = currentUser?.nombreCompleto ?: "Usuario"
}

