package com.example.telephases.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.telephases.data.local.database.Converters;
import com.example.telephases.data.local.entities.PatientEntity;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PatientDao_Impl implements PatientDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PatientEntity> __insertionAdapterOfPatientEntity;

  private final EntityDeletionOrUpdateAdapter<PatientEntity> __deletionAdapterOfPatientEntity;

  private final EntityDeletionOrUpdateAdapter<PatientEntity> __updateAdapterOfPatientEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeletePatient;

  private final SharedSQLiteStatement __preparedStmtOfMarkPatientAsSynced;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePatientServerId;

  private final SharedSQLiteStatement __preparedStmtOfCleanupOldDeletedPatients;

  private final Converters __converters = new Converters();

  public PatientDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPatientEntity = new EntityInsertionAdapter<PatientEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `patients` (`id`,`primer_nombre`,`segundo_nombre`,`primer_apellido`,`segundo_apellido`,`tipo_documento_id`,`numero_documento`,`email`,`telefono`,`direccion`,`ciudad_id`,`fecha_nacimiento`,`genero`,`tipo_identificacion`,`estado_civil`,`pais`,`municipio`,`departamento`,`entidad_salud_id`,`fecha_registro`,`activo`,`sincronizado`,`server_id`,`fecha_ultima_sincronizacion`,`modificado_localmente`,`fecha_modificacion_local`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PatientEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getPrimerNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPrimerNombre());
        }
        if (entity.getSegundoNombre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSegundoNombre());
        }
        if (entity.getPrimerApellido() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPrimerApellido());
        }
        if (entity.getSegundoApellido() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSegundoApellido());
        }
        statement.bindLong(6, entity.getTipoDocumentoId());
        if (entity.getNumeroDocumento() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNumeroDocumento());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getEmail());
        }
        if (entity.getTelefono() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getTelefono());
        }
        if (entity.getDireccion() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getDireccion());
        }
        if (entity.getCiudadId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getCiudadId());
        }
        if (entity.getFechaNacimiento() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getFechaNacimiento());
        }
        if (entity.getGenero() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getGenero());
        }
        if (entity.getTipoIdentificacion() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getTipoIdentificacion());
        }
        if (entity.getEstadoCivil() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getEstadoCivil());
        }
        if (entity.getPais() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getPais());
        }
        if (entity.getMunicipio() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getMunicipio());
        }
        if (entity.getDepartamento() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getDepartamento());
        }
        if (entity.getEntidadSaludId() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getEntidadSaludId());
        }
        if (entity.getFechaRegistro() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getFechaRegistro());
        }
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(21, _tmp);
        final int _tmp_1 = entity.getSincronizado() ? 1 : 0;
        statement.bindLong(22, _tmp_1);
        if (entity.getServerId() == null) {
          statement.bindNull(23);
        } else {
          statement.bindString(23, entity.getServerId());
        }
        if (entity.getFechaUltimaSincronizacion() == null) {
          statement.bindNull(24);
        } else {
          statement.bindString(24, entity.getFechaUltimaSincronizacion());
        }
        final int _tmp_2 = entity.getModificadoLocalmente() ? 1 : 0;
        statement.bindLong(25, _tmp_2);
        if (entity.getFechaModificacionLocal() == null) {
          statement.bindNull(26);
        } else {
          statement.bindString(26, entity.getFechaModificacionLocal());
        }
      }
    };
    this.__deletionAdapterOfPatientEntity = new EntityDeletionOrUpdateAdapter<PatientEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `patients` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PatientEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfPatientEntity = new EntityDeletionOrUpdateAdapter<PatientEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `patients` SET `id` = ?,`primer_nombre` = ?,`segundo_nombre` = ?,`primer_apellido` = ?,`segundo_apellido` = ?,`tipo_documento_id` = ?,`numero_documento` = ?,`email` = ?,`telefono` = ?,`direccion` = ?,`ciudad_id` = ?,`fecha_nacimiento` = ?,`genero` = ?,`tipo_identificacion` = ?,`estado_civil` = ?,`pais` = ?,`municipio` = ?,`departamento` = ?,`entidad_salud_id` = ?,`fecha_registro` = ?,`activo` = ?,`sincronizado` = ?,`server_id` = ?,`fecha_ultima_sincronizacion` = ?,`modificado_localmente` = ?,`fecha_modificacion_local` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PatientEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getPrimerNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPrimerNombre());
        }
        if (entity.getSegundoNombre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSegundoNombre());
        }
        if (entity.getPrimerApellido() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPrimerApellido());
        }
        if (entity.getSegundoApellido() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSegundoApellido());
        }
        statement.bindLong(6, entity.getTipoDocumentoId());
        if (entity.getNumeroDocumento() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNumeroDocumento());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getEmail());
        }
        if (entity.getTelefono() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getTelefono());
        }
        if (entity.getDireccion() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getDireccion());
        }
        if (entity.getCiudadId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getCiudadId());
        }
        if (entity.getFechaNacimiento() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getFechaNacimiento());
        }
        if (entity.getGenero() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getGenero());
        }
        if (entity.getTipoIdentificacion() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getTipoIdentificacion());
        }
        if (entity.getEstadoCivil() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getEstadoCivil());
        }
        if (entity.getPais() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getPais());
        }
        if (entity.getMunicipio() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getMunicipio());
        }
        if (entity.getDepartamento() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getDepartamento());
        }
        if (entity.getEntidadSaludId() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getEntidadSaludId());
        }
        if (entity.getFechaRegistro() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getFechaRegistro());
        }
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(21, _tmp);
        final int _tmp_1 = entity.getSincronizado() ? 1 : 0;
        statement.bindLong(22, _tmp_1);
        if (entity.getServerId() == null) {
          statement.bindNull(23);
        } else {
          statement.bindString(23, entity.getServerId());
        }
        if (entity.getFechaUltimaSincronizacion() == null) {
          statement.bindNull(24);
        } else {
          statement.bindString(24, entity.getFechaUltimaSincronizacion());
        }
        final int _tmp_2 = entity.getModificadoLocalmente() ? 1 : 0;
        statement.bindLong(25, _tmp_2);
        if (entity.getFechaModificacionLocal() == null) {
          statement.bindNull(26);
        } else {
          statement.bindString(26, entity.getFechaModificacionLocal());
        }
        if (entity.getId() == null) {
          statement.bindNull(27);
        } else {
          statement.bindString(27, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeletePatient = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE patients SET activo = 0, modificado_localmente = 1, fecha_modificacion_local = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkPatientAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE patients \n"
                + "        SET sincronizado = 1, \n"
                + "            fecha_ultima_sincronizacion = ?,\n"
                + "            modificado_localmente = 0,\n"
                + "            fecha_modificacion_local = NULL\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePatientServerId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE patients SET server_id = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfCleanupOldDeletedPatients = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        DELETE FROM patients \n"
                + "        WHERE activo = 0 \n"
                + "        AND datetime(fecha_modificacion_local) < datetime('now', '-30 days')\n"
                + "    ";
        return _query;
      }
    };
  }

  @Override
  public Object insertPatient(final PatientEntity patient, final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPatientEntity.insertAndReturnId(patient);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertPatients(final List<PatientEntity> patients,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPatientEntity.insert(patients);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deletePatientPermanently(final PatientEntity patient,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPatientEntity.handle(patient);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object updatePatient(final PatientEntity patient, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPatientEntity.handle(patient);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deletePatient(final String patientId, final String timestamp,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePatient.acquire();
        int _argIndex = 1;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
        }
        _argIndex = 2;
        if (patientId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, patientId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeletePatient.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object markPatientAsSynced(final String patientId, final String timestamp,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkPatientAsSynced.acquire();
        int _argIndex = 1;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
        }
        _argIndex = 2;
        if (patientId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, patientId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkPatientAsSynced.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object updatePatientServerId(final String patientId, final String serverId,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePatientServerId.acquire();
        int _argIndex = 1;
        if (serverId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, serverId);
        }
        _argIndex = 2;
        if (patientId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, patientId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdatePatientServerId.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object cleanupOldDeletedPatients(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCleanupOldDeletedPatients.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfCleanupOldDeletedPatients.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object getPatientById(final String patientId,
      final Continuation<? super PatientEntity> arg1) {
    final String _sql = "SELECT * FROM patients WHERE id = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PatientEntity>() {
      @Override
      @Nullable
      public PatientEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final PatientEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _result = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object getPatientByDocument(final String numeroDocumento,
      final Continuation<? super PatientEntity> arg1) {
    final String _sql = "SELECT * FROM patients WHERE numero_documento = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (numeroDocumento == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, numeroDocumento);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PatientEntity>() {
      @Override
      @Nullable
      public PatientEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final PatientEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _result = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object searchPatientsByName(final String searchTerm,
      final Continuation<? super List<PatientEntity>> arg1) {
    final String _sql = "\n"
            + "        SELECT * FROM patients \n"
            + "        WHERE activo = 1 \n"
            + "        AND (primer_nombre LIKE '%' || ? || '%' \n"
            + "             OR primer_apellido LIKE '%' || ? || '%'\n"
            + "             OR segundo_nombre LIKE '%' || ? || '%'\n"
            + "             OR segundo_apellido LIKE '%' || ? || '%')\n"
            + "        ORDER BY primer_nombre ASC, primer_apellido ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (searchTerm == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTerm);
    }
    _argIndex = 2;
    if (searchTerm == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTerm);
    }
    _argIndex = 3;
    if (searchTerm == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTerm);
    }
    _argIndex = 4;
    if (searchTerm == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTerm);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _item = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object getAllPatients(final Continuation<? super List<PatientEntity>> arg0) {
    final String _sql = "SELECT * FROM patients WHERE activo = 1 ORDER BY fecha_registro DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _item = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Flow<List<PatientEntity>> getAllPatientsFlow() {
    final String _sql = "SELECT * FROM patients WHERE activo = 1 ORDER BY fecha_registro DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"patients"}, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _item = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRecentPatients(final int limit,
      final Continuation<? super List<PatientEntity>> arg1) {
    final String _sql = "SELECT * FROM patients WHERE activo = 1 ORDER BY fecha_registro DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _item = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object getUnsyncedPatients(final Continuation<? super List<PatientEntity>> arg0) {
    final String _sql = "SELECT * FROM patients WHERE sincronizado = 0 AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _item = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object getLocallyModifiedPatients(final Continuation<? super List<PatientEntity>> arg0) {
    final String _sql = "SELECT * FROM patients WHERE modificado_localmente = 1 AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _item = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object getTotalPatientsCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM patients WHERE activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object getUnsyncedPatientsCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM patients WHERE sincronizado = 0 AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object getTodayPatientsCount(final Continuation<? super Integer> arg0) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM patients \n"
            + "        WHERE activo = 1 \n"
            + "        AND date(fecha_registro) = date('now', 'localtime')\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object getLastRegisteredPatient(final Continuation<? super PatientEntity> arg0) {
    final String _sql = "SELECT * FROM patients WHERE activo = 1 ORDER BY fecha_registro DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PatientEntity>() {
      @Override
      @Nullable
      public PatientEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final PatientEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _result = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object getDuplicatePatients(final Continuation<? super List<PatientEntity>> arg0) {
    final String _sql = "\n"
            + "        SELECT * FROM patients p1\n"
            + "        WHERE EXISTS (\n"
            + "            SELECT 1 FROM patients p2 \n"
            + "            WHERE p2.numero_documento = p1.numero_documento \n"
            + "            AND p2.id != p1.id \n"
            + "            AND p2.activo = 1\n"
            + "        ) AND p1.activo = 1\n"
            + "        ORDER BY p1.fecha_registro DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _item = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object existsPatientWithDocument(final String numeroDocumento,
      final Continuation<? super Boolean> arg1) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM patients WHERE numero_documento = ? AND activo = 1)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (numeroDocumento == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, numeroDocumento);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = __converters.toBoolean(_tmp);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object searchPatientsWithFilters(final String searchTerm, final String genero,
      final Integer ciudadId, final int limit, final int offset,
      final Continuation<? super List<PatientEntity>> arg5) {
    final String _sql = "\n"
            + "        SELECT * FROM patients \n"
            + "        WHERE activo = 1\n"
            + "        AND (? IS NULL OR \n"
            + "             primer_nombre LIKE '%' || ? || '%' OR \n"
            + "             primer_apellido LIKE '%' || ? || '%' OR\n"
            + "             numero_documento LIKE '%' || ? || '%')\n"
            + "        AND (? IS NULL OR genero = ?)\n"
            + "        AND (? IS NULL OR ciudad_id = ?)\n"
            + "        ORDER BY fecha_registro DESC\n"
            + "        LIMIT ? OFFSET ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 10);
    int _argIndex = 1;
    if (searchTerm == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTerm);
    }
    _argIndex = 2;
    if (searchTerm == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTerm);
    }
    _argIndex = 3;
    if (searchTerm == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTerm);
    }
    _argIndex = 4;
    if (searchTerm == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTerm);
    }
    _argIndex = 5;
    if (genero == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, genero);
    }
    _argIndex = 6;
    if (genero == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, genero);
    }
    _argIndex = 7;
    if (ciudadId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, ciudadId);
    }
    _argIndex = 8;
    if (ciudadId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, ciudadId);
    }
    _argIndex = 9;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 10;
    _statement.bindLong(_argIndex, offset);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfTipoIdentificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_identificacion");
          final int _cursorIndexOfEstadoCivil = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_civil");
          final int _cursorIndexOfPais = CursorUtil.getColumnIndexOrThrow(_cursor, "pais");
          final int _cursorIndexOfMunicipio = CursorUtil.getColumnIndexOrThrow(_cursor, "municipio");
          final int _cursorIndexOfDepartamento = CursorUtil.getColumnIndexOrThrow(_cursor, "departamento");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPrimerNombre;
            if (_cursor.isNull(_cursorIndexOfPrimerNombre)) {
              _tmpPrimerNombre = null;
            } else {
              _tmpPrimerNombre = _cursor.getString(_cursorIndexOfPrimerNombre);
            }
            final String _tmpSegundoNombre;
            if (_cursor.isNull(_cursorIndexOfSegundoNombre)) {
              _tmpSegundoNombre = null;
            } else {
              _tmpSegundoNombre = _cursor.getString(_cursorIndexOfSegundoNombre);
            }
            final String _tmpPrimerApellido;
            if (_cursor.isNull(_cursorIndexOfPrimerApellido)) {
              _tmpPrimerApellido = null;
            } else {
              _tmpPrimerApellido = _cursor.getString(_cursorIndexOfPrimerApellido);
            }
            final String _tmpSegundoApellido;
            if (_cursor.isNull(_cursorIndexOfSegundoApellido)) {
              _tmpSegundoApellido = null;
            } else {
              _tmpSegundoApellido = _cursor.getString(_cursorIndexOfSegundoApellido);
            }
            final int _tmpTipoDocumentoId;
            _tmpTipoDocumentoId = _cursor.getInt(_cursorIndexOfTipoDocumentoId);
            final String _tmpNumeroDocumento;
            if (_cursor.isNull(_cursorIndexOfNumeroDocumento)) {
              _tmpNumeroDocumento = null;
            } else {
              _tmpNumeroDocumento = _cursor.getString(_cursorIndexOfNumeroDocumento);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final Integer _tmpCiudadId;
            if (_cursor.isNull(_cursorIndexOfCiudadId)) {
              _tmpCiudadId = null;
            } else {
              _tmpCiudadId = _cursor.getInt(_cursorIndexOfCiudadId);
            }
            final String _tmpFechaNacimiento;
            if (_cursor.isNull(_cursorIndexOfFechaNacimiento)) {
              _tmpFechaNacimiento = null;
            } else {
              _tmpFechaNacimiento = _cursor.getString(_cursorIndexOfFechaNacimiento);
            }
            final String _tmpGenero;
            if (_cursor.isNull(_cursorIndexOfGenero)) {
              _tmpGenero = null;
            } else {
              _tmpGenero = _cursor.getString(_cursorIndexOfGenero);
            }
            final String _tmpTipoIdentificacion;
            if (_cursor.isNull(_cursorIndexOfTipoIdentificacion)) {
              _tmpTipoIdentificacion = null;
            } else {
              _tmpTipoIdentificacion = _cursor.getString(_cursorIndexOfTipoIdentificacion);
            }
            final String _tmpEstadoCivil;
            if (_cursor.isNull(_cursorIndexOfEstadoCivil)) {
              _tmpEstadoCivil = null;
            } else {
              _tmpEstadoCivil = _cursor.getString(_cursorIndexOfEstadoCivil);
            }
            final String _tmpPais;
            if (_cursor.isNull(_cursorIndexOfPais)) {
              _tmpPais = null;
            } else {
              _tmpPais = _cursor.getString(_cursorIndexOfPais);
            }
            final String _tmpMunicipio;
            if (_cursor.isNull(_cursorIndexOfMunicipio)) {
              _tmpMunicipio = null;
            } else {
              _tmpMunicipio = _cursor.getString(_cursorIndexOfMunicipio);
            }
            final String _tmpDepartamento;
            if (_cursor.isNull(_cursorIndexOfDepartamento)) {
              _tmpDepartamento = null;
            } else {
              _tmpDepartamento = _cursor.getString(_cursorIndexOfDepartamento);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFechaUltimaSincronizacion;
            if (_cursor.isNull(_cursorIndexOfFechaUltimaSincronizacion)) {
              _tmpFechaUltimaSincronizacion = null;
            } else {
              _tmpFechaUltimaSincronizacion = _cursor.getString(_cursorIndexOfFechaUltimaSincronizacion);
            }
            final boolean _tmpModificadoLocalmente;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfModificadoLocalmente);
            _tmpModificadoLocalmente = _tmp_2 != 0;
            final String _tmpFechaModificacionLocal;
            if (_cursor.isNull(_cursorIndexOfFechaModificacionLocal)) {
              _tmpFechaModificacionLocal = null;
            } else {
              _tmpFechaModificacionLocal = _cursor.getString(_cursorIndexOfFechaModificacionLocal);
            }
            _item = new PatientEntity(_tmpId,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpEmail,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpTipoIdentificacion,_tmpEstadoCivil,_tmpPais,_tmpMunicipio,_tmpDepartamento,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpSincronizado,_tmpServerId,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg5);
  }

  @Override
  public Object getSyncStats(final Continuation<? super PatientDao.SyncStats> arg0) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            COUNT(*) as total,\n"
            + "            SUM(CASE WHEN sincronizado = 1 THEN 1 ELSE 0 END) as sincronizados,\n"
            + "            SUM(CASE WHEN modificado_localmente = 1 THEN 1 ELSE 0 END) as modificados_localmente\n"
            + "        FROM patients \n"
            + "        WHERE activo = 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PatientDao.SyncStats>() {
      @Override
      @NonNull
      public PatientDao.SyncStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTotal = 0;
          final int _cursorIndexOfSincronizados = 1;
          final int _cursorIndexOfModificadosLocalmente = 2;
          final PatientDao.SyncStats _result;
          if (_cursor.moveToFirst()) {
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            final int _tmpSincronizados;
            _tmpSincronizados = _cursor.getInt(_cursorIndexOfSincronizados);
            final int _tmpModificados_localmente;
            _tmpModificados_localmente = _cursor.getInt(_cursorIndexOfModificadosLocalmente);
            _result = new PatientDao.SyncStats(_tmpTotal,_tmpSincronizados,_tmpModificados_localmente);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object markPatientsAsSynced(final List<String> patientIds, final String timestamp,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("\n");
        _stringBuilder.append("        UPDATE patients ");
        _stringBuilder.append("\n");
        _stringBuilder.append("        SET sincronizado = 1, ");
        _stringBuilder.append("\n");
        _stringBuilder.append("            fecha_ultima_sincronizacion = ");
        _stringBuilder.append("?");
        _stringBuilder.append(",");
        _stringBuilder.append("\n");
        _stringBuilder.append("            modificado_localmente = 0,");
        _stringBuilder.append("\n");
        _stringBuilder.append("            fecha_modificacion_local = NULL");
        _stringBuilder.append("\n");
        _stringBuilder.append("        WHERE id IN (");
        final int _inputSize = patientIds.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        _stringBuilder.append("\n");
        _stringBuilder.append("    ");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
        }
        _argIndex = 2;
        for (String _item : patientIds) {
          if (_item == null) {
            _stmt.bindNull(_argIndex);
          } else {
            _stmt.bindString(_argIndex, _item);
          }
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg2);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
