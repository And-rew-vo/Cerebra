package com.cerebra.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cerebra.app.data.local.entity.TextDocument
import kotlinx.coroutines.flow.Flow

@Dao
interface TextDocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTextDocument(document: TextDocument): Long

    @Update
    suspend fun updateTextDocument(document: TextDocument)

    @Delete
    suspend fun deleteTextDocument(document: TextDocument)

    @Query("SELECT * FROM text_documents WHERE userId = :userId ORDER BY lastTrainedAt DESC")
    fun getAllTexts(userId: Int): Flow<List<TextDocument>>

    @Query("SELECT * FROM text_documents WHERE userId = :userId AND progress < 100 ORDER BY lastTrainedAt DESC")
    fun getUnfinishedTexts(userId: Int): Flow<List<TextDocument>>

    @Query("SELECT * FROM text_documents WHERE id = :id")
    suspend fun getTextDocumentById(id: Int): TextDocument?
    
    @Query("SELECT COUNT(*) FROM text_documents WHERE userId = :userId")
    fun getTextCount(userId: Int): Flow<Int>
    
    @Query("SELECT AVG(progress) FROM text_documents WHERE userId = :userId")
    fun getAverageProgress(userId: Int): Flow<Float?>
}
