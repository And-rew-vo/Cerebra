package com.cerebra.app.data.repository

import com.cerebra.app.data.local.entity.TextDocument
import com.cerebra.app.data.local.entity.User
import kotlinx.coroutines.flow.Flow

interface CerebraRepository {
    // User
    suspend fun registerUser(user: User): Long
    suspend fun loginUser(email: String): User?
    suspend fun getUserById(userId: Int): User? // Optional helper

    // TextDocument
    suspend fun saveTextDocument(document: TextDocument): Long
    suspend fun updateTextDocument(document: TextDocument)
    suspend fun deleteTextDocument(document: TextDocument)
    suspend fun getTextDocumentById(id: Int): TextDocument?
    fun getAllTexts(userId: Int): Flow<List<TextDocument>>
    fun getUnfinishedTexts(userId: Int): Flow<List<TextDocument>>
    
    // Stats
    fun getTextCount(userId: Int): Flow<Int>
    fun getAverageProgress(userId: Int): Flow<Float?>
}
