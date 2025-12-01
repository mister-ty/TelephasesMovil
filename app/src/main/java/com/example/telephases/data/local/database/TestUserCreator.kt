package com.example.telephases.data.local.database

import com.example.telephases.data.local.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant

/**
 * Creador de usuario de prueba para testing
 */
object TestUserCreator {
    
    /**
     * Crea un usuario de prueba con token válido
     */
    suspend fun createTestUser(userDao: com.example.telephases.data.local.dao.UserDao) = withContext(Dispatchers.IO) {
        try {
            val testUser = UserEntity(
                id = "test-user-001",
                username = "test@telephases.com",
                email = "test@telephases.com",
                passwordHash = UserEntity.hashPasswordConsistent("test123"),
                primerNombre = "Usuario",
                segundoNombre = null,
                primerApellido = "Prueba",
                segundoApellido = null,
                tipoDocumentoId = 1,
                numeroDocumento = "1234567890",
                telefono = "+57-1-234-5678",
                direccion = "Dirección de Prueba",
                ciudadId = null,
                fechaNacimiento = null,
                genero = null,
                rolId = 1,
                entidadSaludId = 1,
                fechaRegistro = Instant.now().toString(),
                activo = true,
                tokenActual = "TEST_TOKEN_123456789",
                fechaExpiracionToken = "NEVER_EXPIRES",
                ultimoLogin = Instant.now().toString(),
                sincronizado = true,
                fechaUltimaSincronizacion = Instant.now().toString(),
                modificadoLocalmente = false,
                fechaModificacionLocal = null
            )
            
            userDao.upsertUser(testUser)
            println("✅ Usuario de prueba creado: ${testUser.username}")
            testUser
        } catch (e: Exception) {
            println("❌ Error creando usuario de prueba: ${e.message}")
            null
        }
    }
}
