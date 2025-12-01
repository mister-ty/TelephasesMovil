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
import com.example.telephases.data.local.entities.ExamEntity;
import java.lang.Class;
import java.lang.Double;
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
public final class ExamDao_Impl implements ExamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExamEntity> __insertionAdapterOfExamEntity;

  private final EntityDeletionOrUpdateAdapter<ExamEntity> __deletionAdapterOfExamEntity;

  private final EntityDeletionOrUpdateAdapter<ExamEntity> __updateAdapterOfExamEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExam;

  private final SharedSQLiteStatement __preparedStmtOfMarkExamAsSynced;

  private final SharedSQLiteStatement __preparedStmtOfUpdateExamHealthStatus;

  private final SharedSQLiteStatement __preparedStmtOfCleanupOldDeletedExams;

  private final SharedSQLiteStatement __preparedStmtOfCleanupOrphanedExams;

  public ExamDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExamEntity = new EntityInsertionAdapter<ExamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exams` (`id`,`patient_id`,`tipo_examen_nombre`,`tipo_examen_id`,`titulo`,`valor`,`unidad`,`observaciones`,`datos_adicionales`,`fecha_creacion`,`fecha_modificacion`,`activo`,`estado_codigo`,`estado_nombre`,`estado_emoji`,`estado_color`,`estado_descripcion`,`estado_nivel_urgencia`,`sincronizado`,`fecha_ultima_sincronizacion`,`modificado_localmente`,`fecha_modificacion_local`,`server_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getPatientId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPatientId());
        }
        if (entity.getTipoExamenNombre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTipoExamenNombre());
        }
        if (entity.getTipoExamenId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getTipoExamenId());
        }
        if (entity.getTitulo() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTitulo());
        }
        if (entity.getValor() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getValor());
        }
        if (entity.getUnidad() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getUnidad());
        }
        if (entity.getObservaciones() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getObservaciones());
        }
        if (entity.getDatosAdicionales() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDatosAdicionales());
        }
        if (entity.getFechaCreacion() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getFechaCreacion());
        }
        if (entity.getFechaModificacion() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getFechaModificacion());
        }
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getEstadoCodigo() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getEstadoCodigo());
        }
        if (entity.getEstadoNombre() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getEstadoNombre());
        }
        if (entity.getEstadoEmoji() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getEstadoEmoji());
        }
        if (entity.getEstadoColor() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getEstadoColor());
        }
        if (entity.getEstadoDescripcion() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getEstadoDescripcion());
        }
        if (entity.getEstadoNivelUrgencia() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getEstadoNivelUrgencia());
        }
        final int _tmp_1 = entity.getSincronizado() ? 1 : 0;
        statement.bindLong(19, _tmp_1);
        if (entity.getFechaUltimaSincronizacion() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getFechaUltimaSincronizacion());
        }
        final int _tmp_2 = entity.getModificadoLocalmente() ? 1 : 0;
        statement.bindLong(21, _tmp_2);
        if (entity.getFechaModificacionLocal() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getFechaModificacionLocal());
        }
        if (entity.getServerId() == null) {
          statement.bindNull(23);
        } else {
          statement.bindLong(23, entity.getServerId());
        }
      }
    };
    this.__deletionAdapterOfExamEntity = new EntityDeletionOrUpdateAdapter<ExamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exams` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfExamEntity = new EntityDeletionOrUpdateAdapter<ExamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exams` SET `id` = ?,`patient_id` = ?,`tipo_examen_nombre` = ?,`tipo_examen_id` = ?,`titulo` = ?,`valor` = ?,`unidad` = ?,`observaciones` = ?,`datos_adicionales` = ?,`fecha_creacion` = ?,`fecha_modificacion` = ?,`activo` = ?,`estado_codigo` = ?,`estado_nombre` = ?,`estado_emoji` = ?,`estado_color` = ?,`estado_descripcion` = ?,`estado_nivel_urgencia` = ?,`sincronizado` = ?,`fecha_ultima_sincronizacion` = ?,`modificado_localmente` = ?,`fecha_modificacion_local` = ?,`server_id` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getPatientId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPatientId());
        }
        if (entity.getTipoExamenNombre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTipoExamenNombre());
        }
        if (entity.getTipoExamenId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getTipoExamenId());
        }
        if (entity.getTitulo() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTitulo());
        }
        if (entity.getValor() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getValor());
        }
        if (entity.getUnidad() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getUnidad());
        }
        if (entity.getObservaciones() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getObservaciones());
        }
        if (entity.getDatosAdicionales() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDatosAdicionales());
        }
        if (entity.getFechaCreacion() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getFechaCreacion());
        }
        if (entity.getFechaModificacion() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getFechaModificacion());
        }
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getEstadoCodigo() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getEstadoCodigo());
        }
        if (entity.getEstadoNombre() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getEstadoNombre());
        }
        if (entity.getEstadoEmoji() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getEstadoEmoji());
        }
        if (entity.getEstadoColor() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getEstadoColor());
        }
        if (entity.getEstadoDescripcion() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getEstadoDescripcion());
        }
        if (entity.getEstadoNivelUrgencia() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getEstadoNivelUrgencia());
        }
        final int _tmp_1 = entity.getSincronizado() ? 1 : 0;
        statement.bindLong(19, _tmp_1);
        if (entity.getFechaUltimaSincronizacion() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getFechaUltimaSincronizacion());
        }
        final int _tmp_2 = entity.getModificadoLocalmente() ? 1 : 0;
        statement.bindLong(21, _tmp_2);
        if (entity.getFechaModificacionLocal() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getFechaModificacionLocal());
        }
        if (entity.getServerId() == null) {
          statement.bindNull(23);
        } else {
          statement.bindLong(23, entity.getServerId());
        }
        if (entity.getId() == null) {
          statement.bindNull(24);
        } else {
          statement.bindString(24, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteExam = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE exams SET activo = 0, modificado_localmente = 1, fecha_modificacion_local = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkExamAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE exams \n"
                + "        SET sincronizado = 1, \n"
                + "            fecha_ultima_sincronizacion = ?,\n"
                + "            modificado_localmente = 0,\n"
                + "            fecha_modificacion_local = NULL,\n"
                + "            server_id = ?\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateExamHealthStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE exams \n"
                + "        SET estado_codigo = ?,\n"
                + "            estado_nombre = ?,\n"
                + "            estado_emoji = ?,\n"
                + "            estado_color = ?,\n"
                + "            estado_descripcion = ?,\n"
                + "            estado_nivel_urgencia = ?\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfCleanupOldDeletedExams = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        DELETE FROM exams \n"
                + "        WHERE activo = 0 \n"
                + "        AND datetime(fecha_modificacion_local) < datetime('now', '-30 days')\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfCleanupOrphanedExams = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE exams \n"
                + "        SET activo = 0, modificado_localmente = 1, fecha_modificacion_local = ?\n"
                + "        WHERE id IN (\n"
                + "            SELECT e.id FROM exams e\n"
                + "            LEFT JOIN patients p ON e.patient_id = p.id\n"
                + "            WHERE e.activo = 1 \n"
                + "            AND p.id IS NULL \n"
                + "            AND e.sincronizado = 1\n"
                + "        )\n"
                + "    ";
        return _query;
      }
    };
  }

  @Override
  public Object insertExam(final ExamEntity exam, final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExamEntity.insertAndReturnId(exam);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertExams(final List<ExamEntity> exams, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExamEntity.insert(exams);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteExamPermanently(final ExamEntity exam,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExamEntity.handle(exam);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object updateExam(final ExamEntity exam, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfExamEntity.handle(exam);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteExam(final String examId, final String timestamp,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExam.acquire();
        int _argIndex = 1;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
        }
        _argIndex = 2;
        if (examId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, examId);
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
          __preparedStmtOfDeleteExam.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Object markExamAsSynced(final String examId, final long serverId, final String timestamp,
      final Continuation<? super Unit> arg3) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkExamAsSynced.acquire();
        int _argIndex = 1;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, serverId);
        _argIndex = 3;
        if (examId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, examId);
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
          __preparedStmtOfMarkExamAsSynced.release(_stmt);
        }
      }
    }, arg3);
  }

  @Override
  public Object updateExamHealthStatus(final String examId, final String estadoCodigo,
      final String estadoNombre, final String estadoEmoji, final String estadoColor,
      final String estadoDescripcion, final Integer estadoNivelUrgencia,
      final Continuation<? super Unit> arg7) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateExamHealthStatus.acquire();
        int _argIndex = 1;
        if (estadoCodigo == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, estadoCodigo);
        }
        _argIndex = 2;
        if (estadoNombre == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, estadoNombre);
        }
        _argIndex = 3;
        if (estadoEmoji == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, estadoEmoji);
        }
        _argIndex = 4;
        if (estadoColor == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, estadoColor);
        }
        _argIndex = 5;
        if (estadoDescripcion == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, estadoDescripcion);
        }
        _argIndex = 6;
        if (estadoNivelUrgencia == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, estadoNivelUrgencia);
        }
        _argIndex = 7;
        if (examId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, examId);
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
          __preparedStmtOfUpdateExamHealthStatus.release(_stmt);
        }
      }
    }, arg7);
  }

  @Override
  public Object cleanupOldDeletedExams(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCleanupOldDeletedExams.acquire();
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
          __preparedStmtOfCleanupOldDeletedExams.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object cleanupOrphanedExams(final String timestamp,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCleanupOrphanedExams.acquire();
        int _argIndex = 1;
        if (timestamp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, timestamp);
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
          __preparedStmtOfCleanupOrphanedExams.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Object getExamById(final String examId, final Continuation<? super ExamEntity> arg1) {
    final String _sql = "SELECT * FROM exams WHERE id = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (examId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, examId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExamEntity>() {
      @Override
      @Nullable
      public ExamEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final ExamEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _result = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object getExamsByPatient(final String patientId,
      final Continuation<? super List<ExamEntity>> arg1) {
    final String _sql = "SELECT * FROM exams WHERE patient_id = ? AND activo = 1 ORDER BY fecha_creacion DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Flow<List<ExamEntity>> getExamsByPatientFlow(final String patientId) {
    final String _sql = "SELECT * FROM exams WHERE patient_id = ? AND activo = 1 ORDER BY fecha_creacion DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exams"}, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object getExamsByType(final String tipoExamen,
      final Continuation<? super List<ExamEntity>> arg1) {
    final String _sql = "SELECT * FROM exams WHERE tipo_examen_nombre = ? AND activo = 1 ORDER BY fecha_creacion DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tipoExamen == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tipoExamen);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object getExamByTitleAndValue(final String patientId, final String titulo,
      final String valor, final Continuation<? super ExamEntity> arg3) {
    final String _sql = "SELECT * FROM exams WHERE patient_id = ? AND titulo = ? AND valor = ? AND activo = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    _argIndex = 2;
    if (titulo == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, titulo);
    }
    _argIndex = 3;
    if (valor == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, valor);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExamEntity>() {
      @Override
      @Nullable
      public ExamEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final ExamEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _result = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg3);
  }

  @Override
  public Object getAllExamsByPatientDebug(final String patientId,
      final Continuation<? super List<ExamEntity>> arg1) {
    final String _sql = "SELECT * FROM exams WHERE patient_id = ? ORDER BY fecha_creacion DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object getLatestExamsByTypeForPatient(final String patientId,
      final Continuation<? super List<ExamEntity>> arg1) {
    final String _sql = "\n"
            + "        SELECT * FROM exams e1\n"
            + "        WHERE e1.patient_id = ? \n"
            + "        AND e1.activo = 1\n"
            + "        AND e1.fecha_creacion = (\n"
            + "            SELECT MAX(e2.fecha_creacion) \n"
            + "            FROM exams e2 \n"
            + "            WHERE e2.patient_id = ? \n"
            + "            AND e2.tipo_examen_nombre = e1.tipo_examen_nombre \n"
            + "            AND e2.activo = 1\n"
            + "        )\n"
            + "        ORDER BY e1.fecha_creacion DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    _argIndex = 2;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object getLatestExamsByType(final Continuation<? super List<ExamEntity>> arg0) {
    final String _sql = "\n"
            + "        SELECT * FROM exams e1\n"
            + "        WHERE e1.activo = 1\n"
            + "        AND e1.fecha_creacion = (\n"
            + "            SELECT MAX(e2.fecha_creacion) \n"
            + "            FROM exams e2 \n"
            + "            WHERE e2.tipo_examen_nombre = e1.tipo_examen_nombre \n"
            + "            AND e2.activo = 1\n"
            + "        )\n"
            + "        ORDER BY e1.fecha_creacion DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object getExamsByDateRange(final String startDate, final String endDate,
      final Continuation<? super List<ExamEntity>> arg2) {
    final String _sql = "\n"
            + "        SELECT * FROM exams \n"
            + "        WHERE activo = 1 \n"
            + "        AND datetime(fecha_creacion) BETWEEN datetime(?) AND datetime(?)\n"
            + "        ORDER BY fecha_creacion DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (startDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startDate);
    }
    _argIndex = 2;
    if (endDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, endDate);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg2);
  }

  @Override
  public Object getPatientExamsByDateRange(final String patientId, final String startDate,
      final String endDate, final Continuation<? super List<ExamEntity>> arg3) {
    final String _sql = "\n"
            + "        SELECT * FROM exams \n"
            + "        WHERE patient_id = ? \n"
            + "        AND activo = 1 \n"
            + "        AND datetime(fecha_creacion) BETWEEN datetime(?) AND datetime(?)\n"
            + "        ORDER BY fecha_creacion DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    _argIndex = 2;
    if (startDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startDate);
    }
    _argIndex = 3;
    if (endDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, endDate);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg3);
  }

  @Override
  public Object getUnsyncedExams(final Continuation<? super List<ExamEntity>> arg0) {
    final String _sql = "SELECT * FROM exams WHERE sincronizado = 0 AND activo = 1 ORDER BY fecha_creacion ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object getLocallyModifiedExams(final Continuation<? super List<ExamEntity>> arg0) {
    final String _sql = "SELECT * FROM exams WHERE modificado_localmente = 1 AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object getTotalExamsCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM exams WHERE activo = 1";
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
  public Object getPatientExamsCount(final String patientId,
      final Continuation<? super Integer> arg1) {
    final String _sql = "SELECT COUNT(*) FROM exams WHERE patient_id = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
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
  public Object getExamsCountByType(final String tipoExamen,
      final Continuation<? super Integer> arg1) {
    final String _sql = "SELECT COUNT(*) FROM exams WHERE tipo_examen_nombre = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tipoExamen == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tipoExamen);
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
  public Object getUnsyncedExamsCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM exams WHERE sincronizado = 0 AND activo = 1";
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
  public Object getTodayExamsCount(final Continuation<? super Integer> arg0) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM exams \n"
            + "        WHERE activo = 1 \n"
            + "        AND date(fecha_creacion) = date('now', 'localtime')\n"
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
  public Object getPatientExamsCountInRange(final String patientId, final String startDate,
      final String endDate, final Continuation<? super Integer> arg3) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM exams \n"
            + "        WHERE patient_id = ? \n"
            + "        AND activo = 1 \n"
            + "        AND datetime(fecha_creacion) BETWEEN datetime(?) AND datetime(?)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    _argIndex = 2;
    if (startDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startDate);
    }
    _argIndex = 3;
    if (endDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, endDate);
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
    }, arg3);
  }

  @Override
  public Object getExamsWithFilters(final String patientId, final String tipoExamen,
      final String startDate, final String endDate, final int limit, final int offset,
      final Continuation<? super List<ExamEntity>> arg6) {
    final String _sql = "\n"
            + "        SELECT * FROM exams \n"
            + "        WHERE activo = 1\n"
            + "        AND (? IS NULL OR patient_id = ?)\n"
            + "        AND (? IS NULL OR tipo_examen_nombre = ?)\n"
            + "        AND (? IS NULL OR datetime(fecha_creacion) >= datetime(?))\n"
            + "        AND (? IS NULL OR datetime(fecha_creacion) <= datetime(?))\n"
            + "        ORDER BY fecha_creacion DESC\n"
            + "        LIMIT ? OFFSET ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 10);
    int _argIndex = 1;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    _argIndex = 2;
    if (patientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, patientId);
    }
    _argIndex = 3;
    if (tipoExamen == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tipoExamen);
    }
    _argIndex = 4;
    if (tipoExamen == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tipoExamen);
    }
    _argIndex = 5;
    if (startDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startDate);
    }
    _argIndex = 6;
    if (startDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startDate);
    }
    _argIndex = 7;
    if (endDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, endDate);
    }
    _argIndex = 8;
    if (endDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, endDate);
    }
    _argIndex = 9;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 10;
    _statement.bindLong(_argIndex, offset);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg6);
  }

  @Override
  public Object getExamStatsByType(final Continuation<? super List<ExamDao.ExamTypeStats>> arg0) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            tipo_examen_nombre,\n"
            + "            COUNT(*) as total,\n"
            + "            AVG(CAST(valor AS REAL)) as promedio,\n"
            + "            MIN(CAST(valor AS REAL)) as minimo,\n"
            + "            MAX(CAST(valor AS REAL)) as maximo\n"
            + "        FROM exams \n"
            + "        WHERE activo = 1 \n"
            + "        AND valor NOT LIKE '%/%' -- Excluir valores como presión arterial\n"
            + "        GROUP BY tipo_examen_nombre\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamDao.ExamTypeStats>>() {
      @Override
      @NonNull
      public List<ExamDao.ExamTypeStats> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTipoExamenNombre = 0;
          final int _cursorIndexOfTotal = 1;
          final int _cursorIndexOfPromedio = 2;
          final int _cursorIndexOfMinimo = 3;
          final int _cursorIndexOfMaximo = 4;
          final List<ExamDao.ExamTypeStats> _result = new ArrayList<ExamDao.ExamTypeStats>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamDao.ExamTypeStats _item;
            final String _tmpTipo_examen_nombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipo_examen_nombre = null;
            } else {
              _tmpTipo_examen_nombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            final Double _tmpPromedio;
            if (_cursor.isNull(_cursorIndexOfPromedio)) {
              _tmpPromedio = null;
            } else {
              _tmpPromedio = _cursor.getDouble(_cursorIndexOfPromedio);
            }
            final Double _tmpMinimo;
            if (_cursor.isNull(_cursorIndexOfMinimo)) {
              _tmpMinimo = null;
            } else {
              _tmpMinimo = _cursor.getDouble(_cursorIndexOfMinimo);
            }
            final Double _tmpMaximo;
            if (_cursor.isNull(_cursorIndexOfMaximo)) {
              _tmpMaximo = null;
            } else {
              _tmpMaximo = _cursor.getDouble(_cursorIndexOfMaximo);
            }
            _item = new ExamDao.ExamTypeStats(_tmpTipo_examen_nombre,_tmpTotal,_tmpPromedio,_tmpMinimo,_tmpMaximo);
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
  public Object getRecentExams(final int limit, final Continuation<? super List<ExamEntity>> arg1) {
    final String _sql = "SELECT * FROM exams WHERE activo = 1 ORDER BY fecha_creacion DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object searchExamsByValue(final String searchValue,
      final Continuation<? super List<ExamEntity>> arg1) {
    final String _sql = "\n"
            + "        SELECT * FROM exams \n"
            + "        WHERE activo = 1 \n"
            + "        AND valor LIKE '%' || ? || '%'\n"
            + "        ORDER BY fecha_creacion DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (searchValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchValue);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object getCriticalExams(final int minUrgencyLevel,
      final Continuation<? super List<ExamEntity>> arg1) {
    final String _sql = "\n"
            + "        SELECT * FROM exams \n"
            + "        WHERE activo = 1 \n"
            + "        AND estado_nivel_urgencia IS NOT NULL \n"
            + "        AND estado_nivel_urgencia >= ?\n"
            + "        ORDER BY estado_nivel_urgencia DESC, fecha_creacion DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, minUrgencyLevel);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object getSyncStats(final Continuation<? super ExamDao.SyncStats> arg0) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            COUNT(*) as total,\n"
            + "            SUM(CASE WHEN sincronizado = 1 THEN 1 ELSE 0 END) as sincronizados,\n"
            + "            SUM(CASE WHEN modificado_localmente = 1 THEN 1 ELSE 0 END) as modificados_localmente\n"
            + "        FROM exams \n"
            + "        WHERE activo = 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExamDao.SyncStats>() {
      @Override
      @NonNull
      public ExamDao.SyncStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTotal = 0;
          final int _cursorIndexOfSincronizados = 1;
          final int _cursorIndexOfModificadosLocalmente = 2;
          final ExamDao.SyncStats _result;
          if (_cursor.moveToFirst()) {
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            final int _tmpSincronizados;
            _tmpSincronizados = _cursor.getInt(_cursorIndexOfSincronizados);
            final int _tmpModificados_localmente;
            _tmpModificados_localmente = _cursor.getInt(_cursorIndexOfModificadosLocalmente);
            _result = new ExamDao.SyncStats(_tmpTotal,_tmpSincronizados,_tmpModificados_localmente);
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
  public Object getOrphanedExams(final Continuation<? super List<ExamEntity>> arg0) {
    final String _sql = "\n"
            + "        SELECT e.* FROM exams e\n"
            + "        LEFT JOIN patients p ON e.patient_id = p.id\n"
            + "        WHERE e.activo = 1 AND (p.id IS NULL OR p.activo = 0)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfTipoExamenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_nombre");
          final int _cursorIndexOfTipoExamenId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_examen_id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfUnidad = CursorUtil.getColumnIndexOrThrow(_cursor, "unidad");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfDatosAdicionales = CursorUtil.getColumnIndexOrThrow(_cursor, "datos_adicionales");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_creacion");
          final int _cursorIndexOfFechaModificacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final int _cursorIndexOfEstadoCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_codigo");
          final int _cursorIndexOfEstadoNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nombre");
          final int _cursorIndexOfEstadoEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_emoji");
          final int _cursorIndexOfEstadoColor = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_color");
          final int _cursorIndexOfEstadoDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_descripcion");
          final int _cursorIndexOfEstadoNivelUrgencia = CursorUtil.getColumnIndexOrThrow(_cursor, "estado_nivel_urgencia");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final int _cursorIndexOfFechaUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_ultima_sincronizacion");
          final int _cursorIndexOfModificadoLocalmente = CursorUtil.getColumnIndexOrThrow(_cursor, "modificado_localmente");
          final int _cursorIndexOfFechaModificacionLocal = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha_modificacion_local");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "server_id");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpTipoExamenNombre;
            if (_cursor.isNull(_cursorIndexOfTipoExamenNombre)) {
              _tmpTipoExamenNombre = null;
            } else {
              _tmpTipoExamenNombre = _cursor.getString(_cursorIndexOfTipoExamenNombre);
            }
            final Integer _tmpTipoExamenId;
            if (_cursor.isNull(_cursorIndexOfTipoExamenId)) {
              _tmpTipoExamenId = null;
            } else {
              _tmpTipoExamenId = _cursor.getInt(_cursorIndexOfTipoExamenId);
            }
            final String _tmpTitulo;
            if (_cursor.isNull(_cursorIndexOfTitulo)) {
              _tmpTitulo = null;
            } else {
              _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            }
            final String _tmpValor;
            if (_cursor.isNull(_cursorIndexOfValor)) {
              _tmpValor = null;
            } else {
              _tmpValor = _cursor.getString(_cursorIndexOfValor);
            }
            final String _tmpUnidad;
            if (_cursor.isNull(_cursorIndexOfUnidad)) {
              _tmpUnidad = null;
            } else {
              _tmpUnidad = _cursor.getString(_cursorIndexOfUnidad);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final String _tmpDatosAdicionales;
            if (_cursor.isNull(_cursorIndexOfDatosAdicionales)) {
              _tmpDatosAdicionales = null;
            } else {
              _tmpDatosAdicionales = _cursor.getString(_cursorIndexOfDatosAdicionales);
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
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            final String _tmpEstadoCodigo;
            if (_cursor.isNull(_cursorIndexOfEstadoCodigo)) {
              _tmpEstadoCodigo = null;
            } else {
              _tmpEstadoCodigo = _cursor.getString(_cursorIndexOfEstadoCodigo);
            }
            final String _tmpEstadoNombre;
            if (_cursor.isNull(_cursorIndexOfEstadoNombre)) {
              _tmpEstadoNombre = null;
            } else {
              _tmpEstadoNombre = _cursor.getString(_cursorIndexOfEstadoNombre);
            }
            final String _tmpEstadoEmoji;
            if (_cursor.isNull(_cursorIndexOfEstadoEmoji)) {
              _tmpEstadoEmoji = null;
            } else {
              _tmpEstadoEmoji = _cursor.getString(_cursorIndexOfEstadoEmoji);
            }
            final String _tmpEstadoColor;
            if (_cursor.isNull(_cursorIndexOfEstadoColor)) {
              _tmpEstadoColor = null;
            } else {
              _tmpEstadoColor = _cursor.getString(_cursorIndexOfEstadoColor);
            }
            final String _tmpEstadoDescripcion;
            if (_cursor.isNull(_cursorIndexOfEstadoDescripcion)) {
              _tmpEstadoDescripcion = null;
            } else {
              _tmpEstadoDescripcion = _cursor.getString(_cursorIndexOfEstadoDescripcion);
            }
            final Integer _tmpEstadoNivelUrgencia;
            if (_cursor.isNull(_cursorIndexOfEstadoNivelUrgencia)) {
              _tmpEstadoNivelUrgencia = null;
            } else {
              _tmpEstadoNivelUrgencia = _cursor.getInt(_cursorIndexOfEstadoNivelUrgencia);
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
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            _item = new ExamEntity(_tmpId,_tmpPatientId,_tmpTipoExamenNombre,_tmpTipoExamenId,_tmpTitulo,_tmpValor,_tmpUnidad,_tmpObservaciones,_tmpDatosAdicionales,_tmpFechaCreacion,_tmpFechaModificacion,_tmpActivo,_tmpEstadoCodigo,_tmpEstadoNombre,_tmpEstadoEmoji,_tmpEstadoColor,_tmpEstadoDescripcion,_tmpEstadoNivelUrgencia,_tmpSincronizado,_tmpFechaUltimaSincronizacion,_tmpModificadoLocalmente,_tmpFechaModificacionLocal,_tmpServerId);
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
  public Object markExamsAsSynced(final List<String> examIds, final String timestamp,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("\n");
        _stringBuilder.append("        UPDATE exams ");
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
        final int _inputSize = examIds.size();
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
        for (String _item : examIds) {
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
