package com.example.telephases.data.local.tools

import android.util.Log
import com.example.telephases.data.local.database.EntidadesSaludInitializer
import com.example.telephases.data.local.database.AppDatabase
import com.example.telephases.data.local.entities.EntidadSaludEntity
import com.example.telephases.data.local.entities.UserEntity

/**
 * Herramienta de recuperación para re-generar entidades y usuarios asociados
 * Úsese solo en escenarios de recuperación. Mantener DISABLED en producción.
 */
object RecoverySeeder {
    private const val tag = "RecoverySeeder"

    // Cambiar a true SOLO cuando necesites ejecutar la recuperación manual
    const val ENABLED: Boolean = false

    /**
     * Ejecuta la recuperación: asegura 49 entidades y un usuario por entidad
     * con username/email contacto@... y contraseña admin123 (hash correcto).
     */
    suspend fun run(database: AppDatabase) {
        if (!ENABLED) {
            Log.d(tag, "⏭️ RecoverySeeder desactivado (ENABLED=false)")
            return
        }

        val entidadDao = database.entidadSaludDao()
        val userDao = database.userDao()

        Log.d(tag, "🛟 Iniciando RecoverySeeder...")

        // 1) Asegurar entidades del catálogo
        val catalogo = EntidadesSaludInitializer.getEntidadesSaludColombia()
        catalogo.forEach { entidad ->
            try {
                // Insert IGNORE para evitar duplicados
                val id = entidadDao.insert(entidad).toInt()
                if (id > 0) {
                    Log.d(tag, "✅ Entidad creada: ${entidad.nombreEntidad} (ID: $id)")
                }
            } catch (e: Exception) {
                Log.w(tag, "ℹ️ Entidad ya existente o error controlado: ${entidad.nombreEntidad} -> ${e.message}")
            }
        }

        // 2) Leer todas las entidades activas con sus IDs reales
        val entidades = entidadDao.getAllActiveSync()
        Log.d(tag, "🔍 Entidades activas: ${entidades.size}")

        // 3) Para cada entidad, crear o normalizar el usuario admin de la entidad
        var usuariosCreados = 0
        var usuariosActualizados = 0
        entidades.forEach { entidad ->
            val email = entidad.contactoPrincipalEmail
                ?: "admin@${entidad.nombreEntidad.lowercase().replace(" ", "").replace(".", "").replace(",", "").replace("(", "").replace(")", "")}.com"
            val username = email

            // Buscar usuario por entidad ID
            val existente = userDao.getUserByEntidadSaludId(entidad.id)
            if (existente == null) {
                // Crear uno nuevo estándar
                val nuevo = createUserForEntidad(entidad, username, email)
                userDao.insert(nuevo)
                usuariosCreados++
                Log.d(tag, "👤 Usuario creado: ${nuevo.username} (Entidad ${entidad.id})")
            } else {
                // Normalizar username/email si difieren
                if (existente.username != username || existente.email != email) {
                    val normalizado = existente.copy(
                        username = username,
                        email = email
                    )
                    userDao.updateUser(normalizado)
                    usuariosActualizados++
                    Log.d(tag, "✏️ Usuario normalizado: ${normalizado.username} (Entidad ${entidad.id})")
                }
                // Garantizar entidad_salud_id seteado
                if (existente.entidadSaludId == null || existente.entidadSaludId == 0) {
                    userDao.updateUserEntidadSaludId(existente.id, entidad.id)
                    usuariosActualizados++
                    Log.d(tag, "🔗 entidadSaludId fijado para ${existente.username} -> ${entidad.id}")
                }
            }
        }

        Log.d(tag, "✅ RecoverySeeder completado. Creados=$usuariosCreados, Actualizados=$usuariosActualizados")
    }

    private fun createUserForEntidad(
        entidad: EntidadSaludEntity,
        username: String,
        email: String
    ): UserEntity {
        val nombreLimpio = entidad.nombreEntidad.uppercase()
        val adminNombre = nombreLimpio.split(" ").firstOrNull() ?: "ADMIN"
        val numeroDocumento = (entidad.nit ?: "9000000000").filter { it.isDigit() }.padEnd(10, '0')

        return UserEntity.createForOfflineRegistration(
            username = username,
            email = email,
            primerNombre = adminNombre,
            primerApellido = "ADMIN",
            numeroDocumento = numeroDocumento,
            telefono = entidad.contactoPrincipalTelefono,
            direccion = "Sede Principal",
            rolId = 1,
            entidadSaludId = entidad.id,
            password = "admin123"
        )
    }
}


