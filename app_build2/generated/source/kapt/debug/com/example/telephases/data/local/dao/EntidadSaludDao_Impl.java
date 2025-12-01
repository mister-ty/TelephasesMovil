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
import com.example.telephases.data.local.entities.EntidadSaludEntity;
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
public final class EntidadSaludDao_Impl implements EntidadSaludDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EntidadSaludEntity> __insertionAdapterOfEntidadSaludEntity;

  private final EntityDeletionOrUpdateAdapter<EntidadSaludEntity> __deletionAdapterOfEntidadSaludEntity;

  private final EntityDeletionOrUpdateAdapter<EntidadSaludEntity> __updateAdapterOfEntidadSaludEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public EntidadSaludDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEntidadSaludEntity = new EntityInsertionAdapter<EntidadSaludEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `entidades_salud` (`id`,`nombreEntidad`,`nit`,`contactoPrincipalNombre`,`contactoPrincipalEmail`,`contactoPrincipalTelefono`,`configuracionJson`,`fechaRegistro`,`activa`,`serverId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EntidadSaludEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombreEntidad() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombreEntidad());
        }
        if (entity.getNit() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNit());
        }
        if (entity.getContactoPrincipalNombre() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getContactoPrincipalNombre());
        }
        if (entity.getContactoPrincipalEmail() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getContactoPrincipalEmail());
        }
        if (entity.getContactoPrincipalTelefono() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getContactoPrincipalTelefono());
        }
        if (entity.getConfiguracionJson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getConfiguracionJson());
        }
        if (entity.getFechaRegistro() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFechaRegistro());
        }
        final int _tmp = entity.getActiva() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getServerId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getServerId());
        }
      }
    };
    this.__deletionAdapterOfEntidadSaludEntity = new EntityDeletionOrUpdateAdapter<EntidadSaludEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `entidades_salud` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EntidadSaludEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfEntidadSaludEntity = new EntityDeletionOrUpdateAdapter<EntidadSaludEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `entidades_salud` SET `id` = ?,`nombreEntidad` = ?,`nit` = ?,`contactoPrincipalNombre` = ?,`contactoPrincipalEmail` = ?,`contactoPrincipalTelefono` = ?,`configuracionJson` = ?,`fechaRegistro` = ?,`activa` = ?,`serverId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EntidadSaludEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombreEntidad() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombreEntidad());
        }
        if (entity.getNit() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNit());
        }
        if (entity.getContactoPrincipalNombre() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getContactoPrincipalNombre());
        }
        if (entity.getContactoPrincipalEmail() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getContactoPrincipalEmail());
        }
        if (entity.getContactoPrincipalTelefono() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getContactoPrincipalTelefono());
        }
        if (entity.getConfiguracionJson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getConfiguracionJson());
        }
        if (entity.getFechaRegistro() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFechaRegistro());
        }
        final int _tmp = entity.getActiva() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getServerId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getServerId());
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE entidades_salud SET activa = 0 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final EntidadSaludEntity entidad, final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfEntidadSaludEntity.insertAndReturnId(entidad);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertAll(final List<EntidadSaludEntity> entidades,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEntidadSaludEntity.insert(entidades);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object delete(final EntidadSaludEntity entidad, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfEntidadSaludEntity.handle(entidad);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object update(final EntidadSaludEntity entidad, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfEntidadSaludEntity.handle(entidad);
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
  public Flow<List<EntidadSaludEntity>> getAllActive() {
    final String _sql = "SELECT * FROM entidades_salud WHERE activa = 1 ORDER BY nombreEntidad ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"entidades_salud"}, new Callable<List<EntidadSaludEntity>>() {
      @Override
      @NonNull
      public List<EntidadSaludEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombreEntidad = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEntidad");
          final int _cursorIndexOfNit = CursorUtil.getColumnIndexOrThrow(_cursor, "nit");
          final int _cursorIndexOfContactoPrincipalNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalNombre");
          final int _cursorIndexOfContactoPrincipalEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalEmail");
          final int _cursorIndexOfContactoPrincipalTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalTelefono");
          final int _cursorIndexOfConfiguracionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "configuracionJson");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaRegistro");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<EntidadSaludEntity> _result = new ArrayList<EntidadSaludEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EntidadSaludEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombreEntidad;
            if (_cursor.isNull(_cursorIndexOfNombreEntidad)) {
              _tmpNombreEntidad = null;
            } else {
              _tmpNombreEntidad = _cursor.getString(_cursorIndexOfNombreEntidad);
            }
            final String _tmpNit;
            if (_cursor.isNull(_cursorIndexOfNit)) {
              _tmpNit = null;
            } else {
              _tmpNit = _cursor.getString(_cursorIndexOfNit);
            }
            final String _tmpContactoPrincipalNombre;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalNombre)) {
              _tmpContactoPrincipalNombre = null;
            } else {
              _tmpContactoPrincipalNombre = _cursor.getString(_cursorIndexOfContactoPrincipalNombre);
            }
            final String _tmpContactoPrincipalEmail;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalEmail)) {
              _tmpContactoPrincipalEmail = null;
            } else {
              _tmpContactoPrincipalEmail = _cursor.getString(_cursorIndexOfContactoPrincipalEmail);
            }
            final String _tmpContactoPrincipalTelefono;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalTelefono)) {
              _tmpContactoPrincipalTelefono = null;
            } else {
              _tmpContactoPrincipalTelefono = _cursor.getString(_cursorIndexOfContactoPrincipalTelefono);
            }
            final String _tmpConfiguracionJson;
            if (_cursor.isNull(_cursorIndexOfConfiguracionJson)) {
              _tmpConfiguracionJson = null;
            } else {
              _tmpConfiguracionJson = _cursor.getString(_cursorIndexOfConfiguracionJson);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
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
            _item = new EntidadSaludEntity(_tmpId,_tmpNombreEntidad,_tmpNit,_tmpContactoPrincipalNombre,_tmpContactoPrincipalEmail,_tmpContactoPrincipalTelefono,_tmpConfiguracionJson,_tmpFechaRegistro,_tmpActiva,_tmpServerId);
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
  public Object getById(final int id, final Continuation<? super EntidadSaludEntity> arg1) {
    final String _sql = "SELECT * FROM entidades_salud WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EntidadSaludEntity>() {
      @Override
      @Nullable
      public EntidadSaludEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombreEntidad = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEntidad");
          final int _cursorIndexOfNit = CursorUtil.getColumnIndexOrThrow(_cursor, "nit");
          final int _cursorIndexOfContactoPrincipalNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalNombre");
          final int _cursorIndexOfContactoPrincipalEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalEmail");
          final int _cursorIndexOfContactoPrincipalTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalTelefono");
          final int _cursorIndexOfConfiguracionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "configuracionJson");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaRegistro");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final EntidadSaludEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombreEntidad;
            if (_cursor.isNull(_cursorIndexOfNombreEntidad)) {
              _tmpNombreEntidad = null;
            } else {
              _tmpNombreEntidad = _cursor.getString(_cursorIndexOfNombreEntidad);
            }
            final String _tmpNit;
            if (_cursor.isNull(_cursorIndexOfNit)) {
              _tmpNit = null;
            } else {
              _tmpNit = _cursor.getString(_cursorIndexOfNit);
            }
            final String _tmpContactoPrincipalNombre;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalNombre)) {
              _tmpContactoPrincipalNombre = null;
            } else {
              _tmpContactoPrincipalNombre = _cursor.getString(_cursorIndexOfContactoPrincipalNombre);
            }
            final String _tmpContactoPrincipalEmail;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalEmail)) {
              _tmpContactoPrincipalEmail = null;
            } else {
              _tmpContactoPrincipalEmail = _cursor.getString(_cursorIndexOfContactoPrincipalEmail);
            }
            final String _tmpContactoPrincipalTelefono;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalTelefono)) {
              _tmpContactoPrincipalTelefono = null;
            } else {
              _tmpContactoPrincipalTelefono = _cursor.getString(_cursorIndexOfContactoPrincipalTelefono);
            }
            final String _tmpConfiguracionJson;
            if (_cursor.isNull(_cursorIndexOfConfiguracionJson)) {
              _tmpConfiguracionJson = null;
            } else {
              _tmpConfiguracionJson = _cursor.getString(_cursorIndexOfConfiguracionJson);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
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
            _result = new EntidadSaludEntity(_tmpId,_tmpNombreEntidad,_tmpNit,_tmpContactoPrincipalNombre,_tmpContactoPrincipalEmail,_tmpContactoPrincipalTelefono,_tmpConfiguracionJson,_tmpFechaRegistro,_tmpActiva,_tmpServerId);
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
      final Continuation<? super EntidadSaludEntity> arg1) {
    final String _sql = "SELECT * FROM entidades_salud WHERE serverId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, serverId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EntidadSaludEntity>() {
      @Override
      @Nullable
      public EntidadSaludEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombreEntidad = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEntidad");
          final int _cursorIndexOfNit = CursorUtil.getColumnIndexOrThrow(_cursor, "nit");
          final int _cursorIndexOfContactoPrincipalNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalNombre");
          final int _cursorIndexOfContactoPrincipalEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalEmail");
          final int _cursorIndexOfContactoPrincipalTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalTelefono");
          final int _cursorIndexOfConfiguracionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "configuracionJson");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaRegistro");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final EntidadSaludEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombreEntidad;
            if (_cursor.isNull(_cursorIndexOfNombreEntidad)) {
              _tmpNombreEntidad = null;
            } else {
              _tmpNombreEntidad = _cursor.getString(_cursorIndexOfNombreEntidad);
            }
            final String _tmpNit;
            if (_cursor.isNull(_cursorIndexOfNit)) {
              _tmpNit = null;
            } else {
              _tmpNit = _cursor.getString(_cursorIndexOfNit);
            }
            final String _tmpContactoPrincipalNombre;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalNombre)) {
              _tmpContactoPrincipalNombre = null;
            } else {
              _tmpContactoPrincipalNombre = _cursor.getString(_cursorIndexOfContactoPrincipalNombre);
            }
            final String _tmpContactoPrincipalEmail;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalEmail)) {
              _tmpContactoPrincipalEmail = null;
            } else {
              _tmpContactoPrincipalEmail = _cursor.getString(_cursorIndexOfContactoPrincipalEmail);
            }
            final String _tmpContactoPrincipalTelefono;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalTelefono)) {
              _tmpContactoPrincipalTelefono = null;
            } else {
              _tmpContactoPrincipalTelefono = _cursor.getString(_cursorIndexOfContactoPrincipalTelefono);
            }
            final String _tmpConfiguracionJson;
            if (_cursor.isNull(_cursorIndexOfConfiguracionJson)) {
              _tmpConfiguracionJson = null;
            } else {
              _tmpConfiguracionJson = _cursor.getString(_cursorIndexOfConfiguracionJson);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
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
            _result = new EntidadSaludEntity(_tmpId,_tmpNombreEntidad,_tmpNit,_tmpContactoPrincipalNombre,_tmpContactoPrincipalEmail,_tmpContactoPrincipalTelefono,_tmpConfiguracionJson,_tmpFechaRegistro,_tmpActiva,_tmpServerId);
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
  public Object getByNit(final String nit, final Continuation<? super EntidadSaludEntity> arg1) {
    final String _sql = "SELECT * FROM entidades_salud WHERE nit = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (nit == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nit);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EntidadSaludEntity>() {
      @Override
      @Nullable
      public EntidadSaludEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombreEntidad = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEntidad");
          final int _cursorIndexOfNit = CursorUtil.getColumnIndexOrThrow(_cursor, "nit");
          final int _cursorIndexOfContactoPrincipalNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalNombre");
          final int _cursorIndexOfContactoPrincipalEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalEmail");
          final int _cursorIndexOfContactoPrincipalTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalTelefono");
          final int _cursorIndexOfConfiguracionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "configuracionJson");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaRegistro");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final EntidadSaludEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombreEntidad;
            if (_cursor.isNull(_cursorIndexOfNombreEntidad)) {
              _tmpNombreEntidad = null;
            } else {
              _tmpNombreEntidad = _cursor.getString(_cursorIndexOfNombreEntidad);
            }
            final String _tmpNit;
            if (_cursor.isNull(_cursorIndexOfNit)) {
              _tmpNit = null;
            } else {
              _tmpNit = _cursor.getString(_cursorIndexOfNit);
            }
            final String _tmpContactoPrincipalNombre;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalNombre)) {
              _tmpContactoPrincipalNombre = null;
            } else {
              _tmpContactoPrincipalNombre = _cursor.getString(_cursorIndexOfContactoPrincipalNombre);
            }
            final String _tmpContactoPrincipalEmail;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalEmail)) {
              _tmpContactoPrincipalEmail = null;
            } else {
              _tmpContactoPrincipalEmail = _cursor.getString(_cursorIndexOfContactoPrincipalEmail);
            }
            final String _tmpContactoPrincipalTelefono;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalTelefono)) {
              _tmpContactoPrincipalTelefono = null;
            } else {
              _tmpContactoPrincipalTelefono = _cursor.getString(_cursorIndexOfContactoPrincipalTelefono);
            }
            final String _tmpConfiguracionJson;
            if (_cursor.isNull(_cursorIndexOfConfiguracionJson)) {
              _tmpConfiguracionJson = null;
            } else {
              _tmpConfiguracionJson = _cursor.getString(_cursorIndexOfConfiguracionJson);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
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
            _result = new EntidadSaludEntity(_tmpId,_tmpNombreEntidad,_tmpNit,_tmpContactoPrincipalNombre,_tmpContactoPrincipalEmail,_tmpContactoPrincipalTelefono,_tmpConfiguracionJson,_tmpFechaRegistro,_tmpActiva,_tmpServerId);
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
    final String _sql = "SELECT COUNT(*) FROM entidades_salud WHERE activa = 1";
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
    final String _sql = "SELECT COUNT(*) FROM entidades_salud WHERE serverId IS NULL AND activa = 1";
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
  public Object getUnsynced(final Continuation<? super List<EntidadSaludEntity>> arg0) {
    final String _sql = "SELECT * FROM entidades_salud WHERE serverId IS NULL AND activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EntidadSaludEntity>>() {
      @Override
      @NonNull
      public List<EntidadSaludEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombreEntidad = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEntidad");
          final int _cursorIndexOfNit = CursorUtil.getColumnIndexOrThrow(_cursor, "nit");
          final int _cursorIndexOfContactoPrincipalNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalNombre");
          final int _cursorIndexOfContactoPrincipalEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalEmail");
          final int _cursorIndexOfContactoPrincipalTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalTelefono");
          final int _cursorIndexOfConfiguracionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "configuracionJson");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaRegistro");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<EntidadSaludEntity> _result = new ArrayList<EntidadSaludEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EntidadSaludEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombreEntidad;
            if (_cursor.isNull(_cursorIndexOfNombreEntidad)) {
              _tmpNombreEntidad = null;
            } else {
              _tmpNombreEntidad = _cursor.getString(_cursorIndexOfNombreEntidad);
            }
            final String _tmpNit;
            if (_cursor.isNull(_cursorIndexOfNit)) {
              _tmpNit = null;
            } else {
              _tmpNit = _cursor.getString(_cursorIndexOfNit);
            }
            final String _tmpContactoPrincipalNombre;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalNombre)) {
              _tmpContactoPrincipalNombre = null;
            } else {
              _tmpContactoPrincipalNombre = _cursor.getString(_cursorIndexOfContactoPrincipalNombre);
            }
            final String _tmpContactoPrincipalEmail;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalEmail)) {
              _tmpContactoPrincipalEmail = null;
            } else {
              _tmpContactoPrincipalEmail = _cursor.getString(_cursorIndexOfContactoPrincipalEmail);
            }
            final String _tmpContactoPrincipalTelefono;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalTelefono)) {
              _tmpContactoPrincipalTelefono = null;
            } else {
              _tmpContactoPrincipalTelefono = _cursor.getString(_cursorIndexOfContactoPrincipalTelefono);
            }
            final String _tmpConfiguracionJson;
            if (_cursor.isNull(_cursorIndexOfConfiguracionJson)) {
              _tmpConfiguracionJson = null;
            } else {
              _tmpConfiguracionJson = _cursor.getString(_cursorIndexOfConfiguracionJson);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
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
            _item = new EntidadSaludEntity(_tmpId,_tmpNombreEntidad,_tmpNit,_tmpContactoPrincipalNombre,_tmpContactoPrincipalEmail,_tmpContactoPrincipalTelefono,_tmpConfiguracionJson,_tmpFechaRegistro,_tmpActiva,_tmpServerId);
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
  public Object getAllActiveSync(final Continuation<? super List<EntidadSaludEntity>> arg0) {
    final String _sql = "SELECT * FROM entidades_salud WHERE activa = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EntidadSaludEntity>>() {
      @Override
      @NonNull
      public List<EntidadSaludEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombreEntidad = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreEntidad");
          final int _cursorIndexOfNit = CursorUtil.getColumnIndexOrThrow(_cursor, "nit");
          final int _cursorIndexOfContactoPrincipalNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalNombre");
          final int _cursorIndexOfContactoPrincipalEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalEmail");
          final int _cursorIndexOfContactoPrincipalTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "contactoPrincipalTelefono");
          final int _cursorIndexOfConfiguracionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "configuracionJson");
          final int _cursorIndexOfFechaRegistro = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaRegistro");
          final int _cursorIndexOfActiva = CursorUtil.getColumnIndexOrThrow(_cursor, "activa");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final List<EntidadSaludEntity> _result = new ArrayList<EntidadSaludEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EntidadSaludEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombreEntidad;
            if (_cursor.isNull(_cursorIndexOfNombreEntidad)) {
              _tmpNombreEntidad = null;
            } else {
              _tmpNombreEntidad = _cursor.getString(_cursorIndexOfNombreEntidad);
            }
            final String _tmpNit;
            if (_cursor.isNull(_cursorIndexOfNit)) {
              _tmpNit = null;
            } else {
              _tmpNit = _cursor.getString(_cursorIndexOfNit);
            }
            final String _tmpContactoPrincipalNombre;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalNombre)) {
              _tmpContactoPrincipalNombre = null;
            } else {
              _tmpContactoPrincipalNombre = _cursor.getString(_cursorIndexOfContactoPrincipalNombre);
            }
            final String _tmpContactoPrincipalEmail;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalEmail)) {
              _tmpContactoPrincipalEmail = null;
            } else {
              _tmpContactoPrincipalEmail = _cursor.getString(_cursorIndexOfContactoPrincipalEmail);
            }
            final String _tmpContactoPrincipalTelefono;
            if (_cursor.isNull(_cursorIndexOfContactoPrincipalTelefono)) {
              _tmpContactoPrincipalTelefono = null;
            } else {
              _tmpContactoPrincipalTelefono = _cursor.getString(_cursorIndexOfContactoPrincipalTelefono);
            }
            final String _tmpConfiguracionJson;
            if (_cursor.isNull(_cursorIndexOfConfiguracionJson)) {
              _tmpConfiguracionJson = null;
            } else {
              _tmpConfiguracionJson = _cursor.getString(_cursorIndexOfConfiguracionJson);
            }
            final String _tmpFechaRegistro;
            if (_cursor.isNull(_cursorIndexOfFechaRegistro)) {
              _tmpFechaRegistro = null;
            } else {
              _tmpFechaRegistro = _cursor.getString(_cursorIndexOfFechaRegistro);
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
            _item = new EntidadSaludEntity(_tmpId,_tmpNombreEntidad,_tmpNit,_tmpContactoPrincipalNombre,_tmpContactoPrincipalEmail,_tmpContactoPrincipalTelefono,_tmpConfiguracionJson,_tmpFechaRegistro,_tmpActiva,_tmpServerId);
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
