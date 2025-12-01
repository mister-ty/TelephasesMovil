// app/src/main/java/com/example/telephases/network/RegisterRequest.kt
package com.example.telephases.network

import java.time.LocalDate

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val primer_nombre: String,
    val segundo_nombre: String?,
    val primer_apellido: String,
    val segundo_apellido: String?,
    val tipo_documento_id: Int,
    val numero_documento: String,
    val telefono: String?,
    val direccion: String?,
    val ciudad_id: Int?,
    val fecha_nacimiento: String?, // ISO yyyy-MM-dd
    val genero: String?,            // 'M','F' o 'O'
    val rol_id: Int,
    val entidad_salud_id: Int? = null
)
