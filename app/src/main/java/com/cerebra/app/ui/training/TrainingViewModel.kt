package com.cerebra.app.ui.training

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextDocument
import com.cerebra.app.data.repository.CerebraRepository
import com.cerebra.app.domain.ProcessedToken
import com.cerebra.app.domain.TextProcessor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrainingUiState(
    val isLoading: Boolean = false,
    val text: TextDocument? = null,
    val tokens: List<ProcessedToken> = emptyList(),
    val userInputs: Map<Int, String> = emptyMap(), // Index -> Input
    val validationStatus: Map<Int, Boolean> = emptyMap(), // Index -> IsCorrect
    val isComplete: Boolean = false,
    val progress: Int = 0
)

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val repository: CerebraRepository,
    private val textProcessor: TextProcessor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val textId: Int = checkNotNull(savedStateHandle["textId"])
    
    private val _uiState = MutableStateFlow(TrainingUiState(isLoading = true))
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    init {
        loadText()
    }

    private fun loadText() {
        viewModelScope.launch {
            val text = repository.getTextDocumentById(textId)
            if (text != null) {
                val tokens = textProcessor.processTextForTraining(text.content)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    text = text,
                    tokens = tokens,
                    userInputs = emptyMap(),
                    validationStatus = emptyMap(),
                    progress = text.progress
                )
            } else {
                // Handle error
            }
        }
    }

    fun onInputChange(index: Int, input: String) {
        val currentInputs = _uiState.value.userInputs.toMutableMap()
        currentInputs[index] = input
        
        // Immediate Validation
        val token = _uiState.value.tokens.find { it.index == index }
        val currentValidation = _uiState.value.validationStatus.toMutableMap()
        
        if (token != null) {
            val isCorrect = textProcessor.validateWord(input, token.originalWord)
            currentValidation[index] = isCorrect
        }

        _uiState.value = _uiState.value.copy(
            userInputs = currentInputs,
            validationStatus = currentValidation
        )
        
        checkCompletion()
    }

    private fun checkCompletion() {
        val hiddenTokens = _uiState.value.tokens.filter { it.isHidden }
        if (hiddenTokens.isEmpty()) return

        val correctCount = hiddenTokens.count { token ->
            _uiState.value.validationStatus[token.index] == true
        }
        
        // Calculate progress based on correct answers vs total hidden words
        // If all hidden words are correct, progress is 100% (for this session logic)
        // Ideally we might want to average it or something, but let's say completion of "Training" = 100% of the session.
        // We will update the Document progress. 
        // Logic: (Correct / TotalHidden) * 100
        val newProgress = ((correctCount.toFloat() / hiddenTokens.size.toFloat()) * 100).toInt()
        
        // Only save if progress improved or it's a new training? 
        // Requirement: "Update the progress of the TextDocument based on correct answers."
        // Let's just update it to the result of this session? Or accumulate? 
        // "Unfinished trainings" implies we want to reach 100%. 
        // So update the document with the current session's score.
        
        _uiState.value = _uiState.value.copy(
            progress = newProgress,
            isComplete = correctCount == hiddenTokens.size
        )
    }

    fun saveProgress() {
        val text = _uiState.value.text ?: return
        val currentProgress = _uiState.value.progress
        
        viewModelScope.launch {
            repository.updateTextDocument(
                text.copy(
                    progress = currentProgress,
                    lastTrainedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
