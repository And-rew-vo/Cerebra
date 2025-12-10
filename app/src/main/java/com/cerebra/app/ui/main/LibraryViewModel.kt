package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextDocument
import com.cerebra.app.data.repository.CerebraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: CerebraRepository
) : ViewModel() {

    // For MVP, we assume a single user for now or handled by Auth State.
    // Ideally we pass userId. Let's assume userId = 1 or passed via init if we had a SessionManager.
    // For this MVP, I'll pass userId to methods or assume 0/1 depending on Auth implementation.
    // Let's rely on the Screen passing the userId or AuthViewModel sharing it.
    // To keep it simple, I'll modify repository to NOT require userId if I just query all, 
    // BUT the requirement says "User" based. 
    // I will add a `currentUserId` setter or flow.
    
    private val _currentUserId = MutableStateFlow<Int?>(null)
    
    val uiState: StateFlow<LibraryUiState> = _currentUserId
        .map { userId ->
             if (userId != null) {
                 LibraryUiState(isLoading = true) // Placeholder, real flow below
             } else {
                 LibraryUiState()
             }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LibraryUiState(isLoading = true))

    // Actually, distinct flows are better.
    private val _texts = MutableStateFlow<List<TextDocument>>(emptyList())
    val texts: StateFlow<List<TextDocument>> = _texts
    
    fun loadTexts(userId: Int) {
        viewModelScope.launch {
            repository.getAllTexts(userId).collect {
                _texts.value = it
            }
        }
    }

    fun addText(userId: Int, title: String, content: String) {
        viewModelScope.launch {
            val document = TextDocument(
                userId = userId,
                title = title,
                content = content,
                progress = 0,
                lastTrainedAt = System.currentTimeMillis()
            )
            repository.saveTextDocument(document)
            // Flow will auto-update
        }
    }
}
