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
import com.example.telephases.data.local.entities.UsuarioSedeEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class UsuarioSedeDao_Impl implements UsuarioSedeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UsuarioSedeEntity> __insertionAdapterOfUsuarioSedeEntity;

  private final EntityDeletionOrUpdateAdapter<UsuarioSedeEntity> __deletionAdapterOfUsuarioSedeEntity;

  private final EntityDeletionOrUpdateAdapter<UsuarioSedeEntity> __updateAdapterOfUsuarioSedeEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  private final SharedSQLiteStatement __preparedStmtOfSoftDeleteByUsuario;

  private final SharedSQLiteStatement __preparedStmtOfSoftDeleteBySede;

  public UsuarioSedeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUsuarioSedeEntity = new EntityInsertionAdapter<UsuarioSedeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `usuario_sedes` (`usuarioId`,`sedeId`,`fechaAsignacion`,`activa`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UsuarioSedeEntity entity) {
        if (entity.getUsuarioId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUsuarioId());
        }
        statement.bindLong(2, entity.getSedeId());
        if (entity.getFechaAsignacion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFechaAsignacion());
        }
        final int _tmp = entity.getActiva() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__deletionAdapterOfUsuarioSedeEntity = new EntityDeletionOrUpdateAdapter<UsuarioSedeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `usuario_sedes` WHERE `usuarioId` = ? AND `sedeId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UsuarioSedeEntity entity) {
        if (entity.getUsuarioId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUsuarioId());
        }
        statement.bindLong(2, entity.getSedeId());
      }
    };
    this.__updateAdapterOfUsuarioSedeEntity = new EntityDeletionOrUpdateAdapter<UsuarioSedeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `usuario_sedes` SET `usuarioId` = ?,`sedeId` = ?,`fechaAsignacion` = ?,`activa` = ? WHERE `usuarioId` = ? AND `sedeId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UsuarioSedeEntity entity) {
        if (entity.getUsuarioId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUsuarioId());
        }
        statement.bindLong(2, entity.getSedeId());
        if (entity.getFechaAsignacion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFechaAsignacion());
        }
        final int _tmp = entity.getActiva() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getUsuarioId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUsuarioId());
        }
        statement.bindLong(6, entity.getSedeId());
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE usuario_sedes SET activa = 0 WHERE usuarioId = ? AND sedeId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSoftDeleteByUsuario = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE usuario_sedes SET activa = 0 WHERE usuarioId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSoftDeleteBySede = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE usuario_sedes SET activa = 0 WHERE sedeId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final UsuarioSedeEntity usuarioSede,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUsuarioSedeEntity.insert(usuarioSede);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<UsuarioSedeEntity> usuarioSedes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUsuarioSedeEntity.insert(usuarioSedes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final UsuarioSedeEntity usuarioSede,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfUsuarioSedeEntity.handle(usuarioSede);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final UsuarioSedeEntity usuarioSede,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUsuarioSedeEntity.handle(usuarioSede);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object softDelete(final String usuarioId, final int sedeId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDelete.acquire();
        int _argIndex = 1;
        if (usuarioId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, usuarioId);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, sedeId);
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
          __preparedStmtOfSoftDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object softDeleteByUsuario(final String usuarioId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDeleteByUsuario.acquire();
        int _argIndex = 1;
        if (usuarioId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, usuarioId);
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
          __preparedStmtOfSoftDeleteByUsuario.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object softDeleteBySede(final int sedeId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDeleteBySede.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, sedeId);
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
          __preparedStmtOfSoftDeleteBySede.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<UsuarioSedeEntity>> getAllActive() {
    final String _sql = "SELECT * FROM usuario_sedes WHERE activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"usuario_sedes"}, new Callable<List<UsuarioSedeEntity>>() {
      @Override
      @NonNull
      public List<UsuarioSedeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfSedeId = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeId");
          final int _cursorIndexOfFechaAsignacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAsignacion");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final List<UsuarioSedeEntity> _result = new ArrayList<UsuarioSedeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UsuarioSedeEntity _item;
            final String _tmpUsuarioId;
            if (_cursor.isNull(_cursorIndexOfUsuarioId)) {
              _tmpUsuarioId = null;
            } else {
              _tmpUsuarioId = _cursor.getString(_cursorIndexOfUsuarioId);
            }
            final int _tmpSedeId;
            _tmpSedeId = _cursor.getInt(_cursorIndexOfSedeId);
            final String _tmpFechaAsignacion;
            if (_cursor.isNull(_cursorIndexOfFechaAsignacion)) {
              _tmpFechaAsignacion = null;
            } else {
              _tmpFechaAsignacion = _cursor.getString(_cursorIndexOfFechaAsignacion);
            }
            final boolean _tmpActiva;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiva);
            _tmpActiva = _tmp != 0;
            _item = new UsuarioSedeEntity(_tmpUsuarioId,_tmpSedeId,_tmpFechaAsignacion,_tmpActiva);
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
  public Flow<List<UsuarioSedeEntity>> getByUsuario(final String usuarioId) {
    final String _sql = "SELECT * FROM usuario_sedes WHERE usuarioId = ? AND activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (usuarioId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, usuarioId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"usuario_sedes"}, new Callable<List<UsuarioSedeEntity>>() {
      @Override
      @NonNull
      public List<UsuarioSedeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfSedeId = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeId");
          final int _cursorIndexOfFechaAsignacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAsignacion");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final List<UsuarioSedeEntity> _result = new ArrayList<UsuarioSedeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UsuarioSedeEntity _item;
            final String _tmpUsuarioId;
            if (_cursor.isNull(_cursorIndexOfUsuarioId)) {
              _tmpUsuarioId = null;
            } else {
              _tmpUsuarioId = _cursor.getString(_cursorIndexOfUsuarioId);
            }
            final int _tmpSedeId;
            _tmpSedeId = _cursor.getInt(_cursorIndexOfSedeId);
            final String _tmpFechaAsignacion;
            if (_cursor.isNull(_cursorIndexOfFechaAsignacion)) {
              _tmpFechaAsignacion = null;
            } else {
              _tmpFechaAsignacion = _cursor.getString(_cursorIndexOfFechaAsignacion);
            }
            final boolean _tmpActiva;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiva);
            _tmpActiva = _tmp != 0;
            _item = new UsuarioSedeEntity(_tmpUsuarioId,_tmpSedeId,_tmpFechaAsignacion,_tmpActiva);
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
  public Flow<List<UsuarioSedeEntity>> getBySede(final int sedeId) {
    final String _sql = "SELECT * FROM usuario_sedes WHERE sedeId = ? AND activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sedeId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"usuario_sedes"}, new Callable<List<UsuarioSedeEntity>>() {
      @Override
      @NonNull
      public List<UsuarioSedeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfSedeId = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeId");
          final int _cursorIndexOfFechaAsignacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAsignacion");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final List<UsuarioSedeEntity> _result = new ArrayList<UsuarioSedeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UsuarioSedeEntity _item;
            final String _tmpUsuarioId;
            if (_cursor.isNull(_cursorIndexOfUsuarioId)) {
              _tmpUsuarioId = null;
            } else {
              _tmpUsuarioId = _cursor.getString(_cursorIndexOfUsuarioId);
            }
            final int _tmpSedeId;
            _tmpSedeId = _cursor.getInt(_cursorIndexOfSedeId);
            final String _tmpFechaAsignacion;
            if (_cursor.isNull(_cursorIndexOfFechaAsignacion)) {
              _tmpFechaAsignacion = null;
            } else {
              _tmpFechaAsignacion = _cursor.getString(_cursorIndexOfFechaAsignacion);
            }
            final boolean _tmpActiva;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiva);
            _tmpActiva = _tmp != 0;
            _item = new UsuarioSedeEntity(_tmpUsuarioId,_tmpSedeId,_tmpFechaAsignacion,_tmpActiva);
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
  public Object getByUsuarioAndSede(final String usuarioId, final int sedeId,
      final Continuation<? super UsuarioSedeEntity> $completion) {
    final String _sql = "SELECT * FROM usuario_sedes WHERE usuarioId = ? AND sedeId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (usuarioId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, usuarioId);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, sedeId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UsuarioSedeEntity>() {
      @Override
      @Nullable
      public UsuarioSedeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfSedeId = CursorUtil.getColumnIndexOrThrow(_cursor, "sedeId");
          final int _cursorIndexOfFechaAsignacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAsignacion");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final UsuarioSedeEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUsuarioId;
            if (_cursor.isNull(_cursorIndexOfUsuarioId)) {
              _tmpUsuarioId = null;
            } else {
              _tmpUsuarioId = _cursor.getString(_cursorIndexOfUsuarioId);
            }
            final int _tmpSedeId;
            _tmpSedeId = _cursor.getInt(_cursorIndexOfSedeId);
            final String _tmpFechaAsignacion;
            if (_cursor.isNull(_cursorIndexOfFechaAsignacion)) {
              _tmpFechaAsignacion = null;
            } else {
              _tmpFechaAsignacion = _cursor.getString(_cursorIndexOfFechaAsignacion);
            }
            final boolean _tmpActiva;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiva);
            _tmpActiva = _tmp != 0;
            _result = new UsuarioSedeEntity(_tmpUsuarioId,_tmpSedeId,_tmpFechaAsignacion,_tmpActiva);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getActiveCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM usuario_sedes WHERE activa = 1";
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
    }, $completion);
  }

  @Override
  public Object getCountByUsuario(final String usuarioId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM usuario_sedes WHERE usuarioId = ? AND activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (usuarioId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, usuarioId);
    }
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
    }, $completion);
  }

  @Override
  public Object getCountBySede(final int sedeId, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM usuario_sedes WHERE sedeId = ? AND activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sedeId);
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
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
