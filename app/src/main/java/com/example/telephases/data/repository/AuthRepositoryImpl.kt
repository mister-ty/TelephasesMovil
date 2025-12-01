package com.example.telephases.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.telephases.data.local.dao.UserDao
import com.example.telephases.data.local.dao.SyncMetadataDao
import com.example.telephases.data.local.entities.UserEntity
import com.example.telephases.network.ApiInterface
import com.example.telephases.network.AuthResponse
import com.example.telephases.network.Credentials
import com.example.telephases.network.RegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Base64
import org.json.JSONObject

/**
 * Implementación del Repository para manejo de autenticación
 * Implementa patrón Offline-First con cache de sesiones
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val syncMetadataDao: SyncMetadataDao,
    private val apiService: ApiInterface,
    private val context: Context
) : AuthRepository {

    private val tag = "AuthRepository"
    
    // Cache en memoria del usuario actual para evitar consultas constantes a la BD
    private var currentUserCache: UserEntity? = null
    private var lastCacheUpdate: Long = 0
    private val cacheValidityMs = 5000L // 5 segundos de validez del cache

    // ========== MÉTODOS DE CACHE ==========
    
    /**
     * Invalida el cache del usuario actual
     */
    private fun invalidateUserCache() {
        currentUserCache = null
        lastCacheUpdate = 0
        Log.d(tag, "🧹 Cache de usuario invalidado")
    }
    
    /**
     * Actualiza el cache con un usuario específico
     */
    private fun updateUserCache(user: UserEntity?) {
        currentUserCache = user
        lastCacheUpdate = System.currentTimeMillis()
        Log.d(tag, "💾 Cache actualizado: ${user?.username ?: "null"}")
    }
    
    /**
     * Fuerza la actualización del cache desde la base de datos
     */
    suspend fun refreshUserCache() = withContext(Dispatchers.IO) {
        try {
            val user = userDao.getCurrentLoggedUser()
            updateUserCache(user)
            Log.d(tag, "🔄 Cache refrescado desde BD: ${user?.username ?: "null"}")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error refrescando cache", e)
            invalidateUserCache()
        }
    }

    // ========== OPERACIONES LOCALES (OFFLINE) ==========

    override suspend fun getCurrentUser(): UserEntity? = withContext(Dispatchers.IO) {
        try {
            // Verificar cache primero
            val now = System.currentTimeMillis()
            if (currentUserCache != null && (now - lastCacheUpdate) < cacheValidityMs) {
                Log.d(tag, "🔄 Usando cache de usuario: ${currentUserCache?.username}")
                return@withContext currentUserCache
            }
            
            // Si no hay cache válido, consultar BD
            val user = userDao.getCurrentLoggedUser()
            if (user != null) {
                // Actualizar cache
                currentUserCache = user
                lastCacheUpdate = now
                Log.d(tag, "💾 Cache actualizado con usuario: ${user.username}")
            } else {
                // Limpiar cache si no hay usuario
                currentUserCache = null
                lastCacheUpdate = now
                Log.d(tag, "🧹 Cache limpiado - no hay usuario logueado")
            }
            
            user
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo usuario actual", e)
            null
        }
    }

    override fun getCurrentUserFlow(): Flow<UserEntity?> {
        // TODO: Implementar Flow para usuario actual
        return userDao.getAllUsersFlow().map { users: List<UserEntity> ->
            users.firstOrNull { user -> 
                user.tokenActual?.isNotEmpty() == true &&
                user.fechaExpiracionToken?.toLongOrNull()?.let { it > System.currentTimeMillis() } == true
            }
        }
    }

    override suspend fun hasValidSession(): Boolean = withContext(Dispatchers.IO) {
        try {
            val currentUser = userDao.getCurrentLoggedUser()
            currentUser?.isTokenValid == true
        } catch (e: Exception) {
            Log.e(tag, "Error verificando sesión válida", e)
            false
        }
    }

    override suspend fun getCurrentToken(): String? = withContext(Dispatchers.IO) {
        try {
            val currentUser = userDao.getCurrentLoggedUser()
            println("🔍 Usuario actual obtenido: $currentUser")
            println("🔍 Token actual: ${currentUser?.tokenActual}")
            println("🔍 Token válido: ${currentUser?.isTokenValid}")
            
            if (currentUser?.isTokenValid == true) {
                println("🔍 Token válido encontrado: ${currentUser.tokenActual}")
                currentUser.tokenActual
            } else {
                println("❌ Token no válido o usuario no encontrado")
                null
            }
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo token actual", e)
            println("❌ Error obteniendo token: ${e.message}")
            null
        }
    }

    override suspend fun saveUserLocally(user: UserEntity): Unit = withContext(Dispatchers.IO) {
        try {
            userDao.upsertUser(user)
            Log.d(tag, "✅ Usuario guardado localmente: ${user.username}")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error guardando usuario localmente", e)
            throw AuthRepository.AuthError.UnknownError(e)
        }
    }

    override suspend fun updateUserToken(userId: String, token: String, expiration: String): Unit = withContext(Dispatchers.IO) {
        try {
            userDao.updateUserToken(userId, token, expiration)
            Log.d(tag, "✅ Token actualizado para usuario: $userId")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error actualizando token", e)
            throw AuthRepository.AuthError.UnknownError(e)
        }
    }

    override suspend fun logoutLocal() = withContext(Dispatchers.IO) {
        try {
            val currentUser = userDao.getCurrentLoggedUser()
            if (currentUser != null) {
                userDao.invalidateUserToken(currentUser.id)
                // INVALIDAR CACHE AL HACER LOGOUT
                invalidateUserCache()
                Log.d(tag, "✅ Logout local exitoso")
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en logout local", e)
            throw AuthRepository.AuthError.UnknownError(e)
        }
    }

    override suspend fun clearAllSessions(): Unit = withContext(Dispatchers.IO) {
        try {
            userDao.invalidateAllTokens()
            Log.d(tag, "✅ Todas las sesiones limpiadas")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error limpiando todas las sesiones", e)
            throw AuthRepository.AuthError.UnknownError(e)
        }
    }

    // ========== OPERACIONES REMOTAS (ONLINE) ==========

    override suspend fun loginRemote(credentials: Credentials): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.login(credentials)
            Log.d(tag, "✅ Login remoto exitoso")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en login remoto", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun registerRemote(request: RegisterRequest): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.register(request)
            Log.d(tag, "✅ Registro remoto exitoso")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en registro remoto", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun validateTokenRemote(token: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            // TODO: Implementar endpoint de validación de token en la API
            // Por ahora simulamos validación local
            val isValid = isTokenValid(token)
            Result.success(isValid)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error validando token remotamente", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun logoutRemote(token: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            // TODO: Implementar endpoint de logout en la API
            // Por ahora simulamos éxito
            Log.d(tag, "✅ Logout remoto simulado")
            Result.success(true)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en logout remoto", e)
            Result.failure(mapNetworkException(e))
        }
    }

    // ========== OPERACIONES UNIFICADAS (OFFLINE-FIRST) ==========

    override suspend fun login(credentials: Credentials): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val networkAvailable = isNetworkAvailable()
            Log.d(tag, "📡 Estado de red: ${if (networkAvailable) "CONECTADO" else "DESCONECTADO"}")
            
            if (networkAvailable) {
                // MODO ONLINE: Intentar login remoto
                Log.d(tag, "🌐 Intentando login remoto para: ${credentials.username}")
                val remoteResult = loginRemote(credentials)
                if (remoteResult.isSuccess) {
                    val authResponse = remoteResult.getOrThrow()
                    
                    // Decodificar información del token
                    val tokenInfo = decodeToken(authResponse.token)
                    // Conservar entidad_salud_id si ya existía localmente para evitar perder FKs
                    val existingLocal = try {
                        tokenInfo?.userId?.takeIf { it.isNotBlank() }?.let { id ->
                            userDao.getUserById(id)
                        } ?: userDao.getUserByCredential(credentials.username)
                    } catch (e: Exception) { null }
                    
                    // Crear usuario local desde respuesta, preservando entidadSaludId previa si la hay
                    val user = UserEntity.fromLoginResponse(
                        token = authResponse.token,
                        userId = tokenInfo?.userId,
                        email = tokenInfo?.email ?: credentials.username,
                        username = credentials.username,
                        rolId = tokenInfo?.rolId ?: 1,
                        entidadSaludId = existingLocal?.entidadSaludId
                    )
                    
                    // FORZAR GUARDADO LOCAL - SIEMPRE guardar después de login remoto exitoso
                    Log.d(tag, "🔧 FORZANDO guardado local de usuario remoto: ${user.username}")
                    
                    // INVALIDAR TOKENS DE OTROS USUARIOS (NO del usuario actual)
                    Log.d(tag, "🔧 Invalidando tokens de otros usuarios (manteniendo ${user.username})...")
                    userDao.invalidateAllTokensExcept(user.id)
                    
                    // Verificar que se invalidaron correctamente
                    val usersWithTokens = userDao.getUsersWithValidTokens()
                    Log.d(tag, "🔍 Usuarios con tokens después de invalidar: ${usersWithTokens.size}")
                    usersWithTokens.forEach { u ->
                        Log.d(tag, "  - ${u.username}: ${u.tokenActual}")
                    }
                    
                    // FORZAR GUARDADO LOCAL - SIEMPRE guardar después de login remoto exitoso
                    Log.d(tag, "🔧 FORZANDO guardado local de usuario remoto: ${user.username} (ID: ${user.id})")
                    
                    // Verificar si ya existe un usuario con el mismo username
                    val existingUser = userDao.getUserByCredential(user.username)
                    Log.d(tag, "🔍 Usuario existente encontrado: $existingUser")
                    
                    if (existingUser != null) {
                        // Si existe, actualizar con el nuevo token
                        Log.d(tag, "🔄 Actualizando usuario existente con nuevo token...")
                        val updatedUser = existingUser.copy(
                            tokenActual = user.tokenActual,
                            fechaExpiracionToken = user.fechaExpiracionToken,
                            ultimoLogin = user.ultimoLogin,
                            email = user.email,
                            rolId = user.rolId,
                            sincronizado = true,
                            fechaUltimaSincronizacion = user.fechaUltimaSincronizacion
                        )
                        userDao.updateUser(updatedUser)
                        Log.d(tag, "✅ Usuario existente actualizado con nuevo token")
                    } else {
                        // Si no existe, crear nuevo usuario
                        Log.d(tag, "🆕 Creando nuevo usuario...")
                        try {
                            val upsertResult = userDao.upsertUser(user)
                            Log.d(tag, "🔧 Resultado upsertUser: $upsertResult")
                        } catch (e: Exception) {
                            Log.e(tag, "❌ Error en upsertUser: ${e.message}", e)
                        }
                    }
                    
                    // ACTUALIZAR ULTIMO LOGIN para asegurar que sea el usuario más reciente
                    val currentTime = java.time.Instant.now().toString()
                    val userIdToUpdate = existingUser?.id ?: user.id
                    userDao.updateLastLogin(userIdToUpdate, currentTime)
                    
                    // FORZAR ACTUALIZACIÓN DE TOKEN para asegurar que se guarde correctamente
                    userDao.updateUserToken(userIdToUpdate, user.tokenActual ?: authResponse.token, user.fechaExpiracionToken ?: "NEVER_EXPIRES")
                    
                    // VERIFICAR QUE SE GUARDÓ CORRECTAMENTE
                    val savedUser = userDao.getUserById(userIdToUpdate)
                    Log.d(tag, "🔍 Usuario guardado verificado: $savedUser")
                    Log.d(tag, "🔍 Token del usuario guardado: ${savedUser?.tokenActual}")
                    Log.d(tag, "🔍 Fecha expiración: ${savedUser?.fechaExpiracionToken}")
                    
                    // VERIFICAR POR USERNAME TAMBIÉN
                    val userByUsername = userDao.getUserByCredential(user.username)
                    Log.d(tag, "🔍 Usuario por username: $userByUsername - Token: ${userByUsername?.tokenActual}")
                    
                    // Verificar que el nuevo usuario tiene token
                    val newUserWithToken = userDao.getCurrentLoggedUser()
                    Log.d(tag, "🔍 Usuario actual después de guardar: ${newUserWithToken?.username}")
                    Log.d(tag, "🔍 Token del usuario guardado: ${newUserWithToken?.tokenActual}")
                    Log.d(tag, "🔍 Fecha expiración: ${newUserWithToken?.fechaExpiracionToken}")
                    
                    // Debug adicional: buscar por ID específico
                    val userById = userDao.getUserById(userIdToUpdate)
                    Log.d(tag, "🔍 Usuario por ID: ${userById?.username} - Token: ${userById?.tokenActual}")
                    
                    // Debug adicional: verificar todos los usuarios con tokens
                    val allUsersWithTokens = userDao.getUsersWithValidTokens()
                    Log.d(tag, "🔍 TODOS los usuarios con tokens válidos: ${allUsersWithTokens.size}")
                    allUsersWithTokens.forEach { u ->
                        Log.d(tag, "  - ${u.username} (ID: ${u.id}): Token=${u.tokenActual}, UltimoLogin=${u.ultimoLogin}")
                    }
                    // Refuerzo: si teníamos entidadSaludId previa y el nuevo quedó NULL, persistir la previa
                    if (existingLocal?.entidadSaludId != null && user.entidadSaludId == null && (tokenInfo?.userId?.isNotBlank() == true)) {
                        try {
                            userDao.updateUserEntidad(tokenInfo!!.userId, existingLocal.entidadSaludId)
                        } catch (_: Exception) {}
                    }
                    
                    // ACTUALIZAR CACHE INMEDIATAMENTE
                    val finalUser = savedUser ?: user
                    updateUserCache(finalUser)
                    
                    // FORZAR HABILITACIÓN DE AUTO-SYNC después del login exitoso
                    try {
                        syncMetadataDao.updateAutoSyncEnabled(true)
                        Log.d(tag, "✅ Auto-sync habilitado después del login")
                    } catch (e: Exception) {
                        Log.e(tag, "❌ Error habilitando auto-sync", e)
                    }
                    
                    Log.d(tag, "✅ Login online exitoso y guardado localmente")
                    return@withContext Result.success(finalUser)
                } else {
                    val error = remoteResult.exceptionOrNull()
                    Log.w(tag, "⚠️ Fallo login online: ${error?.message}, intentando cache local")
                }
            }

            // MODO OFFLINE: Buscar en cache local
            Log.d(tag, "🔍 Buscando usuario en BD local: ${credentials.username}")
            
            // DEBUG: Verificar TODOS los usuarios en BD local
            val allUsersDebug = userDao.getAllUsersDebug()
            Log.d(tag, "🐛 [DEBUG] Total usuarios en BD local: ${allUsersDebug.size}")
            allUsersDebug.forEach { user ->
                Log.d(tag, "🐛 [DEBUG] Usuario: ${user.username} | Email: ${user.email} | Activo: ${user.activo} | Token: ${user.tokenActual != null} | Sync: ${user.sincronizado}")
            }
            
            val localUser = userDao.getUserByCredential(credentials.username)
            Log.d(tag, "🔍 Usuario encontrado en BD local: ${localUser?.username ?: "NO ENCONTRADO"}")
            
            if (localUser != null && localUser.isTokenValid) {
                // Verificar contraseña
                if (!localUser.verifyPassword(credentials.password)) {
                    Log.d(tag, "❌ Contraseña incorrecta para usuario: ${localUser.username}")
                    return@withContext Result.failure(AuthRepository.AuthError.InvalidCredentials)
                }
                Log.d(tag, "✅ Login offline exitoso desde cache")
                
                // INVALIDAR TOKENS DE OTROS USUARIOS (NO del usuario actual)
                Log.d(tag, "🔧 Invalidando tokens de otros usuarios (manteniendo ${localUser.username})...")
                userDao.invalidateAllTokensExcept(localUser.id)
                
                // Verificar que se invalidaron correctamente
                val usersWithTokens = userDao.getUsersWithValidTokens()
                Log.d(tag, "🔍 Usuarios con tokens después de invalidar: ${usersWithTokens.size}")
                usersWithTokens.forEach { u ->
                    Log.d(tag, "  - ${u.username}: ${u.tokenActual}")
                }
                
                // ACTUALIZAR TOKEN DEL USUARIO ACTUAL - Asegurar que sea un token válido
                val currentTime = java.time.Instant.now().toString()
                val validToken = if (localUser.tokenActual.isNullOrBlank() || localUser.tokenActual == "RESTORED_FROM_CLOUD") {
                    "OFFLINE_PERMANENT_${localUser.id}_${System.currentTimeMillis()}"
                } else {
                    localUser.tokenActual
                }
                
                userDao.updateUserToken(localUser.id, validToken, localUser.fechaExpiracionToken ?: "NEVER_EXPIRES")
                
                // ACTUALIZAR ULTIMO LOGIN para asegurar que sea el usuario más reciente
                userDao.updateLastLogin(localUser.id, currentTime)
                
                // Verificar que el usuario actual tiene token
                val newUserWithToken = userDao.getCurrentLoggedUser()
                Log.d(tag, "🔍 Usuario actual después de actualizar token: ${newUserWithToken?.username}")
                
                // Debug adicional: verificar todos los usuarios con tokens
                val allUsersWithTokens = userDao.getUsersWithValidTokens()
                Log.d(tag, "🔍 TODOS los usuarios con tokens válidos: ${allUsersWithTokens.size}")
                allUsersWithTokens.forEach { u ->
                    Log.d(tag, "  - ${u.username} (ID: ${u.id}): Token=${u.tokenActual}, UltimoLogin=${u.ultimoLogin}")
                }
                
                // ACTUALIZAR CACHE INMEDIATAMENTE con el token corregido
                val updatedUser = localUser.copy(
                    tokenActual = validToken,
                    ultimoLogin = currentTime
                )
                updateUserCache(updatedUser)
                
                // FORZAR HABILITACIÓN DE AUTO-SYNC después del login offline
                try {
                    syncMetadataDao.updateAutoSyncEnabled(true)
                    Log.d(tag, "✅ Auto-sync habilitado después del login offline")
                } catch (e: Exception) {
                    Log.e(tag, "❌ Error habilitando auto-sync", e)
                }
                
                return@withContext Result.success(updatedUser)
            } else if (localUser != null) {
                Log.d(tag, "⚠️ Usuario encontrado pero token inválido: ${localUser.username}")
                Log.d(tag, "🔍 Token: ${localUser.tokenActual}, Expiración: ${localUser.fechaExpiracionToken}")
                
                // Verificar contraseña
                if (!localUser.verifyPassword(credentials.password)) {
                    Log.d(tag, "❌ Contraseña incorrecta para usuario: ${localUser.username}")
                    return@withContext Result.failure(AuthRepository.AuthError.InvalidCredentials)
                }
                
                // INVALIDAR TOKENS DE OTROS USUARIOS (NO del usuario actual)
                Log.d(tag, "🔧 Invalidando tokens de otros usuarios (manteniendo ${localUser.username})...")
                userDao.invalidateAllTokensExcept(localUser.id)
                
                // Verificar que se invalidaron correctamente
                val usersWithTokens = userDao.getUsersWithValidTokens()
                Log.d(tag, "🔍 Usuarios con tokens después de invalidar: ${usersWithTokens.size}")
                usersWithTokens.forEach { u ->
                    Log.d(tag, "  - ${u.username}: ${u.tokenActual}")
                }
                
                // GENERAR TOKEN PERMANENTE PARA USUARIO OFFLINE
                Log.d(tag, "🔧 Generando token permanente para usuario offline")
                val permanentToken = "OFFLINE_PERMANENT_${localUser.id}_${System.currentTimeMillis()}"
                val permanentExpiration = "NEVER_EXPIRES"
                
                userDao.updateUserToken(localUser.id, permanentToken, permanentExpiration)
                
                // ACTUALIZAR ULTIMO LOGIN para asegurar que sea el usuario más reciente
                val currentTime = java.time.Instant.now().toString()
                userDao.updateLastLogin(localUser.id, currentTime)
                
                // Verificar que el usuario actual tiene token
                val newUserWithToken = userDao.getCurrentLoggedUser()
                Log.d(tag, "🔍 Usuario actual después de generar token permanente: ${newUserWithToken?.username}")
                
                // Debug adicional: verificar todos los usuarios con tokens
                val allUsersWithTokens = userDao.getUsersWithValidTokens()
                Log.d(tag, "🔍 TODOS los usuarios con tokens válidos: ${allUsersWithTokens.size}")
                allUsersWithTokens.forEach { u ->
                    Log.d(tag, "  - ${u.username} (ID: ${u.id}): Token=${u.tokenActual}, UltimoLogin=${u.ultimoLogin}")
                }
                
                val updatedUser = localUser.copy(
                    tokenActual = permanentToken,
                    fechaExpiracionToken = permanentExpiration,
                    ultimoLogin = currentTime
                )
                
                // ACTUALIZAR CACHE INMEDIATAMENTE
                updateUserCache(updatedUser)
                
                // FORZAR HABILITACIÓN DE AUTO-SYNC después del login con token permanente
                try {
                    syncMetadataDao.updateAutoSyncEnabled(true)
                    Log.d(tag, "✅ Auto-sync habilitado después del login con token permanente")
                } catch (e: Exception) {
                    Log.e(tag, "❌ Error habilitando auto-sync", e)
                }
                
                Log.d(tag, "✅ Token permanente generado para usuario offline: ${updatedUser.username}")
                return@withContext Result.success(updatedUser)
            }

            // NO SE PUDO AUTENTICAR
            Log.d(tag, "❌ No se pudo autenticar: credenciales inválidas o sin cache")
            Result.failure(AuthRepository.AuthError.InvalidCredentials)

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en login", e)
            Result.failure(AuthRepository.AuthError.UnknownError(e))
        }
    }

    override suspend fun register(request: RegisterRequest): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            // Verificar si ya existe localmente
            val existingByUsername = userDao.existsUserWithUsername(request.username)
            val existingByEmail = userDao.existsUserWithEmail(request.email)
            
            if (existingByUsername || existingByEmail) {
                return@withContext Result.failure(AuthRepository.AuthError.UserAlreadyExists)
            }

            if (isNetworkAvailable()) {
                // MODO ONLINE: Registrar en servidor
                val remoteResult = registerRemote(request)
                if (remoteResult.isSuccess) {
                    val authResponse = remoteResult.getOrThrow()
                    
                    // Crear usuario local desde respuesta
                    val user = UserEntity.fromLoginResponse(
                        token = authResponse.token,
                        email = request.email,
                        username = request.username,
                        rolId = request.rol_id ?: 1
                    )
                    
                    userDao.upsertUser(user)
                    Log.d(tag, "✅ Usuario registrado online y guardado localmente")
                    return@withContext Result.success(user)
                } else {
                    Log.w(tag, "⚠️ Fallo registro online, guardando offline")
                }
            }

            // MODO OFFLINE: Guardar solo localmente
            val offlineUser = UserEntity.createForOfflineRegistration(
                username = request.username,
                email = request.email,
                primerNombre = request.primer_nombre,
                primerApellido = request.primer_apellido,
                numeroDocumento = request.numero_documento,
                segundoNombre = request.segundo_nombre,
                segundoApellido = request.segundo_apellido,
                tipoDocumentoId = request.tipo_documento_id,
                telefono = request.telefono,
                direccion = request.direccion,
                ciudadId = request.ciudad_id,
                fechaNacimiento = request.fecha_nacimiento,
                genero = request.genero,
                rolId = request.rol_id ?: 1
            )

            userDao.upsertUser(offlineUser)
            Log.d(tag, "✅ Usuario registrado offline: ${offlineUser.username}")
            Result.success(offlineUser)

        } catch (e: Exception) {
            Log.e(tag, "❌ Error en registro", e)
            Result.failure(AuthRepository.AuthError.UnknownError(e))
        }
    }

    override suspend fun logout(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val currentUser = getCurrentUser()
            
            // Logout local
            logoutLocal()
            
            // Si hay red y usuario con token, notificar al servidor
            if (isNetworkAvailable() && currentUser?.tokenActual != null) {
                try {
                    logoutRemote(currentUser.tokenActual!!)
                } catch (e: Exception) {
                    Log.w(tag, "⚠️ Error en logout remoto, pero logout local exitoso", e)
                }
            }
            
            Log.d(tag, "✅ Logout completado")
            Result.success(true)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en logout", e)
            Result.failure(AuthRepository.AuthError.UnknownError(e))
        }
    }

    override suspend fun refreshTokenIfNeeded(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val currentUser = getCurrentUser()
            if (currentUser == null) {
                return@withContext Result.failure(AuthRepository.AuthError.SessionExpired)
            }
            
            if (currentUser.isTokenValid) {
                // Token aún válido
                return@withContext Result.success(currentUser.tokenActual!!)
            }
            
            // Token expirado, necesita renovación
            if (isNetworkAvailable()) {
                // TODO: Implementar endpoint de refresh token en la API
                Log.d(tag, "ℹ️ Refresh token no implementado en API")
                Result.failure(AuthRepository.AuthError.TokenExpired)
            } else {
                Result.failure(AuthRepository.AuthError.NetworkError)
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error refrescando token", e)
            Result.failure(AuthRepository.AuthError.UnknownError(e))
        }
    }

    // ========== GESTIÓN DE USUARIOS ==========

    override suspend fun getAllUsers(): List<UserEntity> = withContext(Dispatchers.IO) {
        try {
            userDao.getAllUsers()
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo todos los usuarios", e)
            emptyList()
        }
    }

    override suspend fun getUserByCredential(credential: String): UserEntity? = withContext(Dispatchers.IO) {
        try {
            userDao.getUserByCredential(credential)
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo usuario por credencial", e)
            null
        }
    }

    override suspend fun existsUserWithUsername(username: String): Boolean = withContext(Dispatchers.IO) {
        try {
            userDao.existsUserWithUsername(username)
        } catch (e: Exception) {
            Log.e(tag, "Error verificando existencia de username", e)
            false
        }
    }

    override suspend fun existsUserWithEmail(email: String): Boolean = withContext(Dispatchers.IO) {
        try {
            userDao.existsUserWithEmail(email)
        } catch (e: Exception) {
            Log.e(tag, "Error verificando existencia de email", e)
            false
        }
    }

    override suspend fun updateUser(user: UserEntity): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val updatedUser = user.copy(
                modificadoLocalmente = true,
                fechaModificacionLocal = java.time.Instant.now().toString(),
                sincronizado = false
            )
            
            userDao.updateUser(updatedUser)
            Log.d(tag, "✅ Usuario actualizado: ${user.username}")
            Result.success(updatedUser)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error actualizando usuario", e)
            Result.failure(AuthRepository.AuthError.UnknownError(e))
        }
    }

    // ========== SINCRONIZACIÓN ==========

    override suspend fun syncUsers(): Result<AuthRepository.SyncResult> = withContext(Dispatchers.IO) {
        Log.d(tag, "🔄 Iniciando sincronización de usuarios...")
        
        try {
            // Obtener usuarios no sincronizados
            val unsyncedUsers = userDao.getUnsyncedUsers()
            Log.d(tag, "📊 Usuarios pendientes de sincronización: ${unsyncedUsers.size}")
            
            if (unsyncedUsers.isEmpty()) {
                Log.d(tag, "✅ No hay usuarios pendientes de sincronización")
                return@withContext Result.success(
                    AuthRepository.SyncResult(
                        success = true,
                        syncedCount = 0,
                        failedCount = 0,
                        totalCount = 0,
                        errors = emptyList()
                    )
                )
            }
            
            // Verificar conectividad
            if (!isNetworkAvailable()) {
                Log.w(tag, "⚠️ Sin conectividad - cancelando sincronización")
                return@withContext Result.failure(AuthRepository.AuthError.NetworkError)
            }
            
            // Obtener token actual
            val token = getCurrentToken()
            // TEMPORALMENTE DESHABILITADO - Permitir sincronización con OFFLINE_TOKEN para actualizar entidad_salud_id
            /*if (token == null || token.startsWith("OFFLINE_TOKEN") || token.startsWith("TEST_TOKEN_")) {
                Log.w(tag, "⚠️ Token no válido para sincronización con servidor")
                return@withContext Result.failure(AuthRepository.AuthError.TokenInvalid)
            }*/
            if (token == null) {
                Log.w(tag, "⚠️ Token nulo - cancelando sincronización")
                return@withContext Result.failure(AuthRepository.AuthError.TokenInvalid)
            }
            Log.d(tag, "✅ Usando token para sincronización: ${token.take(30)}...")
            
            var syncedCount = 0
            var failedCount = 0
            val errors = mutableListOf<String>()
            
            // Sincronizar cada usuario
            for (user in unsyncedUsers) {
                try {
                    // Log.d(tag, "📤 Sincronizando usuario: ${user.email}")
                    // Log.d(tag, "🔍 ROL ORIGINAL en BD local: ${user.rolId}")
                    
                    // Aplicar mapeo de roles antes de enviar
                    val mappedUser = mapRoleForCloud(user)
                    // Log.d(tag, "🔍 ROL DESPUÉS DE MAPEO: ${mappedUser.rolId}")
                    
                    // Crear request con el usuario mapeado
                    val request = RegisterRequest(
                        username = mappedUser.username,
                        email = mappedUser.email,
                        password = "admin123", // Contraseña por defecto para usuarios de entidades
                        primer_nombre = mappedUser.primerNombre,
                        segundo_nombre = mappedUser.segundoNombre,
                        primer_apellido = mappedUser.primerApellido,
                        segundo_apellido = mappedUser.segundoApellido,
                        tipo_documento_id = mappedUser.tipoDocumentoId,
                        numero_documento = mappedUser.numeroDocumento,
                        telefono = mappedUser.telefono,
                        direccion = mappedUser.direccion,
                        ciudad_id = mappedUser.ciudadId,
                        fecha_nacimiento = mappedUser.fechaNacimiento,
                        genero = mappedUser.genero,
                        rol_id = mappedUser.rolId, // Rol ya mapeado
                        entidad_salud_id = mappedUser.entidadSaludId
                    )
                    
                    // Log.d(tag, "🔍 ROL EN REQUEST A ENVIAR: ${request.rol_id}")
                    
                    // Enviar al servidor
                    apiService.register(request)
                    
                    // Marcar como sincronizado
                    userDao.markUserAsSynced(user.id)
                    syncedCount++
                    
                    // Log.d(tag, "✅ Usuario ${user.email} sincronizado correctamente")
                    
                } catch (e: Exception) {
                    failedCount++
                    val errorMsg = "Error sincronizando ${user.email}: ${e.message}"
                    errors.add(errorMsg)
                    Log.e(tag, "❌ $errorMsg", e)
                }
            }
            
            Log.d(tag, "📊 Sincronización completada: $syncedCount exitosos, $failedCount fallidos")
            
            Result.success(
                AuthRepository.SyncResult(
                    success = failedCount == 0,
                    syncedCount = syncedCount,
                    failedCount = failedCount,
                    totalCount = unsyncedUsers.size,
                    errors = errors
                )
            )
            
        } catch (e: Exception) {
            Log.e(tag, "❌ Error crítico en sincronización de usuarios", e)
            Result.failure(AuthRepository.AuthError.UnknownError(e))
        }
    }

    override suspend fun getUnsyncedUsers(): List<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val unsyncedUsers = userDao.getUnsyncedUsers()
            Log.d(tag, "📊 Usuarios no sincronizados: ${unsyncedUsers.size}")
            unsyncedUsers
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo usuarios no sincronizados", e)
            emptyList()
        }
    }
    
    /**
     * Mapea el rol del usuario de local a nube
     * Regla: TODOS los usuarios de la tabla users (rolId = 1 o 2) se convierten en Médico (rolId = 3) en la nube
     * Solo los pacientes reales (rolId = 2 con email que no sea contacto@) se mantienen como Paciente (rolId = 2)
     */
    private fun mapRoleForCloud(user: UserEntity): UserEntity {
        // REGLA: TODOS los usuarios de la tabla users (rolId = 1 o 2) se convierten en Médico (rolId = 3) en la nube
        if (user.rolId == 1 || user.rolId == 2) {
            Log.d(tag, "🔄 Usuario ${user.email}: ${user.rolId} (tablet) -> 3 (Médico en nube)")
            return user.copy(rolId = 3)
        }
        
        // Otros roles se mantienen
        Log.d(tag, "➡️ Usuario ${user.email} mantiene rol: ${user.rolId}")
        return user
    }
    
    /**
     * Mapea el rol del usuario de nube a local (inverso)
     * Regla: Todos los Médicos de la nube (rolId = 3) se convierten en Admin (rolId = 1) en la tablet
     * Los pacientes (rolId = 2) se mantienen como pacientes
     */
    private fun mapRoleFromCloud(user: UserEntity): UserEntity {
        // REGLA: Todos los Médicos de la nube (rolId = 3) se convierten en Admin (rolId = 1) en la tablet
        if (user.rolId == 3) {
            Log.d(tag, "🔄 Usuario ${user.email}: ${user.rolId} (nube) -> 1 (Admin en tablet)")
            return user.copy(rolId = 1)
        }
        
        // Otros roles se mantienen
        Log.d(tag, "➡️ Usuario ${user.email} mantiene rol: ${user.rolId}")
        return user
    }

    override suspend fun forceSyncFromServer(): Result<AuthRepository.SyncResult> = withContext(Dispatchers.IO) {
        // DESHABILITADO: Sincronización forzada deshabilitada para debugging
        Log.d(tag, "⚠️ forceSyncFromServer DESHABILITADO para debugging")
        Result.success(AuthRepository.SyncResult(true, 0, 0, 0))
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

    override suspend fun getAuthStats(): AuthRepository.AuthStats = withContext(Dispatchers.IO) {
        try {
            val stats = userDao.getUserActivityStats()
            val hasActiveSession = hasValidSession()
            val unsyncedUsers = getUnsyncedUsers()
            
            AuthRepository.AuthStats(
                totalUsers = stats.total_users,
                activeUsers = stats.active_sessions,
                syncedUsers = stats.synchronized_users,
                unsyncedUsers = unsyncedUsers.size,
                todayLogins = stats.logged_today,
                hasActiveSession = hasActiveSession
            )
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo estadísticas de auth", e)
            AuthRepository.AuthStats(0, 0, 0, 0, 0, false)
        }
    }

    override suspend fun performMaintenance(): Unit = withContext(Dispatchers.IO) {
        try {
            userDao.cleanupExpiredTokens()
            userDao.cleanupOldDeletedUsers()
            Log.d(tag, "✅ Mantenimiento de auth completado")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en mantenimiento de auth", e)
        }
    }

    override suspend fun isTokenValid(token: String): Boolean {
        return try {
            val tokenInfo = decodeToken(token)
            tokenInfo?.isExpired == false
        } catch (e: Exception) {
            Log.e(tag, "Error validando token", e)
            false
        }
    }

    override suspend fun decodeToken(token: String): AuthRepository.TokenInfo? {
        println("🔍 INICIO decodeToken - Token recibido: $token")
        
        try {
            println("🔍 Decodificando token JWT...")
            println("🔍 Token completo: $token")
            
            // Decodificar JWT básico (sin verificación de firma)
            val parts = token.split(".")
            println("🔍 Partes del token: ${parts.size}")
            
            if (parts.size != 3) {
                println("❌ Token no tiene 3 partes")
                return null
            }
            
            println("🔍 Decodificando payload...")
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            println("🔍 Payload decodificado: $payload")
            
            println("🔍 Parseando JSON...")
            val json = JSONObject(payload)
            println("🔍 JSON parseado correctamente")
            
            val userId = json.optString("userId", "")
            val email = json.optString("email", "")
            val rolId = json.optInt("rolId", 1)
            val iat = json.optLong("iat", 0)
            val exp = json.optLong("exp", 0)
            
            println("🔍 Valores extraídos:")
            println("  - userId: '$userId'")
            println("  - email: '$email'")
            println("  - rolId: $rolId")
            println("  - iat: $iat")
            println("  - exp: $exp")
            
            println("🔍 Creando TokenInfo...")
            val tokenInfo = AuthRepository.TokenInfo(
                userId = userId,
                email = email,
                rolId = rolId,
                issuedAt = iat,
                expiresAt = exp
            )
            
            println("🔍 TokenInfo creado: $tokenInfo")
            println("🔍 FIN decodeToken - Devolviendo: $tokenInfo")
            return tokenInfo
        } catch (e: Exception) {
            println("❌ Error decodificando token: ${e.message}")
            println("❌ Stack trace:")
            e.printStackTrace()
            println("🔍 FIN decodeToken - Devolviendo null por error")
            return null
        }
    }

    // ========== MÉTODOS PRIVADOS ==========

    private fun mapNetworkException(exception: Throwable): AuthRepository.AuthError {
        return when (exception) {
            is java.net.UnknownHostException, 
            is java.net.ConnectException,
            is java.net.SocketTimeoutException -> AuthRepository.AuthError.NetworkError
            is retrofit2.HttpException -> {
                when (exception.code()) {
                    401 -> AuthRepository.AuthError.InvalidCredentials
                    404 -> AuthRepository.AuthError.UserNotFound
                    409 -> AuthRepository.AuthError.UserAlreadyExists
                    422 -> AuthRepository.AuthError.ValidationError
                    else -> AuthRepository.AuthError.ServerError(exception.code(), exception.message())
                }
            }
            else -> AuthRepository.AuthError.UnknownError(exception)
        }
    }
}

