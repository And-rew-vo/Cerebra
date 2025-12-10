package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.domain.repository.TextRepository
import com.cerebra.app.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTextViewModel @Inject constructor(
    private val textRepository: TextRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    fun saveText(title: String, content: String, onSuccess: () -> Unit) {
        if (title.isBlank() || content.isBlank()) return // Simple validation

        viewModelScope.launch {
            val userId = userPreferencesRepository.currentUserId.first()
            if (userId != null) {
                val text = TextEntity(
                    userId = userId,
                    title = title,
                    content = content,
                    progress = "", // Initial state
                    lastTrainedAt = System.currentTimeMillis()
                )
                textRepository.saveText(text)
                onSuccess()
            }
        }
    }
}
