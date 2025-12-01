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
import com.example.telephases.data.local.entities.MaletaEntity;
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
public final class MaletaDao_Impl implements MaletaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MaletaEntity> __insertionAdapterOfMaletaEntity;

  private final EntityDeletionOrUpdateAdapter<MaletaEntity> __deletionAdapterOfMaletaEntity;

  private final EntityDeletionOrUpdateAdapter<MaletaEntity> __updateAdapterOfMaletaEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public MaletaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMaletaEntity = new EntityInsertionAdapter<MaletaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `maletas` (`id`,`identificadorInvima`,`nombreMaleta`,`descripcion`,`asignadaAUsuarioId`,`entidadSaludId`,`ultimaRevision`,`activa`,`serverId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MaletaEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getIdentificadorInvima() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getIdentificadorInvima());
        }
        if (entity.getNombreMaleta() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNombreMaleta());
        }
        if (entity.getDescripcion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescripcion());
        }
        if (entity.getAsignadaAUsuarioId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAsignadaAUsuarioId());
        }
        if (entity.getEntidadSaludId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getEntidadSaludId());
        }
        if (entity.getUltimaRevision() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getUltimaRevision());
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
    this.__deletionAdapterOfMaletaEntity = new EntityDeletionOrUpdateAdapter<MaletaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `maletas` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MaletaEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMaletaEntity = new EntityDeletionOrUpdateAdapter<MaletaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `maletas` SET `id` = ?,`identificadorInvima` = ?,`nombreMaleta` = ?,`descripcion` = ?,`asignadaAUsuarioId` = ?,`entidadSaludId` = ?,`ultimaRevision` = ?,`activa` = ?,`serverId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MaletaEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getIdentificadorInvima() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getIdentificadorInvima());
        }
        if (entity.getNombreMaleta() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNombreMaleta());
        }
        if (entity.getDescripcion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescripcion());
        }
        if (entity.getAsignadaAUsuarioId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAsignadaAUsuarioId());
        }
        if (entity.getEntidadSaludId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getEntidadSaludId());
        }
        if (entity.getUltimaRevision() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getUltimaRevision());
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
        final String _query = "UPDATE maletas SET activa = 0 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MaletaEntity maleta, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMaletaEntity.insertAndReturnId(maleta);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMaleta(final MaletaEntity maleta,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMaletaEntity.insertAndReturnId(maleta);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<MaletaEntity> maletas,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMaletaEntity.insert(maletas);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final MaletaEntity maleta, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMaletaEntity.handle(maleta);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final MaletaEntity maleta, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMaletaEntity.handle(maleta);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMaleta(final MaletaEntity maleta,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMaletaEntity.handle(maleta);
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
  public Flow<List<MaletaEntity>> getAllActive() {
    final String _sql = "SELECT * FROM maletas WHERE activa = 1 ORDER BY nombreMaleta ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"maletas"}, new Callable<List<MaletaEntity>>() {
      @Override
      @NonNull
      public List<MaletaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdentificadorInvima = CursorUtil.getColumnIndexOrThrow(_cursor, "identificadorInvima");
          final int _cursorIndexOfNombreMaleta = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreMaleta");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfAsignadaAUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "asignadaAUsuarioId");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfUltimaRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaRevision");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<MaletaEntity> _result = new ArrayList<MaletaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MaletaEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpIdentificadorInvima;
            if (_cursor.isNull(_cursorIndexOfIdentificadorInvima)) {
              _tmpIdentificadorInvima = null;
            } else {
              _tmpIdentificadorInvima = _cursor.getString(_cursorIndexOfIdentificadorInvima);
            }
            final String _tmpNombreMaleta;
            if (_cursor.isNull(_cursorIndexOfNombreMaleta)) {
              _tmpNombreMaleta = null;
            } else {
              _tmpNombreMaleta = _cursor.getString(_cursorIndexOfNombreMaleta);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpAsignadaAUsuarioId;
            if (_cursor.isNull(_cursorIndexOfAsignadaAUsuarioId)) {
              _tmpAsignadaAUsuarioId = null;
            } else {
              _tmpAsignadaAUsuarioId = _cursor.getString(_cursorIndexOfAsignadaAUsuarioId);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpUltimaRevision;
            if (_cursor.isNull(_cursorIndexOfUltimaRevision)) {
              _tmpUltimaRevision = null;
            } else {
              _tmpUltimaRevision = _cursor.getString(_cursorIndexOfUltimaRevision);
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
            _item = new MaletaEntity(_tmpId,_tmpIdentificadorInvima,_tmpNombreMaleta,_tmpDescripcion,_tmpAsignadaAUsuarioId,_tmpEntidadSaludId,_tmpUltimaRevision,_tmpActiva,_tmpServerId);
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
  public Flow<List<MaletaEntity>> getByUsuario(final String usuarioId) {
    final String _sql = "SELECT * FROM maletas WHERE asignadaAUsuarioId = ? AND activa = 1 ORDER BY nombreMaleta ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (usuarioId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, usuarioId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"maletas"}, new Callable<List<MaletaEntity>>() {
      @Override
      @NonNull
      public List<MaletaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdentificadorInvima = CursorUtil.getColumnIndexOrThrow(_cursor, "identificadorInvima");
          final int _cursorIndexOfNombreMaleta = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreMaleta");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfAsignadaAUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "asignadaAUsuarioId");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfUltimaRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaRevision");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<MaletaEntity> _result = new ArrayList<MaletaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MaletaEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpIdentificadorInvima;
            if (_cursor.isNull(_cursorIndexOfIdentificadorInvima)) {
              _tmpIdentificadorInvima = null;
            } else {
              _tmpIdentificadorInvima = _cursor.getString(_cursorIndexOfIdentificadorInvima);
            }
            final String _tmpNombreMaleta;
            if (_cursor.isNull(_cursorIndexOfNombreMaleta)) {
              _tmpNombreMaleta = null;
            } else {
              _tmpNombreMaleta = _cursor.getString(_cursorIndexOfNombreMaleta);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpAsignadaAUsuarioId;
            if (_cursor.isNull(_cursorIndexOfAsignadaAUsuarioId)) {
              _tmpAsignadaAUsuarioId = null;
            } else {
              _tmpAsignadaAUsuarioId = _cursor.getString(_cursorIndexOfAsignadaAUsuarioId);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpUltimaRevision;
            if (_cursor.isNull(_cursorIndexOfUltimaRevision)) {
              _tmpUltimaRevision = null;
            } else {
              _tmpUltimaRevision = _cursor.getString(_cursorIndexOfUltimaRevision);
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
            _item = new MaletaEntity(_tmpId,_tmpIdentificadorInvima,_tmpNombreMaleta,_tmpDescripcion,_tmpAsignadaAUsuarioId,_tmpEntidadSaludId,_tmpUltimaRevision,_tmpActiva,_tmpServerId);
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
  public Object getById(final int id, final Continuation<? super MaletaEntity> $completion) {
    final String _sql = "SELECT * FROM maletas WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MaletaEntity>() {
      @Override
      @Nullable
      public MaletaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdentificadorInvima = CursorUtil.getColumnIndexOrThrow(_cursor, "identificadorInvima");
          final int _cursorIndexOfNombreMaleta = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreMaleta");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfAsignadaAUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "asignadaAUsuarioId");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfUltimaRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaRevision");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final MaletaEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpIdentificadorInvima;
            if (_cursor.isNull(_cursorIndexOfIdentificadorInvima)) {
              _tmpIdentificadorInvima = null;
            } else {
              _tmpIdentificadorInvima = _cursor.getString(_cursorIndexOfIdentificadorInvima);
            }
            final String _tmpNombreMaleta;
            if (_cursor.isNull(_cursorIndexOfNombreMaleta)) {
              _tmpNombreMaleta = null;
            } else {
              _tmpNombreMaleta = _cursor.getString(_cursorIndexOfNombreMaleta);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpAsignadaAUsuarioId;
            if (_cursor.isNull(_cursorIndexOfAsignadaAUsuarioId)) {
              _tmpAsignadaAUsuarioId = null;
            } else {
              _tmpAsignadaAUsuarioId = _cursor.getString(_cursorIndexOfAsignadaAUsuarioId);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpUltimaRevision;
            if (_cursor.isNull(_cursorIndexOfUltimaRevision)) {
              _tmpUltimaRevision = null;
            } else {
              _tmpUltimaRevision = _cursor.getString(_cursorIndexOfUltimaRevision);
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
            _result = new MaletaEntity(_tmpId,_tmpIdentificadorInvima,_tmpNombreMaleta,_tmpDescripcion,_tmpAsignadaAUsuarioId,_tmpEntidadSaludId,_tmpUltimaRevision,_tmpActiva,_tmpServerId);
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
  public Object getMaletaById(final int id, final Continuation<? super MaletaEntity> $completion) {
    final String _sql = "SELECT * FROM maletas WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MaletaEntity>() {
      @Override
      @Nullable
      public MaletaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdentificadorInvima = CursorUtil.getColumnIndexOrThrow(_cursor, "identificadorInvima");
          final int _cursorIndexOfNombreMaleta = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreMaleta");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfAsignadaAUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "asignadaAUsuarioId");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfUltimaRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaRevision");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final MaletaEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpIdentificadorInvima;
            if (_cursor.isNull(_cursorIndexOfIdentificadorInvima)) {
              _tmpIdentificadorInvima = null;
            } else {
              _tmpIdentificadorInvima = _cursor.getString(_cursorIndexOfIdentificadorInvima);
            }
            final String _tmpNombreMaleta;
            if (_cursor.isNull(_cursorIndexOfNombreMaleta)) {
              _tmpNombreMaleta = null;
            } else {
              _tmpNombreMaleta = _cursor.getString(_cursorIndexOfNombreMaleta);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpAsignadaAUsuarioId;
            if (_cursor.isNull(_cursorIndexOfAsignadaAUsuarioId)) {
              _tmpAsignadaAUsuarioId = null;
            } else {
              _tmpAsignadaAUsuarioId = _cursor.getString(_cursorIndexOfAsignadaAUsuarioId);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpUltimaRevision;
            if (_cursor.isNull(_cursorIndexOfUltimaRevision)) {
              _tmpUltimaRevision = null;
            } else {
              _tmpUltimaRevision = _cursor.getString(_cursorIndexOfUltimaRevision);
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
            _result = new MaletaEntity(_tmpId,_tmpIdentificadorInvima,_tmpNombreMaleta,_tmpDescripcion,_tmpAsignadaAUsuarioId,_tmpEntidadSaludId,_tmpUltimaRevision,_tmpActiva,_tmpServerId);
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
  public Object getMaletaByUsuarioId(final String usuarioId,
      final Continuation<? super MaletaEntity> $completion) {
    final String _sql = "SELECT * FROM maletas WHERE asignadaAUsuarioId = ? AND activa = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (usuarioId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, usuarioId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MaletaEntity>() {
      @Override
      @Nullable
      public MaletaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdentificadorInvima = CursorUtil.getColumnIndexOrThrow(_cursor, "identificadorInvima");
          final int _cursorIndexOfNombreMaleta = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreMaleta");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfAsignadaAUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "asignadaAUsuarioId");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfUltimaRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaRevision");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final MaletaEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpIdentificadorInvima;
            if (_cursor.isNull(_cursorIndexOfIdentificadorInvima)) {
              _tmpIdentificadorInvima = null;
            } else {
              _tmpIdentificadorInvima = _cursor.getString(_cursorIndexOfIdentificadorInvima);
            }
            final String _tmpNombreMaleta;
            if (_cursor.isNull(_cursorIndexOfNombreMaleta)) {
              _tmpNombreMaleta = null;
            } else {
              _tmpNombreMaleta = _cursor.getString(_cursorIndexOfNombreMaleta);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpAsignadaAUsuarioId;
            if (_cursor.isNull(_cursorIndexOfAsignadaAUsuarioId)) {
              _tmpAsignadaAUsuarioId = null;
            } else {
              _tmpAsignadaAUsuarioId = _cursor.getString(_cursorIndexOfAsignadaAUsuarioId);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpUltimaRevision;
            if (_cursor.isNull(_cursorIndexOfUltimaRevision)) {
              _tmpUltimaRevision = null;
            } else {
              _tmpUltimaRevision = _cursor.getString(_cursorIndexOfUltimaRevision);
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
            _result = new MaletaEntity(_tmpId,_tmpIdentificadorInvima,_tmpNombreMaleta,_tmpDescripcion,_tmpAsignadaAUsuarioId,_tmpEntidadSaludId,_tmpUltimaRevision,_tmpActiva,_tmpServerId);
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
      final Continuation<? super MaletaEntity> $completion) {
    final String _sql = "SELECT * FROM maletas WHERE serverId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, serverId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MaletaEntity>() {
      @Override
      @Nullable
      public MaletaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdentificadorInvima = CursorUtil.getColumnIndexOrThrow(_cursor, "identificadorInvima");
          final int _cursorIndexOfNombreMaleta = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreMaleta");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfAsignadaAUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "asignadaAUsuarioId");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfUltimaRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaRevision");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final MaletaEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpIdentificadorInvima;
            if (_cursor.isNull(_cursorIndexOfIdentificadorInvima)) {
              _tmpIdentificadorInvima = null;
            } else {
              _tmpIdentificadorInvima = _cursor.getString(_cursorIndexOfIdentificadorInvima);
            }
            final String _tmpNombreMaleta;
            if (_cursor.isNull(_cursorIndexOfNombreMaleta)) {
              _tmpNombreMaleta = null;
            } else {
              _tmpNombreMaleta = _cursor.getString(_cursorIndexOfNombreMaleta);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpAsignadaAUsuarioId;
            if (_cursor.isNull(_cursorIndexOfAsignadaAUsuarioId)) {
              _tmpAsignadaAUsuarioId = null;
            } else {
              _tmpAsignadaAUsuarioId = _cursor.getString(_cursorIndexOfAsignadaAUsuarioId);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpUltimaRevision;
            if (_cursor.isNull(_cursorIndexOfUltimaRevision)) {
              _tmpUltimaRevision = null;
            } else {
              _tmpUltimaRevision = _cursor.getString(_cursorIndexOfUltimaRevision);
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
            _result = new MaletaEntity(_tmpId,_tmpIdentificadorInvima,_tmpNombreMaleta,_tmpDescripcion,_tmpAsignadaAUsuarioId,_tmpEntidadSaludId,_tmpUltimaRevision,_tmpActiva,_tmpServerId);
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
  public Object getByIdentificadorInvima(final String identificadorInvima,
      final Continuation<? super MaletaEntity> $completion) {
    final String _sql = "SELECT * FROM maletas WHERE identificadorInvima = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (identificadorInvima == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, identificadorInvima);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MaletaEntity>() {
      @Override
      @Nullable
      public MaletaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdentificadorInvima = CursorUtil.getColumnIndexOrThrow(_cursor, "identificadorInvima");
          final int _cursorIndexOfNombreMaleta = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreMaleta");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfAsignadaAUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "asignadaAUsuarioId");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfUltimaRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaRevision");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final MaletaEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpIdentificadorInvima;
            if (_cursor.isNull(_cursorIndexOfIdentificadorInvima)) {
              _tmpIdentificadorInvima = null;
            } else {
              _tmpIdentificadorInvima = _cursor.getString(_cursorIndexOfIdentificadorInvima);
            }
            final String _tmpNombreMaleta;
            if (_cursor.isNull(_cursorIndexOfNombreMaleta)) {
              _tmpNombreMaleta = null;
            } else {
              _tmpNombreMaleta = _cursor.getString(_cursorIndexOfNombreMaleta);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpAsignadaAUsuarioId;
            if (_cursor.isNull(_cursorIndexOfAsignadaAUsuarioId)) {
              _tmpAsignadaAUsuarioId = null;
            } else {
              _tmpAsignadaAUsuarioId = _cursor.getString(_cursorIndexOfAsignadaAUsuarioId);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpUltimaRevision;
            if (_cursor.isNull(_cursorIndexOfUltimaRevision)) {
              _tmpUltimaRevision = null;
            } else {
              _tmpUltimaRevision = _cursor.getString(_cursorIndexOfUltimaRevision);
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
            _result = new MaletaEntity(_tmpId,_tmpIdentificadorInvima,_tmpNombreMaleta,_tmpDescripcion,_tmpAsignadaAUsuarioId,_tmpEntidadSaludId,_tmpUltimaRevision,_tmpActiva,_tmpServerId);
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
    final String _sql = "SELECT COUNT(*) FROM maletas WHERE activa = 1";
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
    final String _sql = "SELECT COUNT(*) FROM maletas WHERE serverId IS NULL AND activa = 1";
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
  public Object getUnsynced(final Continuation<? super List<MaletaEntity>> $completion) {
    final String _sql = "SELECT * FROM maletas WHERE serverId IS NULL AND activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MaletaEntity>>() {
      @Override
      @NonNull
      public List<MaletaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdentificadorInvima = CursorUtil.getColumnIndexOrThrow(_cursor, "identificadorInvima");
          final int _cursorIndexOfNombreMaleta = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreMaleta");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfAsignadaAUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "asignadaAUsuarioId");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfUltimaRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaRevision");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<MaletaEntity> _result = new ArrayList<MaletaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MaletaEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpIdentificadorInvima;
            if (_cursor.isNull(_cursorIndexOfIdentificadorInvima)) {
              _tmpIdentificadorInvima = null;
            } else {
              _tmpIdentificadorInvima = _cursor.getString(_cursorIndexOfIdentificadorInvima);
            }
            final String _tmpNombreMaleta;
            if (_cursor.isNull(_cursorIndexOfNombreMaleta)) {
              _tmpNombreMaleta = null;
            } else {
              _tmpNombreMaleta = _cursor.getString(_cursorIndexOfNombreMaleta);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpAsignadaAUsuarioId;
            if (_cursor.isNull(_cursorIndexOfAsignadaAUsuarioId)) {
              _tmpAsignadaAUsuarioId = null;
            } else {
              _tmpAsignadaAUsuarioId = _cursor.getString(_cursorIndexOfAsignadaAUsuarioId);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpUltimaRevision;
            if (_cursor.isNull(_cursorIndexOfUltimaRevision)) {
              _tmpUltimaRevision = null;
            } else {
              _tmpUltimaRevision = _cursor.getString(_cursorIndexOfUltimaRevision);
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
            _item = new MaletaEntity(_tmpId,_tmpIdentificadorInvima,_tmpNombreMaleta,_tmpDescripcion,_tmpAsignadaAUsuarioId,_tmpEntidadSaludId,_tmpUltimaRevision,_tmpActiva,_tmpServerId);
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
  public Object getAllActiveSync(final Continuation<? super List<MaletaEntity>> $completion) {
    final String _sql = "SELECT * FROM maletas WHERE activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MaletaEntity>>() {
      @Override
      @NonNull
      public List<MaletaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdentificadorInvima = CursorUtil.getColumnIndexOrThrow(_cursor, "identificadorInvima");
          final int _cursorIndexOfNombreMaleta = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreMaleta");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfAsignadaAUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "asignadaAUsuarioId");
          final int _cursorIndexOfEntidadSaludId = CursorUtil.getColumnIndexOrThrow(_cursor, "entidadSaludId");
          final int _cursorIndexOfUltimaRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaRevision");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<MaletaEntity> _result = new ArrayList<MaletaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MaletaEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpIdentificadorInvima;
            if (_cursor.isNull(_cursorIndexOfIdentificadorInvima)) {
              _tmpIdentificadorInvima = null;
            } else {
              _tmpIdentificadorInvima = _cursor.getString(_cursorIndexOfIdentificadorInvima);
            }
            final String _tmpNombreMaleta;
            if (_cursor.isNull(_cursorIndexOfNombreMaleta)) {
              _tmpNombreMaleta = null;
            } else {
              _tmpNombreMaleta = _cursor.getString(_cursorIndexOfNombreMaleta);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpAsignadaAUsuarioId;
            if (_cursor.isNull(_cursorIndexOfAsignadaAUsuarioId)) {
              _tmpAsignadaAUsuarioId = null;
            } else {
              _tmpAsignadaAUsuarioId = _cursor.getString(_cursorIndexOfAsignadaAUsuarioId);
            }
            final Integer _tmpEntidadSaludId;
            if (_cursor.isNull(_cursorIndexOfEntidadSaludId)) {
              _tmpEntidadSaludId = null;
            } else {
              _tmpEntidadSaludId = _cursor.getInt(_cursorIndexOfEntidadSaludId);
            }
            final String _tmpUltimaRevision;
            if (_cursor.isNull(_cursorIndexOfUltimaRevision)) {
              _tmpUltimaRevision = null;
            } else {
              _tmpUltimaRevision = _cursor.getString(_cursorIndexOfUltimaRevision);
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
            _item = new MaletaEntity(_tmpId,_tmpIdentificadorInvima,_tmpNombreMaleta,_tmpDescripcion,_tmpAsignadaAUsuarioId,_tmpEntidadSaludId,_tmpUltimaRevision,_tmpActiva,_tmpServerId);
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
