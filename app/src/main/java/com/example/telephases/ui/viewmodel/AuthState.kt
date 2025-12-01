package com.example.telephases.ui.viewmodel

import com.example.telephases.data.local.entities.UserEntity

sealed class AuthState {
    object Idle    : AuthState()
    object Loading : AuthState()
    data class Success(val token: String, val user: UserEntity? = null) : AuthState()
    data class Error(val msg: String)    : AuthState()
}
