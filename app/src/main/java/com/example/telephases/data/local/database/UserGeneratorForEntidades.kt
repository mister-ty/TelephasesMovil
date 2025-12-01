package com.example.telephases.data.local.database

import com.example.telephases.data.local.entities.UserEntity
import com.example.telephases.data.local.entities.EntidadSaludEntity
import java.util.UUID

/**
 * Generador de usuarios para entidades de salud
 * Crea usuarios administradores para cada entidad de salud existente
 */
object UserGeneratorForEntidades {
    
    /**
     * Genera usuarios para todas las entidades de salud
     */
    fun generateUsersForEntidades(entidades: List<EntidadSaludEntity>): List<UserEntity> {
        return entidades.map { entidad ->
            createUserForEntidad(entidad)
        }
    }
    
    /**
     * Crea un usuario para una entidad de salud específica
     */
    private fun createUserForEntidad(entidad: EntidadSaludEntity): UserEntity {
        // Extraer nombre de usuario del email
        val email = entidad.contactoPrincipalEmail ?: "admin@${entidad.nombreEntidad.lowercase().replace(" ", "").replace(".", "").replace(",", "").replace("(", "").replace(")", "")}.com"
        val username = email // Usar el email completo como username para evitar duplicados
        
        // Generar nombre completo basado en el nombre de la entidad
        val nombreCompleto = generateNombreFromEntidad(entidad.nombreEntidad)
        val partes = nombreCompleto.split(" ")
        val primerNombre = partes.firstOrNull() ?: "Administrador"
        val primerApellido = partes.drop(1).joinToString(" ").takeIf { it.isNotEmpty() } ?: "Sistema"
        
        // Generar número de documento único
        val numeroDocumento = generateDocumentoFromNIT(entidad.nit)
        
        return UserEntity.createForOfflineRegistration(
            username = username,
            email = email,
            primerNombre = primerNombre,
            primerApellido = primerApellido,
            numeroDocumento = numeroDocumento,
            telefono = entidad.contactoPrincipalTelefono,
            direccion = "Sede Principal",
            rolId = 1, // Administrador
            entidadSaludId = entidad.id,
            password = "admin123" // Contraseña correcta para usuarios del servidor
        )
    }
    
    /**
     * Genera un nombre completo basado en el nombre de la entidad
     */
    private fun generateNombreFromEntidad(nombreEntidad: String): String {
        // Mapeo de nombres comunes para entidades de salud
        val nombresMap = mapOf(
            "ASMET" to "Carlos",
            "FAMISANAR" to "María",
            "SANITAS" to "Ana",
            "SURA" to "Luis",
            "NUEVA EPS" to "Patricia",
            "SALUD TOTAL" to "Roberto",
            "SAVIA" to "Carmen",
            "MEDIMAS" to "Diego",
            "COMPENSAR" to "Sofia",
            "COOSALUD" to "Andrés",
            "POSITIVA" to "Laura",
            "COLMENA" to "Miguel",
            "EQUIDAD" to "Isabel",
            "BOLIVAR" to "Fernando",
            "AXA" to "Valentina",
            "LIBERTY" to "Ricardo",
            "MUNDIAL" to "Natalia",
            "PREVISORA" to "Alejandro",
            "SURAMERICANA" to "Gabriela",
            "ADRES" to "Oscar",
            "FONDO NACIONAL" to "Claudia",
            "SANIDAD MILITAR" to "Héctor",
            "ANTIOQUIA" to "Beatriz",
            "DISPENSARIO" to "Jorge",
            "HOSPITAL" to "Mónica",
            "IPS" to "Sergio",
            "MEDISALUD" to "Diana",
            "FUNDACION" to "César",
            "SUMIMEDICAL" to "Lorena",
            "PARTICULARES" to "Felipe",
            "MUTUAL" to "Adriana",
            "INDIGENA" to "Mario",
            "CAJACOPI" to "Teresa",
            "CAPITALSALUD" to "Pablo",
            "COMFAORIENTE" to "Rosa",
            "ECOOPSOS" to "Alberto",
            "ECOPETROL" to "Elena",
            "FIDEICOMISO" to "Javier",
            "FERROCARRILES" to "Silvia",
            "MAPFRE" to "Daniel",
            "REGIONAL" to "Lucía",
            "SEGUROS" to "Antonio",
            "UNION TEMPORAL" to "Martha",
            "UT" to "Rafael"
        )
        
        // Buscar coincidencia en el nombre de la entidad
        for ((key, nombre) in nombresMap) {
            if (nombreEntidad.uppercase().contains(key)) {
                return "$nombre Administrador"
            }
        }
        
        // Si no hay coincidencia, usar el primer nombre de la entidad
        val primerNombre = nombreEntidad.split(" ").firstOrNull() ?: "Admin"
        return "$primerNombre Administrador"
    }
    
    /**
     * Genera un número de documento basado en el NIT de la entidad
     */
    private fun generateDocumentoFromNIT(nit: String?): String {
        if (nit.isNullOrEmpty()) {
            return "1000000000" // Documento por defecto
        }
        
        // Extraer números del NIT y crear un documento
        val numeros = nit.filter { it.isDigit() }
        return if (numeros.length >= 8) {
            numeros.take(10) // Tomar hasta 10 dígitos
        } else {
            numeros.padStart(10, '0') // Rellenar con ceros
        }
    }
    
    /**
     * Genera contraseñas por defecto para los usuarios
     */
    fun generateDefaultPassword(entidad: EntidadSaludEntity): String {
        val nombreCorto = entidad.nombreEntidad
            .replace("S.A.S", "")
            .replace("S.A", "")
            .replace("EPS", "")
            .replace("ARL", "")
            .replace("SOAT", "")
            .replace("LTDA", "")
            .replace(" ", "")
            .uppercase()
            .take(6)
        
        return "${nombreCorto}2024!"
    }
    
    /**
     * Genera contraseña simple para testing
     */
    fun generateSimplePassword(): String {
        return "admin123"
    }
    
    /**
     * Genera un resumen de usuarios creados
     */
    fun generateUserSummary(users: List<UserEntity>, entidades: List<com.example.telephases.data.local.entities.EntidadSaludEntity>): String {
        val summary = StringBuilder()
        summary.appendLine("=== RESUMEN DE USUARIOS GENERADOS ===")
        summary.appendLine("Total de usuarios: ${users.size}")
        summary.appendLine()
        
        users.forEach { user ->
            val entidad = entidades.find { it.id == user.entidadSaludId }
            summary.appendLine("• ${user.nombreCompleto}")
            summary.appendLine("  Username: ${user.username}")
            summary.appendLine("  Email: ${user.email}")
            summary.appendLine("  Documento: ${user.numeroDocumento}")
            summary.appendLine("  Entidad: ${entidad?.nombreEntidad ?: "No encontrada"}")
            summary.appendLine("  Entidad ID: ${user.entidadSaludId}")
            summary.appendLine("  Contraseña: admin123")
            summary.appendLine()
        }
        
        return summary.toString()
    }
}
