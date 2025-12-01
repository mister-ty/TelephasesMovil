package com.example.telephases.network

import retrofit2.http.Path
import retrofit2.http.Query
import com.example.telephases.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

// Peticiones y respuestas para autenticación
data class AuthResponse(
    val token: String
)

// Modelos para exámenes
data class ExamRequest(
    val tipo_examen_nombre: String,
    val titulo: String,
    val valor: String,
    val unidad: String? = null,
    val observaciones: String? = null,
    val datos_adicionales: Map<String, Any>? = null,
    val patientId: String? = null,
    val paciente_id: String? = null
)

data class ExamData(
    val id: Long,
    val titulo: String,
    val valor: String,
    val unidad: String?,
    val fecha_creacion: String,
    val fecha_modificacion: String,
    val observaciones: String?,
    val tipo_examen_id: Int?,
    val datos_adicionales: Any?,
    val estado_codigo: String?,
    val estado_nombre: String?,
    val estado_emoji: String?,
    val estado_color: String?,
    val estado_descripcion: String?,
    val estado_nivel_urgencia: Int?
)

data class ExamResponse(
    val examen: ExamData,
    val message: String
)

data class ExamsResponse(
    val examenes: List<ExamData>
)

// =====================
// Modelos para Estudios
// =====================

data class StudyExam(
    val tipo_examen_nombre: String,
    val titulo: String,
    val valor: String,
    val unidad: String? = null,
    val observaciones: String? = null,
    val datos_adicionales: Map<String, Any>? = null
)

data class StudyRequest(
    val paciente_id: String,
    val fecha_estudio: String? = null,
    val observaciones: String? = null,
    val examenes: List<StudyExam>,
    val maleta_id: String? = null,
    val sede_id: String? = null
)

data class StudyCreateResponse(
    val estudio_id: Long,
    val exam_ids: List<Long>,
    val exams_count: Int,
    val message: String
)

// Modelos para pacientes
data class Patient(
    val id: String,
    val primer_nombre: String,
    val segundo_nombre: String?,
    val primer_apellido: String,
    val segundo_apellido: String?,
    val numero_documento: String,
    val email: String?,
    val telefono: String?,
    val direccion: String? = null,
    val ciudad_id: Int? = null,
    val fecha_nacimiento: String?,
    val genero: String?,
    val nombre_completo: String,
    val tipo_identificacion: String? = null,
    val estado_civil: String? = null,
    val pais: String? = null,
    val municipio: String? = null,
    val departamento: String? = null,
    val tipo_usuario: String? = null,
    val entidad_salud_id: Int? = null
)

data class PatientSearchResponse(
    val paciente: Patient
)

data class PatientsListResponse(
    val pacientes: List<Patient>
)

data class PatientSyncRequest(
    val patients: List<PatientSyncData>
)

data class PatientSyncData(
    val id: String,
    val username: String?,
    val primer_nombre: String,
    val segundo_nombre: String?,
    val primer_apellido: String,
    val segundo_apellido: String?,
    val tipo_documento_id: Int,
    val numero_documento: String,
    val email: String?,
    val telefono: String?,
    val direccion: String?,
    val ciudad_id: Int?,
    val fecha_nacimiento: String?,
    val genero: String?,
    val tipo_identificacion: String?,
    val estado_civil: String?,
    val pais: String?,
    val municipio: String?,
    val departamento: String?,
    val entidad_salud_id: Int?,
    val rol_id: Int?
)

data class PatientSyncResponse(
    val message: String,
    val results: PatientSyncResults
)

data class PatientSyncResults(
    val total: Int,
    val synced: Int,
    val failed: Int,
    val created: List<String>,
    val errors: List<PatientSyncError>
)

data class PatientSyncError(
    val id: String,
    val email: String?,
    val error: String
)

