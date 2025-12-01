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
import com.example.telephases.data.local.database.Converters;
import com.example.telephases.data.local.entities.SyncMetadataEntity;
import java.lang.Boolean;
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
public final class SyncMetadataDao_Impl implements SyncMetadataDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SyncMetadataEntity> __insertionAdapterOfSyncMetadataEntity;

  private final EntityDeletionOrUpdateAdapter<SyncMetadataEntity> __deletionAdapterOfSyncMetadataEntity;

  private final EntityDeletionOrUpdateAdapter<SyncMetadataEntity> __updateAdapterOfSyncMetadataEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMetadataByKey;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMetadataValue;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSyncStatus;

  private final SharedSQLiteStatement __preparedStmtOfCleanupOldMetadata;

  private final Converters __converters = new Converters();

  public SyncMetadataDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSyncMetadataEntity = new EntityInsertionAdapter<SyncMetadataEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sync_metadata` (`key`,`value`,`last_updated`,`sync_status`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncMetadataEntity entity) {
        if (entity.getKey() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getKey());
        }
        if (entity.getValue() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getValue());
        }
        if (entity.getLastUpdated() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getLastUpdated());
        }
        if (entity.getSyncStatus() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSyncStatus());
        }
      }
    };
    this.__deletionAdapterOfSyncMetadataEntity = new EntityDeletionOrUpdateAdapter<SyncMetadataEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `sync_metadata` WHERE `key` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncMetadataEntity entity) {
        if (entity.getKey() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getKey());
        }
      }
    };
    this.__updateAdapterOfSyncMetadataEntity = new EntityDeletionOrUpdateAdapter<SyncMetadataEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sync_metadata` SET `key` = ?,`value` = ?,`last_updated` = ?,`sync_status` = ? WHERE `key` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncMetadataEntity entity) {
        if (entity.getKey() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getKey());
        }
        if (entity.getValue() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getValue());
        }
        if (entity.getLastUpdated() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getLastUpdated());
        }
        if (entity.getSyncStatus() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSyncStatus());
        }
        if (entity.getKey() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getKey());
        }
      }
    };
    this.__preparedStmtOfDeleteMetadataByKey = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sync_metadata WHERE key = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateMetadataValue = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sync_metadata SET value = ?, last_updated = ? WHERE key = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSyncStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sync_metadata SET sync_status = ?, last_updated = ? WHERE key = ?";
        return _query;
      }
    };
    this.__preparedStmtOfCleanupOldMetadata = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sync_metadata WHERE datetime(last_updated) < datetime('now', '-30 days')";
        return _query;
      }
    };
  }

  @Override
  public Object insertMetadata(final SyncMetadataEntity metadata,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSyncMetadataEntity.insert(metadata);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertMetadatas(final List<SyncMetadataEntity> metadatas,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSyncMetadataEntity.insert(metadatas);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteMetadata(final SyncMetadataEntity metadata,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSyncMetadataEntity.handle(metadata);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object updateMetadata(final SyncMetadataEntity metadata,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSyncMetadataEntity.handle(metadata);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteMetadataByKey(final String key, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMetadataByKey.acquire();
        int _argIndex = 1;
        if (key == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, key);
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
          __preparedStmtOfDeleteMetadataByKey.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Object updateMetadataValue(final String key, final String value, final String timestamp,
      final Continuation<? super Unit> arg3) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMetadataValue.acquire();
        int _argIndex = 1;
        if (value == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, value);
        }
        _argIndex = 2;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
        }
        _argIndex = 3;
        if (key == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, key);
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
          __preparedStmtOfUpdateMetadataValue.release(_stmt);
        }
      }
    }, arg3);
  }

  @Override
  public Object updateSyncStatus(final String key, final String status, final String timestamp,
      final Continuation<? super Unit> arg3) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSyncStatus.acquire();
        int _argIndex = 1;
        if (status == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, status);
        }
        _argIndex = 2;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
        }
        _argIndex = 3;
        if (key == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, key);
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
          __preparedStmtOfUpdateSyncStatus.release(_stmt);
        }
      }
    }, arg3);
  }

  @Override
  public Object cleanupOldMetadata(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCleanupOldMetadata.acquire();
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
          __preparedStmtOfCleanupOldMetadata.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object getMetadataByKey(final String key,
      final Continuation<? super SyncMetadataEntity> arg1) {
    final String _sql = "SELECT * FROM sync_metadata WHERE key = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (key == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, key);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SyncMetadataEntity>() {
      @Override
      @Nullable
      public SyncMetadataEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_updated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
          final SyncMetadataEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpKey;
            if (_cursor.isNull(_cursorIndexOfKey)) {
              _tmpKey = null;
            } else {
              _tmpKey = _cursor.getString(_cursorIndexOfKey);
            }
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _result = new SyncMetadataEntity(_tmpKey,_tmpValue,_tmpLastUpdated,_tmpSyncStatus);
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
  public Object getAllMetadata(final Continuation<? super List<SyncMetadataEntity>> arg0) {
    final String _sql = "SELECT * FROM sync_metadata ORDER BY last_updated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SyncMetadataEntity>>() {
      @Override
      @NonNull
      public List<SyncMetadataEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_updated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
          final List<SyncMetadataEntity> _result = new ArrayList<SyncMetadataEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SyncMetadataEntity _item;
            final String _tmpKey;
            if (_cursor.isNull(_cursorIndexOfKey)) {
              _tmpKey = null;
            } else {
              _tmpKey = _cursor.getString(_cursorIndexOfKey);
            }
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _item = new SyncMetadataEntity(_tmpKey,_tmpValue,_tmpLastUpdated,_tmpSyncStatus);
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
  public Flow<List<SyncMetadataEntity>> getAllMetadataFlow() {
    final String _sql = "SELECT * FROM sync_metadata ORDER BY last_updated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sync_metadata"}, new Callable<List<SyncMetadataEntity>>() {
      @Override
      @NonNull
      public List<SyncMetadataEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_updated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
          final List<SyncMetadataEntity> _result = new ArrayList<SyncMetadataEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SyncMetadataEntity _item;
            final String _tmpKey;
            if (_cursor.isNull(_cursorIndexOfKey)) {
              _tmpKey = null;
            } else {
              _tmpKey = _cursor.getString(_cursorIndexOfKey);
            }
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _item = new SyncMetadataEntity(_tmpKey,_tmpValue,_tmpLastUpdated,_tmpSyncStatus);
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
  public Object getMetadataByStatus(final String status,
      final Continuation<? super List<SyncMetadataEntity>> arg1) {
    final String _sql = "SELECT * FROM sync_metadata WHERE sync_status = ? ORDER BY last_updated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (status == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, status);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SyncMetadataEntity>>() {
      @Override
      @NonNull
      public List<SyncMetadataEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_updated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
          final List<SyncMetadataEntity> _result = new ArrayList<SyncMetadataEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SyncMetadataEntity _item;
            final String _tmpKey;
            if (_cursor.isNull(_cursorIndexOfKey)) {
              _tmpKey = null;
            } else {
              _tmpKey = _cursor.getString(_cursorIndexOfKey);
            }
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _item = new SyncMetadataEntity(_tmpKey,_tmpValue,_tmpLastUpdated,_tmpSyncStatus);
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
  public Object getMetadataValue(final String key, final Continuation<? super String> arg1) {
    final String _sql = "SELECT value FROM sync_metadata WHERE key = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (key == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, key);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<String>() {
      @Override
      @Nullable
      public String call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final String _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getString(0);
            }
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
  public Object existsMetadataWithKey(final String key, final Continuation<? super Boolean> arg1) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM sync_metadata WHERE key = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (key == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, key);
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
  public Object getErrorMetadata(final Continuation<? super List<SyncMetadataEntity>> arg0) {
    final String _sql = "SELECT * FROM sync_metadata WHERE sync_status = 'failed' ORDER BY last_updated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SyncMetadataEntity>>() {
      @Override
      @NonNull
      public List<SyncMetadataEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_updated");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
          final List<SyncMetadataEntity> _result = new ArrayList<SyncMetadataEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SyncMetadataEntity _item;
            final String _tmpKey;
            if (_cursor.isNull(_cursorIndexOfKey)) {
              _tmpKey = null;
            } else {
              _tmpKey = _cursor.getString(_cursorIndexOfKey);
            }
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _item = new SyncMetadataEntity(_tmpKey,_tmpValue,_tmpLastUpdated,_tmpSyncStatus);
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
  public Object getMetadataCountByStatus(final String status,
      final Continuation<? super Integer> arg1) {
    final String _sql = "SELECT COUNT(*) FROM sync_metadata WHERE sync_status = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (status == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, status);
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
    }, arg1);
  }

  @Override
  public Object updateLastSyncTimestamp(final String timestamp,
      final Continuation<? super Unit> arg1) {
    return SyncMetadataDao.DefaultImpls.updateLastSyncTimestamp(SyncMetadataDao_Impl.this, timestamp, arg1);
  }

  @Override
  public Object getLastSyncTimestamp(final Continuation<? super String> arg0) {
    return SyncMetadataDao.DefaultImpls.getLastSyncTimestamp(SyncMetadataDao_Impl.this, arg0);
  }

  @Override
  public Object updatePendingPatientsCount(final int count, final Continuation<? super Unit> arg1) {
    return SyncMetadataDao.DefaultImpls.updatePendingPatientsCount(SyncMetadataDao_Impl.this, count, arg1);
  }

  @Override
  public Object getPendingPatientsCount(final Continuation<? super Integer> arg0) {
    return SyncMetadataDao.DefaultImpls.getPendingPatientsCount(SyncMetadataDao_Impl.this, arg0);
  }

  @Override
  public Object updatePendingExamsCount(final int count, final Continuation<? super Unit> arg1) {
    return SyncMetadataDao.DefaultImpls.updatePendingExamsCount(SyncMetadataDao_Impl.this, count, arg1);
  }

  @Override
  public Object getPendingExamsCount(final Continuation<? super Integer> arg0) {
    return SyncMetadataDao.DefaultImpls.getPendingExamsCount(SyncMetadataDao_Impl.this, arg0);
  }

  @Override
  public Object updateNetworkStatus(final boolean isConnected,
      final Continuation<? super Unit> arg1) {
    return SyncMetadataDao.DefaultImpls.updateNetworkStatus(SyncMetadataDao_Impl.this, isConnected, arg1);
  }

  @Override
  public Object getNetworkStatus(final Continuation<? super Boolean> arg0) {
    return SyncMetadataDao.DefaultImpls.getNetworkStatus(SyncMetadataDao_Impl.this, arg0);
  }

  @Override
  public Object updateAutoSyncEnabled(final boolean enabled,
      final Continuation<? super Unit> arg1) {
    return SyncMetadataDao.DefaultImpls.updateAutoSyncEnabled(SyncMetadataDao_Impl.this, enabled, arg1);
  }

  @Override
  public Object isAutoSyncEnabled(final Continuation<? super Boolean> arg0) {
    return SyncMetadataDao.DefaultImpls.isAutoSyncEnabled(SyncMetadataDao_Impl.this, arg0);
  }

  @Override
  public Object logSyncError(final String errorMessage, final Continuation<? super Unit> arg1) {
    return SyncMetadataDao.DefaultImpls.logSyncError(SyncMetadataDao_Impl.this, errorMessage, arg1);
  }

  @Override
  public Object getLastSyncError(final Continuation<? super String> arg0) {
    return SyncMetadataDao.DefaultImpls.getLastSyncError(SyncMetadataDao_Impl.this, arg0);
  }

  @Override
  public Object markSuccessfulSync(final String timestamp, final Continuation<? super Unit> arg1) {
    return SyncMetadataDao.DefaultImpls.markSuccessfulSync(SyncMetadataDao_Impl.this, timestamp, arg1);
  }

  @Override
  public Object getLastSuccessfulSync(final Continuation<? super String> arg0) {
    return SyncMetadataDao.DefaultImpls.getLastSuccessfulSync(SyncMetadataDao_Impl.this, arg0);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
