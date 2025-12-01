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
import com.example.telephases.data.local.entities.UserEntity;
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
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserEntity> __insertionAdapterOfUserEntity;

  private final EntityDeletionOrUpdateAdapter<UserEntity> __deletionAdapterOfUserEntity;

  private final EntityDeletionOrUpdateAdapter<UserEntity> __updateAdapterOfUserEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUser;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUserToken;

  private final SharedSQLiteStatement __preparedStmtOfInvalidateUserToken;

  private final SharedSQLiteStatement __preparedStmtOfInvalidateAllTokens;

  private final SharedSQLiteStatement __preparedStmtOfCleanupExpiredTokens;

  private final SharedSQLiteStatement __preparedStmtOfMarkUserAsSynced;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUserServerId;

  private final SharedSQLiteStatement __preparedStmtOfCleanupOldDeletedUsers;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastLogin;

  private final Converters __converters = new Converters();

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserEntity = new EntityInsertionAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`id`,`username`,`email`,`password_hash`,`primer_nombre`,`segundo_nombre`,`primer_apellido`,`segundo_apellido`,`tipo_documento_id`,`numero_documento`,`telefono`,`direccion`,`ciudad_id`,`fecha_nacimiento`,`genero`,`rol_id`,`entidad_salud_id`,`fecha_registro`,`activo`,`token_actual`,`fecha_expiracion_token`,`ultimo_login`,`sincronizado`,`fecha_ultima_sincronizacion`,`modificado_localmente`,`fecha_modificacion_local`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getUsername() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUsername());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getPasswordHash() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPasswordHash());
        }
        if (entity.getPrimerNombre() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPrimerNombre());
        }
        if (entity.getSegundoNombre() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getSegundoNombre());
        }
        if (entity.getPrimerApellido() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPrimerApellido());
        }
        if (entity.getSegundoApellido() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSegundoApellido());
        }
        statement.bindLong(9, entity.getTipoDocumentoId());
        if (entity.getNumeroDocumento() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNumeroDocumento());
        }
        if (entity.getTelefono() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getTelefono());
        }
        if (entity.getDireccion() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getDireccion());
        }
        if (entity.getCiudadId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getCiudadId());
        }
        if (entity.getFechaNacimiento() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getFechaNacimiento());
        }
        if (entity.getGenero() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getGenero());
        }
        statement.bindLong(16, entity.getRolId());
        if (entity.getEntidadSaludId() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getEntidadSaludId());
        }
        if (entity.getFechaRegistro() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getFechaRegistro());
        }
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(19, _tmp);
        if (entity.getTokenActual() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getTokenActual());
        }
        if (entity.getFechaExpiracionToken() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getFechaExpiracionToken());
        }
        if (entity.getUltimoLogin() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getUltimoLogin());
        }
        final int _tmp_1 = entity.getSincronizado() ? 1 : 0;
        statement.bindLong(23, _tmp_1);
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
    this.__deletionAdapterOfUserEntity = new EntityDeletionOrUpdateAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `users` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfUserEntity = new EntityDeletionOrUpdateAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `users` SET `id` = ?,`username` = ?,`email` = ?,`password_hash` = ?,`primer_nombre` = ?,`segundo_nombre` = ?,`primer_apellido` = ?,`segundo_apellido` = ?,`tipo_documento_id` = ?,`numero_documento` = ?,`telefono` = ?,`direccion` = ?,`ciudad_id` = ?,`fecha_nacimiento` = ?,`genero` = ?,`rol_id` = ?,`entidad_salud_id` = ?,`fecha_registro` = ?,`activo` = ?,`token_actual` = ?,`fecha_expiracion_token` = ?,`ultimo_login` = ?,`sincronizado` = ?,`fecha_ultima_sincronizacion` = ?,`modificado_localmente` = ?,`fecha_modificacion_local` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getUsername() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUsername());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getPasswordHash() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPasswordHash());
        }
        if (entity.getPrimerNombre() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPrimerNombre());
        }
        if (entity.getSegundoNombre() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getSegundoNombre());
        }
        if (entity.getPrimerApellido() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPrimerApellido());
        }
        if (entity.getSegundoApellido() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSegundoApellido());
        }
        statement.bindLong(9, entity.getTipoDocumentoId());
        if (entity.getNumeroDocumento() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNumeroDocumento());
        }
        if (entity.getTelefono() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getTelefono());
        }
        if (entity.getDireccion() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getDireccion());
        }
        if (entity.getCiudadId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getCiudadId());
        }
        if (entity.getFechaNacimiento() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getFechaNacimiento());
        }
        if (entity.getGenero() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getGenero());
        }
        statement.bindLong(16, entity.getRolId());
        if (entity.getEntidadSaludId() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getEntidadSaludId());
        }
        if (entity.getFechaRegistro() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getFechaRegistro());
        }
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(19, _tmp);
        if (entity.getTokenActual() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getTokenActual());
        }
        if (entity.getFechaExpiracionToken() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getFechaExpiracionToken());
        }
        if (entity.getUltimoLogin() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getUltimoLogin());
        }
        final int _tmp_1 = entity.getSincronizado() ? 1 : 0;
        statement.bindLong(23, _tmp_1);
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
    this.__preparedStmtOfDeleteUser = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET activo = 0, modificado_localmente = 1, fecha_modificacion_local = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateUserToken = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE users \n"
                + "        SET token_actual = ?,\n"
                + "            fecha_expiracion_token = ?,\n"
                + "            ultimo_login = ?\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfInvalidateUserToken = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE users \n"
                + "        SET token_actual = NULL,\n"
                + "            fecha_expiracion_token = NULL\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfInvalidateAllTokens = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE users \n"
                + "        SET token_actual = NULL,\n"
                + "            fecha_expiracion_token = NULL\n"
                + "        WHERE token_actual IS NOT NULL\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfCleanupExpiredTokens = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE users \n"
                + "        SET token_actual = NULL,\n"
                + "            fecha_expiracion_token = NULL\n"
                + "        WHERE datetime(fecha_expiracion_token) <= datetime('now')\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfMarkUserAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE users \n"
                + "        SET sincronizado = 1, \n"
                + "            fecha_ultima_sincronizacion = ?,\n"
                + "            modificado_localmente = 0,\n"
                + "            fecha_modificacion_local = NULL\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateUserServerId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET id = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfCleanupOldDeletedUsers = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        DELETE FROM users \n"
                + "        WHERE activo = 0 \n"
                + "        AND datetime(fecha_modificacion_local) < datetime('now', '-30 days')\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastLogin = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET ultimo_login = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertUser(final UserEntity user, final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfUserEntity.insertAndReturnId(user);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertUsers(final List<UserEntity> users, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserEntity.insert(users);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteUserPermanently(final UserEntity user,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfUserEntity.handle(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object updateUser(final UserEntity user, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserEntity.handle(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteUser(final String userId, final String timestamp,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteUser.acquire();
        int _argIndex = 1;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
        }
        _argIndex = 2;
        if (userId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfDeleteUser.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object updateUserToken(final String userId, final String token, final String expiration,
      final String loginTime, final Continuation<? super Unit> arg4) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUserToken.acquire();
        int _argIndex = 1;
        if (token == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, token);
        }
        _argIndex = 2;
        if (expiration == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, expiration);
        }
        _argIndex = 3;
        if (loginTime == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, loginTime);
        }
        _argIndex = 4;
        if (userId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfUpdateUserToken.release(_stmt);
        }
      }
    }, arg4);
  }

  @Override
  public Object invalidateUserToken(final String userId, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfInvalidateUserToken.acquire();
        int _argIndex = 1;
        if (userId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfInvalidateUserToken.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Object invalidateAllTokens(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfInvalidateAllTokens.acquire();
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
          __preparedStmtOfInvalidateAllTokens.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object cleanupExpiredTokens(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCleanupExpiredTokens.acquire();
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
          __preparedStmtOfCleanupExpiredTokens.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object markUserAsSynced(final String userId, final String timestamp,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkUserAsSynced.acquire();
        int _argIndex = 1;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
        }
        _argIndex = 2;
        if (userId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfMarkUserAsSynced.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object updateUserServerId(final String oldId, final String newId,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUserServerId.acquire();
        int _argIndex = 1;
        if (newId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, newId);
        }
        _argIndex = 2;
        if (oldId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, oldId);
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
          __preparedStmtOfUpdateUserServerId.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object cleanupOldDeletedUsers(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCleanupOldDeletedUsers.acquire();
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
          __preparedStmtOfCleanupOldDeletedUsers.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object updateLastLogin(final String userId, final String timestamp,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastLogin.acquire();
        int _argIndex = 1;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
        }
        _argIndex = 2;
        if (userId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfUpdateLastLogin.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object getUserByUsername(final String username,
      final Continuation<? super UserEntity> arg1) {
    final String _sql = "SELECT * FROM users WHERE username = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
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
  public Object getUserByEmail(final String email, final Continuation<? super UserEntity> arg1) {
    final String _sql = "SELECT * FROM users WHERE email = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
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
  public Object getUserByCredential(final String credential,
      final Continuation<? super UserEntity> arg1) {
    final String _sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (credential == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, credential);
    }
    _argIndex = 2;
    if (credential == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, credential);
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
  public Object getAllUsersDebug(final Continuation<? super List<UserEntity>> arg0) {
    final String _sql = "SELECT * FROM users ORDER BY fecha_registro DESC";
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
  public Object getUserById(final String userId, final Continuation<? super UserEntity> arg1) {
    final String _sql = "SELECT * FROM users WHERE id = ? AND activo = 1";
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
  public Object getCurrentLoggedUser(final Continuation<? super UserEntity> arg0) {
    final String _sql = "\n"
            + "        SELECT * FROM users \n"
            + "        WHERE token_actual IS NOT NULL \n"
            + "        AND datetime(fecha_expiracion_token) > datetime('now') \n"
            + "        AND activo = 1\n"
            + "        ORDER BY ultimo_login DESC\n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
    }, arg0);
  }

  @Override
  public Object existsUserWithUsername(final String username,
      final Continuation<? super Boolean> arg1) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM users WHERE username = ? AND activo = 1)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
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
  public Object existsUserWithEmail(final String email, final Continuation<? super Boolean> arg1) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ? AND activo = 1)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
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
  public Object existsUserWithDocument(final String numeroDocumento,
      final Continuation<? super Boolean> arg1) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM users WHERE numero_documento = ? AND activo = 1)";
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
  public Object getUsersWithValidTokens(final Continuation<? super List<UserEntity>> arg0) {
    final String _sql = "\n"
            + "        SELECT * FROM users \n"
            + "        WHERE token_actual IS NOT NULL \n"
            + "        AND datetime(fecha_expiracion_token) > datetime('now') \n"
            + "        AND activo = 1\n"
            + "    ";
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
  public Object getAllUsers(final Continuation<? super List<UserEntity>> arg0) {
    final String _sql = "SELECT * FROM users WHERE activo = 1 ORDER BY fecha_registro DESC";
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
  public Flow<List<UserEntity>> getAllUsersFlow() {
    final String _sql = "SELECT * FROM users WHERE activo = 1 ORDER BY fecha_registro DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users"}, new Callable<List<UserEntity>>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object searchUsersByName(final String searchTerm,
      final Continuation<? super List<UserEntity>> arg1) {
    final String _sql = "\n"
            + "        SELECT * FROM users \n"
            + "        WHERE activo = 1 \n"
            + "        AND (primer_nombre LIKE '%' || ? || '%' \n"
            + "             OR primer_apellido LIKE '%' || ? || '%'\n"
            + "             OR segundo_nombre LIKE '%' || ? || '%'\n"
            + "             OR segundo_apellido LIKE '%' || ? || '%'\n"
            + "             OR username LIKE '%' || ? || '%')\n"
            + "        ORDER BY primer_nombre ASC, primer_apellido ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
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
    if (searchTerm == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTerm);
    }
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
    }, arg1);
  }

  @Override
  public Object getUsersByRole(final int rolId, final Continuation<? super List<UserEntity>> arg1) {
    final String _sql = "SELECT * FROM users WHERE rol_id = ? AND activo = 1 ORDER BY fecha_registro DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rolId);
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
    }, arg1);
  }

  @Override
  public Object getAdminUsers(final Continuation<? super List<UserEntity>> arg0) {
    final String _sql = "SELECT * FROM users WHERE rol_id = 1 AND activo = 1 ORDER BY fecha_registro DESC";
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
  public Object getRecentUsers(final int limit, final Continuation<? super List<UserEntity>> arg1) {
    final String _sql = "SELECT * FROM users WHERE activo = 1 ORDER BY fecha_registro DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
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
    }, arg1);
  }

  @Override
  public Object getUnsyncedUsers(final Continuation<? super List<UserEntity>> arg0) {
    final String _sql = "SELECT * FROM users WHERE sincronizado = 0 AND activo = 1";
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
  public Object getLocallyModifiedUsers(final Continuation<? super List<UserEntity>> arg0) {
    final String _sql = "SELECT * FROM users WHERE modificado_localmente = 1 AND activo = 1";
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
  public Object getTotalUsersCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM users WHERE activo = 1";
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
  public Object getUsersCountByRole(final int rolId, final Continuation<? super Integer> arg1) {
    final String _sql = "SELECT COUNT(*) FROM users WHERE rol_id = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rolId);
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
  public Object getAdminUsersCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM users WHERE rol_id = 1 AND activo = 1";
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
  public Object getUnsyncedUsersCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM users WHERE sincronizado = 0 AND activo = 1";
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
  public Object getTodayUsersCount(final Continuation<? super Integer> arg0) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM users \n"
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
  public Object getActiveSessionsCount(final Continuation<? super Integer> arg0) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM users \n"
            + "        WHERE token_actual IS NOT NULL \n"
            + "        AND datetime(fecha_expiracion_token) > datetime('now') \n"
            + "        AND activo = 1\n"
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
  public Object getLastRegisteredUser(final Continuation<? super UserEntity> arg0) {
    final String _sql = "SELECT * FROM users WHERE activo = 1 ORDER BY fecha_registro DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
    }, arg0);
  }

  @Override
  public Object searchUsersWithFilters(final String searchTerm, final Integer rolId,
      final String genero, final int limit, final int offset,
      final Continuation<? super List<UserEntity>> arg5) {
    final String _sql = "\n"
            + "        SELECT * FROM users \n"
            + "        WHERE activo = 1\n"
            + "        AND (? IS NULL OR \n"
            + "             primer_nombre LIKE '%' || ? || '%' OR \n"
            + "             primer_apellido LIKE '%' || ? || '%' OR\n"
            + "             username LIKE '%' || ? || '%' OR\n"
            + "             email LIKE '%' || ? || '%')\n"
            + "        AND (? IS NULL OR rol_id = ?)\n"
            + "        AND (? IS NULL OR genero = ?)\n"
            + "        ORDER BY fecha_registro DESC\n"
            + "        LIMIT ? OFFSET ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 11);
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
    if (searchTerm == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTerm);
    }
    _argIndex = 6;
    if (rolId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, rolId);
    }
    _argIndex = 7;
    if (rolId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, rolId);
    }
    _argIndex = 8;
    if (genero == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, genero);
    }
    _argIndex = 9;
    if (genero == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, genero);
    }
    _argIndex = 10;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 11;
    _statement.bindLong(_argIndex, offset);
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
    }, arg5);
  }

  @Override
  public Object getUserActivityStats(final Continuation<? super UserDao.UserActivityStats> arg0) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            COUNT(*) as total_users,\n"
            + "            SUM(CASE WHEN token_actual IS NOT NULL AND datetime(fecha_expiracion_token) > datetime('now') THEN 1 ELSE 0 END) as active_sessions,\n"
            + "            SUM(CASE WHEN date(ultimo_login) = date('now', 'localtime') THEN 1 ELSE 0 END) as logged_today,\n"
            + "            SUM(CASE WHEN sincronizado = 1 THEN 1 ELSE 0 END) as synchronized_users\n"
            + "        FROM users \n"
            + "        WHERE activo = 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserDao.UserActivityStats>() {
      @Override
      @NonNull
      public UserDao.UserActivityStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTotalUsers = 0;
          final int _cursorIndexOfActiveSessions = 1;
          final int _cursorIndexOfLoggedToday = 2;
          final int _cursorIndexOfSynchronizedUsers = 3;
          final UserDao.UserActivityStats _result;
          if (_cursor.moveToFirst()) {
            final int _tmpTotal_users;
            _tmpTotal_users = _cursor.getInt(_cursorIndexOfTotalUsers);
            final int _tmpActive_sessions;
            _tmpActive_sessions = _cursor.getInt(_cursorIndexOfActiveSessions);
            final int _tmpLogged_today;
            _tmpLogged_today = _cursor.getInt(_cursorIndexOfLoggedToday);
            final int _tmpSynchronized_users;
            _tmpSynchronized_users = _cursor.getInt(_cursorIndexOfSynchronizedUsers);
            _result = new UserDao.UserActivityStats(_tmpTotal_users,_tmpActive_sessions,_tmpLogged_today,_tmpSynchronized_users);
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
  public Object getSyncStats(final Continuation<? super UserDao.SyncStats> arg0) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            COUNT(*) as total,\n"
            + "            SUM(CASE WHEN sincronizado = 1 THEN 1 ELSE 0 END) as sincronizados,\n"
            + "            SUM(CASE WHEN modificado_localmente = 1 THEN 1 ELSE 0 END) as modificados_localmente\n"
            + "        FROM users \n"
            + "        WHERE activo = 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserDao.SyncStats>() {
      @Override
      @NonNull
      public UserDao.SyncStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTotal = 0;
          final int _cursorIndexOfSincronizados = 1;
          final int _cursorIndexOfModificadosLocalmente = 2;
          final UserDao.SyncStats _result;
          if (_cursor.moveToFirst()) {
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            final int _tmpSincronizados;
            _tmpSincronizados = _cursor.getInt(_cursorIndexOfSincronizados);
            final int _tmpModificados_localmente;
            _tmpModificados_localmente = _cursor.getInt(_cursorIndexOfModificadosLocalmente);
            _result = new UserDao.SyncStats(_tmpTotal,_tmpSincronizados,_tmpModificados_localmente);
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
  public Object getDuplicateUsersByEmail(final Continuation<? super List<UserEntity>> arg0) {
    final String _sql = "\n"
            + "        SELECT * FROM users u1\n"
            + "        WHERE EXISTS (\n"
            + "            SELECT 1 FROM users u2 \n"
            + "            WHERE u2.email = u1.email \n"
            + "            AND u2.id != u1.id \n"
            + "            AND u2.activo = 1\n"
            + "        ) AND u1.activo = 1\n"
            + "        ORDER BY u1.fecha_registro DESC\n"
            + "    ";
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
  public Object getDuplicateUsersByUsername(final Continuation<? super List<UserEntity>> arg0) {
    final String _sql = "\n"
            + "        SELECT * FROM users u1\n"
            + "        WHERE EXISTS (\n"
            + "            SELECT 1 FROM users u2 \n"
            + "            WHERE u2.username = u1.username \n"
            + "            AND u2.id != u1.id \n"
            + "            AND u2.activo = 1\n"
            + "        ) AND u1.activo = 1\n"
            + "        ORDER BY u1.fecha_registro DESC\n"
            + "    ";
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
  public Object markUsersAsSynced(final List<String> userIds, final String timestamp,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("\n");
        _stringBuilder.append("        UPDATE users ");
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
        final int _inputSize = userIds.size();
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
        for (String _item : userIds) {
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
