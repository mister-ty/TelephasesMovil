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
import com.example.telephases.data.local.entities.TipoExamenEntity;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Double;
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
public final class TipoExamenDao_Impl implements TipoExamenDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TipoExamenEntity> __insertionAdapterOfTipoExamenEntity;

  private final EntityDeletionOrUpdateAdapter<TipoExamenEntity> __updateAdapterOfTipoExamenEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTipoExamen;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTipoExamenConfig;

  private final Converters __converters = new Converters();

  public TipoExamenDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTipoExamenEntity = new EntityInsertionAdapter<TipoExamenEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exam_types` (`id`,`nombre`,`descripcion`,`activo`,`fecha_creacion`,`unidad_default`,`valor_minimo`,`valor_maximo`,`requiere_dispositivo_ble`,`icono`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TipoExamenEntity entity) {
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
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getFechaCreacion() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFechaCreacion());
        }
        if (entity.getUnidadDefault() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUnidadDefault());
        }
        if (entity.getValorMinimo() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getValorMinimo());
        }
        if (entity.getValorMaximo() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getValorMaximo());
        }
        final int _tmp_1 = entity.getRequiereDispositivoBle() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        if (entity.getIcono() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getIcono());
        }
      }
    };
    this.__updateAdapterOfTipoExamenEntity = new EntityDeletionOrUpdateAdapter<TipoExamenEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exam_types` SET `id` = ?,`nombre` = ?,`descripcion` = ?,`activo` = ?,`fecha_creacion` = ?,`unidad_default` = ?,`valor_minimo` = ?,`valor_maximo` = ?,`requiere_dispositivo_ble` = ?,`icono` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TipoExamenEntity entity) {
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
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getFechaCreacion() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFechaCreacion());
        }
        if (entity.getUnidadDefault() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUnidadDefault());
        }
        if (entity.getValorMinimo() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getValorMinimo());
        }
        if (entity.getValorMaximo() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getValorMaximo());
        }
        final int _tmp_1 = entity.getRequiereDispositivoBle() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        if (entity.getIcono() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getIcono());
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteTipoExamen = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE exam_types SET activo = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTipoExamenConfig = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE exam_types \n"
                + "        SET unidad_default = ?,\n"
                + "            valor_minimo = ?,\n"
                + "            valor_maximo = ?,\n"
                + "            requiere_dispositivo_ble = ?\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
  }

  @Override
  public Object insertTipoExamen(final TipoExamenEntity tipoExamen,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTipoExamenEntity.insert(tipoExamen);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertTiposExamen(final List<TipoExamenEntity> tiposExamen,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTipoExamenEntity.insert(tiposExamen);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object updateTipoExamen(final TipoExamenEntity tipoExamen,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTipoExamenEntity.handle(tipoExamen);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteTipoExamen(final int tipoExamenId, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTipoExamen.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tipoExamenId);
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
          __preparedStmtOfDeleteTipoExamen.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Object updateTipoExamenConfig(final int tipoExamenId, final String unidad,
      final Double valorMinimo, final Double valorMaximo, final boolean requiereBle,
      final Continuation<? super Unit> arg5) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTipoExamenConfig.acquire();
        int _argIndex = 1;
        if (unidad == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, unidad);
        }
        _argIndex = 2;
        if (valorMinimo == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindDouble(_argIndex, valorMinimo);
        }
        _argIndex = 3;
        if (valorMaximo == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindDouble(_argIndex, valorMaximo);
        }
        _argIndex = 4;
        final int _tmp = requiereBle ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, tipoExamenId);
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
          __preparedStmtOfUpdateTipoExamenConfig.release(_stmt);
        }
      }
    }, arg5);
  }

  @Override
  public Object getTipoExamenById(final int id, final Continuation<? super TipoExamenEntity> arg1) {
    final String _sql = "SELECT * FROM exam_types WHERE id = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TipoExamenEntity>() {
      @Override
      @Nullable
      public TipoExamenEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfUnidadDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad_default");
          final int _cursorIndexOfValorMinimo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_minimo");
          final int _cursorIndexOfValorMaximo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_maximo");
          final int _cursorIndexOfRequiereDispositivoBle = CursorUtil.getColumnIndexOrThrow(_cursor, "requiere_dispositivo_ble");
          final int _cursorIndexOfIcono = CursorUtil.getColumnIndexOrThrow(_cursor, "icono");
          final TipoExamenEntity _result;
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpFechaCreacion;
            if (_cursor.isNull(_cursorIndexOfFechaCreacion)) {
              _tmpFechaCreacion = null;
            } else {
              _tmpFechaCreacion = _cursor.getString(_cursorIndexOfFechaCreacion);
            }
            final String _tmpUnidadDefault;
            if (_cursor.isNull(_cursorIndexOfUnidadDefault)) {
              _tmpUnidadDefault = null;
            } else {
              _tmpUnidadDefault = _cursor.getString(_cursorIndexOfUnidadDefault);
            }
            final Double _tmpValorMinimo;
            if (_cursor.isNull(_cursorIndexOfValorMinimo)) {
              _tmpValorMinimo = null;
            } else {
              _tmpValorMinimo = _cursor.getDouble(_cursorIndexOfValorMinimo);
            }
            final Double _tmpValorMaximo;
            if (_cursor.isNull(_cursorIndexOfValorMaximo)) {
              _tmpValorMaximo = null;
            } else {
              _tmpValorMaximo = _cursor.getDouble(_cursorIndexOfValorMaximo);
            }
            final boolean _tmpRequiereDispositivoBle;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRequiereDispositivoBle);
            _tmpRequiereDispositivoBle = _tmp_1 != 0;
            final String _tmpIcono;
            if (_cursor.isNull(_cursorIndexOfIcono)) {
              _tmpIcono = null;
            } else {
              _tmpIcono = _cursor.getString(_cursorIndexOfIcono);
            }
            _result = new TipoExamenEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpActivo,_tmpFechaCreacion,_tmpUnidadDefault,_tmpValorMinimo,_tmpValorMaximo,_tmpRequiereDispositivoBle,_tmpIcono);
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
  public Object getTipoExamenByNombre(final String nombre,
      final Continuation<? super TipoExamenEntity> arg1) {
    final String _sql = "SELECT * FROM exam_types WHERE nombre = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (nombre == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nombre);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TipoExamenEntity>() {
      @Override
      @Nullable
      public TipoExamenEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfUnidadDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad_default");
          final int _cursorIndexOfValorMinimo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_minimo");
          final int _cursorIndexOfValorMaximo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_maximo");
          final int _cursorIndexOfRequiereDispositivoBle = CursorUtil.getColumnIndexOrThrow(_cursor, "requiere_dispositivo_ble");
          final int _cursorIndexOfIcono = CursorUtil.getColumnIndexOrThrow(_cursor, "icono");
          final TipoExamenEntity _result;
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpFechaCreacion;
            if (_cursor.isNull(_cursorIndexOfFechaCreacion)) {
              _tmpFechaCreacion = null;
            } else {
              _tmpFechaCreacion = _cursor.getString(_cursorIndexOfFechaCreacion);
            }
            final String _tmpUnidadDefault;
            if (_cursor.isNull(_cursorIndexOfUnidadDefault)) {
              _tmpUnidadDefault = null;
            } else {
              _tmpUnidadDefault = _cursor.getString(_cursorIndexOfUnidadDefault);
            }
            final Double _tmpValorMinimo;
            if (_cursor.isNull(_cursorIndexOfValorMinimo)) {
              _tmpValorMinimo = null;
            } else {
              _tmpValorMinimo = _cursor.getDouble(_cursorIndexOfValorMinimo);
            }
            final Double _tmpValorMaximo;
            if (_cursor.isNull(_cursorIndexOfValorMaximo)) {
              _tmpValorMaximo = null;
            } else {
              _tmpValorMaximo = _cursor.getDouble(_cursorIndexOfValorMaximo);
            }
            final boolean _tmpRequiereDispositivoBle;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRequiereDispositivoBle);
            _tmpRequiereDispositivoBle = _tmp_1 != 0;
            final String _tmpIcono;
            if (_cursor.isNull(_cursorIndexOfIcono)) {
              _tmpIcono = null;
            } else {
              _tmpIcono = _cursor.getString(_cursorIndexOfIcono);
            }
            _result = new TipoExamenEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpActivo,_tmpFechaCreacion,_tmpUnidadDefault,_tmpValorMinimo,_tmpValorMaximo,_tmpRequiereDispositivoBle,_tmpIcono);
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
  public Object getAllTiposExamen(final Continuation<? super List<TipoExamenEntity>> arg0) {
    final String _sql = "SELECT * FROM exam_types WHERE activo = 1 ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TipoExamenEntity>>() {
      @Override
      @NonNull
      public List<TipoExamenEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfUnidadDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad_default");
          final int _cursorIndexOfValorMinimo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_minimo");
          final int _cursorIndexOfValorMaximo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_maximo");
          final int _cursorIndexOfRequiereDispositivoBle = CursorUtil.getColumnIndexOrThrow(_cursor, "requiere_dispositivo_ble");
          final int _cursorIndexOfIcono = CursorUtil.getColumnIndexOrThrow(_cursor, "icono");
          final List<TipoExamenEntity> _result = new ArrayList<TipoExamenEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TipoExamenEntity _item;
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpFechaCreacion;
            if (_cursor.isNull(_cursorIndexOfFechaCreacion)) {
              _tmpFechaCreacion = null;
            } else {
              _tmpFechaCreacion = _cursor.getString(_cursorIndexOfFechaCreacion);
            }
            final String _tmpUnidadDefault;
            if (_cursor.isNull(_cursorIndexOfUnidadDefault)) {
              _tmpUnidadDefault = null;
            } else {
              _tmpUnidadDefault = _cursor.getString(_cursorIndexOfUnidadDefault);
            }
            final Double _tmpValorMinimo;
            if (_cursor.isNull(_cursorIndexOfValorMinimo)) {
              _tmpValorMinimo = null;
            } else {
              _tmpValorMinimo = _cursor.getDouble(_cursorIndexOfValorMinimo);
            }
            final Double _tmpValorMaximo;
            if (_cursor.isNull(_cursorIndexOfValorMaximo)) {
              _tmpValorMaximo = null;
            } else {
              _tmpValorMaximo = _cursor.getDouble(_cursorIndexOfValorMaximo);
            }
            final boolean _tmpRequiereDispositivoBle;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRequiereDispositivoBle);
            _tmpRequiereDispositivoBle = _tmp_1 != 0;
            final String _tmpIcono;
            if (_cursor.isNull(_cursorIndexOfIcono)) {
              _tmpIcono = null;
            } else {
              _tmpIcono = _cursor.getString(_cursorIndexOfIcono);
            }
            _item = new TipoExamenEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpActivo,_tmpFechaCreacion,_tmpUnidadDefault,_tmpValorMinimo,_tmpValorMaximo,_tmpRequiereDispositivoBle,_tmpIcono);
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
  public Flow<List<TipoExamenEntity>> getAllTiposExamenFlow() {
    final String _sql = "SELECT * FROM exam_types WHERE activo = 1 ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exam_types"}, new Callable<List<TipoExamenEntity>>() {
      @Override
      @NonNull
      public List<TipoExamenEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfUnidadDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad_default");
          final int _cursorIndexOfValorMinimo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_minimo");
          final int _cursorIndexOfValorMaximo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_maximo");
          final int _cursorIndexOfRequiereDispositivoBle = CursorUtil.getColumnIndexOrThrow(_cursor, "requiere_dispositivo_ble");
          final int _cursorIndexOfIcono = CursorUtil.getColumnIndexOrThrow(_cursor, "icono");
          final List<TipoExamenEntity> _result = new ArrayList<TipoExamenEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TipoExamenEntity _item;
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpFechaCreacion;
            if (_cursor.isNull(_cursorIndexOfFechaCreacion)) {
              _tmpFechaCreacion = null;
            } else {
              _tmpFechaCreacion = _cursor.getString(_cursorIndexOfFechaCreacion);
            }
            final String _tmpUnidadDefault;
            if (_cursor.isNull(_cursorIndexOfUnidadDefault)) {
              _tmpUnidadDefault = null;
            } else {
              _tmpUnidadDefault = _cursor.getString(_cursorIndexOfUnidadDefault);
            }
            final Double _tmpValorMinimo;
            if (_cursor.isNull(_cursorIndexOfValorMinimo)) {
              _tmpValorMinimo = null;
            } else {
              _tmpValorMinimo = _cursor.getDouble(_cursorIndexOfValorMinimo);
            }
            final Double _tmpValorMaximo;
            if (_cursor.isNull(_cursorIndexOfValorMaximo)) {
              _tmpValorMaximo = null;
            } else {
              _tmpValorMaximo = _cursor.getDouble(_cursorIndexOfValorMaximo);
            }
            final boolean _tmpRequiereDispositivoBle;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRequiereDispositivoBle);
            _tmpRequiereDispositivoBle = _tmp_1 != 0;
            final String _tmpIcono;
            if (_cursor.isNull(_cursorIndexOfIcono)) {
              _tmpIcono = null;
            } else {
              _tmpIcono = _cursor.getString(_cursorIndexOfIcono);
            }
            _item = new TipoExamenEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpActivo,_tmpFechaCreacion,_tmpUnidadDefault,_tmpValorMinimo,_tmpValorMaximo,_tmpRequiereDispositivoBle,_tmpIcono);
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
  public Object getTiposExamenBLE(final Continuation<? super List<TipoExamenEntity>> arg0) {
    final String _sql = "SELECT * FROM exam_types WHERE requiere_dispositivo_ble = 1 AND activo = 1 ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TipoExamenEntity>>() {
      @Override
      @NonNull
      public List<TipoExamenEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfUnidadDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad_default");
          final int _cursorIndexOfValorMinimo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_minimo");
          final int _cursorIndexOfValorMaximo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_maximo");
          final int _cursorIndexOfRequiereDispositivoBle = CursorUtil.getColumnIndexOrThrow(_cursor, "requiere_dispositivo_ble");
          final int _cursorIndexOfIcono = CursorUtil.getColumnIndexOrThrow(_cursor, "icono");
          final List<TipoExamenEntity> _result = new ArrayList<TipoExamenEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TipoExamenEntity _item;
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpFechaCreacion;
            if (_cursor.isNull(_cursorIndexOfFechaCreacion)) {
              _tmpFechaCreacion = null;
            } else {
              _tmpFechaCreacion = _cursor.getString(_cursorIndexOfFechaCreacion);
            }
            final String _tmpUnidadDefault;
            if (_cursor.isNull(_cursorIndexOfUnidadDefault)) {
              _tmpUnidadDefault = null;
            } else {
              _tmpUnidadDefault = _cursor.getString(_cursorIndexOfUnidadDefault);
            }
            final Double _tmpValorMinimo;
            if (_cursor.isNull(_cursorIndexOfValorMinimo)) {
              _tmpValorMinimo = null;
            } else {
              _tmpValorMinimo = _cursor.getDouble(_cursorIndexOfValorMinimo);
            }
            final Double _tmpValorMaximo;
            if (_cursor.isNull(_cursorIndexOfValorMaximo)) {
              _tmpValorMaximo = null;
            } else {
              _tmpValorMaximo = _cursor.getDouble(_cursorIndexOfValorMaximo);
            }
            final boolean _tmpRequiereDispositivoBle;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRequiereDispositivoBle);
            _tmpRequiereDispositivoBle = _tmp_1 != 0;
            final String _tmpIcono;
            if (_cursor.isNull(_cursorIndexOfIcono)) {
              _tmpIcono = null;
            } else {
              _tmpIcono = _cursor.getString(_cursorIndexOfIcono);
            }
            _item = new TipoExamenEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpActivo,_tmpFechaCreacion,_tmpUnidadDefault,_tmpValorMinimo,_tmpValorMaximo,_tmpRequiereDispositivoBle,_tmpIcono);
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
  public Object getTiposExamenManual(final Continuation<? super List<TipoExamenEntity>> arg0) {
    final String _sql = "SELECT * FROM exam_types WHERE requiere_dispositivo_ble = 0 AND activo = 1 ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TipoExamenEntity>>() {
      @Override
      @NonNull
      public List<TipoExamenEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfUnidadDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad_default");
          final int _cursorIndexOfValorMinimo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_minimo");
          final int _cursorIndexOfValorMaximo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_maximo");
          final int _cursorIndexOfRequiereDispositivoBle = CursorUtil.getColumnIndexOrThrow(_cursor, "requiere_dispositivo_ble");
          final int _cursorIndexOfIcono = CursorUtil.getColumnIndexOrThrow(_cursor, "icono");
          final List<TipoExamenEntity> _result = new ArrayList<TipoExamenEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TipoExamenEntity _item;
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpFechaCreacion;
            if (_cursor.isNull(_cursorIndexOfFechaCreacion)) {
              _tmpFechaCreacion = null;
            } else {
              _tmpFechaCreacion = _cursor.getString(_cursorIndexOfFechaCreacion);
            }
            final String _tmpUnidadDefault;
            if (_cursor.isNull(_cursorIndexOfUnidadDefault)) {
              _tmpUnidadDefault = null;
            } else {
              _tmpUnidadDefault = _cursor.getString(_cursorIndexOfUnidadDefault);
            }
            final Double _tmpValorMinimo;
            if (_cursor.isNull(_cursorIndexOfValorMinimo)) {
              _tmpValorMinimo = null;
            } else {
              _tmpValorMinimo = _cursor.getDouble(_cursorIndexOfValorMinimo);
            }
            final Double _tmpValorMaximo;
            if (_cursor.isNull(_cursorIndexOfValorMaximo)) {
              _tmpValorMaximo = null;
            } else {
              _tmpValorMaximo = _cursor.getDouble(_cursorIndexOfValorMaximo);
            }
            final boolean _tmpRequiereDispositivoBle;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRequiereDispositivoBle);
            _tmpRequiereDispositivoBle = _tmp_1 != 0;
            final String _tmpIcono;
            if (_cursor.isNull(_cursorIndexOfIcono)) {
              _tmpIcono = null;
            } else {
              _tmpIcono = _cursor.getString(_cursorIndexOfIcono);
            }
            _item = new TipoExamenEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpActivo,_tmpFechaCreacion,_tmpUnidadDefault,_tmpValorMinimo,_tmpValorMaximo,_tmpRequiereDispositivoBle,_tmpIcono);
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
  public Object searchTiposExamen(final String searchTerm,
      final Continuation<? super List<TipoExamenEntity>> arg1) {
    final String _sql = "\n"
            + "        SELECT * FROM exam_types \n"
            + "        WHERE activo = 1 \n"
            + "        AND (nombre LIKE '%' || ? || '%' OR descripcion LIKE '%' || ? || '%')\n"
            + "        ORDER BY nombre ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
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
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TipoExamenEntity>>() {
      @Override
      @NonNull
      public List<TipoExamenEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfUnidadDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad_default");
          final int _cursorIndexOfValorMinimo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_minimo");
          final int _cursorIndexOfValorMaximo = CursorUtil.getColumnIndexOrThrow(_cursor, "valor_maximo");
          final int _cursorIndexOfRequiereDispositivoBle = CursorUtil.getColumnIndexOrThrow(_cursor, "requiere_dispositivo_ble");
          final int _cursorIndexOfIcono = CursorUtil.getColumnIndexOrThrow(_cursor, "icono");
          final List<TipoExamenEntity> _result = new ArrayList<TipoExamenEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TipoExamenEntity _item;
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpFechaCreacion;
            if (_cursor.isNull(_cursorIndexOfFechaCreacion)) {
              _tmpFechaCreacion = null;
            } else {
              _tmpFechaCreacion = _cursor.getString(_cursorIndexOfFechaCreacion);
            }
            final String _tmpUnidadDefault;
            if (_cursor.isNull(_cursorIndexOfUnidadDefault)) {
              _tmpUnidadDefault = null;
            } else {
              _tmpUnidadDefault = _cursor.getString(_cursorIndexOfUnidadDefault);
            }
            final Double _tmpValorMinimo;
            if (_cursor.isNull(_cursorIndexOfValorMinimo)) {
              _tmpValorMinimo = null;
            } else {
              _tmpValorMinimo = _cursor.getDouble(_cursorIndexOfValorMinimo);
            }
            final Double _tmpValorMaximo;
            if (_cursor.isNull(_cursorIndexOfValorMaximo)) {
              _tmpValorMaximo = null;
            } else {
              _tmpValorMaximo = _cursor.getDouble(_cursorIndexOfValorMaximo);
            }
            final boolean _tmpRequiereDispositivoBle;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRequiereDispositivoBle);
            _tmpRequiereDispositivoBle = _tmp_1 != 0;
            final String _tmpIcono;
            if (_cursor.isNull(_cursorIndexOfIcono)) {
              _tmpIcono = null;
            } else {
              _tmpIcono = _cursor.getString(_cursorIndexOfIcono);
            }
            _item = new TipoExamenEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpActivo,_tmpFechaCreacion,_tmpUnidadDefault,_tmpValorMinimo,_tmpValorMaximo,_tmpRequiereDispositivoBle,_tmpIcono);
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
  public Object existsTipoExamenWithNombre(final String nombre,
      final Continuation<? super Boolean> arg1) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM exam_types WHERE nombre = ? AND activo = 1)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (nombre == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nombre);
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
  public Object getTotalTiposExamenCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM exam_types WHERE activo = 1";
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
  public Object getTiposExamenBLECount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM exam_types WHERE requiere_dispositivo_ble = 1 AND activo = 1";
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
  public Object getAllTiposExamenNames(final Continuation<? super List<String>> arg0) {
    final String _sql = "SELECT nombre FROM exam_types WHERE activo = 1 ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
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
  public Object initializeDefaultTypes(final Continuation<? super Unit> arg0) {
    return TipoExamenDao.DefaultImpls.initializeDefaultTypes(TipoExamenDao_Impl.this, arg0);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
