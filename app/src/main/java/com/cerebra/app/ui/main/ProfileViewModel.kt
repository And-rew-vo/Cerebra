package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.UserEntity
import com.cerebra.app.data.repository.UserPreferencesRepository
import com.cerebra.app.domain.repository.TextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: TextRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            userPreferencesRepository.isDarkMode.collect { isDark ->
                _uiState.value = _uiState.value.copy(isDarkMode = isDark)
            }
        }
    }

    fun loadProfile(user: UserEntity) {
        viewModelScope.launch {
            launch {
                repository.getTextCount(user.id).collect { count ->
                    _uiState.value = _uiState.value.copy(textCount = count)
                }
            }
            _uiState.value = _uiState.value.copy(user = user)
        }
    }
    
    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDarkMode(isDark)
        }
    }
}
