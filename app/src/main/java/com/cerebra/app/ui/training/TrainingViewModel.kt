package com.cerebra.app.ui.training

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.domain.Chunk
import com.cerebra.app.domain.Difficulty
import com.cerebra.app.domain.TextProcessor
import com.cerebra.app.domain.repository.TextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
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
    val userInputs: Map<Int, String> = emptyMap(),
    val validationStatus: Map<Int, Boolean> = emptyMap(),
    val difficulty: Difficulty = Difficulty.LOW,
    val shuffledIndices: List<Int> = emptyList(),
    val activeHintTokenIndex: Int? = null
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
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    textEntity = text
                )
                
                
                if (!text.shuffledIndicesJson.isNullOrEmpty()) {
                    val indices = parseIndices(text.shuffledIndicesJson)
                    val diff = parseDifficulty(text.progress) ?: Difficulty.LOW
                    startTraining(
                        difficulty = diff, 
                        startIndex = text.savedChunkIndex, 
                        restoredIndices = indices
                    )
                }
            }
        }
    }

    private fun parseIndices(json: String): List<Int> {
        return try {
            val ja = JSONArray(json)
            val list = mutableListOf<Int>()
            for (i in 0 until ja.length()) {
                list.add(ja.getInt(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseDifficulty(progressJson: String): Difficulty? {
        return try {
            val json = JSONObject(progressJson)
            val name = json.optString("difficulty")
            if (name.isNotEmpty()) Difficulty.valueOf(name) else null
        } catch (e: Exception) { null }
    }

    fun setDifficulty(difficulty: Difficulty) {
        _uiState.value = _uiState.value.copy(difficulty = difficulty)
    }

    fun startTraining(
        difficulty: Difficulty,
        title: String? = null,
        content: String? = null,
        startIndex: Int = 0, 
        restoredIndices: List<Int> = emptyList()
    ) {
        var text = _uiState.value.textEntity ?: return
        
        if (title != null && content != null && (title != text.title || content != text.content)) {
            text = text.copy(title = title, content = content)
            viewModelScope.launch {
                repository.updateText(text)
            }
            _uiState.value = _uiState.value.copy(textEntity = text)
        }

        val session = textProcessor.createSession(text.content, difficulty)
        
        val indices = if (restoredIndices.isNotEmpty() && restoredIndices.size == session.chunks.size) {
            restoredIndices
        } else {
            (session.chunks.indices).shuffled()
        }

        if (startIndex >= session.chunks.size) {
            _uiState.value = _uiState.value.copy(phase = TrainingPhase.COMPLETED)
            return
        }

        val realChunkIndex = indices[startIndex]
        val chunk = session.chunks[realChunkIndex]
        
        _uiState.value = _uiState.value.copy(
            phase = TrainingPhase.TRAINING,
            chunks = session.chunks,
            shuffledIndices = indices,
            currentChunkIndex = startIndex,
            currentChunk = chunk,
            difficulty = difficulty,
            userInputs = emptyMap(),
            validationStatus = emptyMap()
        )
        
        
        if (restoredIndices.isEmpty()) {
             saveProgress()
        }
    }

    fun restartTraining() {
        val difficulty = _uiState.value.difficulty
        val text = _uiState.value.textEntity
        
        viewModelScope.launch {
            if (text != null) {
                repository.updateText(
                    text.copy(
                        progress = "0",
                        savedChunkIndex = 0,
                        shuffledIndicesJson = null,
                        lastTrainedAt = System.currentTimeMillis()
                    )
                )
                 _uiState.value = _uiState.value.copy(
                     textEntity = text,
                     phase = TrainingPhase.SETUP,
                     chunks = emptyList(),
                     currentChunkIndex = 0,
                     currentChunk = null,
                     shuffledIndices = emptyList(),
                     userInputs = emptyMap(),
                     validationStatus = emptyMap()
                 )
            }
        }
    }

    fun revealHint(tokenIndex: Int) {
        val currentUiState = _uiState.value
        val newActiveHintIndex = if (currentUiState.activeHintTokenIndex == tokenIndex) null else tokenIndex
        val currentValidation = currentUiState.validationStatus.toMutableMap()
        currentValidation[tokenIndex] = false 
        
        _uiState.value = currentUiState.copy(
            activeHintTokenIndex = newActiveHintIndex,
            validationStatus = currentValidation
        )
    }

    fun onInputChange(tokenIndex: Int, input: String) {
        val current = _uiState.value
        val currentInputs = current.userInputs.toMutableMap()
        currentInputs[tokenIndex] = input
        
        val chunk = current.currentChunk ?: return
        val token = chunk.tokens.find { it.index == tokenIndex }
        val currentValidation = current.validationStatus.toMutableMap()

        if (token != null) {
            val isCorrect = textProcessor.validateWord(input, token.originalWord)
            currentValidation[tokenIndex] = isCorrect
        }

        _uiState.value = current.copy(
            userInputs = currentInputs,
            validationStatus = currentValidation
        )
        
        checkChunkCompletion()
    }

    private fun checkChunkCompletion() {
        val chunk = _uiState.value.currentChunk ?: return
        val hiddenTokens = chunk.tokens.filter { it.isHidden }
        val allCorrect = hiddenTokens.all { _uiState.value.validationStatus[it.index] == true }

        if (allCorrect && hiddenTokens.isNotEmpty()) {
            saveProgress()
        }
    }

    fun nextChunk() {
        val nextIndex = _uiState.value.currentChunkIndex + 1
        if (nextIndex < _uiState.value.chunks.size) {
            val realChunkIndex = _uiState.value.shuffledIndices[nextIndex]
            val nextChunk = _uiState.value.chunks[realChunkIndex]
            _uiState.value = _uiState.value.copy(
                currentChunkIndex = nextIndex,
                currentChunk = nextChunk,
                userInputs = emptyMap(),
                validationStatus = emptyMap()
            )
            saveProgress()
        } else {
            _uiState.value = _uiState.value.copy(phase = TrainingPhase.COMPLETED)
            saveCompletion()
        }
    }

    private fun saveProgress() {
        val text = _uiState.value.textEntity ?: return
        val index = _uiState.value.currentChunkIndex
        val difficulty = _uiState.value.difficulty
        val totalChunks = _uiState.value.chunks.size.toFloat()
        val percent = ((index.toFloat() / totalChunks) * 100).toInt()

        val json = JSONObject()
        json.put("chunkIndex", index)
        json.put("difficulty", difficulty.name)
        json.put("percent", percent)
        
        val indicesArray = JSONArray(_uiState.value.shuffledIndices)
        
        viewModelScope.launch {
            repository.updateText(
                text.copy(
                    progress = json.toString(),
                    savedChunkIndex = index,
                    shuffledIndicesJson = indicesArray.toString(),
                    lastTrainedAt = System.currentTimeMillis()
                )
            )
        }
    }
    
    private fun saveCompletion() {
        val text = _uiState.value.textEntity ?: return
        val json = JSONObject()
        json.put("percent", 100)
        json.put("difficulty", _uiState.value.difficulty.name)
        
        viewModelScope.launch {
            repository.updateText(text.copy(
                progress = json.toString(), 
                savedChunkIndex = _uiState.value.chunks.size, 
                lastTrainedAt = System.currentTimeMillis()
            ))
        }
    }

    fun deleteText(onDeleted: () -> Unit) {
        val text = _uiState.value.textEntity ?: return
        viewModelScope.launch {
            repository.deleteText(text)
            onDeleted()
        }
    }
}
