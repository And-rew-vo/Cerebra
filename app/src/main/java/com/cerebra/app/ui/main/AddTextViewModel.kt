package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.domain.repository.TextRepository
import com.cerebra.app.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AddTextViewModel @Inject constructor(
    private val textRepository: TextRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val textId: Int? = savedStateHandle.get<String>("textId")?.toIntOrNull()

    private val _uiState = MutableStateFlow<TextEntity?>(null)
    val uiState: StateFlow<TextEntity?> = _uiState

    init {
        if (textId != null) {
            viewModelScope.launch {
                val text = textRepository.getTextById(textId)
                _uiState.value = text
            }
        }
    }

    fun saveText(title: String, content: String, onSuccess: () -> Unit) {
        if (title.isBlank() || content.isBlank()) return

        viewModelScope.launch {
            val userId = userPreferencesRepository.currentUserId.first()
            if (userId != null) {
                if (textId != null) {
                     val currentText = _uiState.value
                     if (currentText != null) {
                         textRepository.updateText(currentText.copy(title = title, content = content))
                     }
                } else {
                    val text = TextEntity(
                        userId = userId,
                        title = title,
                        content = content,
                        progress = "",
                        lastTrainedAt = System.currentTimeMillis()
                    )
                    textRepository.saveText(text)
                }
                onSuccess()
            }
        }
    }
}
