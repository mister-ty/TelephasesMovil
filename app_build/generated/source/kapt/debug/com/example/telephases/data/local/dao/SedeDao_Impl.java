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
import com.example.telephases.data.local.entities.SedeEntity;
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
public final class SedeDao_Impl implements SedeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SedeEntity> __insertionAdapterOfSedeEntity;

  private final EntityDeletionOrUpdateAdapter<SedeEntity> __deletionAdapterOfSedeEntity;

  private final EntityDeletionOrUpdateAdapter<SedeEntity> __updateAdapterOfSedeEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public SedeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSedeEntity = new EntityInsertionAdapter<SedeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sedes` (`id`,`entidadSaludId`,`nombreSede`,`direccion`,`telefono`,`ciudad`,`responsableSedeNombre`,`activa`,`serverId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SedeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getEntidadSaludId());
        if (entity.getNombreSede() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNombreSede());
        }
        if (entity.getDireccion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDireccion());
        }
        if (entity.getTelefono() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTelefono());
        }
        if (entity.getCiudad() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCiudad());
        }
        if (entity.getResponsableSedeNombre() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getResponsableSedeNombre());
        }
        final int _tmp = entity.getActiva() ? 1 : 0;
        statement.bindLong(8, _tmp);
        if (entity.getServerId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getServerId());
        }
      }
    };
    this.__deletionAdapterOfSedeEntity = new EntityDeletionOrUpdateAdapter<SedeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `sedes` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SedeEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSedeEntity = new EntityDeletionOrUpdateAdapter<SedeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sedes` SET `id` = ?,`entidadSaludId` = ?,`nombreSede` = ?,`direccion` = ?,`telefono` = ?,`ciudad` = ?,`responsableSedeNombre` = ?,`activa` = ?,`serverId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SedeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getEntidadSaludId());
        if (entity.getNombreSede() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNombreSede());
        }
        if (entity.getDireccion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDireccion());
        }
        if (entity.getTelefono() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTelefono());
        }
        if (entity.getCiudad() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCiudad());
        }
        if (entity.getResponsableSedeNombre() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getResponsableSedeNombre());
        }
        final int _tmp = entity.getActiva() ? 1 : 0;
        statement.bindLong(8, _tmp);
        if (entity.getServerId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getServerId());
        }
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sedes SET activa = 0 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SedeEntity sede, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSedeEntity.insertAndReturnId(sede);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<SedeEntity> sedes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSedeEntity.insert(sedes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final SedeEntity sede, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSedeEntity.handle(sede);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SedeEntity sede, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSedeEntity.handle(sede);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object softDelete(final int id, final Continuation<? super Unit> $completion) {
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
    }, $completion);
  }

  @Override
  public Flow<List<SedeEntity>> getAllActive() {
    final String _sql = "SELECT * FROM sedes WHERE activa = 1 ORDER BY nombreSede ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sedes"}, new Callable<List<SedeEntity>>() {
      @Override
      @NonNull
      public List<SedeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfNombreSede = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreSede");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfCiudad = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad");
          final int _cursorIndexOfResponsableSedeNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "responsableSedeNombre");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<SedeEntity> _result = new ArrayList<SedeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SedeEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpEntidadSaludId;
            _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            final String _tmpNombreSede;
            if (_cursor.isNull(_cursorIndexOfNombreSede)) {
              _tmpNombreSede = null;
            } else {
              _tmpNombreSede = _cursor.getString(_cursorIndexOfNombreSede);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpCiudad;
            if (_cursor.isNull(_cursorIndexOfCiudad)) {
              _tmpCiudad = null;
            } else {
              _tmpCiudad = _cursor.getString(_cursorIndexOfCiudad);
            }
            final String _tmpResponsableSedeNombre;
            if (_cursor.isNull(_cursorIndexOfResponsableSedeNombre)) {
              _tmpResponsableSedeNombre = null;
            } else {
              _tmpResponsableSedeNombre = _cursor.getString(_cursorIndexOfResponsableSedeNombre);
            }
            final boolean _tmpActiva;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiva);
            _tmpActiva = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _item = new SedeEntity(_tmpId,_tmpEntidadSaludId,_tmpNombreSede,_tmpDireccion,_tmpTelefono,_tmpCiudad,_tmpResponsableSedeNombre,_tmpActiva,_tmpServerId);
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
  public Flow<List<SedeEntity>> getByEntidadSalud(final int entidadSaludId) {
    final String _sql = "SELECT * FROM sedes WHERE entidadSaludId = ? AND activa = 1 ORDER BY nombreSede ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, entidadSaludId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sedes"}, new Callable<List<SedeEntity>>() {
      @Override
      @NonNull
      public List<SedeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfNombreSede = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreSede");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfCiudad = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad");
          final int _cursorIndexOfResponsableSedeNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "responsableSedeNombre");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<SedeEntity> _result = new ArrayList<SedeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SedeEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpEntidadSaludId;
            _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            final String _tmpNombreSede;
            if (_cursor.isNull(_cursorIndexOfNombreSede)) {
              _tmpNombreSede = null;
            } else {
              _tmpNombreSede = _cursor.getString(_cursorIndexOfNombreSede);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpCiudad;
            if (_cursor.isNull(_cursorIndexOfCiudad)) {
              _tmpCiudad = null;
            } else {
              _tmpCiudad = _cursor.getString(_cursorIndexOfCiudad);
            }
            final String _tmpResponsableSedeNombre;
            if (_cursor.isNull(_cursorIndexOfResponsableSedeNombre)) {
              _tmpResponsableSedeNombre = null;
            } else {
              _tmpResponsableSedeNombre = _cursor.getString(_cursorIndexOfResponsableSedeNombre);
            }
            final boolean _tmpActiva;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiva);
            _tmpActiva = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _item = new SedeEntity(_tmpId,_tmpEntidadSaludId,_tmpNombreSede,_tmpDireccion,_tmpTelefono,_tmpCiudad,_tmpResponsableSedeNombre,_tmpActiva,_tmpServerId);
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
  public Object getById(final int id, final Continuation<? super SedeEntity> $completion) {
    final String _sql = "SELECT * FROM sedes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SedeEntity>() {
      @Override
      @Nullable
      public SedeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfNombreSede = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreSede");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfCiudad = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad");
          final int _cursorIndexOfResponsableSedeNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "responsableSedeNombre");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final SedeEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpEntidadSaludId;
            _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            final String _tmpNombreSede;
            if (_cursor.isNull(_cursorIndexOfNombreSede)) {
              _tmpNombreSede = null;
            } else {
              _tmpNombreSede = _cursor.getString(_cursorIndexOfNombreSede);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpCiudad;
            if (_cursor.isNull(_cursorIndexOfCiudad)) {
              _tmpCiudad = null;
            } else {
              _tmpCiudad = _cursor.getString(_cursorIndexOfCiudad);
            }
            final String _tmpResponsableSedeNombre;
            if (_cursor.isNull(_cursorIndexOfResponsableSedeNombre)) {
              _tmpResponsableSedeNombre = null;
            } else {
              _tmpResponsableSedeNombre = _cursor.getString(_cursorIndexOfResponsableSedeNombre);
            }
            final boolean _tmpActiva;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiva);
            _tmpActiva = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _result = new SedeEntity(_tmpId,_tmpEntidadSaludId,_tmpNombreSede,_tmpDireccion,_tmpTelefono,_tmpCiudad,_tmpResponsableSedeNombre,_tmpActiva,_tmpServerId);
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
  public Object getByServerId(final int serverId,
      final Continuation<? super SedeEntity> $completion) {
    final String _sql = "SELECT * FROM sedes WHERE serverId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, serverId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SedeEntity>() {
      @Override
      @Nullable
      public SedeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfNombreSede = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreSede");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfCiudad = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad");
          final int _cursorIndexOfResponsableSedeNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "responsableSedeNombre");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final SedeEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpEntidadSaludId;
            _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            final String _tmpNombreSede;
            if (_cursor.isNull(_cursorIndexOfNombreSede)) {
              _tmpNombreSede = null;
            } else {
              _tmpNombreSede = _cursor.getString(_cursorIndexOfNombreSede);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpCiudad;
            if (_cursor.isNull(_cursorIndexOfCiudad)) {
              _tmpCiudad = null;
            } else {
              _tmpCiudad = _cursor.getString(_cursorIndexOfCiudad);
            }
            final String _tmpResponsableSedeNombre;
            if (_cursor.isNull(_cursorIndexOfResponsableSedeNombre)) {
              _tmpResponsableSedeNombre = null;
            } else {
              _tmpResponsableSedeNombre = _cursor.getString(_cursorIndexOfResponsableSedeNombre);
            }
            final boolean _tmpActiva;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiva);
            _tmpActiva = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _result = new SedeEntity(_tmpId,_tmpEntidadSaludId,_tmpNombreSede,_tmpDireccion,_tmpTelefono,_tmpCiudad,_tmpResponsableSedeNombre,_tmpActiva,_tmpServerId);
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
    final String _sql = "SELECT COUNT(*) FROM sedes WHERE activa = 1";
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
  public Object getUnsyncedCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM sedes WHERE serverId IS NULL AND activa = 1";
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
  public Object getUnsynced(final Continuation<? super List<SedeEntity>> $completion) {
    final String _sql = "SELECT * FROM sedes WHERE serverId IS NULL AND activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SedeEntity>>() {
      @Override
      @NonNull
      public List<SedeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfNombreSede = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreSede");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfCiudad = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad");
          final int _cursorIndexOfResponsableSedeNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "responsableSedeNombre");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<SedeEntity> _result = new ArrayList<SedeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SedeEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpEntidadSaludId;
            _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            final String _tmpNombreSede;
            if (_cursor.isNull(_cursorIndexOfNombreSede)) {
              _tmpNombreSede = null;
            } else {
              _tmpNombreSede = _cursor.getString(_cursorIndexOfNombreSede);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpCiudad;
            if (_cursor.isNull(_cursorIndexOfCiudad)) {
              _tmpCiudad = null;
            } else {
              _tmpCiudad = _cursor.getString(_cursorIndexOfCiudad);
            }
            final String _tmpResponsableSedeNombre;
            if (_cursor.isNull(_cursorIndexOfResponsableSedeNombre)) {
              _tmpResponsableSedeNombre = null;
            } else {
              _tmpResponsableSedeNombre = _cursor.getString(_cursorIndexOfResponsableSedeNombre);
            }
            final boolean _tmpActiva;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiva);
            _tmpActiva = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _item = new SedeEntity(_tmpId,_tmpEntidadSaludId,_tmpNombreSede,_tmpDireccion,_tmpTelefono,_tmpCiudad,_tmpResponsableSedeNombre,_tmpActiva,_tmpServerId);
            _result.add(_item);
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
  public Object getAllActiveSync(final Continuation<? super List<SedeEntity>> $completion) {
    final String _sql = "SELECT * FROM sedes WHERE activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SedeEntity>>() {
      @Override
      @NonNull
      public List<SedeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfNombreSede = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreSede");
          final int _cursorIndexOfDireccion = CursorUtil.getColumnIndexOrThrow(_cursor, "direccion");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfCiudad = CursorUtil.getColumnIndexOrThrow(_cursor, "ciudad");
          final int _cursorIndexOfResponsableSedeNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "responsableSedeNombre");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<SedeEntity> _result = new ArrayList<SedeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SedeEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpEntidadSaludId;
            _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            final String _tmpNombreSede;
            if (_cursor.isNull(_cursorIndexOfNombreSede)) {
              _tmpNombreSede = null;
            } else {
              _tmpNombreSede = _cursor.getString(_cursorIndexOfNombreSede);
            }
            final String _tmpDireccion;
            if (_cursor.isNull(_cursorIndexOfDireccion)) {
              _tmpDireccion = null;
            } else {
              _tmpDireccion = _cursor.getString(_cursorIndexOfDireccion);
            }
            final String _tmpTelefono;
            if (_cursor.isNull(_cursorIndexOfTelefono)) {
              _tmpTelefono = null;
            } else {
              _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
            }
            final String _tmpCiudad;
            if (_cursor.isNull(_cursorIndexOfCiudad)) {
              _tmpCiudad = null;
            } else {
              _tmpCiudad = _cursor.getString(_cursorIndexOfCiudad);
            }
            final String _tmpResponsableSedeNombre;
            if (_cursor.isNull(_cursorIndexOfResponsableSedeNombre)) {
              _tmpResponsableSedeNombre = null;
            } else {
              _tmpResponsableSedeNombre = _cursor.getString(_cursorIndexOfResponsableSedeNombre);
            }
            final boolean _tmpActiva;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiva);
            _tmpActiva = _tmp != 0;
            final Integer _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getInt(_cursorIndexOfServerId);
            }
            _item = new SedeEntity(_tmpId,_tmpEntidadSaludId,_tmpNombreSede,_tmpDireccion,_tmpTelefono,_tmpCiudad,_tmpResponsableSedeNombre,_tmpActiva,_tmpServerId);
            _result.add(_item);
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
