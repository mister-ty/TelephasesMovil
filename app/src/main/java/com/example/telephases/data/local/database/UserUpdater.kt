package com.example.telephases.data.local.database

import com.example.telephases.data.local.dao.UserDao
import com.example.telephases.data.local.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant

/**
 * Actualizador de usuarios existentes para convertirlos en usuarios funcionales
 */
object UserUpdater {
    
    /**
     * Actualiza todos los usuarios existentes para que tengan tokens válidos
     */
    suspend fun updateExistingUsers(userDao: UserDao) = withContext(Dispatchers.IO) {
        try {
            // Obtener todos los usuarios activos sin token
            val usersWithoutToken = userDao.getAllUsersDebug().filter { 
                it.activo && (it.tokenActual.isNullOrEmpty())
            }
            
            println("🔧 Actualizando ${usersWithoutToken.size} usuarios existentes...")
            
            var updatedCount = 0
            usersWithoutToken.forEach { user ->
                try {
                    // Generar token único para cada usuario
                    val token = "OFFLINE_TOKEN_${user.id}"
                    val expiration = "NEVER_EXPIRES"
                    val currentTime = Instant.now().toString()
                    
                    // Actualizar contraseña con hash consistente
                    val passwordHash = UserEntity.hashPasswordConsistent("admin123")
                    userDao.updateUserPassword(user.id, passwordHash)
                    
                    // Actualizar token y datos de sesión
                    userDao.updateUserToken(user.id, token, expiration, currentTime)
                    
                    // Actualizar otros campos
                    userDao.updateUserSessionData(
                        userId = user.id,
                        sincronizado = true,
                        fechaUltimaSincronizacion = currentTime,
                        modificadoLocalmente = false,
                        fechaModificacionLocal = null
                    )
                    
                    updatedCount++
                    println("✅ Usuario actualizado: ${user.username}")
                    
                } catch (e: Exception) {
                    println("❌ Error actualizando usuario ${user.username}: ${e.message}")
                }
            }
            
            println("🎉 Actualización completada: $updatedCount usuarios actualizados")
            
            // Verificar resultado
            val usersWithTokens = userDao.getUsersWithValidTokens()
            println("📊 Usuarios con tokens válidos: ${usersWithTokens.size}")
            
            updatedCount
        } catch (e: Exception) {
            println("❌ Error en actualización masiva: ${e.message}")
            0
        }
    }
    
    /**
     * Actualiza un usuario específico con token válido
     */
    suspend fun updateSpecificUser(userDao: UserDao, username: String, password: String = "admin123") = withContext(Dispatchers.IO) {
        try {
            val user = userDao.getUserByCredential(username)
            if (user != null) {
                val token = "OFFLINE_TOKEN_${user.id}"
                val expiration = "NEVER_EXPIRES"
                val currentTime = Instant.now().toString()
                
                // Actualizar contraseña con hash consistente
                val passwordHash = UserEntity.hashPasswordConsistent(password)
                userDao.updateUserPassword(user.id, passwordHash)
                
                // Actualizar token y datos de sesión
                userDao.updateUserToken(user.id, token, expiration, currentTime)
                userDao.updateUserSessionData(
                    userId = user.id,
                    sincronizado = true,
                    fechaUltimaSincronizacion = currentTime,
                    modificadoLocalmente = false,
                    fechaModificacionLocal = null
                )
                
                println("✅ Usuario específico actualizado: $username")
                true
            } else {
                println("❌ Usuario no encontrado: $username")
                false
            }
        } catch (e: Exception) {
            println("❌ Error actualizando usuario específico: ${e.message}")
            false
        }
    }
}