data class PatientRegistrationRequest(
    val primer_nombre: String,
    val segundo_nombre: String? = null,
    val primer_apellido: String,
    val segundo_apellido: String? = null,
    val tipo_documento_id: Int,
    val numero_documento: String,
    val email: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val ciudad_id: Int? = null,
    val fecha_nacimiento: String? = null,
    val genero: String? = null,
    val tipo_identificacion: String? = null,
    val estado_civil: String? = null,
    val pais: String? = null,
    val municipio: String? = null,
    val departamento: String? = null,
    val tipo_usuario: String? = null,
    val entidad_salud_id: Int? = null
)

data class PatientRegistrationResponse(
    val paciente: Patient,
    val message: String
)

// =====================
// Modelos para Maletas
// =====================

data class Maleta(
    val id: String,
    val nombre: String,
    val descripcion: String? = null,
    val estado: String? = null,
    val fecha_creacion: String? = null,
    val fecha_modificacion: String? = null,
    val usuario_id: String? = null,
    val entidad_salud_id: Int? = null
)

data class MaletasListResponse(
    val maletas: List<Maleta>
)

data class MaletaCreateRequest(
    val nombre: String,
    val descripcion: String? = null,
    val entidad_salud_id: Int? = null
)

data class MaletaCreateResponse(
    val maleta: Maleta,
    val message: String
)

data class MaletaAsociacionRequest(
    val usuario_id: String,
    val entidad_salud_id: Int? = null
)

data class MaletaAsociacionResponse(
    val success: Boolean,
    val message: String
)

// =====================
// Modelos para Entidades de Salud
// =====================

data class EntidadSalud(
    val id: Int,
    val nombre: String,
    val codigo: String? = null,
    val direccion: String? = null,
    val telefono: String? = null,
    val email: String? = null,
    val fecha_creacion: String? = null,
    val fecha_modificacion: String? = null
)

data class EntidadesSaludListResponse(
    val entidades: List<EntidadSalud>
)

data class EntidadSaludCreateRequest(
    val nombre: String,
    val codigo: String? = null,
    val direccion: String? = null,
    val telefono: String? = null,
    val email: String? = null
)

data class EntidadSaludCreateResponse(
    val entidad: EntidadSalud,
    val message: String
)

// Modelo de ítem (ya existía)

// =====================
// Modelos para Citas
// =====================

data class CitaCreateRequest(
    val paciente_id: String,
    val creado_por_usuario_id: String,
    val estado_cita_id: Int? = 1,
    val fecha_cita: String, // ISO-8601 con zona horaria
    val observaciones_admin: String? = null,
    val observaciones_paciente: String? = null,
    val examenes_previstos: List<Int>? = null
)

data class CitaResumen(
    val id: Int,
    val fecha_cita: String,
    val estado_cita: String? = null,
    val estado_cita_id: Int? = null,
    val paciente_id: String? = null,
    val paciente_nombre: String? = null,
    val paciente_cedula: String? = null,
    val creado_por_usuario_id: String? = null,
    val admin_nombre: String? = null,
    val observaciones_admin: String? = null,
    val observaciones_paciente: String? = null,
    val examenes_previstos: List<ExamenPrevisto>? = null
)

data class ExamenPrevisto(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null
)

data class CitasHoyResponse(
    val citas: List<CitaResumen>
)

data class CitasListResponse(
    val citas: List<CitaResumen>
)

data class CitaCreateResponse(
    val citaId: Int,
    val message: String
)

interface ApiInterface {
    // Ítems
    @GET("api/items")
    suspend fun getItems(): List<Item>

    @POST("api/items")
    suspend fun createItem(@Body item: Item): Item

    // Registro de usuario
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    // Login de usuario
    @POST("api/auth/login")
    suspend fun login(@Body credentials: Credentials): AuthResponse

    // Exámenes médicos
    @POST("api/examenes")
    suspend fun createExam(
        @Header("Authorization") token: String,
        @Body exam: ExamRequest
    ): ExamResponse

    @GET("api/examenes")
    suspend fun getExams(
        @Header("Authorization") token: String
    ): ExamsResponse

    @GET("api/examenes/ultimos")
    suspend fun getLatestExams(
        @Header("Authorization") token: String
    ): ExamsResponse

