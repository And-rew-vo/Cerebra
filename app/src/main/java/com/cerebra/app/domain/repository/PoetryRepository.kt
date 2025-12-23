package com.cerebra.app.domain.repository

import com.cerebra.app.domain.model.TextItem

interface PoetryRepository {
    suspend fun getCommonPoems(): List<TextItem>
    suspend fun searchPoems(query: String): List<TextItem>
    suspend fun getPoemDetails(id: String): TextItem?
}
