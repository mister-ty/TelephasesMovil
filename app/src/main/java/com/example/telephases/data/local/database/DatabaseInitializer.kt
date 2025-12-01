package com.example.telephases.data.local.database

import android.content.Context
import android.util.Log
import com.example.telephases.data.local.entities.TipoExamenEntity
import com.example.telephases.data.local.entities.SyncMetadataEntity
import com.example.telephases.data.local.entities.UserEntity
import com.example.telephases.data.local.entities.EntidadSaludEntity
import com.example.telephases.data.repository.AuthRepository
import com.example.telephases.network.RegisterRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Inicializador de la base de datos
 * Maneja la configuración inicial y migración de datos
 */
@Singleton
class DatabaseInitializer @Inject constructor(
    private val database: AppDatabase,
    private val authRepository: AuthRepository
) {

    private val tag = "DatabaseInitializer"

    /**
     * Inicializa la base de datos con datos predeterminados
     */
    fun initialize() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(tag, "🔄 Iniciando inicialización de base de datos...")
                
                // Inicializar tipos de examen predeterminados
                initializeExamTypes()
                
                // Inicializar entidades de salud
                initializeEntidadesSalud()
                
                // Inicializar usuarios para entidades de salud
                initializeUsersForEntidades()
                
                // Inicializar metadata de sincronización
                initializeSyncMetadata()
                
                // Configurar valores por defecto
                setupDefaultSettings()
                
                // INICIALIZAR USUARIOS SOLO SI NO EXISTEN
                initializeUsersIfNeeded()
                
                // FORZAR que todos los usuarios de entidades NO estén sincronizados
                forceUnsyncedUsersForEntities()
                
                // FORZAR que todos los pacientes NO estén sincronizados
                forceUnsyncedPatients()
                
                Log.d(tag, "✅ Inicialización de base de datos completada")
                
            } catch (e: Exception) {
                Log.e(tag, "❌ Error durante inicialización de base de datos", e)
            }
        }
    }

    /**
     * Inicializa los tipos de examen predeterminados
     */
    private suspend fun initializeExamTypes() {
        val tipoExamenDao = database.tipoExamenDao()
        
        // Verificar si ya existen tipos de examen
        val existingCount = tipoExamenDao.getTotalTiposExamenCount()
        
        if (existingCount == 0) {
            Log.d(tag, "📋 Inicializando tipos de examen predeterminados...")
            
            val defaultTypes = TipoExamenEntity.getDefaultExamTypes()
            tipoExamenDao.insertTiposExamen(defaultTypes)
            
            Log.d(tag, "✅ Insertados ${defaultTypes.size} tipos de examen predeterminados")
        } else {
            Log.d(tag, "ℹ️ Tipos de examen ya existen ($existingCount tipos)")
        }
    }

    /**
     * Inicializa las entidades de salud del sistema colombiano
     */
    private suspend fun initializeEntidadesSalud() {
        val entidadSaludDao = database.entidadSaludDao()
        
        // Verificar si ya existen entidades de salud
        val existingCount = entidadSaludDao.getActiveCount()
        val expectedCount = 49 // Número total de entidades esperadas
        
        Log.d(tag, "🔍 Entidades existentes: $existingCount, Esperadas: $expectedCount")
        
        // SIEMPRE intentar insertar entidades para asegurar que estén todas
            Log.d(tag, "🏥 Inicializando entidades de salud del sistema colombiano...")
            
            val entidadesSalud = EntidadesSaludInitializer.getEntidadesSaludColombia()
        
        // Insertar entidades una por una para capturar los IDs generados
        val entidadesConIds = mutableListOf<EntidadSaludEntity>()
        entidadesSalud.forEach { entidad ->
            try {
                val idGenerado = entidadSaludDao.insert(entidad)
                val entidadConId = entidad.copy(id = idGenerado.toInt())
                entidadesConIds.add(entidadConId)
                Log.d(tag, "✅ Entidad insertada: ${entidad.nombreEntidad} (ID: $idGenerado)")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error insertando entidad ${entidad.nombreEntidad}: ${e.message}")
            }
        }
        
        Log.d(tag, "✅ Insertadas ${entidadesConIds.size} entidades de salud")
    }

    /**
     * Inicializa usuarios para entidades de salud
     */
    private suspend fun initializeUsersForEntidades() {
        val userDao = database.userDao()
        val entidadSaludDao = database.entidadSaludDao()
        
            Log.d(tag, "👥 Inicializando usuarios para entidades de salud...")
            
        // Obtener todas las entidades de salud activas (con sus IDs reales)
            val entidades = entidadSaludDao.getAllActiveSync()
            
            if (entidades.isNotEmpty()) {
            Log.d(tag, "🔍 Encontradas ${entidades.size} entidades de salud")

            // PRIMERO: Actualizar usuarios existentes con entidadSaludId correcto
            Log.d(tag, "🔄 Actualizando usuarios existentes con entidadSaludId correcto...")
            val usuariosExistentes = userDao.getAllUsersSync()
            var usuariosActualizados = 0

            usuariosExistentes.forEach { usuario ->
                if (usuario.entidadSaludId == null || usuario.entidadSaludId == 0) {
                    // Buscar entidad por nombre de usuario (email)
                    val entidadCorrespondiente = entidades.find { entidad ->
                        entidad.contactoPrincipalEmail?.let { email ->
                            usuario.username.contains(email.split("@")[1])
                        } ?: false
                    }
                    
                    if (entidadCorrespondiente != null) {
                        userDao.updateUserEntidadSaludId(usuario.id, entidadCorrespondiente.id)
                        usuariosActualizados++
                        Log.d(tag, "✅ Usuario actualizado: ${usuario.username} -> Entidad ID: ${entidadCorrespondiente.id}")
                    } else {
                        Log.w(tag, "⚠️ No se encontró entidad para usuario: ${usuario.username}")
                    }
                }
            }

            Log.d(tag, "✅ Usuarios actualizados: $usuariosActualizados")

            // SEGUNDO: Crear usuarios faltantes para entidades sin usuario
                var usuariosCreadosLocalmente = 0
                var usuariosSincronizadosServidor = 0
                var usuariosFallidosServidor = 0
                
            entidades.forEach { entidad ->
                // Verificar si ya existe un usuario para esta entidad
                val usuarioExistente = userDao.getUserByEntidadSaludId(entidad.id)
                
                if (usuarioExistente == null) {
                    try {
                        Log.d(tag, "🔍 Creando usuario para entidad: ${entidad.nombreEntidad} (ID: ${entidad.id})")

                        // Crear usuario específico para esta entidad
                        val user = createUserForEntidad(entidad)

                        Log.d(tag, "🔍 Usuario creado con entidadSaludId: ${user.entidadSaludId}")

                        // 1. Crear usuario localmente
                        userDao.insert(user)
                        usuariosCreadosLocalmente++
                        Log.d(tag, "✅ Usuario creado localmente: ${user.username} para ${entidad.nombreEntidad} (Entidad ID: ${entidad.id})")
                        
                        // 2. Los usuarios se sincronizarán automáticamente por UserSyncWorker
                        // No sincronizar durante la inicialización para evitar delays
                        Log.d(tag, "📋 Usuario marcado para sincronización automática")
                        usuariosSincronizadosServidor = 0
                        usuariosFallidosServidor = 0
                        
                    } catch (e: Exception) {
                        Log.e(tag, "❌ Error creando usuario para ${entidad.nombreEntidad}: ${e.message}")
                        if (e.message?.contains("FOREIGN KEY") == true) {
                            Log.e(tag, "🔍 Error de clave foránea - entidad_salud_id: ${entidad.id}")
                        }
                    }
                } else {
                    Log.d(tag, "ℹ️ Usuario ya existe para entidad: ${entidad.nombreEntidad}")
                    }
                }
                
                Log.d(tag, "✅ Resumen de creación de usuarios:")
            Log.d(tag, "   - Actualizados: $usuariosActualizados")
                Log.d(tag, "   - Creados localmente: $usuariosCreadosLocalmente")
                Log.d(tag, "   - Sincronizados con servidor: $usuariosSincronizadosServidor")
                Log.d(tag, "   - Fallidos en servidor: $usuariosFallidosServidor")
                
            } else {
                Log.w(tag, "⚠️ No hay entidades de salud para crear usuarios")
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
            rolId = 2, // Médico (todos los contacto@ deben ser médicos)
            entidadSaludId = entidad.id, // Usar el ID real de la entidad
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
     * Inicializa metadata de sincronización
     */
    private suspend fun initializeSyncMetadata() {
        val metadataDao = database.syncMetadataDao()
        
        Log.d(tag, "⚙️ Inicializando metadata de sincronización...")
        
        // Configurar sincronización automática HABILITADA
        if (!metadataDao.existsMetadataWithKey(SyncMetadataEntity.AUTO_SYNC_ENABLED)) {
            metadataDao.updateAutoSyncEnabled(true)
            Log.d(tag, "✅ Sincronización automática HABILITADA")
        }
        
        // Estado de red inicial (offline)
        if (!metadataDao.existsMetadataWithKey(SyncMetadataEntity.NETWORK_STATUS)) {
            metadataDao.updateNetworkStatus(false)
            Log.d(tag, "✅ Estado de red inicial: offline")
        }
        
        // Inicializar contadores
        metadataDao.updatePendingPatientsCount(0)
        metadataDao.updatePendingExamsCount(0)
        
        Log.d(tag, "✅ Contadores de sincronización inicializados")
    }

    /**
     * Configura valores por defecto de la aplicación
     */
    private suspend fun setupDefaultSettings() {
        Log.d(tag, "⚙️ Configurando valores por defecto...")
        
        // Actualizar contadores reales basados en datos existentes
        val patientDao = database.patientDao()
        val examDao = database.examDao()
        val metadataDao = database.syncMetadataDao()
        
        val unsyncedPatients = patientDao.getUnsyncedPatientsCount()
        val unsyncedExams = examDao.getUnsyncedExamsCount()
        
        metadataDao.updatePendingPatientsCount(unsyncedPatients)
        metadataDao.updatePendingExamsCount(unsyncedExams)
        
        Log.d(tag, "✅ Contadores actualizados: $unsyncedPatients pacientes, $unsyncedExams exámenes pendientes")
    }

    /**
     * Ejecuta tareas de mantenimiento inicial
     */
    fun performInitialMaintenance() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(tag, "🧹 Ejecutando mantenimiento inicial...")
                
                // Limpiar tokens expirados
                database.userDao().cleanupExpiredTokens()
                
                // COMENTADO - Puede eliminar exámenes offline válidos si el paciente no se ha sincronizado
                // database.examDao().cleanupOrphanedExams()
                Log.d(tag, "⚠️ Limpieza de exámenes huérfanos deshabilitada para preservar datos offline")
                
                // Limpiar metadata antiguo
                database.syncMetadataDao().cleanupOldMetadata()
                
                Log.d(tag, "✅ Mantenimiento inicial completado")
                
            } catch (e: Exception) {
                Log.e(tag, "❌ Error durante mantenimiento inicial", e)
            }
        }
    }

    /**
     * Verifica la integridad de la base de datos
     */
    fun checkDatabaseIntegrity() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(tag, "🔍 Verificando integridad de base de datos...")
                
                val integrityReport = database.checkDataIntegrity()
                
                if (integrityReport.hasIntegrityIssues) {
                    Log.w(tag, "⚠️ Problemas de integridad detectados:")
                    Log.w(tag, "   - Exámenes huérfanos: ${integrityReport.orphanedExamsCount}")
                    Log.w(tag, "   - Pacientes duplicados: ${integrityReport.duplicatePatientsCount}")
                    Log.w(tag, "   - Usuarios duplicados (email): ${integrityReport.duplicateUsersByEmailCount}")
                    Log.w(tag, "   - Usuarios duplicados (username): ${integrityReport.duplicateUsersByUsernameCount}")
                    
                    // Auto-reparar problemas menores
                    autoRepairIntegrityIssues()
                } else {
                    Log.d(tag, "✅ Integridad de base de datos verificada - Sin problemas")
                }
                
            } catch (e: Exception) {
                Log.e(tag, "❌ Error verificando integridad de base de datos", e)
            }
        }
    }

    /**
     * Auto-repara problemas menores de integridad
     */
    private suspend fun autoRepairIntegrityIssues() {
        try {
            Log.d(tag, "🔧 Ejecutando auto-reparación...")
            
            // COMENTADO - Puede eliminar exámenes offline válidos
            // database.examDao().cleanupOrphanedExams()
            Log.d(tag, "⚠️ Auto-reparación de exámenes huérfanos deshabilitada")
            
            Log.d(tag, "✅ Auto-reparación completada")
            
        } catch (e: Exception) {
            Log.e(tag, "❌ Error durante auto-reparación", e)
        }
    }

    /**
     * Obtiene estadísticas de inicialización
     */
    suspend fun getInitializationStats(): InitializationStats {
        return try {
            val databaseStats = database.getDatabaseStats()
            val tipoExamenDao = database.tipoExamenDao()
            val metadataDao = database.syncMetadataDao()
            
            InitializationStats(
                isInitialized = true,
                examTypesCount = tipoExamenDao.getTotalTiposExamenCount(),
                patientsCount = databaseStats.totalPatients,
                examsCount = databaseStats.totalExams,
                usersCount = databaseStats.totalUsers,
                autoSyncEnabled = metadataDao.isAutoSyncEnabled(),
                lastInitialization = java.time.Instant.now().toString()
            )
        } catch (e: Exception) {
            Log.e(tag, "❌ Error obteniendo estadísticas de inicialización", e)
            InitializationStats(
                isInitialized = false,
                examTypesCount = 0,
                patientsCount = 0,
                examsCount = 0,
                usersCount = 0,
                autoSyncEnabled = false,
                lastInitialization = null
            )
        }
    }

    /**
     * INICIALIZA USUARIOS SOLO SI NO EXISTEN
     */
    private suspend fun initializeUsersIfNeeded() {
        Log.d(tag, "🔍 Verificando si necesitan crearse usuarios...")
        
        val userDao = database.userDao()
        val existingUsersCount = userDao.getTotalUsersCount()
        
        if (existingUsersCount > 0) {
            Log.d(tag, "✅ Ya existen $existingUsersCount usuarios en la BD. No se crean nuevos.")
            return
        }
        
        Log.d(tag, "🌐 Creando usuarios iniciales...")
        
        val allUsers = listOf(
            // EPS (Entidades Promotoras de Salud)
            createUser("asmet-001", "contacto@asmet.com", "ASMET", "SALUD EPS S.A.S.", "9001234567"),
            createUser("famisanar-001", "contacto@famisanar.com", "EPS", "FAMISANAR SAS", "9002345678"),
            createUser("sanitas-001", "contacto@sanitas.com", "EPS", "SANITAS S.A", "9003456789"),
            createUser("sura-001", "contacto@sura.com", "EPS", "SURA", "9004567890"),
            createUser("nuevaeps-001", "contacto@nuevaeps.com", "NUEVA", "EPS S.A", "9005678901"),
            createUser("saludtotal-001", "contacto@saludtotal.com", "SALUD", "TOTAL", "9006789012"),
            createUser("saviasalud-001", "contacto@saviasalud.com", "SAVIA", "SALUD EPS", "9007890123"),
            createUser("medimas-001", "contacto@medimas.com", "MEDIMAS", "EPS S.A.S", "9008901234"),
            createUser("compensar-001", "contacto@compensar.com", "COMPENSAR", "EPS", "9009012345"),
            createUser("coosalud-001", "contacto@coosalud.com", "COOSALUD", "ENTIDAD PROMOTORA", "9010123456"),
            
            // ARL (Administradoras de Riesgos Laborales)
            createUser("positiva-001", "contacto@positiva.com", "A.R.L.", "POSITIVA COMPAÑÍA", "9011234567"),
            createUser("colmena-001", "contacto@colmena.com", "COLMENA", "ARL", "9012345678"),
            createUser("equidad-001", "contacto@equidad.com", "LA EQUIDAD", "SEGUROS ARL", "9013456789"),
            createUser("bolivar-001", "contacto@bolivar.com", "SEGUROS", "BOLIVAR ARL", "9014567890"),
            createUser("estado-001", "contacto@estado.com", "SEGUROS DE VIDA", "DEL ESTADO ARL", "9015678901"),
            
            // SOAT (Seguro Obligatorio de Accidentes de Tránsito)
            createUser("axa-001", "contacto@axa.com", "AXA", "COLPATRIA SEGUROS", "9016789012"),
            createUser("liberty-001", "contacto@liberty.com", "LIBERTY", "DE SEGUROS SOAT", "9017890123"),
            createUser("mundial-001", "contacto@mundial.com", "MUNDIAL", "DE SEGUROS", "9018901234"),
            createUser("previsora-001", "contacto@previsora.com", "PREVISORA", "S.A COMPAÑÍA", "9019012345"),
            createUser("bolivarsoat-001", "contacto@bolivarsoat.com", "SEGUROS", "COMERCIALES BOLIVAR", "9020123456"),
            createUser("suramericana-001", "contacto@suramericana.com", "SURAMERICANA", "S.A SOAT", "9021234567"),
            
            // Entidades Gubernamentales
            createUser("adres-001", "contacto@adres.gov.co", "ADRES", "GUBERNAMENTAL", "8001234567"),
            createUser("fonsalud-001", "contacto@fonsalud.gov.co", "FONDO NACIONAL", "DE SALUD", "8002345678"),
            createUser("sanidadmilitar-001", "contacto@sanidadmilitar.gov.co", "DIRECCION GENERAL", "SANIDAD MILITAR", "8003456789"),
            createUser("saludantioquia-001", "contacto@saludantioquia.gov.co", "DIRECCION SECCIONAL", "SALUD ANTIOQUIA", "8004567890"),
            
            // IPS (Instituciones Prestadoras de Salud)
            createUser("dispensario-001", "contacto@dispensario.com", "DISPENSARIO", "MÉDICO MEDELLÍN", "9005678902"),
            createUser("hospitalmaria-001", "contacto@hospitalmaria.com", "E.S.E HOSPITAL", "LA MARIA", "9006789013"),
            createUser("ipsantioquia-001", "contacto@ipsantioquia.com", "IPS SALUD", "ANTIOQUIA LTDA", "9007890124"),
            createUser("medisalud-001", "contacto@medisalud.com", "MEDISALUD", "LTDA I.P.S.", "9008901235"),
            createUser("fundacionmedica-001", "contacto@fundacionmedica.com", "FUNDACION", "MEDICO PREVENTIVA", "9009012346"),
            createUser("sumimedical-001", "contacto@sumimedical.com", "SUMIMEDICAL", "S.A.S", "9010123457"),
            
            // Otras Entidades
            createUser("particulares-001", "contacto@particulares.com", "ATENCION", "A PARTICULARES", "9011234568"),
            createUser("mutualser-001", "contacto@mutualser.com", "ASOCIACION", "MUTUAL SER", "9012345679"),
            createUser("aic-001", "contacto@aic.com", "ASOCIACION INDIGENA", "DEL CAUCA AIC", "9013456780"),
            createUser("cajacopi-001", "contacto@cajacopi.com", "CAJACOPI", "ATLANTICO", "9014567891"),
            createUser("capitalsalud-001", "contacto@capitalsalud.com", "CAPITALSALUD", "EPS-S", "9015678902"),
            createUser("comfaoriente-001", "contacto@comfaoriente.com", "COMFAORIENTE", "EPS-S", "9016789013"),
            createUser("ecoopos-001", "contacto@ecoopos.com", "ECOOPSOS", "EPS S.A.S", "9017890124"),
            createUser("ecopetrol-001", "contacto@ecopetrol.com", "ECOPETROL", "S.A", "9018901235"),
            createUser("fideicomiso-001", "contacto@fideicomiso.com", "FIDEICOMISO", "FONDO NACIONAL", "9020123457"),
            createUser("ferrocarriles-001", "contacto@ferrocarriles.com", "FONDO DE PASIVO", "SOCIAL FERROCARRILES", "9021234567"),
            createUser("mapfre-001", "contacto@mapfre.com", "MAPFRE SEGUROS", "GENERAL COLOMBIA", "9022345678"),
            createUser("regional5-001", "contacto@regional5.com", "REGIONAL DE", "ASEGURAMIENTO № 5", "9023456789"),
            createUser("vidasuramericana-001", "contacto@vidasuramericana.com", "SEGUROS DE VIDA", "SURAMERICANA", "9024567890"),
            createUser("segurosestado-001", "contacto@segurosestado.com", "SEGUROS DEL", "ESTADO S.A", "9025678901"),
            createUser("utfosca-001", "contacto@utfosca.com", "UNION TEMPORAL", "RED INTEGRADA FOSCA", "9026789012"),
            createUser("vihvivir-001", "contacto@vihvivir.com", "UNION TEMPORAL", "VIHVIR UNIDOS", "9027890123"),
            createUser("utfosyga-001", "contacto@utfosyga.com", "UT NUEVO", "FOSYGA", "9028901234")
        )
        
        try {
            var createdCount = 0
            for (user in allUsers) {
                try {
                    userDao.upsertUser(user)
                    createdCount++
                    Log.d(tag, "✅ Usuario creado: ${user.nombreCompleto} (${user.username})")
                } catch (e: Exception) {
                    Log.e(tag, "❌ Error creando usuario ${user.username}: ${e.message}")
                }
            }
            
            Log.d(tag, "🎉 INICIALIZACIÓN COMPLETADA: $createdCount usuarios creados")
            
        } catch (e: Exception) {
            Log.e(tag, "💥 ERROR CRÍTICO en inicialización: ${e.message}", e)
        }
    }
    
    /**
     * Fuerza que todos los usuarios de entidades estén marcados como NO sincronizados
     * Esto se ejecuta SIEMPRE en cada inicialización para garantizar la sincronización
     */
    private suspend fun forceUnsyncedUsersForEntities() {
        try {
            Log.d(tag, "🔄 Marcando usuarios de entidades como NO sincronizados...")
            val userDao = database.userDao()
            val allDbUsers = userDao.getAllUsersDebug()
            var markedCount = 0
            
            allDbUsers.forEach { user ->
                // Marcar como NO sincronizados todos los usuarios de entidades (ID > 1)
                if (user.entidadSaludId != null && user.entidadSaludId!! > 1) {
                    try {
                        val updated = user.copy(
                            sincronizado = false,
                            fechaUltimaSincronizacion = null,
                            modificadoLocalmente = true
                        )
                        userDao.upsertUser(updated)
                        markedCount++
                    } catch (e: Exception) {
                        Log.e(tag, "Error marcando usuario: ${e.message}")
                    }
                }
            }
            
            Log.d(tag, "✅ Marcados $markedCount usuarios como NO sincronizados para sincronización")
        } catch (e: Exception) {
            Log.e(tag, "Error en forceUnsyncedUsersForEntities: ${e.message}")
        }
    }

    /**
     * Fuerza que TODOS los pacientes estén marcados como NO sincronizados
     * Esto garantiza que todos los pacientes se sincronicen a la nube con rol_id = 3
     */
    private suspend fun forceUnsyncedPatients() {
        try {
            Log.d(tag, "🔄 Marcando TODOS los pacientes como NO sincronizados...")
            val patientDao = database.patientDao()
            val allPatients = patientDao.getAllPatients()
            var markedCount = 0
            
            allPatients.forEach { patient ->
                try {
                    val updated = patient.copy(
                        sincronizado = false,
                        fechaUltimaSincronizacion = null,
                        modificadoLocalmente = true,
                        fechaModificacionLocal = java.time.Instant.now().toString()
                    )
                    patientDao.insertPatient(updated)
                    markedCount++
                } catch (e: Exception) {
                    Log.e(tag, "Error marcando paciente: ${e.message}")
                }
            }
            
            Log.d(tag, "✅ Marcados $markedCount pacientes como NO sincronizados para sincronización")
        } catch (e: Exception) {
            Log.e(tag, "Error en forceUnsyncedPatients: ${e.message}")
        }
    }
    
    /**
     * Función auxiliar para crear usuarios de forma consistente
     */
    private fun createUser(
        id: String,
        username: String,
        primerNombre: String,
        primerApellido: String,
        numeroDocumento: String
    ): UserEntity {
        return UserEntity(
            id = id,
            username = username,
            email = username,
            passwordHash = UserEntity.hashPasswordConsistent("admin123"),
            primerNombre = primerNombre,
            primerApellido = primerApellido,
            tipoDocumentoId = 1,
            numeroDocumento = numeroDocumento,
            rolId = 1, // ADMIN - Todos los contacto@ deben ser admins en la tablet
            entidadSaludId = 1, // Entidad por defecto
            fechaRegistro = java.time.Instant.now().toString(),
            tokenActual = null, // Sin token inicial
            fechaExpiracionToken = null,
            ultimoLogin = null,
            activo = true
        )
    }

    /**
     * Estadísticas de inicialización
     */
    data class InitializationStats(
        val isInitialized: Boolean,
        val examTypesCount: Int,
        val patientsCount: Int,
        val examsCount: Int,
        val usersCount: Int,
        val autoSyncEnabled: Boolean,
        val lastInitialization: String?
    )
}