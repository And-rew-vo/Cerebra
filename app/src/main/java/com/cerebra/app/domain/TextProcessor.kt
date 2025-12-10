package com.cerebra.app.domain

import com.cerebra.app.data.local.entity.TextDocument
import javax.inject.Inject
import kotlin.random.Random

data class ProcessedToken(
    val originalWord: String,
    val displayValue: String,
    val isHidden: Boolean,
    val index: Int
)

class TextProcessor @Inject constructor() {

    /**
     * Processes text for training.
     * Logic:
     * 1. Tokenize by space.
     * 2. Identify candidates (words length > 2 usually, effectively random).
     * 3. Hide ~25% of words.
     */
    fun processTextForTraining(content: String): List<ProcessedToken> {
        // Regex to split but keep punctuation attached or separate?
        // For MVP simplicity: split by spaces, punctuation is part of the "word" for matching.
        // Better UX: Split punctuation so user doesn't have to type "Hello," vs "Hello".
        // Let's do a simple whitespace split first.
        val tokens = content.split(Regex("\\s+")).filter { it.isNotEmpty() }
        
        val totalTokens = tokens.size
        if (totalTokens == 0) return emptyList()

        val indicesToHideCount = (totalTokens * 0.25).toInt().coerceAtLeast(1)
        val hiddenIndices = mutableSetOf<Int>()
        
        // Randomly select indices
        while (hiddenIndices.size < indicesToHideCount && hiddenIndices.size < totalTokens) {
            val idx = Random.nextInt(totalTokens)
            // Filter: Don't hide very short words if possible, unless text is all short.
            // But user requirement is just "~20-30% of random words".
            if (!hiddenIndices.contains(idx)) {
                hiddenIndices.add(idx)
            }
        }

        return tokens.mapIndexed { index, word ->
            val isHidden = hiddenIndices.contains(index)
            ProcessedToken(
                originalWord = word,
                displayValue = if (isHidden) "" else word,
                isHidden = isHidden,
                index = index
            )
        }
    }
    
    /**
     * Validates input against the original word.
     * Case-insensitive check.
     * Also handles punctuation if it was attached.
     * Ideally, we should strip punctuation for strict checking, 
     * but strictly "text memorization" often implies exact punctuation.
     * Let's be lenient: Case insensitive, trim.
     */
    fun validateWord(input: String, original: String): Boolean {
        // Simple normalization
        val normalizedInput = input.trim().lowercase()
        val normalizedOriginal = original.trim().lowercase()
        
        // If the original has punctuation (e.g. "Hello,"), and user types "hello", that might be annoying.
        // Let's strip standard punctuation from the END for comparison sake 
        // if exact match fails.
        if (normalizedInput == normalizedOriginal) return true
        
        val punctuation = listOf('.', ',', '!', '?', ';', ':')
        val originalStripped = original.trimEnd { it in punctuation }.lowercase()
        
        return normalizedInput == originalStripped
    }
}
