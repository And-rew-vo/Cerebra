package com.cerebra.app.data.repository

import com.cerebra.app.data.api.PoetryDbApi
import com.cerebra.app.data.api.PoetryDbItem
import com.cerebra.app.domain.model.TextItem
import com.cerebra.app.domain.repository.PoetryRepository
import javax.inject.Inject

class PoetryRepositoryImpl @Inject constructor(
    private val api: PoetryDbApi
) : PoetryRepository {

    override suspend fun getCommonPoems(): List<TextItem> {
        return try {

            val response = api.getRandomPoems(50)
            response.asSequence()
                .filter { it.lines.size in 4..30 }
                .take(20)
                .map { it.toTextItem() }
                .toList()
        } catch (e: Exception) {
            e.printStackTrace()
             return listOf(
                TextItem(
                    id = "error",
                    title = "Error Loading Data",
                    author = "System",
                    content = "Error: ${e.message}",
                    isLocal = false
                )
            )
        }
    }

    override suspend fun searchPoems(query: String): List<TextItem> {
        return try {

            val response = api.searchPoems(query)
            response.map { it.toTextItem() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getPoemDetails(id: String): TextItem? {
        return try {

            val response = api.getPoemByTitle(id)
            response.firstOrNull()?.toTextItem()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun PoetryDbItem.toTextItem(): TextItem {
        return TextItem(
            id = this.title,
            title = this.title,
            author = this.author,
            content = this.lines.joinToString("\n"),
            isLocal = false
        )
    }
}
