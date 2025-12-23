package com.cerebra.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cerebra.app.data.local.entity.TextEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TextDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertText(text: TextEntity): Long

    @Update
    suspend fun updateText(text: TextEntity)

    @Delete
    suspend fun deleteText(text: TextEntity)

    @Query("SELECT * FROM texts WHERE userId = :userId ORDER BY lastTrainedAt DESC")
    fun getAllTexts(userId: Int): Flow<List<TextEntity>>

    @Query("SELECT * FROM texts WHERE id = :id")
    suspend fun getTextById(id: Int): TextEntity?
    
    @Query("SELECT COUNT(*) FROM texts WHERE userId = :userId")
    fun getTextCount(userId: Int): Flow<Int>
}
