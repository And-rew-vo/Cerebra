package com.cerebra.app.data.repository

import com.cerebra.app.data.local.dao.TextDocumentDao
import com.cerebra.app.data.local.dao.UserDao
import com.cerebra.app.data.local.entity.TextDocument
import com.cerebra.app.data.local.entity.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CerebraRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val textDocumentDao: TextDocumentDao
) : CerebraRepository {

    override suspend fun registerUser(user: User): Long {
        return userDao.insertUser(user)
    }

    override suspend fun loginUser(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    override suspend fun getUserById(userId: Int): User? {
        // Not strictly needed for MVP login flow if we store user in session, 
        // but good for completeness if needed.
        // For MVP, loginUser returns full User object.
        return null 
    }

    override suspend fun saveTextDocument(document: TextDocument): Long {
        return textDocumentDao.insertTextDocument(document)
    }

    override suspend fun updateTextDocument(document: TextDocument) {
        textDocumentDao.updateTextDocument(document)
    }
    
    override suspend fun deleteTextDocument(document: TextDocument) {
        textDocumentDao.deleteTextDocument(document)
    }

    override suspend fun getTextDocumentById(id: Int): TextDocument? {
        return textDocumentDao.getTextDocumentById(id)
    }

    override fun getAllTexts(userId: Int): Flow<List<TextDocument>> {
        return textDocumentDao.getAllTexts(userId)
    }

    override fun getUnfinishedTexts(userId: Int): Flow<List<TextDocument>> {
        return textDocumentDao.getUnfinishedTexts(userId)
    }
    
    override fun getTextCount(userId: Int): Flow<Int> {
        return textDocumentDao.getTextCount(userId)
    }
    
    override fun getAverageProgress(userId: Int): Flow<Float?> {
        return textDocumentDao.getAverageProgress(userId)
    }
}
