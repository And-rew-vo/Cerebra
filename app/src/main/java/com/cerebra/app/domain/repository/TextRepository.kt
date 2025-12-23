package com.cerebra.app.domain.repository

import com.cerebra.app.data.local.entity.TextEntity
import kotlinx.coroutines.flow.Flow

interface TextRepository {
    suspend fun saveText(text: TextEntity): Long
    suspend fun updateText(text: TextEntity)
    suspend fun deleteText(text: TextEntity)
    suspend fun getTextById(id: Int): TextEntity?
    fun getAllTexts(userId: Int): Flow<List<TextEntity>>
    fun getTextCount(userId: Int): Flow<Int>
}
