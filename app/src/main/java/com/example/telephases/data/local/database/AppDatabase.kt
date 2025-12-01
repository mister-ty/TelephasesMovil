package com.example.telephases.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.telephases.data.local.entities.*
import com.example.telephases.data.local.dao.*

/**
 * Base de datos principal de la aplicación usando Room
 * 
 * Esta base de datos maneja todos los datos offline de la aplicación:
 * - Pacientes
 * - Exámenes médicos 
 * - Usuarios administradores
 * - Tipos de examen
 * - Metadata de sincronización
 */
@Database(
    entities = [
        PatientEntity::class,
        ExamEntity::class,
        UserEntity::class,
        TipoExamenEntity::class,
        SyncMetadataEntity::class,
        EstadoCitaEntity::class,
        CitaEntity::class,
        CitaExamenPrevistoEntity::class,
        EntidadSaludEntity::class,
        SedeEntity::class,
        UsuarioSedeEntity::class,
        MaletaEntity::class,
        DispositivoEntity::class,
        MaletaDispositivoEntity::class
    ],
    version = 12,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // DAOs
    abstract fun patientDao(): PatientDao
    abstract fun examDao(): ExamDao
    abstract fun userDao(): UserDao
    abstract fun tipoExamenDao(): TipoExamenDao
    abstract fun syncMetadataDao(): SyncMetadataDao
    abstract fun citaDao(): CitaDao
    abstract fun entidadSaludDao(): EntidadSaludDao
    abstract fun sedeDao(): SedeDao
    abstract fun usuarioSedeDao(): UsuarioSedeDao
    abstract fun maletaDao(): MaletaDao
    abstract fun dispositivoDao(): DispositivoDao
    abstract fun maletaDispositivoDao(): MaletaDispositivoDao

    companion object {
        const val DATABASE_NAME = "telephases_local.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia de la base de datos (Singleton)
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(DatabaseCallback())
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12) // Para futuras migraciones
                    // .fallbackToDestructiveMigration() // COMENTADO: Preservar datos offline
                    .build()
                
                INSTANCE = instance
                instance
            }
        }

        /**
         * Callback para inicialización de la base de datos
         */
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // La inicialización de datos se hace en el DatabaseInitializer
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // Limpiar tokens expirados al abrir la base de datos
                // Se puede ejecutar en un background thread
            }
        }

        /**
         * Migración para agregar tipo_examen_id a la tabla exams
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar columna tipo_examen_id a la tabla exams
                database.execSQL("ALTER TABLE exams ADD COLUMN tipo_examen_id INTEGER")
            }
        }

        /**
         * Migración para agregar server_id a la tabla patients
         */
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar columna server_id a la tabla patients
                database.execSQL("ALTER TABLE patients ADD COLUMN server_id TEXT")
            }
        }

        /**
         * Migración 3->4: Crea tablas de citas y estado_cita con datos iniciales
         */
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // estado_cita
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS estado_cita (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nombre TEXT NOT NULL,
                        descripcion TEXT
                    )
                    """.trimIndent()
                )

                // citas
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS citas (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        paciente_id TEXT NOT NULL,
                        creado_por_usuario_id TEXT NOT NULL,
                        estado_cita_id INTEGER NOT NULL DEFAULT 1,
                        fecha_cita TEXT NOT NULL,
                        observaciones_admin TEXT,
                        observaciones_paciente TEXT,
                        fecha_creacion TEXT NOT NULL,
                        fecha_modificacion TEXT NOT NULL,
                        FOREIGN KEY(paciente_id) REFERENCES patients(id) ON UPDATE NO ACTION ON DELETE NO ACTION,
                        FOREIGN KEY(creado_por_usuario_id) REFERENCES users(id) ON UPDATE NO ACTION ON DELETE NO ACTION,
                        FOREIGN KEY(estado_cita_id) REFERENCES estado_cita(id) ON UPDATE NO ACTION ON DELETE NO ACTION
                    )
                    """.trimIndent()
                )

                database.execSQL("CREATE INDEX IF NOT EXISTS index_citas_paciente_id ON citas(paciente_id)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_citas_creado_por_usuario_id ON citas(creado_por_usuario_id)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_citas_estado_cita_id ON citas(estado_cita_id)")

                // cita_examenes_previstos
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cita_examenes_previstos (
                        cita_id INTEGER NOT NULL,
                        tipo_examen_id INTEGER NOT NULL,
                        PRIMARY KEY(cita_id, tipo_examen_id),
                        FOREIGN KEY(cita_id) REFERENCES citas(id) ON UPDATE NO ACTION ON DELETE CASCADE,
                        FOREIGN KEY(tipo_examen_id) REFERENCES exam_types(id) ON UPDATE NO ACTION ON DELETE NO ACTION
                    )
                    """.trimIndent()
                )
                database.execSQL("CREATE INDEX IF NOT EXISTS index_cita_examenes_previstos_tipo_examen_id ON cita_examenes_previstos(tipo_examen_id)")

                // Seed inicial de estado_cita si está vacío
                database.execSQL(
                    """
                    INSERT OR IGNORE INTO estado_cita (id, nombre, descripcion) VALUES
                    (1, 'Programada', 'La cita ha sido creada por el personal médico.'),
                    (2, 'Confirmada', 'El paciente ha confirmado su asistencia (futura funcionalidad).'),
                    (3, 'Cancelada por Paciente', 'La cita fue cancelada por el paciente.'),
                    (4, 'Cancelada por Admin', 'La cita fue cancelada por el personal médico.'),
                    (5, 'Completada', 'El paciente asistió y los exámenes fueron realizados.')
                    """.trimIndent()
                )

                // Crear el índice único que Room espera
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_estado_cita_nombre ON estado_cita(nombre)")
            }
        }

        /**
         * Migración 4->5: Actualiza la tabla cita_examenes_previstos para remover Foreign Key a tipo_examen
         * (temporalmente comentada para evitar errores de integridad)
         */
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Crear una nueva tabla cita_examenes_previstos sin la Foreign Key a tipo_examen
                database.execSQL("DROP TABLE IF EXISTS cita_examenes_previstos")
                
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cita_examenes_previstos (
                        cita_id INTEGER NOT NULL,
                        tipo_examen_id INTEGER NOT NULL,
                        PRIMARY KEY(cita_id, tipo_examen_id),
                        FOREIGN KEY(cita_id) REFERENCES citas(id) ON UPDATE NO ACTION ON DELETE CASCADE
                        -- Foreign Key a tipo_examen temporalmente comentada
                        -- FOREIGN KEY(tipo_examen_id) REFERENCES tipo_examen(id) ON UPDATE NO ACTION ON DELETE NO ACTION
                    )
                    """.trimIndent()
                )
                
                database.execSQL("CREATE INDEX IF NOT EXISTS index_cita_examenes_previstos_tipo_examen_id ON cita_examenes_previstos(tipo_examen_id)")
                
                println("✅ Migración 4->5 completada: Tabla cita_examenes_previstos actualizada")
            }
        }

        /**
         * Migración 5->6: Agrega columnas nombre y descripcion a cita_examenes_previstos
         */
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar columnas nombre y descripcion a cita_examenes_previstos
                database.execSQL("ALTER TABLE cita_examenes_previstos ADD COLUMN nombre TEXT")
                database.execSQL("ALTER TABLE cita_examenes_previstos ADD COLUMN descripcion TEXT")
                println("✅ Migración 5->6 completada: Columnas nombre y descripcion agregadas")
            }
        }

        /**
         * Migración 6->7: Crea tablas de entidades de salud, sedes y usuario_sedes
         */
        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Crear tabla entidades_salud
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS entidades_salud (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nombreEntidad TEXT NOT NULL,
                        nit TEXT UNIQUE,
                        contactoPrincipalNombre TEXT,
                        contactoPrincipalEmail TEXT,
                        contactoPrincipalTelefono TEXT,
                        configuracionJson TEXT,
                        fechaRegistro TEXT NOT NULL,
                        activa INTEGER NOT NULL DEFAULT 1,
                        serverId INTEGER
                    )
                    """.trimIndent()
                )

                // Crear tabla sedes
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS sedes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        entidadSaludId INTEGER NOT NULL,
                        nombreSede TEXT NOT NULL,
                        direccion TEXT,
                        telefono TEXT,
                        ciudad TEXT,
                        responsableSedeNombre TEXT,
                        activa INTEGER NOT NULL DEFAULT 1,
                        serverId INTEGER,
                        FOREIGN KEY(entidadSaludId) REFERENCES entidades_salud(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                // Crear tabla usuario_sedes
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS usuario_sedes (
                        usuarioId TEXT NOT NULL,
                        sedeId INTEGER NOT NULL,
                        fechaAsignacion TEXT NOT NULL,
                        activa INTEGER NOT NULL DEFAULT 1,
                        PRIMARY KEY(usuarioId, sedeId),
                        FOREIGN KEY(usuarioId) REFERENCES users(id) ON DELETE CASCADE,
                        FOREIGN KEY(sedeId) REFERENCES sedes(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                // Crear índices para optimizar consultas
                database.execSQL("CREATE INDEX IF NOT EXISTS index_entidades_salud_nit ON entidades_salud(nit)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_entidades_salud_activa ON entidades_salud(activa)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_entidades_salud_serverId ON entidades_salud(serverId)")
                
                database.execSQL("CREATE INDEX IF NOT EXISTS index_sedes_entidadSaludId ON sedes(entidadSaludId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_sedes_activa ON sedes(activa)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_sedes_serverId ON sedes(serverId)")
                
                database.execSQL("CREATE INDEX IF NOT EXISTS index_usuario_sedes_usuarioId ON usuario_sedes(usuarioId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_usuario_sedes_sedeId ON usuario_sedes(sedeId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_usuario_sedes_activa ON usuario_sedes(activa)")

                println("✅ Migración 6->7 completada: Tablas entidades_salud, sedes y usuario_sedes creadas")
            }
        }

        /**
         * Migración 7->8: Agrega campos nuevos a users y patients para el flujo de entidades de salud
         */
        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    // 1. Agregar entidad_salud_id a la tabla users (si no existe)
                    try {
                        database.execSQL("ALTER TABLE users ADD COLUMN entidad_salud_id INTEGER")
                    } catch (e: Exception) {
                        // Puede existir la columna en camelCase de versiones previas
                        println("⚠️ Columna entidad_salud_id ya existe o no se pudo crear: ${e.message}")
                    }
                    // Crear índice con el nombre esperado por Room
                    database.execSQL("CREATE INDEX IF NOT EXISTS index_users_entidad_salud_id ON users(entidad_salud_id)")
                    
                    // 2. Agregar nuevos campos a la tabla patients (si no existen)
                    try {
                        database.execSQL("ALTER TABLE patients ADD COLUMN tipo_identificacion TEXT")
                    } catch (e: Exception) {
                        println("⚠️ Columna tipo_identificacion ya existe en patients")
                    }
                    
                    try {
                        database.execSQL("ALTER TABLE patients ADD COLUMN estado_civil TEXT")
                    } catch (e: Exception) {
                        println("⚠️ Columna estado_civil ya existe en patients")
                    }
                    
                    try {
                        database.execSQL("ALTER TABLE patients ADD COLUMN pais TEXT")
                    } catch (e: Exception) {
                        println("⚠️ Columna pais ya existe en patients")
                    }
                    
                    try {
                        database.execSQL("ALTER TABLE patients ADD COLUMN municipio TEXT")
                    } catch (e: Exception) {
                        println("⚠️ Columna municipio ya existe en patients")
                    }
                    
                    try {
                        database.execSQL("ALTER TABLE patients ADD COLUMN departamento TEXT")
                    } catch (e: Exception) {
                        println("⚠️ Columna departamento ya existe en patients")
                    }
                    
                    try {
                        database.execSQL("ALTER TABLE patients ADD COLUMN entidad_salud_id INTEGER")
                    } catch (e: Exception) {
                        println("⚠️ Columna entidad_salud_id ya existe en patients")
                    }
                    
                    // 3. Crear índice para el nuevo campo entidad_salud_id en patients
                    database.execSQL("CREATE INDEX IF NOT EXISTS index_patients_entidad_salud_id ON patients(entidad_salud_id)")

                    println("✅ Migración 7->8 completada: Campos agregados a users y patients para flujo de entidades de salud")
                } catch (e: Exception) {
                    println("❌ Error en migración 7->8: ${e.message}")
                    throw e
                }
            }
        }

        /**
         * Migración 8->9: Agrega tablas de maletas y dispositivos para gestión de equipos médicos
         */
        private val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. Crear tabla maletas
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS maletas (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        identificadorInvima TEXT NOT NULL,
                        nombreMaleta TEXT,
                        descripcion TEXT,
                        asignadaAUsuarioId TEXT,
                        entidadSaludId INTEGER,
                        ultimaRevision TEXT,
                        activa INTEGER NOT NULL DEFAULT 1,
                        serverId INTEGER,
                        FOREIGN KEY(asignadaAUsuarioId) REFERENCES users(id) ON UPDATE NO ACTION ON DELETE SET NULL,
                        FOREIGN KEY(entidadSaludId) REFERENCES entidades_salud(id) ON UPDATE NO ACTION ON DELETE SET NULL
                    )
                    """.trimIndent()
                )

                // 2. Crear tabla dispositivos
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS dispositivos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        tipoDispositivo TEXT NOT NULL,
                        marca TEXT,
                        modelo TEXT,
                        numeroSerie TEXT,
                        fechaFabricacion TEXT,
                        fechaMantenimiento TEXT,
                        activo INTEGER NOT NULL DEFAULT 1,
                        serverId INTEGER
                    )
                    """.trimIndent()
                )

                // 3. Crear tabla maleta_dispositivos (relación muchos a muchos)
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS maleta_dispositivos (
                        maletaId INTEGER NOT NULL,
                        dispositivoId INTEGER NOT NULL,
                        fechaAsignacion TEXT NOT NULL,
                        activa INTEGER NOT NULL DEFAULT 1,
                        PRIMARY KEY(maletaId, dispositivoId),
                        FOREIGN KEY(maletaId) REFERENCES maletas(id) ON UPDATE NO ACTION ON DELETE CASCADE,
                        FOREIGN KEY(dispositivoId) REFERENCES dispositivos(id) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                // 4. Crear índices para optimizar consultas
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maletas_asignadaAUsuarioId ON maletas(asignadaAUsuarioId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maletas_entidadSaludId ON maletas(entidadSaludId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maletas_activa ON maletas(activa)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_dispositivos_activo ON dispositivos(activo)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maleta_dispositivos_maletaId ON maleta_dispositivos(maletaId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maleta_dispositivos_dispositivoId ON maleta_dispositivos(dispositivoId)")

                println("✅ Migración 8->9 completada: Tablas maletas, dispositivos y maleta_dispositivos creadas")
            }
        }

        /**
         * Migración 9->10: Añade maletaUid único y genera valores para existentes
         */
        private val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1) Añadir columna maletaUid (nullable inicialmente)
                try {
                    database.execSQL("ALTER TABLE maletas ADD COLUMN maletaUid TEXT")
                } catch (e: Exception) {
                    println("⚠️ Columna maletaUid ya existe en maletas: ${e.message}")
                }

                // 2) Rellenar maletaUid para filas existentes con valores pseudo-UUID si están NULL
                // Nota: SQLite no tiene UUID nativo; generamos con random() y time para minimizar colisiones
                database.execSQL(
                    """
                    UPDATE maletas
                    SET maletaUid = (
                        hex(randomblob(4)) || '-' || hex(randomblob(2)) || '-' || hex(randomblob(2)) || '-' || hex(randomblob(2)) || '-' || hex(randomblob(6))
                    )
                    WHERE maletaUid IS NULL
                    """.trimIndent()
                )

                // 3) Crear índices requeridos (alinear con @Entity)
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_maletas_maletaUid ON maletas(maletaUid)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maletas_asignadaAUsuarioId ON maletas(asignadaAUsuarioId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maletas_entidadSaludId ON maletas(entidadSaludId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maletas_activa ON maletas(activa)")

                println("✅ Migración 9->10 completada: Columna maletaUid agregada e indexada")
            }
        }

        /**
         * Migración 10->11: Agregar campo tipo_usuario a la tabla patients
         */
        private val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar columna tipo_usuario a la tabla patients
                try {
                    database.execSQL("ALTER TABLE patients ADD COLUMN tipo_usuario TEXT")
                    println("✅ Migración 10->11 completada: Columna tipo_usuario agregada a patients")
                } catch (e: Exception) {
                    println("⚠️ Error agregando columna tipo_usuario: ${e.message}")
                }
            }
        }

        /**
         * Migración 11->12: Actualizar esquema para incluir nuevas entidades SedeEntity y MaletaEntity
         * Esta migración crea los índices específicos que Room espera para SedeEntity y MaletaEntity
         */
        private val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Crear índices específicos que Room espera para SedeEntity
                database.execSQL("CREATE INDEX IF NOT EXISTS index_sedes_entidadSaludId ON sedes(entidadSaludId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_sedes_nombreSede ON sedes(nombreSede)")
                
                // Crear índices específicos que Room espera para MaletaEntity
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maletas_entidadSaludId ON maletas(entidadSaludId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maletas_maletaUid ON maletas(maletaUid)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_maletas_identificadorInvima ON maletas(identificadorInvima)")
                
                println("✅ Migración 11->12 completada: Índices específicos creados para SedeEntity y MaletaEntity")
            }
        }

        /**
         * Para testing - crea una base de datos en memoria
         */
        fun getInMemoryDatabase(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                AppDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()
        }

        /**
         * Limpia la instancia (útil para testing)
         */
        fun clearInstance() {
            INSTANCE = null
        }
    }

    /**
     * Inicializa datos predeterminados si es necesario
     */
    suspend fun initializeDefaultData() {
        // Inicializar tipos de examen predeterminados
        tipoExamenDao().initializeDefaultTypes()
        
        // Inicializar metadata básico
        val metadataDao = syncMetadataDao()
        val now = java.time.Instant.now().toString()
        
        // Configuración inicial si no existe
        if (!metadataDao.existsMetadataWithKey(SyncMetadataEntity.AUTO_SYNC_ENABLED)) {
            metadataDao.updateAutoSyncEnabled(true)
        }
        
        if (!metadataDao.existsMetadataWithKey(SyncMetadataEntity.NETWORK_STATUS)) {
            metadataDao.updateNetworkStatus(false)
        }
        
        // Inicializar contadores en cero
        metadataDao.updatePendingPatientsCount(0)
        metadataDao.updatePendingExamsCount(0)
    }

    /**
     * Obtiene estadísticas generales de la base de datos
     */
    suspend fun getDatabaseStats(): DatabaseStats {
        val totalPatients = patientDao().getTotalPatientsCount()
        val totalExams = examDao().getTotalExamsCount()
        val totalUsers = userDao().getTotalUsersCount()
        val unsyncedPatients = patientDao().getUnsyncedPatientsCount()
        val unsyncedExams = examDao().getUnsyncedExamsCount()
        val unsyncedUsers = userDao().getUnsyncedUsersCount()
        val activeSession = userDao().getCurrentLoggedUser() != null

        return DatabaseStats(
            totalPatients = totalPatients,
            totalExams = totalExams,
            totalUsers = totalUsers,
            unsyncedPatients = unsyncedPatients,
            unsyncedExams = unsyncedExams,
            unsyncedUsers = unsyncedUsers,
            totalPendingSync = unsyncedPatients + unsyncedExams + unsyncedUsers,
            hasActiveSession = activeSession
        )
    }

    /**
     * Ejecuta operaciones de mantenimiento de la base de datos
     */
    suspend fun performMaintenance() {
        // Limpiar datos eliminados antiguos
        patientDao().cleanupOldDeletedPatients()
        examDao().cleanupOldDeletedExams()
        userDao().cleanupOldDeletedUsers()
        syncMetadataDao().cleanupOldMetadata()
        
        // Limpiar tokens expirados
        userDao().cleanupExpiredTokens()
        
        // COMENTADO - Puede eliminar exámenes offline válidos
        // examDao().cleanupOrphanedExams()
        // Log.d("AppDatabase", "⚠️ Limpieza de exámenes huérfanos deshabilitada")
        
        // Actualizar contadores de metadata
        val pendingPatients = patientDao().getUnsyncedPatientsCount()
        val pendingExams = examDao().getUnsyncedExamsCount()
        
        syncMetadataDao().updatePendingPatientsCount(pendingPatients)
        syncMetadataDao().updatePendingExamsCount(pendingExams)
    }

    /**
     * Verifica la integridad de los datos
     */
    suspend fun checkDataIntegrity(): IntegrityReport {
        val orphanedExams = examDao().getOrphanedExams()
        val duplicatePatients = patientDao().getDuplicatePatients()
        val duplicateUsersByEmail = userDao().getDuplicateUsersByEmail()
        val duplicateUsersByUsername = userDao().getDuplicateUsersByUsername()

        return IntegrityReport(
            orphanedExamsCount = orphanedExams.size,
            duplicatePatientsCount = duplicatePatients.size,
            duplicateUsersByEmailCount = duplicateUsersByEmail.size,
            duplicateUsersByUsernameCount = duplicateUsersByUsername.size,
            hasIntegrityIssues = orphanedExams.isNotEmpty() || 
                                duplicatePatients.isNotEmpty() || 
                                duplicateUsersByEmail.isNotEmpty() || 
                                duplicateUsersByUsername.isNotEmpty()
        )
    }

    /**
     * Estadísticas de la base de datos
     */
    data class DatabaseStats(
        val totalPatients: Int,
        val totalExams: Int,
        val totalUsers: Int,
        val unsyncedPatients: Int,
        val unsyncedExams: Int,
        val unsyncedUsers: Int,
        val totalPendingSync: Int,
        val hasActiveSession: Boolean
    ) {
        val syncCompletionPercentage: Float
            get() {
                val totalItems = totalPatients + totalExams + totalUsers
                return if (totalItems > 0) {
                    ((totalItems - totalPendingSync).toFloat() / totalItems) * 100
                } else 100f
            }
    }

    /**
     * Reporte de integridad de datos
     */
    data class IntegrityReport(
        val orphanedExamsCount: Int,
        val duplicatePatientsCount: Int,
        val duplicateUsersByEmailCount: Int,
        val duplicateUsersByUsernameCount: Int,
        val hasIntegrityIssues: Boolean
    )
}


