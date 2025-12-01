package com.example.telephases.data.repository

import com.example.telephases.network.CitaResumen

interface CitaRepository {
    suspend fun getCitasHoy(): List<CitaResumen>
    suspend fun syncCitasFromServer()
    suspend fun createCita(citaRequest: com.example.telephases.network.CitaCreateRequest): String
    suspend fun getCitaConExamenes(citaId: Int): CitaResumen?
    suspend fun updateCitaEstado(citaId: Int, nuevoEstado: String)
}
