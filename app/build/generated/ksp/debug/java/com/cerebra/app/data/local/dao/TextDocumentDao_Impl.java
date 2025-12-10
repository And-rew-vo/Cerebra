package com.cerebra.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.cerebra.app.data.local.entity.TextDocument;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
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
public final class TextDocumentDao_Impl implements TextDocumentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TextDocument> __insertionAdapterOfTextDocument;

  private final EntityDeletionOrUpdateAdapter<TextDocument> __deletionAdapterOfTextDocument;

  private final EntityDeletionOrUpdateAdapter<TextDocument> __updateAdapterOfTextDocument;

  public TextDocumentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTextDocument = new EntityInsertionAdapter<TextDocument>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `text_documents` (`id`,`userId`,`title`,`content`,`progress`,`lastTrainedAt`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TextDocument entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getContent());
        statement.bindLong(5, entity.getProgress());
        statement.bindLong(6, entity.getLastTrainedAt());
      }
    };
    this.__deletionAdapterOfTextDocument = new EntityDeletionOrUpdateAdapter<TextDocument>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `text_documents` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TextDocument entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTextDocument = new EntityDeletionOrUpdateAdapter<TextDocument>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `text_documents` SET `id` = ?,`userId` = ?,`title` = ?,`content` = ?,`progress` = ?,`lastTrainedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TextDocument entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getContent());
        statement.bindLong(5, entity.getProgress());
        statement.bindLong(6, entity.getLastTrainedAt());
        statement.bindLong(7, entity.getId());
      }
    };
  }

  @Override
  public Object insertTextDocument(final TextDocument document,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTextDocument.insertAndReturnId(document);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTextDocument(final TextDocument document,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTextDocument.handle(document);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTextDocument(final TextDocument document,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTextDocument.handle(document);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TextDocument>> getAllTexts(final int userId) {
    final String _sql = "SELECT * FROM text_documents WHERE userId = ? ORDER BY lastTrainedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"text_documents"}, new Callable<List<TextDocument>>() {
      @Override
      @NonNull
      public List<TextDocument> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfLastTrainedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTrainedAt");
          final List<TextDocument> _result = new ArrayList<TextDocument>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TextDocument _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final long _tmpLastTrainedAt;
            _tmpLastTrainedAt = _cursor.getLong(_cursorIndexOfLastTrainedAt);
            _item = new TextDocument(_tmpId,_tmpUserId,_tmpTitle,_tmpContent,_tmpProgress,_tmpLastTrainedAt);
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
  public Flow<List<TextDocument>> getUnfinishedTexts(final int userId) {
    final String _sql = "SELECT * FROM text_documents WHERE userId = ? AND progress < 100 ORDER BY lastTrainedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"text_documents"}, new Callable<List<TextDocument>>() {
      @Override
      @NonNull
      public List<TextDocument> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfLastTrainedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTrainedAt");
          final List<TextDocument> _result = new ArrayList<TextDocument>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TextDocument _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final long _tmpLastTrainedAt;
            _tmpLastTrainedAt = _cursor.getLong(_cursorIndexOfLastTrainedAt);
            _item = new TextDocument(_tmpId,_tmpUserId,_tmpTitle,_tmpContent,_tmpProgress,_tmpLastTrainedAt);
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
  public Object getTextDocumentById(final int id,
      final Continuation<? super TextDocument> $completion) {
    final String _sql = "SELECT * FROM text_documents WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TextDocument>() {
      @Override
      @Nullable
      public TextDocument call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfLastTrainedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTrainedAt");
          final TextDocument _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final long _tmpLastTrainedAt;
            _tmpLastTrainedAt = _cursor.getLong(_cursorIndexOfLastTrainedAt);
            _result = new TextDocument(_tmpId,_tmpUserId,_tmpTitle,_tmpContent,_tmpProgress,_tmpLastTrainedAt);
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
  public Flow<Integer> getTextCount(final int userId) {
    final String _sql = "SELECT COUNT(*) FROM text_documents WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"text_documents"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
  public Flow<Float> getAverageProgress(final int userId) {
    final String _sql = "SELECT AVG(progress) FROM text_documents WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"text_documents"}, new Callable<Float>() {
      @Override
      @Nullable
      public Float call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Float _result;
          if (_cursor.moveToFirst()) {
            final Float _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getFloat(0);
            }
            _result = _tmp;
          } else {
            _result = null;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
