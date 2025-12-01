package com.example.telephases.data.repository

import com.example.telephases.data.local.entities.MaletaEntity
import com.example.telephases.data.local.entities.EntidadSaludEntity
import com.example.telephases.network.Maleta
import com.example.telephases.network.EntidadSalud
import com.example.telephases.network.MaletaCreateRequest
import com.example.telephases.network.EntidadSaludCreateRequest
import com.example.telephases.network.MaletaAsociacionRequest

/**
 * Repository para manejo de maletas y entidades de salud
 * Incluye sincronización con la nube
 */
interface MaletaRepository {
    
    // ========== OPERACIONES LOCALES ==========
    
    suspend fun getAllMaletas(): List<MaletaEntity>
    suspend fun getMaletaById(maletaId: String): MaletaEntity?
    suspend fun getMaletaByUsuario(usuarioId: String): MaletaEntity?
    suspend fun insertMaleta(maleta: MaletaEntity): String
    suspend fun updateMaleta(maleta: MaletaEntity)
    suspend fun deleteMaleta(maletaId: String)
    
    suspend fun getAllEntidadesSalud(): List<EntidadSaludEntity>
    suspend fun getEntidadSaludById(entidadId: Int): EntidadSaludEntity?
    suspend fun getEntidadSaludByName(nombre: String): EntidadSaludEntity?
    suspend fun insertEntidadSalud(entidad: EntidadSaludEntity): Int
    suspend fun updateEntidadSalud(entidad: EntidadSaludEntity)
    suspend fun deleteEntidadSalud(entidadId: Int)
    
    // ========== OPERACIONES REMOTAS ==========
    
    suspend fun getAllMaletasRemote(token: String): Result<List<Maleta>>
    suspend fun createMaletaRemote(token: String, request: MaletaCreateRequest): Result<Maleta>
    suspend fun asociarMaletaUsuarioRemote(token: String, maletaId: String, request: MaletaAsociacionRequest): Result<Boolean>
    
    suspend fun getAllEntidadesSaludRemote(token: String): Result<List<EntidadSalud>>
    suspend fun createEntidadSaludRemote(token: String, request: EntidadSaludCreateRequest): Result<EntidadSalud>
    
    // ========== OPERACIONES UNIFICADAS (OFFLINE-FIRST) ==========
    
    suspend fun syncMaletas(token: String): Result<SyncResult>
    suspend fun syncEntidadesSalud(token: String): Result<SyncResult>
    suspend fun createMaleta(token: String?, request: MaletaCreateRequest): Result<MaletaEntity>
    suspend fun createEntidadSalud(token: String?, request: EntidadSaludCreateRequest): Result<EntidadSaludEntity>
    
    // ========== UTILIDADES ==========
    
    suspend fun getEntidadSaludName(entidadId: Int?): String
    suspend fun performMaintenance()
    
    data class SyncResult(
        val success: Boolean,
        val syncedCount: Int,
        val failedCount: Int,
        val totalCount: Int
    )
    
    sealed class RepositoryError : Exception() {
        object NetworkError : RepositoryError()
        object DuplicateError : RepositoryError()
        object NotFoundError : RepositoryError()
        data class UnknownError(override val cause: Throwable) : RepositoryError()
    }
}
