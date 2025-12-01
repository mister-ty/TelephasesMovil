package com.example.telephases.data.local.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.telephases.data.local.dao.CitaDao;
import com.example.telephases.data.local.dao.CitaDao_Impl;
import com.example.telephases.data.local.dao.DispositivoDao;
import com.example.telephases.data.local.dao.DispositivoDao_Impl;
import com.example.telephases.data.local.dao.EntidadSaludDao;
import com.example.telephases.data.local.dao.EntidadSaludDao_Impl;
import com.example.telephases.data.local.dao.ExamDao;
import com.example.telephases.data.local.dao.ExamDao_Impl;
import com.example.telephases.data.local.dao.MaletaDao;
import com.example.telephases.data.local.dao.MaletaDao_Impl;
import com.example.telephases.data.local.dao.MaletaDispositivoDao;
import com.example.telephases.data.local.dao.MaletaDispositivoDao_Impl;
import com.example.telephases.data.local.dao.PatientDao;
import com.example.telephases.data.local.dao.PatientDao_Impl;
import com.example.telephases.data.local.dao.SedeDao;
import com.example.telephases.data.local.dao.SedeDao_Impl;
import com.example.telephases.data.local.dao.SyncMetadataDao;
import com.example.telephases.data.local.dao.SyncMetadataDao_Impl;
import com.example.telephases.data.local.dao.TipoExamenDao;
import com.example.telephases.data.local.dao.TipoExamenDao_Impl;
import com.example.telephases.data.local.dao.UserDao;
import com.example.telephases.data.local.dao.UserDao_Impl;
import com.example.telephases.data.local.dao.UsuarioSedeDao;
import com.example.telephases.data.local.dao.UsuarioSedeDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PatientDao _patientDao;

  private volatile ExamDao _examDao;

  private volatile UserDao _userDao;

  private volatile TipoExamenDao _tipoExamenDao;

  private volatile SyncMetadataDao _syncMetadataDao;

  private volatile CitaDao _citaDao;

  private volatile EntidadSaludDao _entidadSaludDao;

  private volatile SedeDao _sedeDao;

  private volatile UsuarioSedeDao _usuarioSedeDao;

  private volatile MaletaDao _maletaDao;

  private volatile DispositivoDao _dispositivoDao;

  private volatile MaletaDispositivoDao _maletaDispositivoDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(9) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `patients` (`id` TEXT NOT NULL, `primer_nombre` TEXT NOT NULL, `segundo_nombre` TEXT, `primer_apellido` TEXT NOT NULL, `segundo_apellido` TEXT, `tipo_documento_id` INTEGER NOT NULL, `numero_documento` TEXT NOT NULL, `email` TEXT, `telefono` TEXT, `direccion` TEXT, `ciudad_id` INTEGER, `fecha_nacimiento` TEXT, `genero` TEXT, `tipo_identificacion` TEXT, `estado_civil` TEXT, `pais` TEXT, `municipio` TEXT, `departamento` TEXT, `entidad_salud_id` INTEGER, `fecha_registro` TEXT NOT NULL, `activo` INTEGER NOT NULL, `sincronizado` INTEGER NOT NULL, `server_id` TEXT, `fecha_ultima_sincronizacion` TEXT, `modificado_localmente` INTEGER NOT NULL, `fecha_modificacion_local` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_patients_numero_documento` ON `patients` (`numero_documento`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_patients_email` ON `patients` (`email`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_patients_entidad_salud_id` ON `patients` (`entidad_salud_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exams` (`id` TEXT NOT NULL, `patient_id` TEXT NOT NULL, `tipo_examen_nombre` TEXT NOT NULL, `tipo_examen_id` INTEGER, `titulo` TEXT NOT NULL, `valor` TEXT NOT NULL, `unidad` TEXT, `observaciones` TEXT, `datos_adicionales` TEXT, `fecha_creacion` TEXT NOT NULL, `fecha_modificacion` TEXT, `activo` INTEGER NOT NULL, `estado_codigo` TEXT, `estado_nombre` TEXT, `estado_emoji` TEXT, `estado_color` TEXT, `estado_descripcion` TEXT, `estado_nivel_urgencia` INTEGER, `sincronizado` INTEGER NOT NULL, `fecha_ultima_sincronizacion` TEXT, `modificado_localmente` INTEGER NOT NULL, `fecha_modificacion_local` TEXT, `server_id` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`patient_id`) REFERENCES `patients`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exams_patient_id` ON `exams` (`patient_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exams_tipo_examen_nombre` ON `exams` (`tipo_examen_nombre`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exams_fecha_creacion` ON `exams` (`fecha_creacion`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exams_sincronizado` ON `exams` (`sincronizado`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` TEXT NOT NULL, `username` TEXT NOT NULL, `email` TEXT NOT NULL, `password_hash` TEXT, `primer_nombre` TEXT NOT NULL, `segundo_nombre` TEXT, `primer_apellido` TEXT NOT NULL, `segundo_apellido` TEXT, `tipo_documento_id` INTEGER NOT NULL, `numero_documento` TEXT NOT NULL, `telefono` TEXT, `direccion` TEXT, `ciudad_id` INTEGER, `fecha_nacimiento` TEXT, `genero` TEXT, `rol_id` INTEGER NOT NULL, `entidad_salud_id` INTEGER, `fecha_registro` TEXT NOT NULL, `activo` INTEGER NOT NULL, `token_actual` TEXT, `fecha_expiracion_token` TEXT, `ultimo_login` TEXT, `sincronizado` INTEGER NOT NULL, `fecha_ultima_sincronizacion` TEXT, `modificado_localmente` INTEGER NOT NULL, `fecha_modificacion_local` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_users_username` ON `users` (`username`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_users_email` ON `users` (`email`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_users_numero_documento` ON `users` (`numero_documento`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_users_entidad_salud_id` ON `users` (`entidad_salud_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exam_types` (`id` INTEGER NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `activo` INTEGER NOT NULL, `fecha_creacion` TEXT NOT NULL, `unidad_default` TEXT, `valor_minimo` REAL, `valor_maximo` REAL, `requiere_dispositivo_ble` INTEGER NOT NULL, `icono` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_exam_types_nombre` ON `exam_types` (`nombre`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sync_metadata` (`key` TEXT NOT NULL, `value` TEXT NOT NULL, `last_updated` TEXT NOT NULL, `sync_status` TEXT NOT NULL, PRIMARY KEY(`key`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `estado_cita` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_estado_cita_nombre` ON `estado_cita` (`nombre`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `citas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `paciente_id` TEXT NOT NULL, `creado_por_usuario_id` TEXT NOT NULL, `estado_cita_id` INTEGER NOT NULL, `fecha_cita` TEXT NOT NULL, `observaciones_admin` TEXT, `observaciones_paciente` TEXT, `fecha_creacion` TEXT NOT NULL, `fecha_modificacion` TEXT NOT NULL, FOREIGN KEY(`paciente_id`) REFERENCES `patients`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`creado_por_usuario_id`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`estado_cita_id`) REFERENCES `estado_cita`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_citas_paciente_id` ON `citas` (`paciente_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_citas_creado_por_usuario_id` ON `citas` (`creado_por_usuario_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_citas_estado_cita_id` ON `citas` (`estado_cita_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cita_examenes_previstos` (`cita_id` INTEGER NOT NULL, `tipo_examen_id` INTEGER NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT NOT NULL, PRIMARY KEY(`cita_id`, `tipo_examen_id`), FOREIGN KEY(`cita_id`) REFERENCES `citas`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_cita_examenes_previstos_tipo_examen_id` ON `cita_examenes_previstos` (`tipo_examen_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `entidades_salud` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombreEntidad` TEXT NOT NULL, `nit` TEXT, `contactoPrincipalNombre` TEXT, `contactoPrincipalEmail` TEXT, `contactoPrincipalTelefono` TEXT, `configuracionJson` TEXT, `fechaRegistro` TEXT NOT NULL, `activa` INTEGER NOT NULL DEFAULT 1, `serverId` INTEGER)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_entidades_salud_nit` ON `entidades_salud` (`nit`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_entidades_salud_activa` ON `entidades_salud` (`activa`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_entidades_salud_serverId` ON `entidades_salud` (`serverId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sedes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `entidadSaludId` INTEGER NOT NULL, `nombreSede` TEXT NOT NULL, `direccion` TEXT, `telefono` TEXT, `ciudad` TEXT, `responsableSedeNombre` TEXT, `activa` INTEGER NOT NULL, `serverId` INTEGER, FOREIGN KEY(`entidadSaludId`) REFERENCES `entidades_salud`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `usuario_sedes` (`usuarioId` TEXT NOT NULL, `sedeId` INTEGER NOT NULL, `fechaAsignacion` TEXT NOT NULL, `activa` INTEGER NOT NULL, PRIMARY KEY(`usuarioId`, `sedeId`), FOREIGN KEY(`usuarioId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`sedeId`) REFERENCES `sedes`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `maletas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `identificadorInvima` TEXT NOT NULL, `nombreMaleta` TEXT, `descripcion` TEXT, `asignadaAUsuarioId` TEXT, `entidadSaludId` INTEGER, `ultimaRevision` TEXT, `activa` INTEGER NOT NULL, `serverId` INTEGER, FOREIGN KEY(`asignadaAUsuarioId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL , FOREIGN KEY(`entidadSaludId`) REFERENCES `entidades_salud`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `dispositivos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tipoDispositivo` TEXT NOT NULL, `marca` TEXT, `modelo` TEXT, `serial` TEXT NOT NULL, `fechaAdquisicion` TEXT, `activo` INTEGER NOT NULL, `serverId` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `maleta_dispositivos` (`maletaId` INTEGER NOT NULL, `dispositivoId` INTEGER NOT NULL, PRIMARY KEY(`maletaId`, `dispositivoId`), FOREIGN KEY(`maletaId`) REFERENCES `maletas`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`dispositivoId`) REFERENCES `dispositivos`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5dcda6e61ac54fe58c9e21058bd387ca')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `patients`");
        db.execSQL("DROP TABLE IF EXISTS `exams`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `exam_types`");
        db.execSQL("DROP TABLE IF EXISTS `sync_metadata`");
        db.execSQL("DROP TABLE IF EXISTS `estado_cita`");
        db.execSQL("DROP TABLE IF EXISTS `citas`");
        db.execSQL("DROP TABLE IF EXISTS `cita_examenes_previstos`");
        db.execSQL("DROP TABLE IF EXISTS `entidades_salud`");
        db.execSQL("DROP TABLE IF EXISTS `sedes`");
        db.execSQL("DROP TABLE IF EXISTS `usuario_sedes`");
        db.execSQL("DROP TABLE IF EXISTS `maletas`");
        db.execSQL("DROP TABLE IF EXISTS `dispositivos`");
        db.execSQL("DROP TABLE IF EXISTS `maleta_dispositivos`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPatients = new HashMap<String, TableInfo.Column>(26);
        _columnsPatients.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("primer_nombre", new TableInfo.Column("primer_nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("segundo_nombre", new TableInfo.Column("segundo_nombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("primer_apellido", new TableInfo.Column("primer_apellido", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("segundo_apellido", new TableInfo.Column("segundo_apellido", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("tipo_documento_id", new TableInfo.Column("tipo_documento_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("numero_documento", new TableInfo.Column("numero_documento", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("telefono", new TableInfo.Column("telefono", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("direccion", new TableInfo.Column("direccion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("ciudad_id", new TableInfo.Column("ciudad_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("fecha_nacimiento", new TableInfo.Column("fecha_nacimiento", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("genero", new TableInfo.Column("genero", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("tipo_identificacion", new TableInfo.Column("tipo_identificacion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("estado_civil", new TableInfo.Column("estado_civil", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("pais", new TableInfo.Column("pais", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("municipio", new TableInfo.Column("municipio", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("departamento", new TableInfo.Column("departamento", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("entidad_salud_id", new TableInfo.Column("entidad_salud_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("fecha_registro", new TableInfo.Column("fecha_registro", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("activo", new TableInfo.Column("activo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("sincronizado", new TableInfo.Column("sincronizado", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("server_id", new TableInfo.Column("server_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("fecha_ultima_sincronizacion", new TableInfo.Column("fecha_ultima_sincronizacion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("modificado_localmente", new TableInfo.Column("modificado_localmente", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("fecha_modificacion_local", new TableInfo.Column("fecha_modificacion_local", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPatients = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPatients = new HashSet<TableInfo.Index>(3);
        _indicesPatients.add(new TableInfo.Index("index_patients_numero_documento", true, Arrays.asList("numero_documento"), Arrays.asList("ASC")));
        _indicesPatients.add(new TableInfo.Index("index_patients_email", true, Arrays.asList("email"), Arrays.asList("ASC")));
        _indicesPatients.add(new TableInfo.Index("index_patients_entidad_salud_id", false, Arrays.asList("entidad_salud_id"), Arrays.asList("ASC")));
        final TableInfo _infoPatients = new TableInfo("patients", _columnsPatients, _foreignKeysPatients, _indicesPatients);
        final TableInfo _existingPatients = TableInfo.read(db, "patients");
        if (!_infoPatients.equals(_existingPatients)) {
          return new RoomOpenHelper.ValidationResult(false, "patients(com.example.telephases.data.local.entities.PatientEntity).\n"
                  + " Expected:\n" + _infoPatients + "\n"
                  + " Found:\n" + _existingPatients);
        }
        final HashMap<String, TableInfo.Column> _columnsExams = new HashMap<String, TableInfo.Column>(23);
        _columnsExams.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("patient_id", new TableInfo.Column("patient_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("tipo_examen_nombre", new TableInfo.Column("tipo_examen_nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("tipo_examen_id", new TableInfo.Column("tipo_examen_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("titulo", new TableInfo.Column("titulo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("valor", new TableInfo.Column("valor", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("unidad", new TableInfo.Column("unidad", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("observaciones", new TableInfo.Column("observaciones", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("datos_adicionales", new TableInfo.Column("datos_adicionales", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("fecha_creacion", new TableInfo.Column("fecha_creacion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("fecha_modificacion", new TableInfo.Column("fecha_modificacion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("activo", new TableInfo.Column("activo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("estado_codigo", new TableInfo.Column("estado_codigo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("estado_nombre", new TableInfo.Column("estado_nombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("estado_emoji", new TableInfo.Column("estado_emoji", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("estado_color", new TableInfo.Column("estado_color", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("estado_descripcion", new TableInfo.Column("estado_descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("estado_nivel_urgencia", new TableInfo.Column("estado_nivel_urgencia", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("sincronizado", new TableInfo.Column("sincronizado", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("fecha_ultima_sincronizacion", new TableInfo.Column("fecha_ultima_sincronizacion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("modificado_localmente", new TableInfo.Column("modificado_localmente", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("fecha_modificacion_local", new TableInfo.Column("fecha_modificacion_local", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("server_id", new TableInfo.Column("server_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExams = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysExams.add(new TableInfo.ForeignKey("patients", "CASCADE", "NO ACTION", Arrays.asList("patient_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExams = new HashSet<TableInfo.Index>(4);
        _indicesExams.add(new TableInfo.Index("index_exams_patient_id", false, Arrays.asList("patient_id"), Arrays.asList("ASC")));
        _indicesExams.add(new TableInfo.Index("index_exams_tipo_examen_nombre", false, Arrays.asList("tipo_examen_nombre"), Arrays.asList("ASC")));
        _indicesExams.add(new TableInfo.Index("index_exams_fecha_creacion", false, Arrays.asList("fecha_creacion"), Arrays.asList("ASC")));
        _indicesExams.add(new TableInfo.Index("index_exams_sincronizado", false, Arrays.asList("sincronizado"), Arrays.asList("ASC")));
        final TableInfo _infoExams = new TableInfo("exams", _columnsExams, _foreignKeysExams, _indicesExams);
        final TableInfo _existingExams = TableInfo.read(db, "exams");
        if (!_infoExams.equals(_existingExams)) {
          return new RoomOpenHelper.ValidationResult(false, "exams(com.example.telephases.data.local.entities.ExamEntity).\n"
                  + " Expected:\n" + _infoExams + "\n"
                  + " Found:\n" + _existingExams);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(26);
        _columnsUsers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password_hash", new TableInfo.Column("password_hash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("primer_nombre", new TableInfo.Column("primer_nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("segundo_nombre", new TableInfo.Column("segundo_nombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("primer_apellido", new TableInfo.Column("primer_apellido", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("segundo_apellido", new TableInfo.Column("segundo_apellido", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("tipo_documento_id", new TableInfo.Column("tipo_documento_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("numero_documento", new TableInfo.Column("numero_documento", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("telefono", new TableInfo.Column("telefono", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("direccion", new TableInfo.Column("direccion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("ciudad_id", new TableInfo.Column("ciudad_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fecha_nacimiento", new TableInfo.Column("fecha_nacimiento", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("genero", new TableInfo.Column("genero", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("rol_id", new TableInfo.Column("rol_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("entidad_salud_id", new TableInfo.Column("entidad_salud_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fecha_registro", new TableInfo.Column("fecha_registro", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("activo", new TableInfo.Column("activo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("token_actual", new TableInfo.Column("token_actual", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fecha_expiracion_token", new TableInfo.Column("fecha_expiracion_token", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("ultimo_login", new TableInfo.Column("ultimo_login", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("sincronizado", new TableInfo.Column("sincronizado", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fecha_ultima_sincronizacion", new TableInfo.Column("fecha_ultima_sincronizacion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("modificado_localmente", new TableInfo.Column("modificado_localmente", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fecha_modificacion_local", new TableInfo.Column("fecha_modificacion_local", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(4);
        _indicesUsers.add(new TableInfo.Index("index_users_username", true, Arrays.asList("username"), Arrays.asList("ASC")));
        _indicesUsers.add(new TableInfo.Index("index_users_email", true, Arrays.asList("email"), Arrays.asList("ASC")));
        _indicesUsers.add(new TableInfo.Index("index_users_numero_documento", true, Arrays.asList("numero_documento"), Arrays.asList("ASC")));
        _indicesUsers.add(new TableInfo.Index("index_users_entidad_salud_id", false, Arrays.asList("entidad_salud_id"), Arrays.asList("ASC")));
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.example.telephases.data.local.entities.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsExamTypes = new HashMap<String, TableInfo.Column>(10);
        _columnsExamTypes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExamTypes.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExamTypes.put("descripcion", new TableInfo.Column("descripcion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExamTypes.put("activo", new TableInfo.Column("activo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExamTypes.put("fecha_creacion", new TableInfo.Column("fecha_creacion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExamTypes.put("unidad_default", new TableInfo.Column("unidad_default", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExamTypes.put("valor_minimo", new TableInfo.Column("valor_minimo", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExamTypes.put("valor_maximo", new TableInfo.Column("valor_maximo", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExamTypes.put("requiere_dispositivo_ble", new TableInfo.Column("requiere_dispositivo_ble", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExamTypes.put("icono", new TableInfo.Column("icono", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExamTypes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExamTypes = new HashSet<TableInfo.Index>(1);
        _indicesExamTypes.add(new TableInfo.Index("index_exam_types_nombre", true, Arrays.asList("nombre"), Arrays.asList("ASC")));
        final TableInfo _infoExamTypes = new TableInfo("exam_types", _columnsExamTypes, _foreignKeysExamTypes, _indicesExamTypes);
        final TableInfo _existingExamTypes = TableInfo.read(db, "exam_types");
        if (!_infoExamTypes.equals(_existingExamTypes)) {
          return new RoomOpenHelper.ValidationResult(false, "exam_types(com.example.telephases.data.local.entities.TipoExamenEntity).\n"
                  + " Expected:\n" + _infoExamTypes + "\n"
                  + " Found:\n" + _existingExamTypes);
        }
        final HashMap<String, TableInfo.Column> _columnsSyncMetadata = new HashMap<String, TableInfo.Column>(4);
        _columnsSyncMetadata.put("key", new TableInfo.Column("key", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncMetadata.put("value", new TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncMetadata.put("last_updated", new TableInfo.Column("last_updated", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncMetadata.put("sync_status", new TableInfo.Column("sync_status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSyncMetadata = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSyncMetadata = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSyncMetadata = new TableInfo("sync_metadata", _columnsSyncMetadata, _foreignKeysSyncMetadata, _indicesSyncMetadata);
        final TableInfo _existingSyncMetadata = TableInfo.read(db, "sync_metadata");
        if (!_infoSyncMetadata.equals(_existingSyncMetadata)) {
          return new RoomOpenHelper.ValidationResult(false, "sync_metadata(com.example.telephases.data.local.entities.SyncMetadataEntity).\n"
                  + " Expected:\n" + _infoSyncMetadata + "\n"
                  + " Found:\n" + _existingSyncMetadata);
        }
        final HashMap<String, TableInfo.Column> _columnsEstadoCita = new HashMap<String, TableInfo.Column>(3);
        _columnsEstadoCita.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstadoCita.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstadoCita.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEstadoCita = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEstadoCita = new HashSet<TableInfo.Index>(1);
        _indicesEstadoCita.add(new TableInfo.Index("index_estado_cita_nombre", true, Arrays.asList("nombre"), Arrays.asList("ASC")));
        final TableInfo _infoEstadoCita = new TableInfo("estado_cita", _columnsEstadoCita, _foreignKeysEstadoCita, _indicesEstadoCita);
        final TableInfo _existingEstadoCita = TableInfo.read(db, "estado_cita");
        if (!_infoEstadoCita.equals(_existingEstadoCita)) {
          return new RoomOpenHelper.ValidationResult(false, "estado_cita(com.example.telephases.data.local.entities.EstadoCitaEntity).\n"
                  + " Expected:\n" + _infoEstadoCita + "\n"
                  + " Found:\n" + _existingEstadoCita);
        }
        final HashMap<String, TableInfo.Column> _columnsCitas = new HashMap<String, TableInfo.Column>(9);
        _columnsCitas.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitas.put("paciente_id", new TableInfo.Column("paciente_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitas.put("creado_por_usuario_id", new TableInfo.Column("creado_por_usuario_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitas.put("estado_cita_id", new TableInfo.Column("estado_cita_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitas.put("fecha_cita", new TableInfo.Column("fecha_cita", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitas.put("observaciones_admin", new TableInfo.Column("observaciones_admin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitas.put("observaciones_paciente", new TableInfo.Column("observaciones_paciente", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitas.put("fecha_creacion", new TableInfo.Column("fecha_creacion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitas.put("fecha_modificacion", new TableInfo.Column("fecha_modificacion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCitas = new HashSet<TableInfo.ForeignKey>(3);
        _foreignKeysCitas.add(new TableInfo.ForeignKey("patients", "NO ACTION", "NO ACTION", Arrays.asList("paciente_id"), Arrays.asList("id")));
        _foreignKeysCitas.add(new TableInfo.ForeignKey("users", "NO ACTION", "NO ACTION", Arrays.asList("creado_por_usuario_id"), Arrays.asList("id")));
        _foreignKeysCitas.add(new TableInfo.ForeignKey("estado_cita", "NO ACTION", "NO ACTION", Arrays.asList("estado_cita_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCitas = new HashSet<TableInfo.Index>(3);
        _indicesCitas.add(new TableInfo.Index("index_citas_paciente_id", false, Arrays.asList("paciente_id"), Arrays.asList("ASC")));
        _indicesCitas.add(new TableInfo.Index("index_citas_creado_por_usuario_id", false, Arrays.asList("creado_por_usuario_id"), Arrays.asList("ASC")));
        _indicesCitas.add(new TableInfo.Index("index_citas_estado_cita_id", false, Arrays.asList("estado_cita_id"), Arrays.asList("ASC")));
        final TableInfo _infoCitas = new TableInfo("citas", _columnsCitas, _foreignKeysCitas, _indicesCitas);
        final TableInfo _existingCitas = TableInfo.read(db, "citas");
        if (!_infoCitas.equals(_existingCitas)) {
          return new RoomOpenHelper.ValidationResult(false, "citas(com.example.telephases.data.local.entities.CitaEntity).\n"
                  + " Expected:\n" + _infoCitas + "\n"
                  + " Found:\n" + _existingCitas);
        }
        final HashMap<String, TableInfo.Column> _columnsCitaExamenesPrevistos = new HashMap<String, TableInfo.Column>(4);
        _columnsCitaExamenesPrevistos.put("cita_id", new TableInfo.Column("cita_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitaExamenesPrevistos.put("tipo_examen_id", new TableInfo.Column("tipo_examen_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitaExamenesPrevistos.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCitaExamenesPrevistos.put("descripcion", new TableInfo.Column("descripcion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCitaExamenesPrevistos = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysCitaExamenesPrevistos.add(new TableInfo.ForeignKey("citas", "CASCADE", "NO ACTION", Arrays.asList("cita_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCitaExamenesPrevistos = new HashSet<TableInfo.Index>(1);
        _indicesCitaExamenesPrevistos.add(new TableInfo.Index("index_cita_examenes_previstos_tipo_examen_id", false, Arrays.asList("tipo_examen_id"), Arrays.asList("ASC")));
        final TableInfo _infoCitaExamenesPrevistos = new TableInfo("cita_examenes_previstos", _columnsCitaExamenesPrevistos, _foreignKeysCitaExamenesPrevistos, _indicesCitaExamenesPrevistos);
        final TableInfo _existingCitaExamenesPrevistos = TableInfo.read(db, "cita_examenes_previstos");
        if (!_infoCitaExamenesPrevistos.equals(_existingCitaExamenesPrevistos)) {
          return new RoomOpenHelper.ValidationResult(false, "cita_examenes_previstos(com.example.telephases.data.local.entities.CitaExamenPrevistoEntity).\n"
                  + " Expected:\n" + _infoCitaExamenesPrevistos + "\n"
                  + " Found:\n" + _existingCitaExamenesPrevistos);
        }
        final HashMap<String, TableInfo.Column> _columnsEntidadesSalud = new HashMap<String, TableInfo.Column>(10);
        _columnsEntidadesSalud.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEntidadesSalud.put("nombreEntidad", new TableInfo.Column("nombreEntidad", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEntidadesSalud.put("nit", new TableInfo.Column("nit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEntidadesSalud.put("contactoPrincipalNombre", new TableInfo.Column("contactoPrincipalNombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEntidadesSalud.put("contactoPrincipalEmail", new TableInfo.Column("contactoPrincipalEmail", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEntidadesSalud.put("contactoPrincipalTelefono", new TableInfo.Column("contactoPrincipalTelefono", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEntidadesSalud.put("configuracionJson", new TableInfo.Column("configuracionJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEntidadesSalud.put("fechaRegistro", new TableInfo.Column("fechaRegistro", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEntidadesSalud.put("activa", new TableInfo.Column("activa", "INTEGER", true, 0, "1", TableInfo.CREATED_FROM_ENTITY));
        _columnsEntidadesSalud.put("serverId", new TableInfo.Column("serverId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEntidadesSalud = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEntidadesSalud = new HashSet<TableInfo.Index>(3);
        _indicesEntidadesSalud.add(new TableInfo.Index("index_entidades_salud_nit", false, Arrays.asList("nit"), Arrays.asList("ASC")));
        _indicesEntidadesSalud.add(new TableInfo.Index("index_entidades_salud_activa", false, Arrays.asList("activa"), Arrays.asList("ASC")));
        _indicesEntidadesSalud.add(new TableInfo.Index("index_entidades_salud_serverId", false, Arrays.asList("serverId"), Arrays.asList("ASC")));
        final TableInfo _infoEntidadesSalud = new TableInfo("entidades_salud", _columnsEntidadesSalud, _foreignKeysEntidadesSalud, _indicesEntidadesSalud);
        final TableInfo _existingEntidadesSalud = TableInfo.read(db, "entidades_salud");
        if (!_infoEntidadesSalud.equals(_existingEntidadesSalud)) {
          return new RoomOpenHelper.ValidationResult(false, "entidades_salud(com.example.telephases.data.local.entities.EntidadSaludEntity).\n"
                  + " Expected:\n" + _infoEntidadesSalud + "\n"
                  + " Found:\n" + _existingEntidadesSalud);
        }
        final HashMap<String, TableInfo.Column> _columnsSedes = new HashMap<String, TableInfo.Column>(9);
        _columnsSedes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSedes.put("entidadSaludId", new TableInfo.Column("entidadSaludId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSedes.put("nombreSede", new TableInfo.Column("nombreSede", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSedes.put("direccion", new TableInfo.Column("direccion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSedes.put("telefono", new TableInfo.Column("telefono", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSedes.put("ciudad", new TableInfo.Column("ciudad", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSedes.put("responsableSedeNombre", new TableInfo.Column("responsableSedeNombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSedes.put("activa", new TableInfo.Column("activa", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSedes.put("serverId", new TableInfo.Column("serverId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSedes = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSedes.add(new TableInfo.ForeignKey("entidades_salud", "CASCADE", "NO ACTION", Arrays.asList("entidadSaludId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSedes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSedes = new TableInfo("sedes", _columnsSedes, _foreignKeysSedes, _indicesSedes);
        final TableInfo _existingSedes = TableInfo.read(db, "sedes");
        if (!_infoSedes.equals(_existingSedes)) {
          return new RoomOpenHelper.ValidationResult(false, "sedes(com.example.telephases.data.local.entities.SedeEntity).\n"
                  + " Expected:\n" + _infoSedes + "\n"
                  + " Found:\n" + _existingSedes);
        }
        final HashMap<String, TableInfo.Column> _columnsUsuarioSedes = new HashMap<String, TableInfo.Column>(4);
        _columnsUsuarioSedes.put("usuarioId", new TableInfo.Column("usuarioId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarioSedes.put("sedeId", new TableInfo.Column("sedeId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarioSedes.put("fechaAsignacion", new TableInfo.Column("fechaAsignacion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarioSedes.put("activa", new TableInfo.Column("activa", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsuarioSedes = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysUsuarioSedes.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("usuarioId"), Arrays.asList("id")));
        _foreignKeysUsuarioSedes.add(new TableInfo.ForeignKey("sedes", "CASCADE", "NO ACTION", Arrays.asList("sedeId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesUsuarioSedes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsuarioSedes = new TableInfo("usuario_sedes", _columnsUsuarioSedes, _foreignKeysUsuarioSedes, _indicesUsuarioSedes);
        final TableInfo _existingUsuarioSedes = TableInfo.read(db, "usuario_sedes");
        if (!_infoUsuarioSedes.equals(_existingUsuarioSedes)) {
          return new RoomOpenHelper.ValidationResult(false, "usuario_sedes(com.example.telephases.data.local.entities.UsuarioSedeEntity).\n"
                  + " Expected:\n" + _infoUsuarioSedes + "\n"
                  + " Found:\n" + _existingUsuarioSedes);
        }
        final HashMap<String, TableInfo.Column> _columnsMaletas = new HashMap<String, TableInfo.Column>(9);
        _columnsMaletas.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaletas.put("identificadorInvima", new TableInfo.Column("identificadorInvima", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaletas.put("nombreMaleta", new TableInfo.Column("nombreMaleta", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaletas.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaletas.put("asignadaAUsuarioId", new TableInfo.Column("asignadaAUsuarioId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaletas.put("entidadSaludId", new TableInfo.Column("entidadSaludId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaletas.put("ultimaRevision", new TableInfo.Column("ultimaRevision", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaletas.put("activa", new TableInfo.Column("activa", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaletas.put("serverId", new TableInfo.Column("serverId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMaletas = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysMaletas.add(new TableInfo.ForeignKey("users", "SET NULL", "NO ACTION", Arrays.asList("asignadaAUsuarioId"), Arrays.asList("id")));
        _foreignKeysMaletas.add(new TableInfo.ForeignKey("entidades_salud", "SET NULL", "NO ACTION", Arrays.asList("entidadSaludId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMaletas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMaletas = new TableInfo("maletas", _columnsMaletas, _foreignKeysMaletas, _indicesMaletas);
        final TableInfo _existingMaletas = TableInfo.read(db, "maletas");
        if (!_infoMaletas.equals(_existingMaletas)) {
          return new RoomOpenHelper.ValidationResult(false, "maletas(com.example.telephases.data.local.entities.MaletaEntity).\n"
                  + " Expected:\n" + _infoMaletas + "\n"
                  + " Found:\n" + _existingMaletas);
        }
        final HashMap<String, TableInfo.Column> _columnsDispositivos = new HashMap<String, TableInfo.Column>(8);
        _columnsDispositivos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDispositivos.put("tipoDispositivo", new TableInfo.Column("tipoDispositivo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDispositivos.put("marca", new TableInfo.Column("marca", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDispositivos.put("modelo", new TableInfo.Column("modelo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDispositivos.put("serial", new TableInfo.Column("serial", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDispositivos.put("fechaAdquisicion", new TableInfo.Column("fechaAdquisicion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDispositivos.put("activo", new TableInfo.Column("activo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDispositivos.put("serverId", new TableInfo.Column("serverId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDispositivos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDispositivos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDispositivos = new TableInfo("dispositivos", _columnsDispositivos, _foreignKeysDispositivos, _indicesDispositivos);
        final TableInfo _existingDispositivos = TableInfo.read(db, "dispositivos");
        if (!_infoDispositivos.equals(_existingDispositivos)) {
          return new RoomOpenHelper.ValidationResult(false, "dispositivos(com.example.telephases.data.local.entities.DispositivoEntity).\n"
                  + " Expected:\n" + _infoDispositivos + "\n"
                  + " Found:\n" + _existingDispositivos);
        }
        final HashMap<String, TableInfo.Column> _columnsMaletaDispositivos = new HashMap<String, TableInfo.Column>(2);
        _columnsMaletaDispositivos.put("maletaId", new TableInfo.Column("maletaId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaletaDispositivos.put("dispositivoId", new TableInfo.Column("dispositivoId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMaletaDispositivos = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysMaletaDispositivos.add(new TableInfo.ForeignKey("maletas", "CASCADE", "NO ACTION", Arrays.asList("maletaId"), Arrays.asList("id")));
        _foreignKeysMaletaDispositivos.add(new TableInfo.ForeignKey("dispositivos", "CASCADE", "NO ACTION", Arrays.asList("dispositivoId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMaletaDispositivos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMaletaDispositivos = new TableInfo("maleta_dispositivos", _columnsMaletaDispositivos, _foreignKeysMaletaDispositivos, _indicesMaletaDispositivos);
        final TableInfo _existingMaletaDispositivos = TableInfo.read(db, "maleta_dispositivos");
        if (!_infoMaletaDispositivos.equals(_existingMaletaDispositivos)) {
          return new RoomOpenHelper.ValidationResult(false, "maleta_dispositivos(com.example.telephases.data.local.entities.MaletaDispositivoEntity).\n"
                  + " Expected:\n" + _infoMaletaDispositivos + "\n"
                  + " Found:\n" + _existingMaletaDispositivos);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "5dcda6e61ac54fe58c9e21058bd387ca", "279ff6f0fa7e212fb46724b5da8ff9b2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "patients","exams","users","exam_types","sync_metadata","estado_cita","citas","cita_examenes_previstos","entidades_salud","sedes","usuario_sedes","maletas","dispositivos","maleta_dispositivos");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `patients`");
      _db.execSQL("DELETE FROM `exams`");
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `exam_types`");
      _db.execSQL("DELETE FROM `sync_metadata`");
      _db.execSQL("DELETE FROM `citas`");
      _db.execSQL("DELETE FROM `estado_cita`");
      _db.execSQL("DELETE FROM `cita_examenes_previstos`");
      _db.execSQL("DELETE FROM `entidades_salud`");
      _db.execSQL("DELETE FROM `sedes`");
      _db.execSQL("DELETE FROM `usuario_sedes`");
      _db.execSQL("DELETE FROM `maletas`");
      _db.execSQL("DELETE FROM `dispositivos`");
      _db.execSQL("DELETE FROM `maleta_dispositivos`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PatientDao.class, PatientDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExamDao.class, ExamDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TipoExamenDao.class, TipoExamenDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SyncMetadataDao.class, SyncMetadataDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CitaDao.class, CitaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EntidadSaludDao.class, EntidadSaludDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SedeDao.class, SedeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UsuarioSedeDao.class, UsuarioSedeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MaletaDao.class, MaletaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DispositivoDao.class, DispositivoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MaletaDispositivoDao.class, MaletaDispositivoDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PatientDao patientDao() {
    if (_patientDao != null) {
      return _patientDao;
    } else {
      synchronized(this) {
        if(_patientDao == null) {
          _patientDao = new PatientDao_Impl(this);
        }
        return _patientDao;
      }
    }
  }

  @Override
  public ExamDao examDao() {
    if (_examDao != null) {
      return _examDao;
    } else {
      synchronized(this) {
        if(_examDao == null) {
          _examDao = new ExamDao_Impl(this);
        }
        return _examDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public TipoExamenDao tipoExamenDao() {
    if (_tipoExamenDao != null) {
      return _tipoExamenDao;
    } else {
      synchronized(this) {
        if(_tipoExamenDao == null) {
          _tipoExamenDao = new TipoExamenDao_Impl(this);
        }
        return _tipoExamenDao;
      }
    }
  }

  @Override
  public SyncMetadataDao syncMetadataDao() {
    if (_syncMetadataDao != null) {
      return _syncMetadataDao;
    } else {
      synchronized(this) {
        if(_syncMetadataDao == null) {
          _syncMetadataDao = new SyncMetadataDao_Impl(this);
        }
        return _syncMetadataDao;
      }
    }
  }

  @Override
  public CitaDao citaDao() {
    if (_citaDao != null) {
      return _citaDao;
    } else {
      synchronized(this) {
        if(_citaDao == null) {
          _citaDao = new CitaDao_Impl(this);
        }
        return _citaDao;
      }
    }
  }

  @Override
  public EntidadSaludDao entidadSaludDao() {
    if (_entidadSaludDao != null) {
      return _entidadSaludDao;
    } else {
      synchronized(this) {
        if(_entidadSaludDao == null) {
          _entidadSaludDao = new EntidadSaludDao_Impl(this);
        }
        return _entidadSaludDao;
      }
    }
  }

  @Override
  public SedeDao sedeDao() {
    if (_sedeDao != null) {
      return _sedeDao;
    } else {
      synchronized(this) {
        if(_sedeDao == null) {
          _sedeDao = new SedeDao_Impl(this);
        }
        return _sedeDao;
      }
    }
  }

  @Override
  public UsuarioSedeDao usuarioSedeDao() {
    if (_usuarioSedeDao != null) {
      return _usuarioSedeDao;
    } else {
      synchronized(this) {
        if(_usuarioSedeDao == null) {
          _usuarioSedeDao = new UsuarioSedeDao_Impl(this);
        }
        return _usuarioSedeDao;
      }
    }
  }

  @Override
  public MaletaDao maletaDao() {
    if (_maletaDao != null) {
      return _maletaDao;
    } else {
      synchronized(this) {
        if(_maletaDao == null) {
          _maletaDao = new MaletaDao_Impl(this);
        }
        return _maletaDao;
      }
    }
  }

  @Override
  public DispositivoDao dispositivoDao() {
    if (_dispositivoDao != null) {
      return _dispositivoDao;
    } else {
      synchronized(this) {
        if(_dispositivoDao == null) {
          _dispositivoDao = new DispositivoDao_Impl(this);
        }
        return _dispositivoDao;
      }
    }
  }

  @Override
  public MaletaDispositivoDao maletaDispositivoDao() {
    if (_maletaDispositivoDao != null) {
      return _maletaDispositivoDao;
    } else {
      synchronized(this) {
        if(_maletaDispositivoDao == null) {
          _maletaDispositivoDao = new MaletaDispositivoDao_Impl(this);
        }
        return _maletaDispositivoDao;
      }
    }
  }
}
