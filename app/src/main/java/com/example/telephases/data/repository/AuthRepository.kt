package com.example.telephases.data.repository

import com.example.telephases.data.local.entities.UserEntity
import com.example.telephases.network.AuthResponse
import com.example.telephases.network.Credentials
import com.example.telephases.network.RegisterRequest
import kotlinx.coroutines.flow.Flow

/**
 * Interface del Repository para manejo de autenticación
 * Implementa el patrón Repository con fuente dual (local + remoto)
 */
interface AuthRepository {

    // ========== OPERACIONES LOCALES (OFFLINE) ==========

    /**
     * Obtiene el usuario actualmente logueado (con token válido)
     */
    suspend fun getCurrentUser(): UserEntity?

    /**
     * Obtiene el usuario actual como Flow
     */
    fun getCurrentUserFlow(): Flow<UserEntity?>

    /**
     * Verifica si hay una sesión activa válida
     */
    suspend fun hasValidSession(): Boolean

    /**
     * Obtiene el token actual si es válido
     */
    suspend fun getCurrentToken(): String?

    /**
     * Guarda un usuario en local después de login/registro exitoso
     */
    suspend fun saveUserLocally(user: UserEntity)

    /**
     * Actualiza el token de un usuario local
     */
    suspend fun updateUserToken(userId: String, token: String, expiration: String)

    /**
     * Invalida la sesión local (logout)
     */
    suspend fun logoutLocal()

    /**
     * Limpia todas las sesiones locales
     */
    suspend fun clearAllSessions()

    // ========== OPERACIONES REMOTAS (ONLINE) ==========

    /**
     * Realiza login en el servidor remoto
     */
    suspend fun loginRemote(credentials: Credentials): Result<AuthResponse>

    /**
     * Realiza registro en el servidor remoto
     */
    suspend fun registerRemote(request: RegisterRequest): Result<AuthResponse>

    /**
     * Valida un token con el servidor remoto
     */
    suspend fun validateTokenRemote(token: String): Result<Boolean>

    /**
     * Realiza logout en el servidor remoto
     */
    suspend fun logoutRemote(token: String): Result<Boolean>

    // ========== OPERACIONES UNIFICADAS (OFFLINE-FIRST) ==========

    /**
     * Realiza login (offline-first)
     * - Si hay conexión: intenta login remoto y guarda local
     * - Si no hay conexión: busca credenciales en cache local
     */
    suspend fun login(credentials: Credentials): Result<UserEntity>

    /**
     * Realiza registro (offline-first)
     * - Si hay conexión: registra en servidor y guarda local
     * - Si no hay conexión: guarda local para sincronizar después
     */
    suspend fun register(request: RegisterRequest): Result<UserEntity>

    /**
     * Realiza logout (unificado)
     * - Invalida sesión local
     * - Si hay conexión, notifica al servidor
     */
    suspend fun logout(): Result<Boolean>

    /**
     * Verifica y refresca el token actual
     */
    suspend fun refreshTokenIfNeeded(): Result<String>

    // ========== GESTIÓN DE USUARIOS ==========

    /**
     * Obtiene todos los usuarios locales
     */
    suspend fun getAllUsers(): List<UserEntity>

    /**
     * Busca un usuario por credencial (username o email)
     */
    suspend fun getUserByCredential(credential: String): UserEntity?

    /**
     * Verifica si existe un usuario con el username dado
     */
    suspend fun existsUserWithUsername(username: String): Boolean

    /**
     * Verifica si existe un usuario con el email dado
     */
    suspend fun existsUserWithEmail(email: String): Boolean

    /**
     * Actualiza información de usuario
     */
    suspend fun updateUser(user: UserEntity): Result<UserEntity>

    // ========== SINCRONIZACIÓN ==========

    /**
     * Sincroniza usuarios pendientes con el servidor
     */
    suspend fun syncUsers(): Result<SyncResult>

    /**
     * Obtiene usuarios que necesitan sincronización
     */
    suspend fun getUnsyncedUsers(): List<UserEntity>

    /**
     * Fuerza sincronización desde servidor
     */
    suspend fun forceSyncFromServer(): Result<SyncResult>

    // ========== UTILIDADES ==========

    /**
     * Verifica el estado de conectividad
     */
    suspend fun isNetworkAvailable(): Boolean

    /**
     * Obtiene estadísticas de autenticación
     */
    suspend fun getAuthStats(): AuthStats

    /**
     * Limpia datos expirados y ejecuta mantenimiento
     */
    suspend fun performMaintenance()

    /**
     * Valida un token localmente (formato y expiración)
     */
    suspend fun isTokenValid(token: String): Boolean

    /**
     * Extrae información del token JWT
     */
    suspend fun decodeToken(token: String): TokenInfo?

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
     * Estadísticas de autenticación
     */
    data class AuthStats(
        val totalUsers: Int,
        val activeUsers: Int,
        val syncedUsers: Int,
        val unsyncedUsers: Int,
        val todayLogins: Int,
        val hasActiveSession: Boolean
    ) {
        val syncPercentage: Float get() = if (totalUsers > 0) (syncedUsers.toFloat() / totalUsers) * 100 else 0f
    }

    /**
     * Información decodificada del token
     */
    data class TokenInfo(
        val userId: String,
        val email: String,
        val rolId: Int,
        val issuedAt: Long,
        val expiresAt: Long
    ) {
        val isExpired: Boolean get() = System.currentTimeMillis() / 1000 > expiresAt
        val remainingTime: Long get() = expiresAt - (System.currentTimeMillis() / 1000)
    }

    /**
     * Estados de resultado de operaciones de autenticación
     */
    sealed class AuthError : Exception() {
        object InvalidCredentials : AuthError()
        object UserNotFound : AuthError()
        object UserAlreadyExists : AuthError()
        object TokenExpired : AuthError()
        object TokenInvalid : AuthError()
        object NetworkError : AuthError()
        object ValidationError : AuthError()
        object SessionExpired : AuthError()
        data class ServerError(val code: Int, override val message: String) : AuthError()
        data class UnknownError(override val cause: Throwable) : AuthError()
    }

    /**
     * Estados de sesión
     */
    enum class SessionState {
        NOT_AUTHENTICATED,
        AUTHENTICATED,
        TOKEN_EXPIRED,
        INVALID_SESSION,
        NETWORK_ERROR
    }
}

