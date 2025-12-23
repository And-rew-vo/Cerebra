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
import com.cerebra.app.data.local.entity.TextEntity;
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
public final class TextDao_Impl implements TextDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TextEntity> __insertionAdapterOfTextEntity;

  private final EntityDeletionOrUpdateAdapter<TextEntity> __deletionAdapterOfTextEntity;

  private final EntityDeletionOrUpdateAdapter<TextEntity> __updateAdapterOfTextEntity;

  public TextDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTextEntity = new EntityInsertionAdapter<TextEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `texts` (`id`,`userId`,`title`,`content`,`progress`,`savedChunkIndex`,`shuffledIndicesJson`,`lastTrainedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TextEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getContent());
        statement.bindString(5, entity.getProgress());
        statement.bindLong(6, entity.getSavedChunkIndex());
        if (entity.getShuffledIndicesJson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getShuffledIndicesJson());
        }
        statement.bindLong(8, entity.getLastTrainedAt());
      }
    };
    this.__deletionAdapterOfTextEntity = new EntityDeletionOrUpdateAdapter<TextEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `texts` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TextEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTextEntity = new EntityDeletionOrUpdateAdapter<TextEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `texts` SET `id` = ?,`userId` = ?,`title` = ?,`content` = ?,`progress` = ?,`savedChunkIndex` = ?,`shuffledIndicesJson` = ?,`lastTrainedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TextEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getContent());
        statement.bindString(5, entity.getProgress());
        statement.bindLong(6, entity.getSavedChunkIndex());
        if (entity.getShuffledIndicesJson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getShuffledIndicesJson());
        }
        statement.bindLong(8, entity.getLastTrainedAt());
        statement.bindLong(9, entity.getId());
      }
    };
  }

  @Override
  public Object insertText(final TextEntity text, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTextEntity.insertAndReturnId(text);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteText(final TextEntity text, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTextEntity.handle(text);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateText(final TextEntity text, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTextEntity.handle(text);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TextEntity>> getAllTexts(final int userId) {
    final String _sql = "SELECT * FROM texts WHERE userId = ? ORDER BY lastTrainedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"texts"}, new Callable<List<TextEntity>>() {
      @Override
      @NonNull
      public List<TextEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfSavedChunkIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "savedChunkIndex");
          final int _cursorIndexOfShuffledIndicesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "shuffledIndicesJson");
          final int _cursorIndexOfLastTrainedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTrainedAt");
          final List<TextEntity> _result = new ArrayList<TextEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TextEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpProgress;
            _tmpProgress = _cursor.getString(_cursorIndexOfProgress);
            final int _tmpSavedChunkIndex;
            _tmpSavedChunkIndex = _cursor.getInt(_cursorIndexOfSavedChunkIndex);
            final String _tmpShuffledIndicesJson;
            if (_cursor.isNull(_cursorIndexOfShuffledIndicesJson)) {
              _tmpShuffledIndicesJson = null;
            } else {
              _tmpShuffledIndicesJson = _cursor.getString(_cursorIndexOfShuffledIndicesJson);
            }
            final long _tmpLastTrainedAt;
            _tmpLastTrainedAt = _cursor.getLong(_cursorIndexOfLastTrainedAt);
            _item = new TextEntity(_tmpId,_tmpUserId,_tmpTitle,_tmpContent,_tmpProgress,_tmpSavedChunkIndex,_tmpShuffledIndicesJson,_tmpLastTrainedAt);
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
  public Object getTextById(final int id, final Continuation<? super TextEntity> $completion) {
    final String _sql = "SELECT * FROM texts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TextEntity>() {
      @Override
      @Nullable
      public TextEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfSavedChunkIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "savedChunkIndex");
          final int _cursorIndexOfShuffledIndicesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "shuffledIndicesJson");
          final int _cursorIndexOfLastTrainedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTrainedAt");
          final TextEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpProgress;
            _tmpProgress = _cursor.getString(_cursorIndexOfProgress);
            final int _tmpSavedChunkIndex;
            _tmpSavedChunkIndex = _cursor.getInt(_cursorIndexOfSavedChunkIndex);
            final String _tmpShuffledIndicesJson;
            if (_cursor.isNull(_cursorIndexOfShuffledIndicesJson)) {
              _tmpShuffledIndicesJson = null;
            } else {
              _tmpShuffledIndicesJson = _cursor.getString(_cursorIndexOfShuffledIndicesJson);
            }
            final long _tmpLastTrainedAt;
            _tmpLastTrainedAt = _cursor.getLong(_cursorIndexOfLastTrainedAt);
            _result = new TextEntity(_tmpId,_tmpUserId,_tmpTitle,_tmpContent,_tmpProgress,_tmpSavedChunkIndex,_tmpShuffledIndicesJson,_tmpLastTrainedAt);
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
    final String _sql = "SELECT COUNT(*) FROM texts WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"texts"}, new Callable<Integer>() {
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
