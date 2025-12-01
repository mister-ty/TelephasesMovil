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
import com.example.telephases.data.local.entities.DispositivoEntity;
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
public final class DispositivoDao_Impl implements DispositivoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DispositivoEntity> __insertionAdapterOfDispositivoEntity;

  private final EntityDeletionOrUpdateAdapter<DispositivoEntity> __deletionAdapterOfDispositivoEntity;

  private final EntityDeletionOrUpdateAdapter<DispositivoEntity> __updateAdapterOfDispositivoEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public DispositivoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDispositivoEntity = new EntityInsertionAdapter<DispositivoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `dispositivos` (`id`,`tipoDispositivo`,`marca`,`modelo`,`serial`,`fechaAdquisicion`,`activo`,`serverId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DispositivoEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTipoDispositivo() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTipoDispositivo());
        }
        if (entity.getMarca() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMarca());
        }
        if (entity.getModelo() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getModelo());
        }
        if (entity.getSerial() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSerial());
        }
        if (entity.getFechaAdquisicion() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFechaAdquisicion());
        }
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getServerId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getServerId());
        }
      }
    };
    this.__deletionAdapterOfDispositivoEntity = new EntityDeletionOrUpdateAdapter<DispositivoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `dispositivos` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DispositivoEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfDispositivoEntity = new EntityDeletionOrUpdateAdapter<DispositivoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `dispositivos` SET `id` = ?,`tipoDispositivo` = ?,`marca` = ?,`modelo` = ?,`serial` = ?,`fechaAdquisicion` = ?,`activo` = ?,`serverId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DispositivoEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTipoDispositivo() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTipoDispositivo());
        }
        if (entity.getMarca() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMarca());
        }
        if (entity.getModelo() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getModelo());
        }
        if (entity.getSerial() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSerial());
        }
        if (entity.getFechaAdquisicion() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFechaAdquisicion());
        }
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getServerId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getServerId());
        }
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE dispositivos SET activo = 0 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final DispositivoEntity dispositivo, final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDispositivoEntity.insertAndReturnId(dispositivo);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertAll(final List<DispositivoEntity> dispositivos,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDispositivoEntity.insert(dispositivos);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object delete(final DispositivoEntity dispositivo, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfDispositivoEntity.handle(dispositivo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object update(final DispositivoEntity dispositivo, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfDispositivoEntity.handle(dispositivo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object softDelete(final int id, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDelete.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
    }, arg1);
  }

  @Override
  public Flow<List<DispositivoEntity>> getAllActive() {
    final String _sql = "SELECT * FROM dispositivos WHERE activo = 1 ORDER BY tipoDispositivo ASC, marca ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"dispositivos"}, new Callable<List<DispositivoEntity>>() {
      @Override
      @NonNull
      public List<DispositivoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTipoDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoDispositivo");
          final int _cursorIndexOfMarca = CursorUtil.getColumnIndexOrThrow(_cursor, "marca");
          final int _cursorIndexOfModelo = CursorUtil.getColumnIndexOrThrow(_cursor, "modelo");
          final int _cursorIndexOfSerial = CursorUtil.getColumnIndexOrThrow(_cursor, "serial");
          final int _cursorIndexOfFechaAdquisicion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAdquisicion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<DispositivoEntity> _result = new ArrayList<DispositivoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DispositivoEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTipoDispositivo;
            if (_cursor.isNull(_cursorIndexOfTipoDispositivo)) {
              _tmpTipoDispositivo = null;
            } else {
              _tmpTipoDispositivo = _cursor.getString(_cursorIndexOfTipoDispositivo);
            }
            final String _tmpMarca;
            if (_cursor.isNull(_cursorIndexOfMarca)) {
              _tmpMarca = null;
            } else {
              _tmpMarca = _cursor.getString(_cursorIndexOfMarca);
            }
            final String _tmpModelo;
            if (_cursor.isNull(_cursorIndexOfModelo)) {
              _tmpModelo = null;
            } else {
              _tmpModelo = _cursor.getString(_cursorIndexOfModelo);
            }
            final String _tmpSerial;
            if (_cursor.isNull(_cursorIndexOfSerial)) {
              _tmpSerial = null;
            } else {
              _tmpSerial = _cursor.getString(_cursorIndexOfSerial);
            }
            final String _tmpFechaAdquisicion;
            if (_cursor.isNull(_cursorIndexOfFechaAdquisicion)) {
              _tmpFechaAdquisicion = null;
            } else {
              _tmpFechaAdquisicion = _cursor.getString(_cursorIndexOfFechaAdquisicion);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _item = new DispositivoEntity(_tmpId,_tmpTipoDispositivo,_tmpMarca,_tmpModelo,_tmpSerial,_tmpFechaAdquisicion,_tmpActivo,_tmpServerId);
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
  public Flow<List<DispositivoEntity>> getByTipo(final String tipoDispositivo) {
    final String _sql = "SELECT * FROM dispositivos WHERE tipoDispositivo = ? AND activo = 1 ORDER BY marca ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tipoDispositivo == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tipoDispositivo);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"dispositivos"}, new Callable<List<DispositivoEntity>>() {
      @Override
      @NonNull
      public List<DispositivoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTipoDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoDispositivo");
          final int _cursorIndexOfMarca = CursorUtil.getColumnIndexOrThrow(_cursor, "marca");
          final int _cursorIndexOfModelo = CursorUtil.getColumnIndexOrThrow(_cursor, "modelo");
          final int _cursorIndexOfSerial = CursorUtil.getColumnIndexOrThrow(_cursor, "serial");
          final int _cursorIndexOfFechaAdquisicion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAdquisicion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<DispositivoEntity> _result = new ArrayList<DispositivoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DispositivoEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTipoDispositivo;
            if (_cursor.isNull(_cursorIndexOfTipoDispositivo)) {
              _tmpTipoDispositivo = null;
            } else {
              _tmpTipoDispositivo = _cursor.getString(_cursorIndexOfTipoDispositivo);
            }
            final String _tmpMarca;
            if (_cursor.isNull(_cursorIndexOfMarca)) {
              _tmpMarca = null;
            } else {
              _tmpMarca = _cursor.getString(_cursorIndexOfMarca);
            }
            final String _tmpModelo;
            if (_cursor.isNull(_cursorIndexOfModelo)) {
              _tmpModelo = null;
            } else {
              _tmpModelo = _cursor.getString(_cursorIndexOfModelo);
            }
            final String _tmpSerial;
            if (_cursor.isNull(_cursorIndexOfSerial)) {
              _tmpSerial = null;
            } else {
              _tmpSerial = _cursor.getString(_cursorIndexOfSerial);
            }
            final String _tmpFechaAdquisicion;
            if (_cursor.isNull(_cursorIndexOfFechaAdquisicion)) {
              _tmpFechaAdquisicion = null;
            } else {
              _tmpFechaAdquisicion = _cursor.getString(_cursorIndexOfFechaAdquisicion);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _item = new DispositivoEntity(_tmpId,_tmpTipoDispositivo,_tmpMarca,_tmpModelo,_tmpSerial,_tmpFechaAdquisicion,_tmpActivo,_tmpServerId);
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
  public Object getById(final int id, final Continuation<? super DispositivoEntity> arg1) {
    final String _sql = "SELECT * FROM dispositivos WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DispositivoEntity>() {
      @Override
      @Nullable
      public DispositivoEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTipoDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoDispositivo");
          final int _cursorIndexOfMarca = CursorUtil.getColumnIndexOrThrow(_cursor, "marca");
          final int _cursorIndexOfModelo = CursorUtil.getColumnIndexOrThrow(_cursor, "modelo");
          final int _cursorIndexOfSerial = CursorUtil.getColumnIndexOrThrow(_cursor, "serial");
          final int _cursorIndexOfFechaAdquisicion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAdquisicion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final DispositivoEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTipoDispositivo;
            if (_cursor.isNull(_cursorIndexOfTipoDispositivo)) {
              _tmpTipoDispositivo = null;
            } else {
              _tmpTipoDispositivo = _cursor.getString(_cursorIndexOfTipoDispositivo);
            }
            final String _tmpMarca;
            if (_cursor.isNull(_cursorIndexOfMarca)) {
              _tmpMarca = null;
            } else {
              _tmpMarca = _cursor.getString(_cursorIndexOfMarca);
            }
            final String _tmpModelo;
            if (_cursor.isNull(_cursorIndexOfModelo)) {
              _tmpModelo = null;
            } else {
              _tmpModelo = _cursor.getString(_cursorIndexOfModelo);
            }
            final String _tmpSerial;
            if (_cursor.isNull(_cursorIndexOfSerial)) {
              _tmpSerial = null;
            } else {
              _tmpSerial = _cursor.getString(_cursorIndexOfSerial);
            }
            final String _tmpFechaAdquisicion;
            if (_cursor.isNull(_cursorIndexOfFechaAdquisicion)) {
              _tmpFechaAdquisicion = null;
            } else {
              _tmpFechaAdquisicion = _cursor.getString(_cursorIndexOfFechaAdquisicion);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _result = new DispositivoEntity(_tmpId,_tmpTipoDispositivo,_tmpMarca,_tmpModelo,_tmpSerial,_tmpFechaAdquisicion,_tmpActivo,_tmpServerId);
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
  public Object getByServerId(final int serverId,
      final Continuation<? super DispositivoEntity> arg1) {
    final String _sql = "SELECT * FROM dispositivos WHERE serverId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, serverId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DispositivoEntity>() {
      @Override
      @Nullable
      public DispositivoEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTipoDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoDispositivo");
          final int _cursorIndexOfMarca = CursorUtil.getColumnIndexOrThrow(_cursor, "marca");
          final int _cursorIndexOfModelo = CursorUtil.getColumnIndexOrThrow(_cursor, "modelo");
          final int _cursorIndexOfSerial = CursorUtil.getColumnIndexOrThrow(_cursor, "serial");
          final int _cursorIndexOfFechaAdquisicion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAdquisicion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final DispositivoEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTipoDispositivo;
            if (_cursor.isNull(_cursorIndexOfTipoDispositivo)) {
              _tmpTipoDispositivo = null;
            } else {
              _tmpTipoDispositivo = _cursor.getString(_cursorIndexOfTipoDispositivo);
            }
            final String _tmpMarca;
            if (_cursor.isNull(_cursorIndexOfMarca)) {
              _tmpMarca = null;
            } else {
              _tmpMarca = _cursor.getString(_cursorIndexOfMarca);
            }
            final String _tmpModelo;
            if (_cursor.isNull(_cursorIndexOfModelo)) {
              _tmpModelo = null;
            } else {
              _tmpModelo = _cursor.getString(_cursorIndexOfModelo);
            }
            final String _tmpSerial;
            if (_cursor.isNull(_cursorIndexOfSerial)) {
              _tmpSerial = null;
            } else {
              _tmpSerial = _cursor.getString(_cursorIndexOfSerial);
            }
            final String _tmpFechaAdquisicion;
            if (_cursor.isNull(_cursorIndexOfFechaAdquisicion)) {
              _tmpFechaAdquisicion = null;
            } else {
              _tmpFechaAdquisicion = _cursor.getString(_cursorIndexOfFechaAdquisicion);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _result = new DispositivoEntity(_tmpId,_tmpTipoDispositivo,_tmpMarca,_tmpModelo,_tmpSerial,_tmpFechaAdquisicion,_tmpActivo,_tmpServerId);
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
  public Object getBySerial(final String serial,
      final Continuation<? super DispositivoEntity> arg1) {
    final String _sql = "SELECT * FROM dispositivos WHERE serial = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (serial == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, serial);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DispositivoEntity>() {
      @Override
      @Nullable
      public DispositivoEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTipoDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoDispositivo");
          final int _cursorIndexOfMarca = CursorUtil.getColumnIndexOrThrow(_cursor, "marca");
          final int _cursorIndexOfModelo = CursorUtil.getColumnIndexOrThrow(_cursor, "modelo");
          final int _cursorIndexOfSerial = CursorUtil.getColumnIndexOrThrow(_cursor, "serial");
          final int _cursorIndexOfFechaAdquisicion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAdquisicion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final DispositivoEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTipoDispositivo;
            if (_cursor.isNull(_cursorIndexOfTipoDispositivo)) {
              _tmpTipoDispositivo = null;
            } else {
              _tmpTipoDispositivo = _cursor.getString(_cursorIndexOfTipoDispositivo);
            }
            final String _tmpMarca;
            if (_cursor.isNull(_cursorIndexOfMarca)) {
              _tmpMarca = null;
            } else {
              _tmpMarca = _cursor.getString(_cursorIndexOfMarca);
            }
            final String _tmpModelo;
            if (_cursor.isNull(_cursorIndexOfModelo)) {
              _tmpModelo = null;
            } else {
              _tmpModelo = _cursor.getString(_cursorIndexOfModelo);
            }
            final String _tmpSerial;
            if (_cursor.isNull(_cursorIndexOfSerial)) {
              _tmpSerial = null;
            } else {
              _tmpSerial = _cursor.getString(_cursorIndexOfSerial);
            }
            final String _tmpFechaAdquisicion;
            if (_cursor.isNull(_cursorIndexOfFechaAdquisicion)) {
              _tmpFechaAdquisicion = null;
            } else {
              _tmpFechaAdquisicion = _cursor.getString(_cursorIndexOfFechaAdquisicion);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _result = new DispositivoEntity(_tmpId,_tmpTipoDispositivo,_tmpMarca,_tmpModelo,_tmpSerial,_tmpFechaAdquisicion,_tmpActivo,_tmpServerId);
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
  public Object getActiveCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM dispositivos WHERE activo = 1";
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
  public Object getUnsyncedCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM dispositivos WHERE serverId IS NULL AND activo = 1";
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
  public Object getUnsynced(final Continuation<? super List<DispositivoEntity>> arg0) {
    final String _sql = "SELECT * FROM dispositivos WHERE serverId IS NULL AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DispositivoEntity>>() {
      @Override
      @NonNull
      public List<DispositivoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTipoDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoDispositivo");
          final int _cursorIndexOfMarca = CursorUtil.getColumnIndexOrThrow(_cursor, "marca");
          final int _cursorIndexOfModelo = CursorUtil.getColumnIndexOrThrow(_cursor, "modelo");
          final int _cursorIndexOfSerial = CursorUtil.getColumnIndexOrThrow(_cursor, "serial");
          final int _cursorIndexOfFechaAdquisicion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAdquisicion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<DispositivoEntity> _result = new ArrayList<DispositivoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DispositivoEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTipoDispositivo;
            if (_cursor.isNull(_cursorIndexOfTipoDispositivo)) {
              _tmpTipoDispositivo = null;
            } else {
              _tmpTipoDispositivo = _cursor.getString(_cursorIndexOfTipoDispositivo);
            }
            final String _tmpMarca;
            if (_cursor.isNull(_cursorIndexOfMarca)) {
              _tmpMarca = null;
            } else {
              _tmpMarca = _cursor.getString(_cursorIndexOfMarca);
            }
            final String _tmpModelo;
            if (_cursor.isNull(_cursorIndexOfModelo)) {
              _tmpModelo = null;
            } else {
              _tmpModelo = _cursor.getString(_cursorIndexOfModelo);
            }
            final String _tmpSerial;
            if (_cursor.isNull(_cursorIndexOfSerial)) {
              _tmpSerial = null;
            } else {
              _tmpSerial = _cursor.getString(_cursorIndexOfSerial);
            }
            final String _tmpFechaAdquisicion;
            if (_cursor.isNull(_cursorIndexOfFechaAdquisicion)) {
              _tmpFechaAdquisicion = null;
            } else {
              _tmpFechaAdquisicion = _cursor.getString(_cursorIndexOfFechaAdquisicion);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _item = new DispositivoEntity(_tmpId,_tmpTipoDispositivo,_tmpMarca,_tmpModelo,_tmpSerial,_tmpFechaAdquisicion,_tmpActivo,_tmpServerId);
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
  public Object getAllActiveSync(final Continuation<? super List<DispositivoEntity>> arg0) {
    final String _sql = "SELECT * FROM dispositivos WHERE activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DispositivoEntity>>() {
      @Override
      @NonNull
      public List<DispositivoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTipoDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipoDispositivo");
          final int _cursorIndexOfMarca = CursorUtil.getColumnIndexOrThrow(_cursor, "marca");
          final int _cursorIndexOfModelo = CursorUtil.getColumnIndexOrThrow(_cursor, "modelo");
          final int _cursorIndexOfSerial = CursorUtil.getColumnIndexOrThrow(_cursor, "serial");
          final int _cursorIndexOfFechaAdquisicion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaAdquisicion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<DispositivoEntity> _result = new ArrayList<DispositivoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DispositivoEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTipoDispositivo;
            if (_cursor.isNull(_cursorIndexOfTipoDispositivo)) {
              _tmpTipoDispositivo = null;
            } else {
              _tmpTipoDispositivo = _cursor.getString(_cursorIndexOfTipoDispositivo);
            }
            final String _tmpMarca;
            if (_cursor.isNull(_cursorIndexOfMarca)) {
              _tmpMarca = null;
            } else {
              _tmpMarca = _cursor.getString(_cursorIndexOfMarca);
            }
            final String _tmpModelo;
            if (_cursor.isNull(_cursorIndexOfModelo)) {
              _tmpModelo = null;
            } else {
              _tmpModelo = _cursor.getString(_cursorIndexOfModelo);
            }
            final String _tmpSerial;
            if (_cursor.isNull(_cursorIndexOfSerial)) {
              _tmpSerial = null;
            } else {
              _tmpSerial = _cursor.getString(_cursorIndexOfSerial);
            }
            final String _tmpFechaAdquisicion;
            if (_cursor.isNull(_cursorIndexOfFechaAdquisicion)) {
              _tmpFechaAdquisicion = null;
            } else {
              _tmpFechaAdquisicion = _cursor.getString(_cursorIndexOfFechaAdquisicion);
            }
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _item = new DispositivoEntity(_tmpId,_tmpTipoDispositivo,_tmpMarca,_tmpModelo,_tmpSerial,_tmpFechaAdquisicion,_tmpActivo,_tmpServerId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
