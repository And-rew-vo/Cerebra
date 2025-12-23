package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.data.repository.UserPreferencesRepository
import com.cerebra.app.domain.model.TextItem
import com.cerebra.app.domain.repository.PoetryRepository
import com.cerebra.app.domain.repository.TextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonTextDetailViewModel @Inject constructor(
    private val textRepository: TextRepository,
    private val poetryRepository: PoetryRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _textItem = MutableStateFlow<TextItem?>(null)
    val textItem: StateFlow<TextItem?> = _textItem

    fun loadTextDetail(id: String) {
        viewModelScope.launch {
            _textItem.value = poetryRepository.getPoemDetails(id)
        }
    }

    fun saveAndStartTraining(textItem: TextItem, onTrainingCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val userId = userPreferencesRepository.currentUserId.first() ?: return@launch
            
            val newText = TextEntity(
                userId = userId,
                title = "${textItem.author} - ${textItem.title}",
                content = textItem.content,
                progress = "0",
                lastTrainedAt = System.currentTimeMillis()
            )
            
            val newId = textRepository.saveText(newText)
            onTrainingCreated(newId)
        }
    }
}
