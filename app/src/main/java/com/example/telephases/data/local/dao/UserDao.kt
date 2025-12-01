package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de usuarios administradores en la base de datos local
 */
@Dao
interface UserDao {

    // ========== OPERACIONES CRUD BÁSICAS ==========

    /**
     * Inserta un nuevo usuario
     */
    // IMPORTANTE: No usar REPLACE para no disparar ON DELETE en FKs referenciadas (maletas)
    @Upsert
    suspend fun upsertUser(user: UserEntity): Long

    /**
     * Inserta múltiples usuarios
     */
    @Upsert
    suspend fun upsertUsers(users: List<UserEntity>)

    /**
     * Actualiza un usuario existente
     */
    @Update
    suspend fun updateUser(user: UserEntity)

    /**
     * Elimina un usuario (soft delete)
     */
    @Query("UPDATE users SET activo = 0, modificado_localmente = 1, fecha_modificacion_local = :timestamp WHERE id = :userId")
    suspend fun deleteUser(userId: String, timestamp: String = java.time.Instant.now().toString())

    /**
     * Elimina un usuario completamente (hard delete)
     */
    @Delete
    suspend fun deleteUserPermanently(user: UserEntity)

    // ========== CONSULTAS DE AUTENTICACIÓN ==========

    /**
     * Obtiene un usuario por username
     */
    @Query("SELECT * FROM users WHERE username = :username AND activo = 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    /**
     * Obtiene un usuario por email
     */
    @Query("SELECT * FROM users WHERE email = :email AND activo = 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * Obtiene un usuario por username o email
     */
    @Query("SELECT * FROM users WHERE (username = :credential OR email = :credential) AND activo = 1")
    suspend fun getUserByCredential(credential: String): UserEntity?

    /**
     * Obtiene un usuario por ID
     */
    @Query("SELECT * FROM users WHERE id = :userId AND activo = 1")
    suspend fun getUserById(userId: String): UserEntity?

    /**
     * Obtiene el usuario actualmente logueado (con token válido)
     * Prioriza el usuario con el último login más reciente
     */
    @Query("""
        SELECT * FROM users 
        WHERE token_actual IS NOT NULL 
        AND token_actual != ''
        AND (fecha_expiracion_token = 'NEVER_EXPIRES' OR fecha_expiracion_token IS NULL OR datetime(fecha_expiracion_token) > datetime('now'))
        AND activo = 1
        ORDER BY ultimo_login DESC, fecha_registro DESC
        LIMIT 1
    """)
    suspend fun getCurrentLoggedUser(): UserEntity?

    /**
     * [DEBUG] Obtiene TODOS los usuarios (incluyendo inactivos)
     */
    @Query("SELECT * FROM users ORDER BY ultimo_login DESC")
    suspend fun getAllUsersDebug(): List<UserEntity>

    /**
     * Verifica si existe un usuario con el username dado
     */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username AND activo = 1)")
    suspend fun existsUserWithUsername(username: String): Boolean

    /**
     * Verifica si existe un usuario con el email dado
     */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email AND activo = 1)")
    suspend fun existsUserWithEmail(email: String): Boolean

