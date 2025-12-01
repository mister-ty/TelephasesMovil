package com.example.telephases.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.telephases.data.local.dao.MaletaDao
import com.example.telephases.data.local.dao.EntidadSaludDao
import com.example.telephases.data.local.entities.MaletaEntity
import com.example.telephases.data.local.entities.EntidadSaludEntity
import com.example.telephases.network.ApiService
import com.example.telephases.network.Maleta
import com.example.telephases.network.EntidadSalud
import com.example.telephases.network.MaletaCreateRequest
import com.example.telephases.network.EntidadSaludCreateRequest
import com.example.telephases.network.MaletaAsociacionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaletaRepositoryImpl @Inject constructor(
    private val maletaDao: MaletaDao,
    private val entidadSaludDao: EntidadSaludDao,
    private val authRepository: AuthRepository,
    private val context: Context
) : MaletaRepository {

    private val tag = "MaletaRepository"
    private val apiService = ApiService.create()

    // ========== OPERACIONES LOCALES ==========

    override suspend fun getAllMaletas(): List<MaletaEntity> = withContext(Dispatchers.IO) {
        try {
            maletaDao.getAllActiveSync()
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo maletas locales", e)
            emptyList()
        }
    }

    override suspend fun getMaletaById(maletaId: String): MaletaEntity? = withContext(Dispatchers.IO) {
        try {
            maletaDao.getMaletaById(maletaId.toIntOrNull() ?: 0)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo maleta por ID: $maletaId", e)
            null
        }
    }

    override suspend fun getMaletaByUsuario(usuarioId: String): MaletaEntity? = withContext(Dispatchers.IO) {
        try {
            maletaDao.getMaletaByUsuarioId(usuarioId)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo maleta por usuario: $usuarioId", e)
            null
        }
    }

    override suspend fun insertMaleta(maleta: MaletaEntity): String = withContext(Dispatchers.IO) {
        try {
            val id = maletaDao.upsertMaleta(maleta)
            Log.d(tag, "✅ Maleta insertada localmente: ${maleta.nombreMaleta}")
            maleta.id.toString()
        } catch (e: Exception) {
            Log.e(tag, "❌ Error insertando maleta local", e)
            throw MaletaRepository.RepositoryError.UnknownError(e)
        }
    }

    override suspend fun updateMaleta(maleta: MaletaEntity): Unit = withContext(Dispatchers.IO) {
        try {
            maletaDao.updateMaleta(maleta)
            Log.d(tag, "✅ Maleta actualizada localmente: ${maleta.nombreMaleta}")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error actualizando maleta local", e)
            throw MaletaRepository.RepositoryError.UnknownError(e)
        }
    }

    override suspend fun deleteMaleta(maletaId: String): Unit = withContext(Dispatchers.IO) {
        try {
            maletaDao.softDelete(maletaId.toIntOrNull() ?: 0)
            Log.d(tag, "✅ Maleta eliminada localmente: $maletaId")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error eliminando maleta local", e)
            throw MaletaRepository.RepositoryError.UnknownError(e)
        }
    }

    override suspend fun getAllEntidadesSalud(): List<EntidadSaludEntity> = withContext(Dispatchers.IO) {
        try {
            entidadSaludDao.getAllActiveSync()
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo entidades de salud locales", e)
            emptyList()
        }
    }

    override suspend fun getEntidadSaludById(entidadId: Int): EntidadSaludEntity? = withContext(Dispatchers.IO) {
        try {
            entidadSaludDao.getById(entidadId)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo entidad de salud por ID: $entidadId", e)
            null
        }
    }

    override suspend fun getEntidadSaludByName(nombre: String): EntidadSaludEntity? = withContext(Dispatchers.IO) {
        try {
            // Buscar por nombre en la lista de entidades activas
            entidadSaludDao.getAllActiveSync().find { it.nombreEntidad.equals(nombre, ignoreCase = true) }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo entidad de salud por nombre: $nombre", e)
            null
        }
    }

    override suspend fun insertEntidadSalud(entidad: EntidadSaludEntity): Int = withContext(Dispatchers.IO) {
        try {
            val id = entidadSaludDao.insert(entidad)
            Log.d(tag, "✅ Entidad de salud insertada localmente: ${entidad.nombreEntidad}")
            entidad.id
        } catch (e: Exception) {
            Log.e(tag, "❌ Error insertando entidad de salud local", e)
            throw MaletaRepository.RepositoryError.UnknownError(e)
        }
    }

    override suspend fun updateEntidadSalud(entidad: EntidadSaludEntity): Unit = withContext(Dispatchers.IO) {
        try {
            entidadSaludDao.update(entidad)
            Log.d(tag, "✅ Entidad de salud actualizada localmente: ${entidad.nombreEntidad}")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error actualizando entidad de salud local", e)
            throw MaletaRepository.RepositoryError.UnknownError(e)
        }
    }

    override suspend fun deleteEntidadSalud(entidadId: Int): Unit = withContext(Dispatchers.IO) {
        try {
            entidadSaludDao.softDelete(entidadId)
            Log.d(tag, "✅ Entidad de salud eliminada localmente: $entidadId")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error eliminando entidad de salud local", e)
            throw MaletaRepository.RepositoryError.UnknownError(e)
        }
    }

    // ========== OPERACIONES REMOTAS ==========

    override suspend fun getAllMaletasRemote(token: String): Result<List<Maleta>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllMaletas("Bearer $token")
            Log.d(tag, "✅ ${response.maletas.size} maletas obtenidas del servidor")
            Result.success(response.maletas)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo maletas del servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun createMaletaRemote(token: String, request: MaletaCreateRequest): Result<Maleta> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createMaleta("Bearer $token", request)
            Log.d(tag, "✅ Maleta creada en servidor: ${response.maleta.nombre}")
            Result.success(response.maleta)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error creando maleta en servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun asociarMaletaUsuarioRemote(token: String, maletaId: String, request: MaletaAsociacionRequest): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.asociarMaletaUsuario("Bearer $token", maletaId, request)
            Log.d(tag, "✅ Maleta asociada con usuario: $maletaId")
            Result.success(response.success)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error asociando maleta con usuario", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun getAllEntidadesSaludRemote(token: String): Result<List<EntidadSalud>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllEntidadesSalud("Bearer $token")
            Log.d(tag, "✅ ${response.entidades.size} entidades de salud obtenidas del servidor")
            Result.success(response.entidades)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo entidades de salud del servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    override suspend fun createEntidadSaludRemote(token: String, request: EntidadSaludCreateRequest): Result<EntidadSalud> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createEntidadSalud("Bearer $token", request)
            Log.d(tag, "✅ Entidad de salud creada en servidor: ${response.entidad.nombre}")
            Result.success(response.entidad)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error creando entidad de salud en servidor", e)
            Result.failure(mapNetworkException(e))
        }
    }

    // ========== OPERACIONES UNIFICADAS (OFFLINE-FIRST) ==========

    override suspend fun syncMaletas(token: String): Result<MaletaRepository.SyncResult> = withContext(Dispatchers.IO) {
        try {
            val remoteResult = getAllMaletasRemote(token)
            if (remoteResult.isSuccess) {
                val serverMaletas = remoteResult.getOrThrow()
                val syncedCount = syncMaletasFromServer(serverMaletas)
                
                val syncResult = MaletaRepository.SyncResult(
                    success = true,
                    syncedCount = syncedCount,
                    failedCount = 0,
                    totalCount = serverMaletas.size
                )
                
                Result.success(syncResult)
            } else {
                Result.failure(remoteResult.exceptionOrNull() ?: MaletaRepository.RepositoryError.NetworkError)
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización de maletas", e)
            Result.failure(MaletaRepository.RepositoryError.UnknownError(e))
        }
    }

    override suspend fun syncEntidadesSalud(token: String): Result<MaletaRepository.SyncResult> = withContext(Dispatchers.IO) {
        try {
            val remoteResult = getAllEntidadesSaludRemote(token)
            if (remoteResult.isSuccess) {
                val serverEntidades = remoteResult.getOrThrow()
                val syncedCount = syncEntidadesSaludFromServer(serverEntidades)
                
                val syncResult = MaletaRepository.SyncResult(
                    success = true,
                    syncedCount = syncedCount,
                    failedCount = 0,
                    totalCount = serverEntidades.size
                )
                
                Result.success(syncResult)
            } else {
                Result.failure(remoteResult.exceptionOrNull() ?: MaletaRepository.RepositoryError.NetworkError)
            }
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización de entidades de salud", e)
            Result.failure(MaletaRepository.RepositoryError.UnknownError(e))
        }
    }

    override suspend fun createMaleta(token: String?, request: MaletaCreateRequest): Result<MaletaEntity> = withContext(Dispatchers.IO) {
        try {
            if (isNetworkAvailable() && token != null) {
                // MODO ONLINE: Crear en servidor primero
                val remoteResult = createMaletaRemote(token, request)
                if (remoteResult.isSuccess) {
                    val serverMaleta = remoteResult.getOrThrow()
                    val localMaleta = MaletaEntity.fromApiModel(serverMaleta)
                    maletaDao.upsertMaleta(localMaleta)
                    
                    Log.d(tag, "✅ Maleta creada online y guardada localmente")
                    return@withContext Result.success(localMaleta)
                } else {
                    Log.w(tag, "⚠️ Fallo creación online, creando offline")
                }
            }

            // MODO OFFLINE: Crear solo localmente
            val offlineMaleta = MaletaEntity.createForOffline(
                identificadorInvima = request.nombre, // Usar nombre como identificador
                nombreMaleta = request.nombre,
                descripcion = request.descripcion,
                entidadSaludId = request.entidad_salud_id
            )
            maletaDao.upsertMaleta(offlineMaleta)
            
            Log.d(tag, "✅ Maleta creada offline")
            Result.success(offlineMaleta)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error creando maleta", e)
            Result.failure(MaletaRepository.RepositoryError.UnknownError(e))
        }
    }

    override suspend fun createEntidadSalud(token: String?, request: EntidadSaludCreateRequest): Result<EntidadSaludEntity> = withContext(Dispatchers.IO) {
        try {
            if (isNetworkAvailable() && token != null) {
                // MODO ONLINE: Crear en servidor primero
                val remoteResult = createEntidadSaludRemote(token, request)
                if (remoteResult.isSuccess) {
                    val serverEntidad = remoteResult.getOrThrow()
                    val localEntidad = EntidadSaludEntity.fromApiModel(serverEntidad)
                    entidadSaludDao.insert(localEntidad)
                    
                    Log.d(tag, "✅ Entidad de salud creada online y guardada localmente")
                    return@withContext Result.success(localEntidad)
                } else {
                    Log.w(tag, "⚠️ Fallo creación online, creando offline")
                }
            }

            // MODO OFFLINE: Crear solo localmente
            val offlineEntidad = EntidadSaludEntity.createForOffline(
                nombre = request.nombre,
                codigo = request.codigo,
                direccion = request.direccion,
                telefono = request.telefono,
                email = request.email
            )
            entidadSaludDao.insert(offlineEntidad)
            
            Log.d(tag, "✅ Entidad de salud creada offline")
            Result.success(offlineEntidad)
        } catch (e: Exception) {
            Log.e(tag, "❌ Error creando entidad de salud", e)
            Result.failure(MaletaRepository.RepositoryError.UnknownError(e))
        }
    }

    // ========== UTILIDADES ==========

    override suspend fun getEntidadSaludName(entidadId: Int?): String = withContext(Dispatchers.IO) {
        if (entidadId == null) return@withContext "No especificada"
        
        try {
            val entidad = entidadSaludDao.getById(entidadId)
            entidad?.nombreEntidad ?: "Entidad ID: $entidadId"
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo nombre de entidad: $entidadId", e)
            "Error obteniendo entidad"
        }
    }

    override suspend fun performMaintenance(): Unit = withContext(Dispatchers.IO) {
        try {
            // Limpiar maletas huérfanas (sin usuario asociado)
            val maletasHuerfanas = maletaDao.getAllActiveSync().filter { it.asignadaAUsuarioId.isNullOrBlank() }
            maletasHuerfanas.forEach { maleta ->
                Log.d(tag, "🧹 Limpiando maleta huérfana: ${maleta.nombreMaleta}")
                maletaDao.softDelete(maleta.id)
            }
            
            Log.d(tag, "✅ Mantenimiento de maletas completado")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en mantenimiento de maletas", e)
        }
    }

    // ========== MÉTODOS PRIVADOS ==========

    private suspend fun syncMaletasFromServer(serverMaletas: List<Maleta>): Int {
        var syncedCount = 0
        try {
            Log.d(tag, "🔄 Iniciando sincronización de ${serverMaletas.size} maletas desde servidor...")
            
            for (serverMaleta in serverMaletas) {
                try {
                    val existingMaleta = maletaDao.getMaletaById(serverMaleta.id.toIntOrNull() ?: 0)
                    
                    if (existingMaleta != null) {
                        // Actualizar maleta existente
                        val updatedMaleta = MaletaEntity.fromApiModel(serverMaleta)
                        maletaDao.updateMaleta(updatedMaleta)
                        Log.d(tag, "🔄 Maleta actualizada: ${serverMaleta.nombre}")
                    } else {
                        // Crear nueva maleta
                        val newMaleta = MaletaEntity.fromApiModel(serverMaleta)
                        maletaDao.upsertMaleta(newMaleta)
                        Log.d(tag, "🔄 Maleta creada: ${serverMaleta.nombre}")
                    }
                    
                    syncedCount++
                } catch (e: Exception) {
                    Log.e(tag, "❌ Error sincronizando maleta: ${serverMaleta.nombre}", e)
                }
            }
            
            Log.d(tag, "✅ Sincronización de maletas completada: $syncedCount/${serverMaletas.size}")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización de maletas", e)
        }
        
        return syncedCount
    }

    private suspend fun syncEntidadesSaludFromServer(serverEntidades: List<EntidadSalud>): Int {
        var syncedCount = 0
        try {
            Log.d(tag, "🔄 Iniciando sincronización de ${serverEntidades.size} entidades de salud desde servidor...")
            
            for (serverEntidad in serverEntidades) {
                try {
                    val existingEntidad = entidadSaludDao.getById(serverEntidad.id)
                    
                    if (existingEntidad != null) {
                        // Actualizar entidad existente
                        val updatedEntidad = EntidadSaludEntity.fromApiModel(serverEntidad)
                        entidadSaludDao.update(updatedEntidad)
                        Log.d(tag, "🔄 Entidad actualizada: ${serverEntidad.nombre}")
                    } else {
                        // Crear nueva entidad
                        val newEntidad = EntidadSaludEntity.fromApiModel(serverEntidad)
                        entidadSaludDao.insert(newEntidad)
                        Log.d(tag, "🔄 Entidad creada: ${serverEntidad.nombre}")
                    }
                    
                    syncedCount++
                } catch (e: Exception) {
                    Log.e(tag, "❌ Error sincronizando entidad: ${serverEntidad.nombre}", e)
                }
            }
            
            Log.d(tag, "✅ Sincronización de entidades de salud completada: $syncedCount/${serverEntidades.size}")
        } catch (e: Exception) {
            Log.e(tag, "❌ Error en sincronización de entidades de salud", e)
        }
        
        return syncedCount
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || 
               capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    private fun mapNetworkException(e: Exception): MaletaRepository.RepositoryError {
        return when {
            e.message?.contains("401") == true -> MaletaRepository.RepositoryError.NetworkError
            e.message?.contains("404") == true -> MaletaRepository.RepositoryError.NotFoundError
            e.message?.contains("409") == true -> MaletaRepository.RepositoryError.DuplicateError
            else -> MaletaRepository.RepositoryError.UnknownError(e)
        }
    }
}