    @GET("api/examenes/paciente/{patientId}")
    suspend fun getPatientExams(
        @Header("Authorization") token: String,
        @Path("patientId") patientId: String
    ): ExamsResponse

    // Estudios
    @POST("api/estudios")
    suspend fun createStudy(
        @Header("Authorization") token: String,
        @Body request: StudyRequest
    ): StudyCreateResponse

    // Pacientes
    @GET("api/pacientes")
    suspend fun getAllPatients(
        @Header("Authorization") token: String
    ): PatientsListResponse

    @GET("api/pacientes/buscar/{documento}")
    suspend fun searchPatient(
        @Header("Authorization") token: String,
        @Path("documento") documento: String
    ): PatientSearchResponse

    @POST("api/pacientes/registrar")
    suspend fun registerPatient(
        @Header("Authorization") token: String,
        @Body request: PatientRegistrationRequest
    ): PatientRegistrationResponse

    // Sincronización masiva de pacientes
    @POST("api/patients/sync")
    suspend fun syncPatients(
        @Header("Authorization") token: String,
        @Body request: PatientSyncRequest
    ): PatientSyncResponse

    // =====================
    // Maletas
    // =====================
    
    @GET("api/maletas")
    suspend fun getAllMaletas(
        @Header("Authorization") token: String
    ): MaletasListResponse

    @POST("api/maletas")
    suspend fun createMaleta(
        @Header("Authorization") token: String,
        @Body request: MaletaCreateRequest
    ): MaletaCreateResponse

    @POST("api/maletas/{maletaId}/asociar")
    suspend fun asociarMaletaUsuario(
        @Header("Authorization") token: String,
        @Path("maletaId") maletaId: String,
        @Body request: MaletaAsociacionRequest
    ): MaletaAsociacionResponse

    // =====================
    // Entidades de Salud
    // =====================
    
    @GET("api/entidades-salud")
    suspend fun getAllEntidadesSalud(
        @Header("Authorization") token: String
    ): EntidadesSaludListResponse

    @POST("api/entidades-salud")
    suspend fun createEntidadSalud(
        @Header("Authorization") token: String,
        @Body request: EntidadSaludCreateRequest
    ): EntidadSaludCreateResponse

    // =====================
    // Citas
    // =====================

    // Citas del día (Tablet)
    @GET("api/citas/hoy")
    suspend fun getCitasHoy(
        @Header("Authorization") token: String
    ): CitasHoyResponse

    // Listar citas (Admin)
    @GET("api/citas")
    suspend fun getCitas(
        @Header("Authorization") token: String,
        @Query("fecha_desde") fechaDesde: String? = null,
        @Query("fecha_hasta") fechaHasta: String? = null
    ): CitasListResponse

    // Crear cita (Admin)
    @POST("api/citas")
    suspend fun createCita(
        @Header("Authorization") token: String,
        @Body request: CitaCreateRequest
    ): CitaCreateResponse
}

object ApiService {

    // URLs para diferentes entornos - AMBAS APUNTAN AL VPS
    private const val DEV_BASE_URL = "http://198.46.189.221:3001/"
    private const val PROD_BASE_URL = "http://198.46.189.221:3001/"
    
    // Configuración de URL base según el entorno
    private val BASE_URL: String
        get() = if (BuildConfig.DEBUG) {
            DEV_BASE_URL  // Desarrollo (VPS en la nube)
        } else {
            PROD_BASE_URL // Producción (VPS en la nube)
        }

    private val okHttpClient = OkHttpClient.Builder().apply {
        // Timeouts aumentados para sincronización masiva
        connectTimeout(60, TimeUnit.SECONDS)
        readTimeout(90, TimeUnit.SECONDS)
        writeTimeout(90, TimeUnit.SECONDS)

        // OPCIONAL: Agregar logging para debug
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        addInterceptor(loggingInterceptor)
    }.build()

    fun create(): ApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiInterface::class.java)
    }
}

// Alias para mantener compatibilidad y facilitar el uso
typealias apiservices = ApiInterface