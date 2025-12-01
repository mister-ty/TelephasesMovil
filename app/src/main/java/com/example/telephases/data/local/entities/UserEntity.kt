package com.example.telephases.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

/**
 * Entidad Room para representar un usuario administrador en la base de datos local
 * Corresponde a la tabla 'usuario' con rol_id = 1 en PostgreSQL
 */
@Entity(
    tableName = "users",
    indices = [
        Index(value = ["username"], unique = true),
        Index(value = ["email"], unique = true),
        Index(value = ["numero_documento"], unique = true),
        Index(value = ["entidad_salud_id"]) // Alinear con migración 7->8
    ]
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String, // UUID string compatible con PostgreSQL

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password_hash")
    val passwordHash: String? = null, // Solo para cache local, no sincronizar

    @ColumnInfo(name = "primer_nombre")
    val primerNombre: String,

    @ColumnInfo(name = "segundo_nombre")
    val segundoNombre: String? = null,

    @ColumnInfo(name = "primer_apellido")
    val primerApellido: String,

    @ColumnInfo(name = "segundo_apellido")
    val segundoApellido: String? = null,

    @ColumnInfo(name = "tipo_documento_id")
    val tipoDocumentoId: Int,

    @ColumnInfo(name = "numero_documento")
    val numeroDocumento: String,

    @ColumnInfo(name = "telefono")
    val telefono: String? = null,

    @ColumnInfo(name = "direccion")
    val direccion: String? = null,

    @ColumnInfo(name = "ciudad_id")
    val ciudadId: Int? = null,

    @ColumnInfo(name = "fecha_nacimiento")
    val fechaNacimiento: String? = null, // ISO date string

    @ColumnInfo(name = "genero")
    val genero: String? = null, // M, F, O

    @ColumnInfo(name = "rol_id")
    val rolId: Int = 1, // 1 = Administrador

    @ColumnInfo(name = "entidad_salud_id")
    val entidadSaludId: Int? = null, // Relación con entidades_salud

    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: String, // ISO datetime string

    @ColumnInfo(name = "activo")
    val activo: Boolean = true,

    // Campos para autenticación local
    @ColumnInfo(name = "token_actual")
    val tokenActual: String? = null,

    @ColumnInfo(name = "fecha_expiracion_token")
    val fechaExpiracionToken: String? = null,

    @ColumnInfo(name = "ultimo_login")
    val ultimoLogin: String? = null,

    // Campos para sincronización
    @ColumnInfo(name = "sincronizado")
    val sincronizado: Boolean = false,

    @ColumnInfo(name = "fecha_ultima_sincronizacion")
    val fechaUltimaSincronizacion: String? = null,

    @ColumnInfo(name = "modificado_localmente")
    val modificadoLocalmente: Boolean = false,

    @ColumnInfo(name = "fecha_modificacion_local")
    val fechaModificacionLocal: String? = null
) {
    /**
     * Propiedades computadas
     */
    val nombreCompleto: String
        get() = "$primerNombre $primerApellido".trim()

    val isTokenValid: Boolean
        get() {
            // Si es token permanente offline, siempre válido
            if (tokenActual?.startsWith("OFFLINE_PERMANENT_") == true) return true
            if (fechaExpiracionToken == "NEVER_EXPIRES") return true
            
            // Validación normal para tokens online
            if (tokenActual == null || fechaExpiracionToken == null) return false
            return try {
                val expiration = java.time.Instant.parse(fechaExpiracionToken)
                expiration.isAfter(java.time.Instant.now())
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Convierte esta entidad para uso en autenticación
     */
    fun toAuthResponse(): com.example.telephases.network.AuthResponse {
        return com.example.telephases.network.AuthResponse(
            token = tokenActual ?: ""
        )
    }

    companion object {
        /**
         * Crea una entidad desde una respuesta de login exitoso
         */
        fun fromLoginResponse(
            token: String,
            userId: String? = null,
            email: String? = null,
            username: String? = null,
            rolId: Int = 1,
            entidadSaludId: Int? = null
        ): UserEntity {
            val now = java.time.Instant.now()
            val expiration = now.plusSeconds(2 * 60 * 60) // 2 horas por defecto
            
            return UserEntity(
                id = userId ?: java.util.UUID.randomUUID().toString(),
                username = username ?: "admin",
                email = email ?: "",
                passwordHash = null, // No almacenar contraseñas
                primerNombre = "Usuario",
                segundoNombre = null,
                primerApellido = "Admin",
                segundoApellido = null,
                tipoDocumentoId = 1,
                numeroDocumento = "0000000000",
                telefono = null,
                direccion = null,
                ciudadId = null,
                fechaNacimiento = null,
                genero = null,
                rolId = rolId,
                entidadSaludId = entidadSaludId,
                fechaRegistro = now.toString(),
                activo = true,
                tokenActual = token,
                fechaExpiracionToken = expiration.toString(),
                ultimoLogin = now.toString(),
                sincronizado = true,
                fechaUltimaSincronizacion = now.toString(),
                modificadoLocalmente = false,
                fechaModificacionLocal = null
            )
        }

        /**
         * Crea una entidad para registro offline
         */
        fun createForOfflineRegistration(
            username: String,
            email: String,
            primerNombre: String,
            primerApellido: String,
            numeroDocumento: String,
            segundoNombre: String? = null,
            segundoApellido: String? = null,
            tipoDocumentoId: Int = 1,
            telefono: String? = null,
            direccion: String? = null,
            ciudadId: Int? = null,
            fechaNacimiento: String? = null,
            genero: String? = null,
            rolId: Int = 1,
            entidadSaludId: Int? = null,
            password: String = "admin123"
        ): UserEntity {
            val now = java.time.Instant.now().toString()
            return UserEntity(
                id = java.util.UUID.randomUUID().toString(), // ID temporal
                username = username,
                email = email,
                passwordHash = UserEntity.hashPassword(password), // Almacenar hash de contraseña
                primerNombre = primerNombre,
                segundoNombre = segundoNombre,
                primerApellido = primerApellido,
                segundoApellido = segundoApellido,
                tipoDocumentoId = tipoDocumentoId,
                numeroDocumento = numeroDocumento,
                telefono = telefono,
                direccion = direccion,
                ciudadId = ciudadId,
                fechaNacimiento = fechaNacimiento,
                genero = genero,
                rolId = rolId,
                entidadSaludId = entidadSaludId,
                fechaRegistro = now,
                activo = true,
                tokenActual = null,
                fechaExpiracionToken = null,
                ultimoLogin = null,
                sincronizado = false, // Creado offline, pendiente de sincronización
                fechaUltimaSincronizacion = null,
                modificadoLocalmente = true,
                fechaModificacionLocal = now
            )
        }

        /**
         * Actualiza el token de un usuario existente
         */
        fun UserEntity.updateToken(newToken: String): UserEntity {
            val now = java.time.Instant.now()
            val expiration = now.plusSeconds(2 * 60 * 60) // 2 horas
            
            return this.copy(
                tokenActual = newToken,
                fechaExpiracionToken = expiration.toString(),
                ultimoLogin = now.toString(),
                fechaUltimaSincronizacion = now.toString()
            )
        }

        /**
         * Marca el usuario como deslogueado
         */
        fun UserEntity.logout(): UserEntity {
            return this.copy(
                tokenActual = null,
                fechaExpiracionToken = null
            )
        }
        
        /**
         * Genera un hash simple para la contraseña
         * NOTA: En producción usar BCrypt o similar
         */
        fun hashPassword(password: String): String {
            // Usar un hash más consistente que hashCode()
            return password.hashCode().toString()
        }
        
        /**
         * Genera un hash consistente para la contraseña usando SHA-256
         * Este método es más consistente que hashCode()
         */
        fun hashPasswordConsistent(password: String): String {
            val bytes = password.toByteArray()
            val digest = java.security.MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(bytes)
            return hash.joinToString("") { "%02x".format(it) }
        }
    }
    
    
    /**
     * Verifica si la contraseña proporcionada coincide con el hash almacenado
     */
    fun verifyPassword(password: String): Boolean {
        return passwordHash != null && passwordHash == UserEntity.hashPasswordConsistent(password)
    }
}


