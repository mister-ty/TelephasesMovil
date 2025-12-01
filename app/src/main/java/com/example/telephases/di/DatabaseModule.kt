package com.example.telephases.di

import android.content.Context
import androidx.room.Room
import com.example.telephases.data.local.database.AppDatabase
import com.example.telephases.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt para base de datos Room
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Proporciona la instancia de AppDatabase
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    /**
     * Proporciona PatientDao
     */
    @Provides
    fun providePatientDao(database: AppDatabase): PatientDao {
        return database.patientDao()
    }

    /**
     * Proporciona ExamDao
     */
    @Provides
    fun provideExamDao(database: AppDatabase): ExamDao {
        return database.examDao()
    }

    /**
     * Proporciona UserDao
     */
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    /**
     * Proporciona TipoExamenDao
     */
    @Provides
    fun provideTipoExamenDao(database: AppDatabase): TipoExamenDao {
        return database.tipoExamenDao()
    }

    /**
     * Proporciona SyncMetadataDao
     */
    @Provides
    fun provideSyncMetadataDao(database: AppDatabase): SyncMetadataDao {
        return database.syncMetadataDao()
    }

    /**
     * Proporciona CitaDao
     */
    @Provides
    fun provideCitaDao(database: AppDatabase): CitaDao {
        return database.citaDao()
    }

    /**
     * Proporciona EntidadSaludDao
     */
    @Provides
    fun provideEntidadSaludDao(database: AppDatabase): EntidadSaludDao {
        return database.entidadSaludDao()
    }

    /**
     * Proporciona SedeDao
     */
    @Provides
    fun provideSedeDao(database: AppDatabase): SedeDao {
        return database.sedeDao()
    }

    /**
     * Proporciona UsuarioSedeDao
     */
    @Provides
    fun provideUsuarioSedeDao(database: AppDatabase): UsuarioSedeDao {
        return database.usuarioSedeDao()
    }

    /**
     * Proporciona MaletaDao
     */
    @Provides
    fun provideMaletaDao(database: AppDatabase): MaletaDao {
        return database.maletaDao()
    }

    /**
     * Proporciona DispositivoDao
     */
    @Provides
    fun provideDispositivoDao(database: AppDatabase): DispositivoDao {
        return database.dispositivoDao()
    }

    /**
     * Proporciona MaletaDispositivoDao
     */
    @Provides
    fun provideMaletaDispositivoDao(database: AppDatabase): MaletaDispositivoDao {
        return database.maletaDispositivoDao()
    }

    /**
     * Proporciona AppDatabase para uso fuera de Hilt
     */
    fun provideAppDatabaseForFactory(context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
}


