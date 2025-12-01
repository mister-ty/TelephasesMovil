package com.example.telephases.di

import android.content.Context
import com.example.telephases.data.local.dao.*
import com.example.telephases.data.repository.*
import com.example.telephases.network.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.telephases.di.NetworkModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Módulo Hilt para repositorios
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Proporciona PatientRepository
     */
    @Provides
    @Singleton
    fun providePatientRepository(
        patientDao: PatientDao,
        syncMetadataDao: SyncMetadataDao,
        apiService: ApiInterface,
        @ApplicationContext context: Context,
        authRepository: AuthRepository
    ): PatientRepository {
        return PatientRepositoryImpl(patientDao, syncMetadataDao, apiService, context, authRepository)
    }

    /**
     * Proporciona ExamRepository
     */
    @Provides
    @Singleton
    fun provideExamRepository(
        examDao: ExamDao,
        patientDao: PatientDao,
        syncMetadataDao: SyncMetadataDao,
        tipoExamenDao: TipoExamenDao,
        sedeDao: SedeDao,
        maletaDao: MaletaDao,
        apiService: ApiInterface,
        authRepository: AuthRepository,
        @ApplicationContext context: Context
    ): ExamRepository {
        return ExamRepositoryImpl(examDao, patientDao, syncMetadataDao, tipoExamenDao, sedeDao, maletaDao, apiService, authRepository, context)
    }

    /**
     * Proporciona AuthRepository
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        syncMetadataDao: SyncMetadataDao,
        apiService: ApiInterface,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(userDao, syncMetadataDao, apiService, context)
    }

    /**
     * Proporciona CitaRepository
     */
    @Provides
    @Singleton
    fun provideCitaRepository(
        citaDao: CitaDao,
        apiService: ApiInterface,
        authRepository: AuthRepository,
        tipoExamenDao: TipoExamenDao
    ): CitaRepository {
        return CitaRepositoryImpl(citaDao, apiService, authRepository, tipoExamenDao)
    }

    /**
     * Proporciona CitaRepository para uso fuera de Hilt
     */
    fun provideCitaRepositoryForFactory(citaDao: CitaDao, apiService: ApiInterface, tipoExamenDao: TipoExamenDao): CitaRepository {
        // Crear un AuthRepository temporal para uso fuera de Hilt
        val tempAuthRepository = object : AuthRepository {
            override suspend fun getCurrentUser(): com.example.telephases.data.local.entities.UserEntity? = null
            override fun getCurrentUserFlow(): kotlinx.coroutines.flow.Flow<com.example.telephases.data.local.entities.UserEntity?> = kotlinx.coroutines.flow.flowOf(null)
            override suspend fun hasValidSession(): Boolean = false
            override suspend fun getCurrentToken(): String? {
                // Intentar obtener el token desde la base de datos local
                return try {
                    // Buscar usuarios con token válido en la base de datos
                    val users = citaDao.getUsersWithToken()
                    users.firstOrNull()?.tokenActual
                } catch (e: Exception) {
                    println("❌ Error obteniendo token: ${e.message}")
                    null
                }
            }
            override suspend fun saveUserLocally(user: com.example.telephases.data.local.entities.UserEntity) {}
            override suspend fun updateUserToken(userId: String, token: String, expiration: String) {}
            override suspend fun logoutLocal() {}
            override suspend fun clearAllSessions() {}
            override suspend fun loginRemote(credentials: com.example.telephases.network.Credentials): Result<com.example.telephases.network.AuthResponse> = Result.failure(Exception("Not implemented"))
            override suspend fun registerRemote(request: com.example.telephases.network.RegisterRequest): Result<com.example.telephases.network.AuthResponse> = Result.failure(Exception("Not implemented"))
            override suspend fun validateTokenRemote(token: String): Result<Boolean> = Result.failure(Exception("Not implemented"))
            override suspend fun logoutRemote(token: String): Result<Boolean> = Result.failure(Exception("Not implemented"))
            override suspend fun login(credentials: com.example.telephases.network.Credentials): Result<com.example.telephases.data.local.entities.UserEntity> = Result.failure(Exception("Not implemented"))
            override suspend fun register(request: com.example.telephases.network.RegisterRequest): Result<com.example.telephases.data.local.entities.UserEntity> = Result.failure(Exception("Not implemented"))
            override suspend fun logout(): Result<Boolean> = Result.failure(Exception("Not implemented"))
            override suspend fun refreshTokenIfNeeded(): Result<String> = Result.failure(Exception("Not implemented"))
            override suspend fun getAllUsers(): List<com.example.telephases.data.local.entities.UserEntity> = emptyList()
            override suspend fun getUserByCredential(credential: String): com.example.telephases.data.local.entities.UserEntity? = null
            override suspend fun existsUserWithUsername(username: String): Boolean = false
            override suspend fun existsUserWithEmail(email: String): Boolean = false
            override suspend fun updateUser(user: com.example.telephases.data.local.entities.UserEntity): Result<com.example.telephases.data.local.entities.UserEntity> = Result.failure(Exception("Not implemented"))
            override suspend fun syncUsers(): Result<com.example.telephases.data.repository.AuthRepository.SyncResult> = Result.failure(Exception("Not implemented"))
            override suspend fun getUnsyncedUsers(): List<com.example.telephases.data.local.entities.UserEntity> = emptyList()
            override suspend fun forceSyncFromServer(): Result<com.example.telephases.data.repository.AuthRepository.SyncResult> = Result.failure(Exception("Not implemented"))
            override suspend fun isNetworkAvailable(): Boolean = false
            override suspend fun getAuthStats(): com.example.telephases.data.repository.AuthRepository.AuthStats = com.example.telephases.data.repository.AuthRepository.AuthStats(0, 0, 0, 0, 0, false)
            override suspend fun performMaintenance() {}
            override suspend fun isTokenValid(token: String): Boolean = false
            override suspend fun decodeToken(token: String): com.example.telephases.data.repository.AuthRepository.TokenInfo? {
                println("🔍 INICIO decodeToken - Token recibido: $token")
                
                try {
                    println("🔍 Decodificando token JWT...")
                    println("🔍 Token completo: $token")
                    
                    // Decodificar JWT básico (sin verificación de firma)
                    val parts = token.split(".")
                    println("🔍 Partes del token: ${parts.size}")
                    
                    if (parts.size != 3) {
                        println("❌ Token no tiene 3 partes")
                        return null
                    }
                    
                    println("🔍 Decodificando payload...")
                    val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
                    println("🔍 Payload decodificado: $payload")
                    
                    println("🔍 Parseando JSON...")
                    val json = org.json.JSONObject(payload)
                    println("🔍 JSON parseado correctamente")
                    
                    val userId = json.optString("userId", "")
                    val email = json.optString("email", "")
                    val rolId = json.optInt("rolId", 1)
                    val iat = json.optLong("iat", 0)
                    val exp = json.optLong("exp", 0)
                    
                    println("🔍 Valores extraídos:")
                    println("  - userId: '$userId'")
                    println("  - email: '$email'")
                    println("  - rolId: $rolId")
                    println("  - iat: $iat")
                    println("  - exp: $exp")
                    
                    println("🔍 Creando TokenInfo...")
                    val tokenInfo = com.example.telephases.data.repository.AuthRepository.TokenInfo(
                        userId = userId,
                        email = email,
                        rolId = rolId,
                        issuedAt = iat,
                        expiresAt = exp
                    )
                    
                    println("🔍 TokenInfo creado: $tokenInfo")
                    println("🔍 FIN decodeToken - Devolviendo: $tokenInfo")
                    return tokenInfo
                } catch (e: Exception) {
                    println("❌ Error decodificando token: ${e.message}")
                    println("❌ Stack trace:")
                    e.printStackTrace()
                    println("🔍 FIN decodeToken - Devolviendo null por error")
                    return null
                }
            }
        }
        return CitaRepositoryImpl(citaDao, apiService, tempAuthRepository, tipoExamenDao)
    }

    /**
     * Proporciona MaletaRepository
     */
    @Provides
    @Singleton
    fun provideMaletaRepository(
        maletaDao: MaletaDao,
        entidadSaludDao: EntidadSaludDao,
        authRepository: AuthRepository,
        @ApplicationContext context: Context
    ): MaletaRepository {
        return MaletaRepositoryImpl(maletaDao, entidadSaludDao, authRepository, context)
    }

    /**
     * Proporciona SyncManager
     */
    @Provides
    @Singleton
    fun provideSyncManager(
        patientRepository: PatientRepository,
        examRepository: ExamRepository,
        authRepository: AuthRepository,
        maletaRepository: MaletaRepository,
        syncMetadataDao: SyncMetadataDao,
        @ApplicationContext context: Context
    ): SyncManager {
        return SyncManager(patientRepository, examRepository, authRepository, maletaRepository, syncMetadataDao, context)
    }

    /**
     * Proporciona CitaViewModel para uso fuera de Hilt
     */
    fun provideCitaViewModelForFactory(
        database: com.example.telephases.data.local.database.AppDatabase,
        citaDao: CitaDao, 
        apiService: ApiInterface,
        patientDao: PatientDao,
        syncMetadataDao: SyncMetadataDao,
        examDao: ExamDao,
        tipoExamenDao: TipoExamenDao,
        @ApplicationContext context: Context
    ): com.example.telephases.ui.viewmodel.CitaViewModel {
        val citaRepository = provideCitaRepositoryForFactory(citaDao, apiService, tipoExamenDao)
        val userDao = DatabaseModule.provideUserDao(database)
        val authRepository = provideAuthRepository(userDao, syncMetadataDao, apiService, context)
        val patientRepository = PatientRepositoryImpl(patientDao, syncMetadataDao, apiService, context, authRepository)
        val sedeDao = DatabaseModule.provideSedeDao(database)
        val maletaDao = DatabaseModule.provideMaletaDao(database)
        val examRepository = ExamRepositoryImpl(examDao, patientDao, syncMetadataDao, tipoExamenDao, sedeDao, maletaDao, apiService, authRepository, context)
        return com.example.telephases.ui.viewmodel.CitaViewModel(citaRepository, patientRepository, examRepository, tipoExamenDao)
    }

    /**
     * Proporciona ApiInterface para uso fuera de Hilt
     */
    fun provideApiService(): ApiInterface {
        val okHttpClient = OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(90, TimeUnit.SECONDS)
            writeTimeout(90, TimeUnit.SECONDS)
            
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(loggingInterceptor)
        }.build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl("http://198.46.189.221:3001/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        return retrofit.create(ApiInterface::class.java)
    }

}


