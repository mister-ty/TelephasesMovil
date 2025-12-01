package com.example.telephases.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telephases.data.repository.CitaRepository
import com.example.telephases.data.repository.PatientRepository
import com.example.telephases.data.repository.ExamRepository
import com.example.telephases.data.local.dao.TipoExamenDao
import com.example.telephases.network.CitaResumen
import com.example.telephases.network.CitaCreateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CitaViewModel(
    private val citaRepository: CitaRepository,
    private val patientRepository: PatientRepository,
    private val examRepository: ExamRepository,
    private val tipoExamenDao: TipoExamenDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CitaUiState())
    val uiState: StateFlow<CitaUiState> = _uiState.asStateFlow()

    fun loadCitasHoy() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val citas = citaRepository.getCitasHoy()
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        citasHoy = citas,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar citas: ${e.message}"
                    )
                }
            }
        }
    }

    fun syncCitasFromServer() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                citaRepository.syncCitasFromServer()
                val citas = citaRepository.getCitasHoy()
                
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val currentTime = dateFormat.format(Date())
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        citasHoy = citas,
                        lastSyncTime = currentTime,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Error en sincronización: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    suspend fun getCitaConExamenes(citaId: Int): CitaResumen? {
        return try {
            citaRepository.getCitaConExamenes(citaId)
        } catch (e: Exception) {
            null
        }
    }
    
    fun createCita(request: CitaCreateRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                citaRepository.createCita(request)
                // Recargar citas después de crear una nueva
                loadCitasHoy()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Error al crear cita: ${e.message}"
                    )
                }
            }
        }
    }
    
    // Métodos para obtener repositorios (necesarios para el diálogo)
    fun getPatientRepository(): PatientRepository {
        return patientRepository
    }
    
    fun getExamRepository(): ExamRepository {
        return examRepository
    }
    
    // Métodos para cargar datos reales
    suspend fun loadPatients(): List<com.example.telephases.data.local.entities.PatientEntity> {
        return try {
            patientRepository.getLocalPatients()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun loadExamTypes(): List<com.example.telephases.data.local.entities.TipoExamenEntity> {
        return try {
            println("🔍 CitaViewModel: Cargando tipos de examen...")
            val examTypes = tipoExamenDao.getAllTiposExamen()
            println("🔍 CitaViewModel: Tipos de examen cargados: ${examTypes.size}")
            examTypes.forEach { examType ->
                println("🔍 CitaViewModel: - ${examType.nombre}: ${examType.descripcion}")
            }
            examTypes
        } catch (e: Exception) {
            println("❌ Error cargando tipos de examen: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun updateCitaEstado(citaId: Int, nuevoEstado: String) {
        try {
            citaRepository.updateCitaEstado(citaId, nuevoEstado)
        } catch (e: Exception) {
            println("Error actualizando estado de cita: ${e.message}")
            throw e
        }
    }
    
    /**
     * Envía los exámenes completados de una cita como un estudio conjunto
     */
    suspend fun enviarExamenesComoEstudio(citaId: Int, examenesCompletados: List<Int>) {
        try {
            println("🎯 CitaViewModel: Enviando ${examenesCompletados.size} exámenes como estudio conjunto...")
            
            // Obtener la cita para obtener el paciente
            val cita = citaRepository.getCitaConExamenes(citaId)
            if (cita == null) {
                println("❌ No se pudo obtener la cita $citaId")
                return
            }
            
            // Obtener el ID del paciente de la cita
            val pacienteId = cita.paciente_id
            if (pacienteId == null) {
                println("❌ La cita no tiene paciente asociado")
                return
            }
            
            // Obtener los exámenes completados de la base de datos local
            val examenes = examRepository.getExamsByCitaId(citaId)
            val examenesParaEnviar = examenes.filter { exam -> 
                examenesCompletados.contains(exam.id.toIntOrNull() ?: -1)
            }
            
            if (examenesParaEnviar.isEmpty()) {
                println("⚠️ No hay exámenes para enviar como estudio")
                return
            }
            
            println("🎯 Enviando ${examenesParaEnviar.size} exámenes como estudio conjunto para paciente $pacienteId")
            
            // Crear el estudio con todos los exámenes
            val studyExams = examenesParaEnviar.map { exam ->
                com.example.telephases.network.StudyExam(
                    tipo_examen_nombre = exam.tipoExamenNombre,
                    titulo = exam.titulo,
                    valor = exam.valor,
                    unidad = exam.unidad,
                    observaciones = exam.observaciones,
                    datos_adicionales = exam.datosAdicionales?.let {
                        try { 
                            com.google.gson.Gson().fromJson(it, Map::class.java) as Map<String, Any> 
                        } catch (_: Exception) { 
                            null 
                        }
                    }
                )
            }
            
            // Crear el estudio
            val study = com.example.telephases.network.StudyRequest(
                paciente_id = pacienteId,
                fecha_estudio = null, // Usar fecha actual
                observaciones = "Estudio médico de ${examenesParaEnviar.size} exámenes completados en cita $citaId",
                examenes = studyExams,
                maleta_id = null, // TODO: Obtener de la cita si está disponible
                sede_id = null    // TODO: Obtener de la cita si está disponible
            )
            
            // Enviar el estudio al servidor
            val result = examRepository.createStudy(study)
            if (result.isSuccess) {
                val studyResponse = result.getOrThrow()
                println("✅ Estudio creado exitosamente con ID ${studyResponse.estudio_id}")
                println("✅ ${studyResponse.exam_ids.size} exámenes sincronizados")
            } else {
                println("❌ Error creando estudio: ${result.exceptionOrNull()?.message}")
            }
            
        } catch (e: Exception) {
            println("❌ Error enviando exámenes como estudio: ${e.message}")
            e.printStackTrace()
        }
    }
}
