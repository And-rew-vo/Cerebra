package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.data.repository.UserPreferencesRepository
import com.cerebra.app.domain.repository.TextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: TextRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _texts = MutableStateFlow<List<TextEntity>>(emptyList())
    val texts: StateFlow<List<TextEntity>> = _texts
    
    init {
        // Auto load for current user
        viewModelScope.launch {
            userPreferencesRepository.currentUserId.collect { userId ->
                userId?.let {
                    repository.getAllTexts(it).collect { list ->
                        _texts.value = list
                    }
                }
            }
        }
    }
}
