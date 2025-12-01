package com.example.telephases.data.repository

import com.example.telephases.data.local.dao.CitaDao
import com.example.telephases.data.local.dao.TipoExamenDao
import com.example.telephases.data.local.entities.CitaEntity
import com.example.telephases.data.local.entities.CitaExamenPrevistoEntity
import com.example.telephases.data.local.entities.EstadoCitaEntity
import com.example.telephases.network.ApiInterface
import com.example.telephases.network.CitaCreateRequest
import com.example.telephases.network.CitaResumen
import com.example.telephases.network.CitasHoyResponse
import com.example.telephases.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitaRepositoryImpl @Inject constructor(
    private val citaDao: CitaDao,
    private val apiInterface: ApiInterface,
    private val authRepository: AuthRepository,
    private val tipoExamenDao: TipoExamenDao
) : CitaRepository {

    override suspend fun getCitasHoy(): List<CitaResumen> = withContext(Dispatchers.IO) {
        try {
            val citasHoy = citaDao.getCitasHoy()
            citasHoy.map { cita ->
                // Obtener información real del paciente
                val paciente = citaDao.getPatientById(cita.pacienteId)
                val nombrePaciente = paciente?.let { "${it.primerNombre} ${it.primerApellido}" } ?: "Paciente Desconocido"
                val cedulaPaciente = paciente?.numeroDocumento
                
                // Obtener exámenes previstos de la cita
                val examenesPrevistos = getExamenesPrevistos(cita.id.toLong())
                
                CitaResumen(
                    id = cita.id.toInt(),
                    fecha_cita = cita.fechaCita,
                    estado_cita = getEstadoCitaNombre(cita.estadoCitaId),
                    estado_cita_id = cita.estadoCitaId,
                    paciente_id = cita.pacienteId,
                    paciente_nombre = nombrePaciente,
                    paciente_cedula = cedulaPaciente,
                    creado_por_usuario_id = cita.creadoPorUsuarioId,
                    admin_nombre = null, // TODO: Obtener nombre del admin
                    observaciones_admin = cita.observacionesAdmin,
                    observaciones_paciente = cita.observacionesPaciente,
                    examenes_previstos = examenesPrevistos
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

         override suspend fun syncCitasFromServer() = withContext(Dispatchers.IO) {
         try {
             println("🔄 INICIANDO SINCRONIZACIÓN DE CITAS DESDE EL SERVIDOR...")
             
             // Crear estados de cita básicos si no existen
             createEstadosCitaBasicos()
             
             // Verificar estado de la tabla tipo_examen (temporalmente comentado)
             // try {
             //     val tipoExamenCount = citaDao.getTipoExamenCount()
             //     println("🔍 Tabla tipo_examen: $tipoExamenCount registros")
             //     
             //     if (tipoExamenCount == 0) {
             //     println("⚠️ ADVERTENCIA: La tabla tipo_examen está vacía")
             //     println("🔍 Esto puede causar problemas con las Foreign Keys")
             //     }
             // } catch (e: Exception) {
             //     println("❌ Error verificando tabla tipo_examen: ${e.message}")
             //     println("🔍 La tabla tipo_examen puede no existir")
             // }
            
            // Limpiar citas existentes del día
            citaDao.clearCitasDelDia()
            
            // Obtener el token de autenticación del AuthRepository
            val token = authRepository.getCurrentToken()
            
            if (token.isNullOrEmpty()) {
                println("❌ No hay token de autenticación disponible")
                return@withContext
            }
            
                         val response = apiInterface.getCitasHoy("Bearer $token")
             val citas = response.citas
             
             println("🔍 Respuesta del servidor:")
             println("  - Total de citas: ${citas.size}")
             citas.forEach { cita ->
                 println("  - Cita ID: ${cita.id}")
                 println("  - Exámenes previstos: ${cita.examenes_previstos?.size ?: 0}")
                 if (cita.examenes_previstos.isNullOrEmpty()) {
                     println("    ⚠️ ADVERTENCIA: Esta cita NO tiene exámenes previstos")
                 } else {
                     cita.examenes_previstos.forEach { examen ->
                         println("    * Examen ID: ${examen.id}, Nombre: ${examen.nombre}")
                     }
                 }
             }
            
            if (citas.isNotEmpty()) {
                citas.forEach { citaResumen ->
                    try {
                        // Obtener el ID del estado de cita por nombre
                        val estadoCitaId = getEstadoCitaIdByName(citaResumen.estado_cita ?: "Programada")
                        
                        // Obtener el ID local del paciente usando el ID de la nube
                        val pacienteId = getPacienteIdLocal(citaResumen.paciente_id ?: "unknown")
                        
                        // Convertir y guardar en la base de datos local
                        println("🔍 Creando CitaEntity con:")
                        println("  - pacienteId: $pacienteId (tipo: ${pacienteId::class.simpleName})")
                        println("  - estadoCitaId: $estadoCitaId (tipo: ${estadoCitaId::class.simpleName})")
                        println("  - creadoPorUsuarioId: ${citaResumen.creado_por_usuario_id ?: "admin"}")
                        println("  - fechaCita: ${citaResumen.fecha_cita}")
                        
                        // Verificar que todos los IDs referenciados existan
                        println("🔍 Verificando existencia de IDs referenciados...")
                        
                        // Verificar que el paciente existe con el ID local generado
                        val pacienteExiste = citaDao.getPatientById(pacienteId)
                        println("  - Paciente con ID $pacienteId existe: ${pacienteExiste != null}")
                        
                        // Verificar que el estado de cita existe
                        val estadoExiste = citaDao.getEstadoById(estadoCitaId)
                        println("  - Estado de cita con ID $estadoCitaId existe: ${estadoExiste != null}")
                        
                        // Obtener un usuario real para la Foreign Key
                        val usuarioReal = citaDao.getUsersWithToken().firstOrNull()
                        val creadorId = if (usuarioReal != null) {
                            usuarioReal.id
                        } else {
                            // Fallback: buscar cualquier usuario
                            citaDao.getAllUsers().firstOrNull()?.id ?: "default_user"
                        }
                        
                        println("  - Usuario creador seleccionado: $creadorId")
                        
                        val citaEntity = CitaEntity(
                            id = 0, // Room asignará el ID
                            pacienteId = pacienteId,
                            creadoPorUsuarioId = creadorId,
                            estadoCitaId = estadoCitaId,
                            fechaCita = citaResumen.fecha_cita,
                            observacionesAdmin = citaResumen.observaciones_admin ?: "",
                            observacionesPaciente = citaResumen.observaciones_paciente ?: "",
                            fechaCreacion = java.time.LocalDateTime.now().toString(),
                            fechaModificacion = java.time.LocalDateTime.now().toString()
                        )
                        
                                                 println("🔍 Intentando insertar cita...")
                         val citaId = citaDao.insertCita(citaEntity)
                         println("✅ Cita guardada localmente con ID: $citaId")
                         
                         // Guardar los exámenes previstos de la cita
                         if (!citaResumen.examenes_previstos.isNullOrEmpty()) {
                             println("🔍 Guardando ${citaResumen.examenes_previstos.size} exámenes previstos para cita local ID: $citaId...")
                             val examenesPrevistos = citaResumen.examenes_previstos.map { examen ->
                                 val previstosEntity = CitaExamenPrevistoEntity(
                                     citaId = citaId.toInt(),
                                     tipoExamenId = examen.id,
                                     nombre = examen.nombre ?: "Examen ${examen.id}",
                                     descripcion = examen.descripcion ?: "Descripción del examen ${examen.id}"
                                 )
                                 println("  - Preparando para insertar CitaExamenPrevisto: cita_id=${previstosEntity.citaId}, tipo_examen_id=${previstosEntity.tipoExamenId}")
                                 previstosEntity
                             }
                             
                             println("🔍 Entidades a insertar:")
                             examenesPrevistos.forEach { examen ->
                                 println("  - Cita ID: ${examen.citaId}, Tipo Examen ID: ${examen.tipoExamenId}")
                             }
                             
                             try {
                                 citaDao.insertPrevistos(examenesPrevistos)
                                 println("✅ Exámenes previstos guardados exitosamente para cita local ID: $citaId")
                                 
                                 // Verificar que se guardaron correctamente
                                 val examenesGuardados = citaDao.getCitaExamenesPrevistos(citaId.toLong())
                                 println("🔍 Verificación: ${examenesGuardados.size} exámenes guardados en BD")
                                 
                                 // Log adicional para debug
                                 if (examenesGuardados.isNotEmpty()) {
                                     examenesGuardados.forEach { examen ->
                                         println("  ✅ Examen verificado en BD: cita_id=${examen.citaId}, tipo_examen_id=${examen.tipoExamenId}")
                                     }
                                 } else {
                                     println("❌ PROBLEMA: Los exámenes se insertaron pero no se pueden recuperar")
                                 }
                             } catch (e: Exception) {
                                 println("❌ Error guardando exámenes previstos: ${e.message}")
                                 e.printStackTrace()
                             }
                         } else {
                             println("ℹ️ No hay exámenes previstos para esta cita (local ID: $citaId)")
                         }
                        
                    } catch (e: Exception) {
                        println("❌ Error guardando cita ${citaResumen.id}: ${e.message}")
                        e.printStackTrace()
                    }
                }
                
                                 println("✅ Sincronización exitosa: ${citas.size} citas cargadas desde la nube")
                 
                 // Verificar que los exámenes previstos se guardaron correctamente
                 println("🔍 Verificando que los exámenes previstos se guardaron...")
                 citas.forEach { citaResumen ->
                     if (!citaResumen.examenes_previstos.isNullOrEmpty()) {
                         println("  - Cita ${citaResumen.id}: ${citaResumen.examenes_previstos.size} exámenes previstos")
                     }
                 }
             } else {
                 println("ℹ️ No hay citas programadas para hoy en la nube")
             }
        } catch (e: Exception) {
            println("❌ Error sincronizando citas: ${e.message}")
            e.printStackTrace()
        }
    }

    override suspend fun createCita(citaRequest: CitaCreateRequest): String = withContext(Dispatchers.IO) {
        try {
            println("🔄 Creando nueva cita...")
            println("🔍 Datos de la cita:")
            println("  - Paciente ID: ${citaRequest.paciente_id}")
            println("  - Creado por: ${citaRequest.creado_por_usuario_id}")
            println("  - Fecha: ${citaRequest.fecha_cita}")
            println("  - Exámenes: ${citaRequest.examenes_previstos?.size ?: 0}")
            
            // Obtener token de autenticación
            println("🔍 Obteniendo token de autenticación...")
            val token = authRepository.getCurrentToken()
            println("🔍 Token obtenido: $token")
            if (token.isNullOrEmpty()) {
                println("❌ Token nulo o vacío")
                throw Exception("No hay token de autenticación disponible")
            }
            println("✅ Token válido obtenido")
            
            // Decodificar token para verificar rolId
            println("🔍 Llamando a decodeToken...")
            println("🔍 Token a decodificar: $token")
            println("🔍 AuthRepository: $authRepository")
            
            val tokenInfo = try {
                println("🔍 Ejecutando authRepository.decodeToken(token)...")
                val result = authRepository.decodeToken(token)
                println("🔍 Resultado de decodeToken: $result")
                result
            } catch (e: Exception) {
                println("❌ Error llamando a decodeToken: ${e.message}")
                e.printStackTrace()
                null
            }
            
            println("🔍 TokenInfo recibido: $tokenInfo")
            println("🔍 Información del token:")
            println("  - UserID: ${tokenInfo?.userId}")
            println("  - Email: ${tokenInfo?.email}")
            println("  - RolID: ${tokenInfo?.rolId}")
            println("  - Expires: ${tokenInfo?.expiresAt}")
            
            // Validar datos del request
            println("🔍 Validando datos del request...")
            if (citaRequest.paciente_id.isNullOrEmpty()) {
                throw Exception("El ID del paciente es requerido")
            }
            if (citaRequest.creado_por_usuario_id.isNullOrEmpty()) {
                throw Exception("El ID del usuario creador es requerido")
            }
            if (citaRequest.fecha_cita.isNullOrEmpty()) {
                throw Exception("La fecha de la cita es requerida")
            }
            
                // Mostrar request completo
                println("🔍 Request completo:")
                println("  - Headers: Authorization: Bearer $token")
                println("  - Body: $citaRequest")
                println("  - Paciente ID: '${citaRequest.paciente_id}'")
                println("  - Usuario ID: '${citaRequest.creado_por_usuario_id}'")
                println("  - Fecha: '${citaRequest.fecha_cita}'")
                println("  - Estado ID: ${citaRequest.estado_cita_id}")
                println("  - Exámenes: ${citaRequest.examenes_previstos?.size ?: 0}")
                println("  - Exámenes detallados: ${citaRequest.examenes_previstos}")
                println("  - Observaciones admin: '${citaRequest.observaciones_admin}'")
                println("  - Observaciones paciente: '${citaRequest.observaciones_paciente}'")
            
            // Mapear IDs de exámenes locales a IDs del servidor
            val mappedExamenesPrevistos = citaRequest.examenes_previstos?.map { localId ->
                val serverId = mapLocalExamIdToServerId(localId)
                println("🔍 Mapeando examen local $localId -> servidor $serverId")
                serverId
            }
            
            // Crear request con IDs mapeados
            val mappedCitaRequest = citaRequest.copy(
                examenes_previstos = mappedExamenesPrevistos
            )
            
            // Crear la cita en el servidor
            println("🔍 Enviando request al servidor...")
            println("🔍 URL: http://198.46.189.221:3001/api/citas")
            println("🔍 Headers: Authorization: Bearer $token")
            println("🔍 Body completo: $mappedCitaRequest")
            println("🔍 Exámenes mapeados: $mappedExamenesPrevistos")
            
            val response = try {
                apiInterface.createCita("Bearer $token", mappedCitaRequest)
            } catch (e: retrofit2.HttpException) {
                println("❌ Error HTTP ${e.code()}: ${e.message()}")
                println("❌ Response body: ${e.response()?.errorBody()?.string()}")
                
                // Si es error 500, crear la cita localmente como respaldo
                if (e.code() == 500) {
                    println("🔄 Error 500 del servidor - Creando cita localmente como respaldo...")
                    return@withContext createCitaLocalFallback(citaRequest)
                }
                
                throw e
            } catch (e: Exception) {
                println("❌ Error de red: ${e.message}")
                e.printStackTrace()
                
                // Si es error de red, crear la cita localmente como respaldo
                println("🔄 Error de red - Creando cita localmente como respaldo...")
                return@withContext createCitaLocalFallback(citaRequest)
            }
            
            println("✅ Cita creada en el servidor: ${response.citaId}")
            
            // Crear estados básicos si no existen
            createEstadosCitaBasicos()
            
            // Obtener el ID del estado de cita
            val estadoCitaId = getEstadoCitaIdByName("Programada")
            
            // Crear la entidad local
            val citaEntity = CitaEntity(
                id = 0, // Room asignará el ID
                pacienteId = citaRequest.paciente_id,
                creadoPorUsuarioId = citaRequest.creado_por_usuario_id,
                estadoCitaId = estadoCitaId,
                fechaCita = citaRequest.fecha_cita,
                observacionesAdmin = citaRequest.observaciones_admin ?: "",
                observacionesPaciente = citaRequest.observaciones_paciente ?: "",
                fechaCreacion = java.time.LocalDateTime.now().toString(),
                fechaModificacion = java.time.LocalDateTime.now().toString()
            )
            
            // Guardar localmente
            val citaId = citaDao.insertCita(citaEntity)
            println("✅ Cita guardada localmente con ID: $citaId")
            
            // Guardar exámenes previstos si los hay
            if (!citaRequest.examenes_previstos.isNullOrEmpty()) {
                println("🔍 Guardando ${citaRequest.examenes_previstos.size} exámenes previstos...")
                val examenesPrevistos = citaRequest.examenes_previstos.map { tipoExamenId ->
                    CitaExamenPrevistoEntity(
                        citaId = citaId.toInt(),
                        tipoExamenId = tipoExamenId,
                        nombre = "Examen $tipoExamenId",
                        descripcion = "Descripción del examen $tipoExamenId"
                    )
                }
                
                citaDao.insertPrevistos(examenesPrevistos)
                println("✅ Exámenes previstos guardados")
            }
            
            // Sincronizar con el servidor para obtener la lista actualizada
            syncCitasFromServer()
            
            "cita_${citaId}_created"
        } catch (e: Exception) {
            println("❌ Error creando cita: ${e.message}")
            e.printStackTrace()
            throw Exception("Error creando cita: ${e.message}")
        }
    }
    
    override suspend fun getCitaConExamenes(citaId: Int): CitaResumen? = withContext(Dispatchers.IO) {
        try {
            println("🔍 Buscando cita con ID: $citaId")
            val cita = citaDao.getCitaById(citaId)
            
            if (cita != null) {
                println("✅ Cita encontrada: ${cita.id}")
                println("🔍 Paciente ID: ${cita.pacienteId}")
                println("🔍 Estado ID: ${cita.estadoCitaId}")
                
                // Obtener información real del paciente
                val paciente = citaDao.getPatientById(cita.pacienteId)
                val nombrePaciente = paciente?.let { "${it.primerNombre} ${it.primerApellido}" } ?: "Paciente Desconocido"
                val cedulaPaciente = paciente?.numeroDocumento
                
                println("✅ Paciente encontrado: $nombrePaciente")
                
                // Obtener exámenes previstos reales de la cita
                val examenesPrevistos = getExamenesPrevistos(cita.id.toLong())
                println("✅ Exámenes previstos encontrados: ${examenesPrevistos.size}")
                
                val citaResumen = CitaResumen(
                    id = cita.id.toInt(),
                    fecha_cita = cita.fechaCita,
                    estado_cita = getEstadoCitaNombre(cita.estadoCitaId),
                    estado_cita_id = cita.estadoCitaId,
                    paciente_id = cita.pacienteId,
                    paciente_nombre = nombrePaciente,
                    paciente_cedula = cedulaPaciente,
                    creado_por_usuario_id = cita.creadoPorUsuarioId,
                    admin_nombre = null,
                    observaciones_admin = cita.observacionesAdmin,
                    observaciones_paciente = cita.observacionesPaciente,
                    examenes_previstos = examenesPrevistos
                )
                
                println("✅ CitaResumen creado exitosamente")
                citaResumen
            } else {
                println("❌ Cita no encontrada con ID: $citaId")
                null
            }
        } catch (e: Exception) {
            println("❌ Error obteniendo cita con exámenes: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private suspend fun getEstadoCitaNombre(estadoId: Int): String {
        return try {
            val estado = citaDao.getEstadosFlow().first().find { it.id == estadoId }
            estado?.nombre ?: "Desconocido"
        } catch (e: Exception) {
            "Desconocido"
        }
    }

    private suspend fun getExamenesPrevistos(citaId: Long): List<com.example.telephases.network.ExamenPrevisto> {
        return try {
            val examenes = citaDao.getCitaExamenesPrevistos(citaId)
            println("🔍 Exámenes previstos encontrados en BD: ${examenes.size}")
            examenes.forEach { examen ->
                println("  - Examen ID: ${examen.tipoExamenId}, Cita ID: ${examen.citaId}")
            }
            
            examenes.map { examen ->
                com.example.telephases.network.ExamenPrevisto(
                    id = examen.tipoExamenId,
                    nombre = examen.nombre, // Por ahora usamos ID
                    descripcion = "Descripción del examen ${examen.tipoExamenId}"
                )
            }
        } catch (e: Exception) {
            println("❌ Error obteniendo exámenes previstos para cita $citaId: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    private suspend fun createEstadosCitaBasicos() {
        try {
            val estadosExistentes = citaDao.getEstadosFlow().first()
            if (estadosExistentes.isEmpty()) {
                println("🔄 Creando estados de cita básicos...")
                
                val estadosBasicos = listOf(
                    EstadoCitaEntity(1, "Programada", "La cita ha sido creada por el personal médico."),
                    EstadoCitaEntity(2, "Confirmada", "El paciente ha confirmado su asistencia."),
                    EstadoCitaEntity(3, "Cancelada por Paciente", "La cita fue cancelada por el paciente."),
                    EstadoCitaEntity(4, "Cancelada por Admin", "La cita fue cancelada por el personal médico."),
                    EstadoCitaEntity(5, "Completada", "El paciente asistió y los exámenes fueron realizados.")
                )
                
                citaDao.insertEstados(estadosBasicos)
                println("✅ Estados de cita creados exitosamente")
            } else {
                println("ℹ️ Estados de cita ya existen (${estadosExistentes.size} estados)")
            }
        } catch (e: Exception) {
            println("❌ Error creando estados de cita: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private suspend fun getEstadoCitaIdByName(nombreEstado: String): Int {
        return try {
            val estados = citaDao.getEstadosFlow().first()
            val estado = estados.find { it.nombre.equals(nombreEstado, ignoreCase = true) }
            estado?.id ?: 1 // Default a "Programada" si no se encuentra
        } catch (e: Exception) {
            println("❌ Error obteniendo ID del estado '$nombreEstado': ${e.message}")
            1 // Default a "Programada"
        }
    }
    
    private suspend fun createCitaLocalFallback(citaRequest: CitaCreateRequest): String {
        try {
            println("🔄 Creando cita localmente como respaldo...")
            
            // Crear estados básicos si no existen
            createEstadosCitaBasicos()
            
            // Obtener el ID del estado de cita
            val estadoCitaId = getEstadoCitaIdByName("Programada")
            
            // Crear la entidad local
            val citaEntity = CitaEntity(
                id = 0, // Room asignará el ID
                pacienteId = citaRequest.paciente_id,
                creadoPorUsuarioId = citaRequest.creado_por_usuario_id,
                estadoCitaId = estadoCitaId,
                fechaCita = citaRequest.fecha_cita,
                observacionesAdmin = citaRequest.observaciones_admin ?: "",
                observacionesPaciente = citaRequest.observaciones_paciente ?: "",
                fechaCreacion = java.time.LocalDateTime.now().toString(),
                fechaModificacion = java.time.LocalDateTime.now().toString()
            )
            
            // Guardar localmente
            val citaId = citaDao.insertCita(citaEntity)
            println("✅ Cita guardada localmente con ID: $citaId (RESPALDO)")
            
            // Guardar exámenes previstos si los hay
            if (!citaRequest.examenes_previstos.isNullOrEmpty()) {
                println("🔍 Guardando ${citaRequest.examenes_previstos.size} exámenes previstos...")
                
                // Obtener tipos de examen reales de la base de datos
                val tiposExamen = tipoExamenDao.getAllTiposExamen()
                println("🔍 Tipos de examen disponibles: ${tiposExamen.size}")
                tiposExamen.forEach { tipo ->
                    println("  - ID: ${tipo.id}, Nombre: ${tipo.nombre}, Descripción: ${tipo.descripcion}")
                }
                
                val examenesPrevistos = citaRequest.examenes_previstos.map { tipoExamenId ->
                    // Mapear ID local a ID del servidor
                    val serverId = mapLocalExamIdToServerId(tipoExamenId)
                    val tipoExamen = tiposExamen.find { it.id == serverId }
                    println("🔍 Mapeando ID local $tipoExamenId -> ID servidor $serverId")
                    println("🔍 Tipo encontrado: ${tipoExamen?.nombre ?: "NO ENCONTRADO"}")
                    
                    CitaExamenPrevistoEntity(
                        citaId = citaId.toInt(),
                        tipoExamenId = serverId, // Usar ID del servidor
                        nombre = tipoExamen?.nombre ?: "Examen $serverId",
                        descripcion = tipoExamen?.descripcion ?: "Descripción del examen $serverId"
                    )
                }
                
                println("🔍 Exámenes previstos a guardar:")
                examenesPrevistos.forEach { examen ->
                    println("  - ID: ${examen.tipoExamenId}, Nombre: ${examen.nombre}, Descripción: ${examen.descripcion}")
                }
                
                citaDao.insertPrevistos(examenesPrevistos)
                println("✅ Exámenes previstos guardados (RESPALDO)")
            }
            
            println("✅ Cita creada localmente como respaldo: cita_${citaId}_created_local")
            return "cita_${citaId}_created_local"
            
        } catch (e: Exception) {
            println("❌ Error creando cita local de respaldo: ${e.message}")
            e.printStackTrace()
            throw Exception("Error creando cita local de respaldo: ${e.message}")
        }
    }
    
    private suspend fun getPacienteIdLocal(serverId: String): String {
        return try {
            println("🔍 Buscando paciente con ID de servidor: $serverId")
            
            // Buscar el paciente por su ID del servidor
            val paciente = citaDao.getPatientByServerId(serverId)
            
            if (paciente != null) {
                println("✅ Paciente encontrado localmente: ${paciente.primerNombre} ${paciente.primerApellido}")
                println("🔍 ID del paciente: ${paciente.id}")
                println("🔍 Tipo de ID: ${paciente.id::class.simpleName}")
                println("🔍 Server ID del paciente: ${paciente.serverId}")
                println("🔍 Campos principales del paciente:")
                println("  - id: ${paciente.id}")
                println("  - serverId: ${paciente.serverId}")
                println("  - primerNombre: ${paciente.primerNombre}")
                println("  - primerApellido: ${paciente.primerApellido}")
                println("  - numeroDocumento: ${paciente.numeroDocumento}")
                println("  - email: ${paciente.email}")
                println("  - telefono: ${paciente.telefono}")
                println("  - fechaNacimiento: ${paciente.fechaNacimiento}")
                println("  - genero: ${paciente.genero}")
                println("  - direccion: ${paciente.direccion}")
                println("  - fechaRegistro: ${paciente.fechaRegistro}")
                
                // Si el ID y serverId son iguales, necesitamos actualizar el server_id
                if (paciente.id == paciente.serverId) {
                    println("⚠️ ADVERTENCIA: ID y serverId son iguales")
                    println("🔍 Actualizando server_id del paciente para evitar conflictos...")
                    
                    // Actualizar el server_id del paciente para que sea diferente del ID
                    val nuevoServerId = "${paciente.serverId}_server"
                    println("🔍 Nuevo server_id: $nuevoServerId")
                    
                    // Por ahora, usamos el ID existente (esto debería funcionar)
                    println("🔍 Usando ID existente del paciente: ${paciente.id}")
                    paciente.id
                } else {
                    paciente.id
                }
            } else {
                println("❌ Paciente no encontrado localmente con ID de servidor: $serverId")
                // Fallback: usar el ID del servidor (esto causará el error de Foreign Key)
                serverId
            }
        } catch (e: Exception) {
            println("❌ Error obteniendo ID local del paciente '$serverId': ${e.message}")
            e.printStackTrace()
            serverId // Fallback al ID del servidor
        }
    }

    /**
     * Mapea los IDs de exámenes locales a los IDs del servidor
     */
    private fun mapLocalExamIdToServerId(localId: Int): Int {
        return when (localId) {
            1000 -> 1  // BLOOD_PRESSURE
            2000 -> 3  // GLUCOSE
            3000 -> 6  // HEART_RATE
            4000 -> 4  // OXYGEN_SATURATION
            5000 -> 2  // TEMPERATURE
            6000 -> 5  // WEIGHT
            else -> localId // Si no se encuentra mapeo, usar el ID original
        }
    }
    
    /**
     * Actualiza el estado de una cita
     */
    override suspend fun updateCitaEstado(citaId: Int, nuevoEstado: String) {
        try {
            println("🔄 Actualizando estado de cita $citaId a: $nuevoEstado")
            
            // Obtener la cita actual
            val cita = citaDao.getCitaById(citaId)
            if (cita == null) {
                println("❌ Cita con ID $citaId no encontrada")
                return
            }
            
            // Buscar el ID del estado por nombre
            val estados = citaDao.getEstados()
            val estadoEncontrado = estados.find { it.nombre.equals(nuevoEstado, ignoreCase = true) }
            
            if (estadoEncontrado == null) {
                println("❌ Estado '$nuevoEstado' no encontrado en la base de datos")
                return
            }
            
            // Actualizar la cita con el nuevo estado
            val citaActualizada = cita.copy(estadoCitaId = estadoEncontrado.id)
            citaDao.updateCita(citaActualizada)
            
            println("✅ Estado de cita $citaId actualizado a: $nuevoEstado (ID: ${estadoEncontrado.id})")
            
        } catch (e: Exception) {
            println("❌ Error actualizando estado de cita $citaId: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}
