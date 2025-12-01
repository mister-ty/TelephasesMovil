package com.example.telephases.ui.viewmodel

import com.example.telephases.network.CitaResumen

data class CitaUiState(
    val isLoading: Boolean = false,
    val citasHoy: List<CitaResumen> = emptyList(),
    val lastSyncTime: String? = null,
    val error: String? = null
)
