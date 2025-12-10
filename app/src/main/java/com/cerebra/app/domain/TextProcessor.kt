package com.cerebra.app.domain

import javax.inject.Inject
import kotlin.random.Random

enum class Difficulty { LOW, HIGH }

data class ProcessedToken(
    val originalWord: String,
    val displayValue: String,
    val isHidden: Boolean,
    val index: Int // Global index or chunk index? Let's use chunk index for UI simplicity.
)

data class Trainingsession(
    val chunks: List<Chunk>,
    val difficulty: Difficulty
)

data class Chunk(
    val id: Int,
    val tokens: List<ProcessedToken>,
    val content: String
)

class TextProcessor @Inject constructor() {

    fun createSession(content: String, difficulty: Difficulty): Trainingsession {
        val chunks = when (difficulty) {
            Difficulty.LOW -> splitIntoSentences(content)
            Difficulty.HIGH -> splitIntoParagraphs(content)
        }

        val processedChunks = chunks.mapIndexed { index, chunkText ->
            processChunk(chunkText, index, difficulty)
        }

        return Trainingsession(processedChunks, difficulty)
    }

    private fun splitIntoSentences(content: String): List<String> {
        val sentences = mutableListOf<String>()
        val regex = Regex("(?<=[.!?])\\s+") // Split after punctuation followed by space
        sentences.addAll(content.split(regex).filter { it.isNotBlank() })
        return sentences
    }

    private fun splitIntoParagraphs(content: String): List<String> {
        return content.split("\n\n", "\r\n\r\n").filter { it.isNotBlank() }
    }

    private fun processChunk(content: String, chunkId: Int, difficulty: Difficulty): Chunk {
        // Split by whitespace
        val words = content.split(Regex("\\s+")).filter { it.isNotEmpty() }
        val tokens = mutableListOf<ProcessedToken>()
        
        val indicesToHide = when (difficulty) {
            Difficulty.LOW -> {
                // Hide 1-2 words
                val count = Random.nextInt(1, 3).coerceAtMost(words.size)
                pickRandomIndices(words.size, count)
            }
            Difficulty.HIGH -> {
                // Many words hidden, ~50%
                val count = (words.size * 0.5).toInt().coerceAtLeast(1)
                pickRandomIndices(words.size, count)
            }
        }

        words.forEachIndexed { index, word ->
            val isHidden = indicesToHide.contains(index)
            tokens.add(
                ProcessedToken(
                    originalWord = word,
                    displayValue = if (isHidden) "" else word,
                    isHidden = isHidden,
                    index = index
                )
            )
        }

        return Chunk(chunkId, tokens, content)
    }

    private fun pickRandomIndices(total: Int, count: Int): Set<Int> {
        val indices = mutableSetOf<Int>()
        while (indices.size < count && indices.size < total) {
            indices.add(Random.nextInt(total))
        }
        return indices
    }

    fun validateWord(input: String, original: String): Boolean {
        // Remove punctuation from original for check
        val cleanOriginal = original.filter { it.isLetterOrDigit() }.lowercase()
        val cleanInput = input.filter { it.isLetterOrDigit() }.lowercase()
        return cleanOriginal == cleanInput
    }
}
