package com.cerebra.app.data.repository

import com.cerebra.app.data.local.dao.TextDao
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.domain.repository.TextRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TextRepositoryImpl @Inject constructor(
    private val textDao: TextDao
) : TextRepository {
    override suspend fun saveText(text: TextEntity): Long {
        return textDao.insertText(text)
    }

    override suspend fun updateText(text: TextEntity) {
        textDao.updateText(text)
    }

    override suspend fun deleteText(text: TextEntity) {
        textDao.deleteText(text)
    }

    override suspend fun getTextById(id: Int): TextEntity? {
        return textDao.getTextById(id)
    }

    override fun getAllTexts(userId: Int): Flow<List<TextEntity>> {
        return textDao.getAllTexts(userId)
    }

    override fun getTextCount(userId: Int): Flow<Int> {
        return textDao.getTextCount(userId)
    }
}
