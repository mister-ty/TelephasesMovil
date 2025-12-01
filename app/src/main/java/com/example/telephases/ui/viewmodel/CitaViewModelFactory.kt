package com.example.telephases.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.telephases.data.repository.CitaRepository
import com.example.telephases.data.repository.PatientRepository
import com.example.telephases.data.repository.ExamRepository
import com.example.telephases.data.local.dao.TipoExamenDao

class CitaViewModelFactory(
    private val citaRepository: CitaRepository,
    private val patientRepository: PatientRepository,
    private val examRepository: ExamRepository,
    private val tipoExamenDao: TipoExamenDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitaViewModel::class.java)) {
            return CitaViewModel(citaRepository, patientRepository, examRepository, tipoExamenDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