    /**
     * Verifica si existe un usuario con el documento dado
     */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE numero_documento = :numeroDocumento AND activo = 1)")
    suspend fun existsUserWithDocument(numeroDocumento: String): Boolean

    // ========== GESTIÓN DE TOKENS Y SESIONES ==========

    /**
     * Actualiza el token de un usuario
     */
    @Query("""
        UPDATE users 
        SET token_actual = :token,
            fecha_expiracion_token = :expiration,
            ultimo_login = :loginTime
        WHERE id = :userId
    """)
    suspend fun updateUserToken(
        userId: String, 
        token: String, 
        expiration: String,
        loginTime: String = java.time.Instant.now().toString()
    )

    @Query("UPDATE users SET entidad_salud_id = :entidadId WHERE id = :userId")
    suspend fun updateUserEntidad(userId: String, entidadId: Int?)

    /**
     * Actualiza el entidadSaludId de un usuario
     */
    @Query("UPDATE users SET entidad_salud_id = :entidadId WHERE id = :userId")
    suspend fun updateUserEntidadSaludId(userId: String, entidadId: Int)

    /**
     * Obtiene un usuario por entidadSaludId
     */
    @Query("SELECT * FROM users WHERE entidad_salud_id = :entidadId AND activo = 1")
    suspend fun getUserByEntidadSaludId(entidadId: Int): UserEntity?

    /**
     * Obtiene todos los usuarios (método síncrono para inicialización)
     */
    @Query("SELECT * FROM users WHERE activo = 1")
    suspend fun getAllUsersSync(): List<UserEntity>

    /**
     * Actualiza la contraseña de un usuario
     */
    @Query("UPDATE users SET password_hash = :passwordHash WHERE id = :userId")
    suspend fun updateUserPassword(userId: String, passwordHash: String)

    /**
     * Actualiza datos de sesión de un usuario
     */
    @Query("""
        UPDATE users 
        SET sincronizado = :sincronizado,
            fecha_ultima_sincronizacion = :fechaUltimaSincronizacion,
            modificado_localmente = :modificadoLocalmente,
            fecha_modificacion_local = :fechaModificacionLocal
        WHERE id = :userId
    """)
    suspend fun updateUserSessionData(
        userId: String,
        sincronizado: Boolean,
        fechaUltimaSincronizacion: String?,
        modificadoLocalmente: Boolean,
        fechaModificacionLocal: String?
    )

    /**
     * Invalida el token de un usuario (logout)
     */
    @Query("""
        UPDATE users 
        SET token_actual = NULL,
            fecha_expiracion_token = NULL
        WHERE id = :userId
    """)
    suspend fun invalidateUserToken(userId: String)

    /**
     * Invalida todos los tokens (logout global)
     */
    @Query("""
        UPDATE users 
        SET token_actual = NULL,
            fecha_expiracion_token = NULL
        WHERE token_actual IS NOT NULL
    """)
    suspend fun invalidateAllTokens()

    /**
     * Invalida todos los tokens EXCEPTO el del usuario especificado
     */
    @Query("""
        UPDATE users 
        SET token_actual = NULL,
            fecha_expiracion_token = NULL
        WHERE token_actual IS NOT NULL 
        AND id != :excludeUserId
    """)
    suspend fun invalidateAllTokensExcept(excludeUserId: String)

    /**
     * Limpia tokens expirados
     */
    @Query("""
        UPDATE users 
        SET token_actual = NULL,
            fecha_expiracion_token = NULL
        WHERE datetime(fecha_expiracion_token) <= datetime('now')
    """)
    suspend fun cleanupExpiredTokens()

    /**
     * Obtiene usuarios con tokens válidos
     */
    @Query("""
        SELECT * FROM users 
        WHERE token_actual IS NOT NULL 
        AND (fecha_expiracion_token = 'NEVER_EXPIRES' OR datetime(fecha_expiracion_token) > datetime('now'))
        AND activo = 1
    """)
    suspend fun getUsersWithValidTokens(): List<UserEntity>

    // ========== CONSULTAS DE BÚSQUEDA ==========

    /**
     * Obtiene todos los usuarios activos
     */
    @Query("SELECT * FROM users WHERE activo = 1 ORDER BY fecha_registro DESC")
    suspend fun getAllUsers(): List<UserEntity>

    /**
     * Obtiene todos los usuarios como Flow
     */
    @Query("SELECT * FROM users WHERE activo = 1 ORDER BY fecha_registro DESC")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    /**
     * Busca usuarios por nombre
     */
    @Query("""
        SELECT * FROM users 
        WHERE activo = 1 
        AND (primer_nombre LIKE '%' || :searchTerm || '%' 
             OR primer_apellido LIKE '%' || :searchTerm || '%'
             OR segundo_nombre LIKE '%' || :searchTerm || '%'
             OR segundo_apellido LIKE '%' || :searchTerm || '%'
             OR username LIKE '%' || :searchTerm || '%')
        ORDER BY primer_nombre ASC, primer_apellido ASC
    """)
    suspend fun searchUsersByName(searchTerm: String): List<UserEntity>

    /**
     * Obtiene usuarios por rol
     */
    @Query("SELECT * FROM users WHERE rol_id = :rolId AND activo = 1 ORDER BY fecha_registro DESC")
    suspend fun getUsersByRole(rolId: Int): List<UserEntity>

    /**
     * Obtiene usuarios administradores
     */
    @Query("SELECT * FROM users WHERE rol_id = 1 AND activo = 1 ORDER BY fecha_registro DESC")
    suspend fun getAdminUsers(): List<UserEntity>

    /**
     * Obtiene usuarios recientes
     */
    @Query("SELECT * FROM users WHERE activo = 1 ORDER BY fecha_registro DESC LIMIT :limit")
    suspend fun getRecentUsers(limit: Int = 10): List<UserEntity>

    // ========== CONSULTAS DE SINCRONIZACIÓN ==========

    /**
     * Obtiene usuarios que necesitan sincronización
     */
    @Query("SELECT * FROM users WHERE sincronizado = 0 AND activo = 1")
    suspend fun getUnsyncedUsers(): List<UserEntity>

    /**
     * Obtiene usuarios modificados localmente
     */
    @Query("SELECT * FROM users WHERE modificado_localmente = 1 AND activo = 1")
    suspend fun getLocallyModifiedUsers(): List<UserEntity>

    /**
     * Marca un usuario como sincronizado
     */
    @Query("""
        UPDATE users 
        SET sincronizado = 1, 
            fecha_ultima_sincronizacion = :timestamp,
            modificado_localmente = 0,
            fecha_modificacion_local = NULL
        WHERE id = :userId
    """)
    suspend fun markUserAsSynced(userId: String, timestamp: String = java.time.Instant.now().toString())

    /**
     * Marca múltiples usuarios como sincronizados
     */
    @Query("""
        UPDATE users 
        SET sincronizado = 1, 
            fecha_ultima_sincronizacion = :timestamp,
            modificado_localmente = 0,
            fecha_modificacion_local = NULL
        WHERE id IN (:userIds)
    """)
    suspend fun markUsersAsSynced(userIds: List<String>, timestamp: String = java.time.Instant.now().toString())

    /**
     * Actualiza el ID del servidor para un usuario creado offline
     */
    @Query("UPDATE users SET id = :newId WHERE id = :oldId")
    suspend fun updateUserServerId(oldId: String, newId: String)

    // ========== CONSULTAS DE ESTADÍSTICAS ==========

    /**
     * Cuenta total de usuarios activos
     */
    @Query("SELECT COUNT(*) FROM users WHERE activo = 1")
    suspend fun getTotalUsersCount(): Int

    /**
     * Cuenta usuarios por rol
     */
    @Query("SELECT COUNT(*) FROM users WHERE rol_id = :rolId AND activo = 1")
    suspend fun getUsersCountByRole(rolId: Int): Int

    /**
     * Cuenta usuarios administradores
     */
    @Query("SELECT COUNT(*) FROM users WHERE rol_id = 1 AND activo = 1")
    suspend fun getAdminUsersCount(): Int

    /**
     * Cuenta usuarios no sincronizados
     */
    @Query("SELECT COUNT(*) FROM users WHERE sincronizado = 0 AND activo = 1")
    suspend fun getUnsyncedUsersCount(): Int

    /**
     * Cuenta usuarios registrados hoy
     */
    @Query("""
        SELECT COUNT(*) FROM users 
        WHERE activo = 1 
        AND date(fecha_registro) = date('now', 'localtime')
    """)
    suspend fun getTodayUsersCount(): Int

    /**
     * Cuenta usuarios logueados actualmente
     */
    @Query("""
        SELECT COUNT(*) FROM users 
        WHERE token_actual IS NOT NULL 
        AND (fecha_expiracion_token = 'NEVER_EXPIRES' OR datetime(fecha_expiracion_token) > datetime('now'))
        AND activo = 1
    """)
    suspend fun getActiveSessionsCount(): Int

    /**
     * Obtiene el último usuario registrado
     */
    @Query("SELECT * FROM users WHERE activo = 1 ORDER BY fecha_registro DESC LIMIT 1")
    suspend fun getLastRegisteredUser(): UserEntity?

    // ========== CONSULTAS AVANZADAS ==========

    /**
     * Busca usuarios con filtros múltiples
     */
    @Query("""
        SELECT * FROM users 
        WHERE activo = 1
        AND (:searchTerm IS NULL OR 
             primer_nombre LIKE '%' || :searchTerm || '%' OR 
             primer_apellido LIKE '%' || :searchTerm || '%' OR
             username LIKE '%' || :searchTerm || '%' OR
             email LIKE '%' || :searchTerm || '%')
        AND (:rolId IS NULL OR rol_id = :rolId)
        AND (:genero IS NULL OR genero = :genero)
        ORDER BY fecha_registro DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun searchUsersWithFilters(
        searchTerm: String? = null,
        rolId: Int? = null,
        genero: String? = null,
        limit: Int = 20,
        offset: Int = 0
    ): List<UserEntity>

    /**
     * Obtiene estadísticas de actividad de usuarios
     */
    @Query("""
        SELECT 
            COUNT(*) as total_users,
            SUM(CASE WHEN token_actual IS NOT NULL AND (fecha_expiracion_token = 'NEVER_EXPIRES' OR datetime(fecha_expiracion_token) > datetime('now')) THEN 1 ELSE 0 END) as active_sessions,
            SUM(CASE WHEN date(ultimo_login) = date('now', 'localtime') THEN 1 ELSE 0 END) as logged_today,
            SUM(CASE WHEN sincronizado = 1 THEN 1 ELSE 0 END) as synchronized_users
        FROM users 
        WHERE activo = 1
    """)
    suspend fun getUserActivityStats(): UserActivityStats

    /**
     * Obtiene estadísticas de sincronización para usuarios
     */
    @Query("""
        SELECT 
            COUNT(*) as total,
            SUM(CASE WHEN sincronizado = 1 THEN 1 ELSE 0 END) as sincronizados,
            SUM(CASE WHEN modificado_localmente = 1 THEN 1 ELSE 0 END) as modificados_localmente
        FROM users 
        WHERE activo = 1
    """)
    suspend fun getSyncStats(): SyncStats

    // ========== OPERACIONES DE MANTENIMIENTO ==========

    /**
     * Limpia usuarios eliminados hace más de X días
     */
    @Query("""
        DELETE FROM users 
        WHERE activo = 0 
        AND datetime(fecha_modificacion_local) < datetime('now', '-30 days')
    """)
    suspend fun cleanupOldDeletedUsers()

    /**
     * Obtiene usuarios duplicados por email
     */
    @Query("""
        SELECT * FROM users u1
        WHERE EXISTS (
            SELECT 1 FROM users u2 
            WHERE u2.email = u1.email 
            AND u2.id != u1.id 
            AND u2.activo = 1
        ) AND u1.activo = 1
        ORDER BY u1.fecha_registro DESC
    """)
    suspend fun getDuplicateUsersByEmail(): List<UserEntity>

    /**
     * Obtiene usuarios duplicados por username
     */
    @Query("""
        SELECT * FROM users u1
        WHERE EXISTS (
            SELECT 1 FROM users u2 
            WHERE u2.username = u1.username 
            AND u2.id != u1.id 
            AND u2.activo = 1
        ) AND u1.activo = 1
        ORDER BY u1.fecha_registro DESC
    """)
    suspend fun getDuplicateUsersByUsername(): List<UserEntity>

    /**
     * Actualiza la fecha de último login
     */
    @Query("UPDATE users SET ultimo_login = :timestamp WHERE id = :userId")
    suspend fun updateLastLogin(userId: String, timestamp: String = java.time.Instant.now().toString())

    /**
     * Cuenta usuarios asociados a entidades de salud
     */
    @Query("SELECT COUNT(*) FROM users WHERE entidad_salud_id IS NOT NULL AND activo = 1")
    suspend fun getUsersCountForEntidades(): Int

    /**
     * Inserta un usuario (método simple para inicialización)
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserEntity): Long

    /**
     * Elimina todos los usuarios asociados a entidades de salud
     */
    @Query("DELETE FROM users WHERE entidad_salud_id IS NOT NULL")
    suspend fun deleteAllUsersForEntidades()


    // ========== CLASES AUXILIARES ==========

    /**
     * Estadísticas de actividad de usuarios
     */
    data class UserActivityStats(
        val total_users: Int,
        val active_sessions: Int,
        val logged_today: Int,
        val synchronized_users: Int
    ) {
        val inactive_users: Int get() = total_users - active_sessions
        val unsynchronized_users: Int get() = total_users - synchronized_users
    }

    /**
     * Estadísticas de sincronización
     */
    data class SyncStats(
        val total: Int,
        val sincronizados: Int,
        val modificados_localmente: Int
    ) {
        val pendientes: Int get() = total - sincronizados
        val porcentajeSincronizado: Float get() = if (total > 0) (sincronizados.toFloat() / total) * 100 else 0f
    }
}


