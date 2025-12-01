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
import com.example.telephases.data.local.entities.MaletaDispositivoEntity;
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
public final class MaletaDispositivoDao_Impl implements MaletaDispositivoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MaletaDispositivoEntity> __insertionAdapterOfMaletaDispositivoEntity;

  private final EntityDeletionOrUpdateAdapter<MaletaDispositivoEntity> __deletionAdapterOfMaletaDispositivoEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByMaletaAndDispositivo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByMaleta;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByDispositivo;

  public MaletaDispositivoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMaletaDispositivoEntity = new EntityInsertionAdapter<MaletaDispositivoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `maleta_dispositivos` (`maletaId`,`dispositivoId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MaletaDispositivoEntity entity) {
        statement.bindLong(1, entity.getMaletaId());
        statement.bindLong(2, entity.getDispositivoId());
      }
    };
    this.__deletionAdapterOfMaletaDispositivoEntity = new EntityDeletionOrUpdateAdapter<MaletaDispositivoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `maleta_dispositivos` WHERE `maletaId` = ? AND `dispositivoId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MaletaDispositivoEntity entity) {
        statement.bindLong(1, entity.getMaletaId());
        statement.bindLong(2, entity.getDispositivoId());
      }
    };
    this.__preparedStmtOfDeleteByMaletaAndDispositivo = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM maleta_dispositivos WHERE maletaId = ? AND dispositivoId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByMaleta = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM maleta_dispositivos WHERE maletaId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByDispositivo = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM maleta_dispositivos WHERE dispositivoId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MaletaDispositivoEntity maletaDispositivo,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMaletaDispositivoEntity.insert(maletaDispositivo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<MaletaDispositivoEntity> maletaDispositivos,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMaletaDispositivoEntity.insert(maletaDispositivos);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final MaletaDispositivoEntity maletaDispositivo,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMaletaDispositivoEntity.handle(maletaDispositivo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByMaletaAndDispositivo(final int maletaId, final int dispositivoId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByMaletaAndDispositivo.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, maletaId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, dispositivoId);
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
          __preparedStmtOfDeleteByMaletaAndDispositivo.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByMaleta(final int maletaId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByMaleta.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, maletaId);
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
          __preparedStmtOfDeleteByMaleta.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByDispositivo(final int dispositivoId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByDispositivo.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, dispositivoId);
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
          __preparedStmtOfDeleteByDispositivo.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MaletaDispositivoEntity>> getAll() {
    final String _sql = "SELECT * FROM maleta_dispositivos";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"maleta_dispositivos"}, new Callable<List<MaletaDispositivoEntity>>() {
      @Override
      @NonNull
      public List<MaletaDispositivoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMaletaId = CursorUtil.getColumnIndexOrThrow(_cursor, "maletaId");
          final int _cursorIndexOfDispositivoId = CursorUtil.getColumnIndexOrThrow(_cursor, "dispositivoId");
          final List<MaletaDispositivoEntity> _result = new ArrayList<MaletaDispositivoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MaletaDispositivoEntity _item;
            final int _tmpMaletaId;
            _tmpMaletaId = _cursor.getInt(_cursorIndexOfMaletaId);
            final int _tmpDispositivoId;
            _tmpDispositivoId = _cursor.getInt(_cursorIndexOfDispositivoId);
            _item = new MaletaDispositivoEntity(_tmpMaletaId,_tmpDispositivoId);
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
  public Flow<List<MaletaDispositivoEntity>> getByMaleta(final int maletaId) {
    final String _sql = "SELECT * FROM maleta_dispositivos WHERE maletaId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, maletaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"maleta_dispositivos"}, new Callable<List<MaletaDispositivoEntity>>() {
      @Override
      @NonNull
      public List<MaletaDispositivoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMaletaId = CursorUtil.getColumnIndexOrThrow(_cursor, "maletaId");
          final int _cursorIndexOfDispositivoId = CursorUtil.getColumnIndexOrThrow(_cursor, "dispositivoId");
          final List<MaletaDispositivoEntity> _result = new ArrayList<MaletaDispositivoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MaletaDispositivoEntity _item;
            final int _tmpMaletaId;
            _tmpMaletaId = _cursor.getInt(_cursorIndexOfMaletaId);
            final int _tmpDispositivoId;
            _tmpDispositivoId = _cursor.getInt(_cursorIndexOfDispositivoId);
            _item = new MaletaDispositivoEntity(_tmpMaletaId,_tmpDispositivoId);
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
  public Flow<List<MaletaDispositivoEntity>> getByDispositivo(final int dispositivoId) {
    final String _sql = "SELECT * FROM maleta_dispositivos WHERE dispositivoId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dispositivoId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"maleta_dispositivos"}, new Callable<List<MaletaDispositivoEntity>>() {
      @Override
      @NonNull
      public List<MaletaDispositivoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMaletaId = CursorUtil.getColumnIndexOrThrow(_cursor, "maletaId");
          final int _cursorIndexOfDispositivoId = CursorUtil.getColumnIndexOrThrow(_cursor, "dispositivoId");
          final List<MaletaDispositivoEntity> _result = new ArrayList<MaletaDispositivoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MaletaDispositivoEntity _item;
            final int _tmpMaletaId;
            _tmpMaletaId = _cursor.getInt(_cursorIndexOfMaletaId);
            final int _tmpDispositivoId;
            _tmpDispositivoId = _cursor.getInt(_cursorIndexOfDispositivoId);
            _item = new MaletaDispositivoEntity(_tmpMaletaId,_tmpDispositivoId);
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
  public Object getByMaletaAndDispositivo(final int maletaId, final int dispositivoId,
      final Continuation<? super MaletaDispositivoEntity> $completion) {
    final String _sql = "SELECT * FROM maleta_dispositivos WHERE maletaId = ? AND dispositivoId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, maletaId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, dispositivoId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MaletaDispositivoEntity>() {
      @Override
      @Nullable
      public MaletaDispositivoEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMaletaId = CursorUtil.getColumnIndexOrThrow(_cursor, "maletaId");
          final int _cursorIndexOfDispositivoId = CursorUtil.getColumnIndexOrThrow(_cursor, "dispositivoId");
          final MaletaDispositivoEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpMaletaId;
            _tmpMaletaId = _cursor.getInt(_cursorIndexOfMaletaId);
            final int _tmpDispositivoId;
            _tmpDispositivoId = _cursor.getInt(_cursorIndexOfDispositivoId);
            _result = new MaletaDispositivoEntity(_tmpMaletaId,_tmpDispositivoId);
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
  public Object getCountByMaleta(final int maletaId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM maleta_dispositivos WHERE maletaId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, maletaId);
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
  public Object getCountByDispositivo(final int dispositivoId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM maleta_dispositivos WHERE dispositivoId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dispositivoId);
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
