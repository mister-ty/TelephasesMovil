

package com.example.telephases.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telephases.data.repository.CitaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamenViewModel @Inject constructor(
    private val citaRepository: CitaRepository
) : ViewModel() {
    
    private val _patientId = MutableStateFlow<String?>(null)
    val patientId: StateFlow<String?> = _patientId.asStateFlow()
    
    fun loadPatientId(citaId: Int) {
        viewModelScope.launch {
            try {
                val cita = citaRepository.getCitaConExamenes(citaId)
                _patientId.value = cita?.paciente_id ?: "temp-patient-id"
            } catch (e: Exception) {
                println("Error obteniendo patientId: ${e.message}")
                _patientId.value = "temp-patient-id"
            }
        }
    }
}
