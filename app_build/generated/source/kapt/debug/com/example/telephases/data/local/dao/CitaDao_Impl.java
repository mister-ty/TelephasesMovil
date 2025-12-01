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
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.telephases.data.local.entities.CitaEntity;
import com.example.telephases.data.local.entities.CitaExamenPrevistoEntity;
import com.example.telephases.data.local.entities.EstadoCitaEntity;
import com.example.telephases.data.local.entities.PatientEntity;
import com.example.telephases.data.local.entities.UserEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class CitaDao_Impl implements CitaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EstadoCitaEntity> __insertionAdapterOfEstadoCitaEntity;

  private final EntityInsertionAdapter<CitaEntity> __insertionAdapterOfCitaEntity;

  private final EntityInsertionAdapter<CitaExamenPrevistoEntity> __insertionAdapterOfCitaExamenPrevistoEntity;

  private final EntityDeletionOrUpdateAdapter<CitaEntity> __deletionAdapterOfCitaEntity;

  private final EntityDeletionOrUpdateAdapter<CitaEntity> __updateAdapterOfCitaEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearCitasDelDia;

  private final SharedSQLiteStatement __preparedStmtOfClearPrevistos;

  public CitaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEstadoCitaEntity = new EntityInsertionAdapter<EstadoCitaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `estado_cita` (`id`,`nombre`,`descripcion`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EstadoCitaEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombre());
        }
        if (entity.getDescripcion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescripcion());
        }
      }
    };
    this.__insertionAdapterOfCitaEntity = new EntityInsertionAdapter<CitaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `citas` (`id`,`paciente_id`,`creado_por_usuario_id`,`estado_cita_id`,`fecha_cita`,`observaciones_admin`,`observaciones_paciente`,`fecha_creacion`,`fecha_modificacion`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CitaEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPacienteId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPacienteId());
        }
        if (entity.getCreadoPorUsuarioId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCreadoPorUsuarioId());
        }
        statement.bindLong(4, entity.getEstadoCitaId());
        if (entity.getFechaCita() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFechaCita());
        }
        if (entity.getObservacionesAdmin() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getObservacionesAdmin());
        }
        if (entity.getObservacionesPaciente() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getObservacionesPaciente());
        }
        if (entity.getFechaCreacion() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFechaCreacion());
        }
        if (entity.getFechaModificacion() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getFechaModificacion());
        }
      }
    };
    this.__insertionAdapterOfCitaExamenPrevistoEntity = new EntityInsertionAdapter<CitaExamenPrevistoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `cita_examenes_previstos` (`cita_id`,`tipo_examen_id`,`nombre`,`descripcion`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CitaExamenPrevistoEntity entity) {
        statement.bindLong(1, entity.getCitaId());
        statement.bindLong(2, entity.getTipoExamenId());
        if (entity.getNombre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNombre());
        }
        if (entity.getDescripcion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescripcion());
        }
      }
    };
    this.__deletionAdapterOfCitaEntity = new EntityDeletionOrUpdateAdapter<CitaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `citas` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CitaEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCitaEntity = new EntityDeletionOrUpdateAdapter<CitaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `citas` SET `id` = ?,`paciente_id` = ?,`creado_por_usuario_id` = ?,`estado_cita_id` = ?,`fecha_cita` = ?,`observaciones_admin` = ?,`observaciones_paciente` = ?,`fecha_creacion` = ?,`fecha_modificacion` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CitaEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPacienteId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPacienteId());
        }
        if (entity.getCreadoPorUsuarioId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCreadoPorUsuarioId());
        }
        statement.bindLong(4, entity.getEstadoCitaId());
        if (entity.getFechaCita() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFechaCita());
        }
        if (entity.getObservacionesAdmin() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getObservacionesAdmin());
        }
        if (entity.getObservacionesPaciente() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getObservacionesPaciente());
        }
        if (entity.getFechaCreacion() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFechaCreacion());
        }
        if (entity.getFechaModificacion() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getFechaModificacion());
        }
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfClearCitasDelDia = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM citas";
        return _query;
      }
    };
    this.__preparedStmtOfClearPrevistos = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cita_examenes_previstos WHERE cita_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertEstados(final List<EstadoCitaEntity> estados,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEstadoCitaEntity.insert(estados);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertCita(final CitaEntity cita, final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCitaEntity.insertAndReturnId(cita);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertPrevistos(final List<CitaExamenPrevistoEntity> previstos,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCitaExamenPrevistoEntity.insert(previstos);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertCitaExamenPrevisto(final CitaExamenPrevistoEntity previsto,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCitaExamenPrevistoEntity.insert(previsto);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteCita(final CitaEntity cita, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCitaEntity.handle(cita);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object updateCita(final CitaEntity cita, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCitaEntity.handle(cita);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object clearCitasDelDia(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearCitasDelDia.acquire();
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
          __preparedStmtOfClearCitasDelDia.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object clearPrevistos(final int citaId, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearPrevistos.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, citaId);
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
          __preparedStmtOfClearPrevistos.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Flow<List<EstadoCitaEntity>> getEstadosFlow() {
    final String _sql = "SELECT * FROM estado_cita ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"estado_cita"}, new Callable<List<EstadoCitaEntity>>() {
      @Override
      @NonNull
      public List<EstadoCitaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final List<EstadoCitaEntity> _result = new ArrayList<EstadoCitaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EstadoCitaEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            _item = new EstadoCitaEntity(_tmpId,_tmpNombre,_tmpDescripcion);
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
  public Flow<List<CitaEntity>> getCitasDeDiaFlow(final String yyyyMmDd) {
    final String _sql = "SELECT * FROM citas WHERE fecha_cita LIKE ? || '%' ORDER BY fecha_cita ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (yyyyMmDd == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, yyyyMmDd);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"citas"}, new Callable<List<CitaEntity>>() {
      @Override
      @NonNull
      public List<CitaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPacienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "paciente_id");
          final int _cursorIndexOfCreadoPorUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "creado_por_usuario_id");
          final int _cursorIndexOfEstadoCitaId = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_cita_id");
          final int _cursorIndexOfFechaCita = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_cita");
          final int _cursorIndexOfObservacionesAdmin = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones_admin");
          final int _cursorIndexOfObservacionesPaciente = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones_paciente");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final List<CitaEntity> _result = new ArrayList<CitaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CitaEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpPacienteId;
            if (_cursor.isNull(_cursorIndexOfPacienteId)) {
              _tmpPacienteId = null;
            } else {
              _tmpPacienteId = _cursor.getString(_cursorIndexOfPacienteId);
            }
            final String _tmpCreadoPorUsuarioId;
            if (_cursor.isNull(_cursorIndexOfCreadoPorUsuarioId)) {
              _tmpCreadoPorUsuarioId = null;
            } else {
              _tmpCreadoPorUsuarioId = _cursor.getString(_cursorIndexOfCreadoPorUsuarioId);
            }
            final int _tmpEstadoCitaId;
            _tmpEstadoCitaId = _cursor.getInt(_cursorIndexOfEstadoCitaId);
            final String _tmpFechaCita;
            if (_cursor.isNull(_cursorIndexOfFechaCita)) {
              _tmpFechaCita = null;
            } else {
              _tmpFechaCita = _cursor.getString(_cursorIndexOfFechaCita);
            }
            final String _tmpObservacionesAdmin;
            if (_cursor.isNull(_cursorIndexOfObservacionesAdmin)) {
              _tmpObservacionesAdmin = null;
            } else {
              _tmpObservacionesAdmin = _cursor.getString(_cursorIndexOfObservacionesAdmin);
            }
            final String _tmpObservacionesPaciente;
            if (_cursor.isNull(_cursorIndexOfObservacionesPaciente)) {
              _tmpObservacionesPaciente = null;
            } else {
              _tmpObservacionesPaciente = _cursor.getString(_cursorIndexOfObservacionesPaciente);
            }
            final String _tmpFechaCreacion;
            if (_cursor.isNull(_cursorIndexOfFechaCreacion)) {
              _tmpFechaCreacion = null;
            } else {
              _tmpFechaCreacion = _cursor.getString(_cursorIndexOfFechaCreacion);
            }
            final String _tmpFechaModificacion;
            if (_cursor.isNull(_cursorIndexOfFechaModificacion)) {
              _tmpFechaModificacion = null;
            } else {
              _tmpFechaModificacion = _cursor.getString(_cursorIndexOfFechaModificacion);
            }
            _item = new CitaEntity(_tmpId,_tmpPacienteId,_tmpCreadoPorUsuarioId,_tmpEstadoCitaId,_tmpFechaCita,_tmpObservacionesAdmin,_tmpObservacionesPaciente,_tmpFechaCreacion,_tmpFechaModificacion);
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
  public Object getCitasHoy(final Continuation<? super List<CitaEntity>> arg0) {
    final String _sql = "SELECT * FROM citas ORDER BY fecha_cita ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CitaEntity>>() {
      @Override
      @NonNull
      public List<CitaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPacienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "paciente_id");
          final int _cursorIndexOfCreadoPorUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "creado_por_usuario_id");
          final int _cursorIndexOfEstadoCitaId = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_cita_id");
          final int _cursorIndexOfFechaCita = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_cita");
          final int _cursorIndexOfObservacionesAdmin = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones_admin");
          final int _cursorIndexOfObservacionesPaciente = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones_paciente");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final List<CitaEntity> _result = new ArrayList<CitaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CitaEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpPacienteId;
            if (_cursor.isNull(_cursorIndexOfPacienteId)) {
              _tmpPacienteId = null;
            } else {
              _tmpPacienteId = _cursor.getString(_cursorIndexOfPacienteId);
            }
            final String _tmpCreadoPorUsuarioId;
            if (_cursor.isNull(_cursorIndexOfCreadoPorUsuarioId)) {
              _tmpCreadoPorUsuarioId = null;
            } else {
              _tmpCreadoPorUsuarioId = _cursor.getString(_cursorIndexOfCreadoPorUsuarioId);
            }
            final int _tmpEstadoCitaId;
            _tmpEstadoCitaId = _cursor.getInt(_cursorIndexOfEstadoCitaId);
            final String _tmpFechaCita;
            if (_cursor.isNull(_cursorIndexOfFechaCita)) {
              _tmpFechaCita = null;
            } else {
              _tmpFechaCita = _cursor.getString(_cursorIndexOfFechaCita);
            }
            final String _tmpObservacionesAdmin;
            if (_cursor.isNull(_cursorIndexOfObservacionesAdmin)) {
              _tmpObservacionesAdmin = null;
            } else {
              _tmpObservacionesAdmin = _cursor.getString(_cursorIndexOfObservacionesAdmin);
            }
            final String _tmpObservacionesPaciente;
            if (_cursor.isNull(_cursorIndexOfObservacionesPaciente)) {
              _tmpObservacionesPaciente = null;
            } else {
              _tmpObservacionesPaciente = _cursor.getString(_cursorIndexOfObservacionesPaciente);
            }
            final String _tmpFechaCreacion;
            if (_cursor.isNull(_cursorIndexOfFechaCreacion)) {
              _tmpFechaCreacion = null;
            } else {
              _tmpFechaCreacion = _cursor.getString(_cursorIndexOfFechaCreacion);
            }
            final String _tmpFechaModificacion;
            if (_cursor.isNull(_cursorIndexOfFechaModificacion)) {
              _tmpFechaModificacion = null;
            } else {
              _tmpFechaModificacion = _cursor.getString(_cursorIndexOfFechaModificacion);
            }
            _item = new CitaEntity(_tmpId,_tmpPacienteId,_tmpCreadoPorUsuarioId,_tmpEstadoCitaId,_tmpFechaCita,_tmpObservacionesAdmin,_tmpObservacionesPaciente,_tmpFechaCreacion,_tmpFechaModificacion);
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
  public Object getCitaExamenesPrevistos(final long citaId,
      final Continuation<? super List<CitaExamenPrevistoEntity>> arg1) {
    final String _sql = "SELECT * FROM cita_examenes_previstos WHERE cita_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, citaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CitaExamenPrevistoEntity>>() {
      @Override
      @NonNull
      public List<CitaExamenPrevistoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCitaId = CursorUtil.getColumnIndexOrThrow(_cursor, "cita_id");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final List<CitaExamenPrevistoEntity> _result = new ArrayList<CitaExamenPrevistoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CitaExamenPrevistoEntity _item;
            final int _tmpCitaId;
            _tmpCitaId = _cursor.getInt(_cursorIndexOfCitaId);
            final int _tmpTipoExamenId;
            _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            _item = new CitaExamenPrevistoEntity(_tmpCitaId,_tmpTipoExamenId,_tmpNombre,_tmpDescripcion);
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
  public Object getUsersWithToken(final Continuation<? super List<UserEntity>> arg0) {
    final String _sql = "SELECT * FROM users WHERE token_actual IS NOT NULL AND token_actual != '' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<UserEntity>>() {
      @Override
      @NonNull
      public List<UserEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "password_hash");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfRolId = CursorUtil.getColumnIndexOrThrow(_cursor, "rol_id");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfTokenActual = CursorUtil.getColumnIndexOrThrow(_cursor, "token_actual");
          final int _cursorIndexOfFechaExpiracionToken = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_expiracion_token");
          final int _cursorIndexOfUltimoLogin = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimo_login");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final List<UserEntity> _result = new ArrayList<UserEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPasswordHash;
            if (_cursor.isNull(_cursorIndexOfPasswordHash)) {
              _tmpPasswordHash = null;
            } else {
              _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
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
            final int _tmpRolId;
            _tmpRolId = _cursor.getInt(_cursorIndexOfRolId);
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
            final String _tmpTokenActual;
            if (_cursor.isNull(_cursorIndexOfTokenActual)) {
              _tmpTokenActual = null;
            } else {
              _tmpTokenActual = _cursor.getString(_cursorIndexOfTokenActual);
            }
            final String _tmpFechaExpiracionToken;
            if (_cursor.isNull(_cursorIndexOfFechaExpiracionToken)) {
              _tmpFechaExpiracionToken = null;
            } else {
              _tmpFechaExpiracionToken = _cursor.getString(_cursorIndexOfFechaExpiracionToken);
            }
            final String _tmpUltimoLogin;
            if (_cursor.isNull(_cursorIndexOfUltimoLogin)) {
              _tmpUltimoLogin = null;
            } else {
              _tmpUltimoLogin = _cursor.getString(_cursorIndexOfUltimoLogin);
            }
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
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
            _item = new UserEntity(_tmpId,_tmpUsername,_tmpEmail,_tmpPasswordHash,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpRolId,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpTokenActual,_tmpFechaExpiracionToken,_tmpUltimoLogin,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
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
  public Object getPatientByServerId(final String serverId,
      final Continuation<? super PatientEntity> arg1) {
    final String _sql = "SELECT * FROM patients WHERE server_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (serverId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, serverId);
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
  public Object getPatientById(final String patientId,
      final Continuation<? super PatientEntity> arg1) {
    final String _sql = "SELECT * FROM patients WHERE id = ? LIMIT 1";
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
  public Object getEstadoById(final int estadoId,
      final Continuation<? super EstadoCitaEntity> arg1) {
    final String _sql = "SELECT * FROM estado_cita WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, estadoId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EstadoCitaEntity>() {
      @Override
      @Nullable
      public EstadoCitaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final EstadoCitaEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            _result = new EstadoCitaEntity(_tmpId,_tmpNombre,_tmpDescripcion);
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
  public Object getUserById(final String userId, final Continuation<? super UserEntity> arg1) {
    final String _sql = "SELECT * FROM users WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "password_hash");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfRolId = CursorUtil.getColumnIndexOrThrow(_cursor, "rol_id");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfTokenActual = CursorUtil.getColumnIndexOrThrow(_cursor, "token_actual");
          final int _cursorIndexOfFechaExpiracionToken = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_expiracion_token");
          final int _cursorIndexOfUltimoLogin = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimo_login");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPasswordHash;
            if (_cursor.isNull(_cursorIndexOfPasswordHash)) {
              _tmpPasswordHash = null;
            } else {
              _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
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
            final int _tmpRolId;
            _tmpRolId = _cursor.getInt(_cursorIndexOfRolId);
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
            final String _tmpTokenActual;
            if (_cursor.isNull(_cursorIndexOfTokenActual)) {
              _tmpTokenActual = null;
            } else {
              _tmpTokenActual = _cursor.getString(_cursorIndexOfTokenActual);
            }
            final String _tmpFechaExpiracionToken;
            if (_cursor.isNull(_cursorIndexOfFechaExpiracionToken)) {
              _tmpFechaExpiracionToken = null;
            } else {
              _tmpFechaExpiracionToken = _cursor.getString(_cursorIndexOfFechaExpiracionToken);
            }
            final String _tmpUltimoLogin;
            if (_cursor.isNull(_cursorIndexOfUltimoLogin)) {
              _tmpUltimoLogin = null;
            } else {
              _tmpUltimoLogin = _cursor.getString(_cursorIndexOfUltimoLogin);
            }
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
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
            _result = new UserEntity(_tmpId,_tmpUsername,_tmpEmail,_tmpPasswordHash,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpRolId,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpTokenActual,_tmpFechaExpiracionToken,_tmpUltimoLogin,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
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
  public Object getAllUsers(final Continuation<? super List<UserEntity>> arg0) {
    final String _sql = "SELECT * FROM users LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<UserEntity>>() {
      @Override
      @NonNull
      public List<UserEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "password_hash");
          final int _cursorIndexOfPrimerNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_nombre");
          final int _cursorIndexOfSegundoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_nombre");
          final int _cursorIndexOfPrimerApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "primer_apellido");
          final int _cursorIndexOfSegundoApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "segundo_apellido");
          final int _cursorIndexOfTipoDocumentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_documento_id");
          final int _cursorIndexOfNumeroDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "numero_documento");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfCiudadId = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad_id");
          final int _cursorIndexOfFechaNacimiento = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_nacimiento");
          final int _cursorIndexOfGenero = CursorUtil.getColumnIndexOrThrow(_cursor, "genero");
          final int _cursorIndexOfRolId = CursorUtil.getColumnIndexOrThrow(_cursor, "rol_id");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidad_salud_id");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_registro");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfTokenActual = CursorUtil.getColumnIndexOrThrow(_cursor, "token_actual");
          final int _cursorIndexOfFechaExpiracionToken = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_expiracion_token");
          final int _cursorIndexOfUltimoLogin = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimo_login");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final List<UserEntity> _result = new ArrayList<UserEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPasswordHash;
            if (_cursor.isNull(_cursorIndexOfPasswordHash)) {
              _tmpPasswordHash = null;
            } else {
              _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
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
            final int _tmpRolId;
            _tmpRolId = _cursor.getInt(_cursorIndexOfRolId);
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
            final String _tmpTokenActual;
            if (_cursor.isNull(_cursorIndexOfTokenActual)) {
              _tmpTokenActual = null;
            } else {
              _tmpTokenActual = _cursor.getString(_cursorIndexOfTokenActual);
            }
            final String _tmpFechaExpiracionToken;
            if (_cursor.isNull(_cursorIndexOfFechaExpiracionToken)) {
              _tmpFechaExpiracionToken = null;
            } else {
              _tmpFechaExpiracionToken = _cursor.getString(_cursorIndexOfFechaExpiracionToken);
            }
            final String _tmpUltimoLogin;
            if (_cursor.isNull(_cursorIndexOfUltimoLogin)) {
              _tmpUltimoLogin = null;
            } else {
              _tmpUltimoLogin = _cursor.getString(_cursorIndexOfUltimoLogin);
            }
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
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
            _item = new UserEntity(_tmpId,_tmpUsername,_tmpEmail,_tmpPasswordHash,_tmpPrimerNombre,_tmpSegundoNombre,_tmpPrimerApellido,_tmpSegundoApellido,_tmpTipoDocumentoId,_tmpNumeroDocumento,_tmpTelefono,_tmpDireccion,_tmpCiudadId,_tmpFechaNacimiento,_tmpGenero,_tmpRolId,_tmpEntidadSaludId,_tmpFechaRegistro,_tmpActivo,_tmpTokenActual,_tmpFechaExpiracionToken,_tmpUltimoLogin,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal);
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
  public Object getCitaById(final int citaId, final Continuation<? super CitaEntity> arg1) {
    final String _sql = "SELECT * FROM citas WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, citaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CitaEntity>() {
      @Override
      @Nullable
      public CitaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPacienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "paciente_id");
          final int _cursorIndexOfCreadoPorUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "creado_por_usuario_id");
          final int _cursorIndexOfEstadoCitaId = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_cita_id");
          final int _cursorIndexOfFechaCita = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_cita");
          final int _cursorIndexOfObservacionesAdmin = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones_admin");
          final int _cursorIndexOfObservacionesPaciente = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones_paciente");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final CitaEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpPacienteId;
            if (_cursor.isNull(_cursorIndexOfPacienteId)) {
              _tmpPacienteId = null;
            } else {
              _tmpPacienteId = _cursor.getString(_cursorIndexOfPacienteId);
            }
            final String _tmpCreadoPorUsuarioId;
            if (_cursor.isNull(_cursorIndexOfCreadoPorUsuarioId)) {
              _tmpCreadoPorUsuarioId = null;
            } else {
              _tmpCreadoPorUsuarioId = _cursor.getString(_cursorIndexOfCreadoPorUsuarioId);
            }
            final int _tmpEstadoCitaId;
            _tmpEstadoCitaId = _cursor.getInt(_cursorIndexOfEstadoCitaId);
            final String _tmpFechaCita;
            if (_cursor.isNull(_cursorIndexOfFechaCita)) {
              _tmpFechaCita = null;
            } else {
              _tmpFechaCita = _cursor.getString(_cursorIndexOfFechaCita);
            }
            final String _tmpObservacionesAdmin;
            if (_cursor.isNull(_cursorIndexOfObservacionesAdmin)) {
              _tmpObservacionesAdmin = null;
            } else {
              _tmpObservacionesAdmin = _cursor.getString(_cursorIndexOfObservacionesAdmin);
            }
            final String _tmpObservacionesPaciente;
            if (_cursor.isNull(_cursorIndexOfObservacionesPaciente)) {
              _tmpObservacionesPaciente = null;
            } else {
              _tmpObservacionesPaciente = _cursor.getString(_cursorIndexOfObservacionesPaciente);
            }
            final String _tmpFechaCreacion;
            if (_cursor.isNull(_cursorIndexOfFechaCreacion)) {
              _tmpFechaCreacion = null;
            } else {
              _tmpFechaCreacion = _cursor.getString(_cursorIndexOfFechaCreacion);
            }
            final String _tmpFechaModificacion;
            if (_cursor.isNull(_cursorIndexOfFechaModificacion)) {
              _tmpFechaModificacion = null;
            } else {
              _tmpFechaModificacion = _cursor.getString(_cursorIndexOfFechaModificacion);
            }
            _result = new CitaEntity(_tmpId,_tmpPacienteId,_tmpCreadoPorUsuarioId,_tmpEstadoCitaId,_tmpFechaCita,_tmpObservacionesAdmin,_tmpObservacionesPaciente,_tmpFechaCreacion,_tmpFechaModificacion);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
