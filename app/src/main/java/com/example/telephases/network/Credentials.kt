package com.example.telephases.network

import com.google.gson.annotations.SerializedName

/**
 * Clase que representa las credenciales para login.
 */
data class Credentials(
    @SerializedName("credential")
    val username: String,
    val password: String
)
