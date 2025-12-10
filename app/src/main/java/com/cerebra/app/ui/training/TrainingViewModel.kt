package com.cerebra.app.ui.training

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.domain.Chunk
import com.cerebra.app.domain.Difficulty
import com.cerebra.app.domain.ProcessedToken
import com.cerebra.app.domain.TextProcessor
import com.cerebra.app.domain.repository.TextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

enum class TrainingPhase { SETUP, TRAINING, COMPLETED }

data class TrainingUiState(
    val isLoading: Boolean = false,
    val phase: TrainingPhase = TrainingPhase.SETUP,
    val textEntity: TextEntity? = null,
    val chunks: List<Chunk> = emptyList(),
    val currentChunkIndex: Int = 0,
    val currentChunk: Chunk? = null,
    val userInputs: Map<Int, String> = emptyMap(), // Token Index -> Input
    val validationStatus: Map<Int, Boolean> = emptyMap(), // Token Index -> IsCorrect
    val difficulty: Difficulty = Difficulty.LOW
)

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val repository: TextRepository,
    private val textProcessor: TextProcessor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val textId: Int = checkNotNull(savedStateHandle.get<Int>("textId"))

    private val _uiState = MutableStateFlow(TrainingUiState(isLoading = true))
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    init {
        loadText()
    }

    private fun loadText() {
        viewModelScope.launch {
            val text = repository.getTextById(textId)
            if (text != null) {
                // Try resume
                val (resumeIndex, resumeDifficulty) = parseProgress(text.progress)
                
                if (resumeIndex > 0 || resumeDifficulty != null) {
                    // Auto-start if we have progress
                    val diff = resumeDifficulty ?: Difficulty.LOW
                    startTraining(diff, resumeIndex)
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    textEntity = text,
                    difficulty = resumeDifficulty ?: Difficulty.LOW
                    // Phase remains SETUP if no progress, or set in startTraining
                )
            } else {
                // Error
            }
        }
    }

    fun setDifficulty(difficulty: Difficulty) {
        _uiState.value = _uiState.value.copy(difficulty = difficulty)
    }

    fun startTraining(difficulty: Difficulty, startIndex: Int = 0) {
        val text = _uiState.value.textEntity ?: return
        val session = textProcessor.createSession(text.content, difficulty)
        
        val validStartIndex = startIndex.coerceIn(0, session.chunks.size - 1)
        val chunk = session.chunks[validStartIndex]
        
        _uiState.value = _uiState.value.copy(
            phase = TrainingPhase.TRAINING,
            chunks = session.chunks,
            currentChunkIndex = validStartIndex,
            currentChunk = chunk,
            difficulty = difficulty,
            userInputs = emptyMap(),
            validationStatus = emptyMap()
        )
    }

    fun onInputChange(tokenIndex: Int, input: String) {
        val currentInputs = _uiState.value.userInputs.toMutableMap()
        currentInputs[tokenIndex] = input
        
        // Validation
        val chunk = _uiState.value.currentChunk ?: return
        val token = chunk.tokens.find { it.index == tokenIndex }
        val currentValidation = _uiState.value.validationStatus.toMutableMap()

        if (token != null) {
            val isCorrect = textProcessor.validateWord(input, token.originalWord)
            currentValidation[tokenIndex] = isCorrect
        }

        _uiState.value = _uiState.value.copy(
            userInputs = currentInputs,
            validationStatus = currentValidation
        )
        
        checkChunkCompletion()
    }

    private fun checkChunkCompletion() {
        val chunk = _uiState.value.currentChunk ?: return
        val hiddenTokens = chunk.tokens.filter { it.isHidden }
        
        val allCorrect = hiddenTokens.all { token ->
             _uiState.value.validationStatus[token.index] == true
        }

        if (allCorrect && hiddenTokens.isNotEmpty()) {
            // Auto-save progress
            saveProgress()
            // Wait for user or auto-advance? 
            // "User must complete one chunk to proceed to the next."
            // UI can show "Next" button or auto-advance. 
            // Let's rely on UI showing a "Next" button when all green.
        }
    }

    fun nextChunk() {
        val nextIndex = _uiState.value.currentChunkIndex + 1
        if (nextIndex < _uiState.value.chunks.size) {
            val nextChunk = _uiState.value.chunks[nextIndex]
            _uiState.value = _uiState.value.copy(
                currentChunkIndex = nextIndex,
                currentChunk = nextChunk,
                userInputs = emptyMap(),
                validationStatus = emptyMap()
            )
            saveProgress()
        } else {
            // Completed text
            _uiState.value = _uiState.value.copy(phase = TrainingPhase.COMPLETED)
            saveCompletion()
        }
    }

    private fun saveProgress() {
        val text = _uiState.value.textEntity ?: return
        val index = _uiState.value.currentChunkIndex
        val difficulty = _uiState.value.difficulty
        val totalChunks = _uiState.value.chunks.size.toFloat()
        
        // Percent logic: (index / total) * 100 roughly
        // Or strictly completed chunks
        val percent = ((index.toFloat() / totalChunks) * 100).toInt()

        val json = JSONObject()
        json.put("chunkIndex", index)
        json.put("difficulty", difficulty.name)
        json.put("percent", percent)

        viewModelScope.launch {
            repository.updateText(
                text.copy(
                    progress = json.toString(),
                    lastTrainedAt = System.currentTimeMillis()
                )
            )
        }
    }
    
    private fun saveCompletion() {
        // 100 percent
        val text = _uiState.value.textEntity ?: return
        val json = JSONObject()
        json.put("chunkIndex", _uiState.value.chunks.size)
        json.put("difficulty", _uiState.value.difficulty.name)
        json.put("percent", 100)
        
        viewModelScope.launch {
            repository.updateText(text.copy(progress = json.toString(), lastTrainedAt = System.currentTimeMillis()))
        }
    }

    private fun parseProgress(jsonString: String): Pair<Int, Difficulty?> {
        return try {
            val json = JSONObject(jsonString)
            val index = json.optInt("chunkIndex", 0)
            val diffName = json.optString("difficulty")
            val diff = if (diffName.isNotEmpty()) Difficulty.valueOf(diffName) else null
            Pair(index, diff)
        } catch (e: Exception) {
            Pair(0, null)
        }
    }
}
