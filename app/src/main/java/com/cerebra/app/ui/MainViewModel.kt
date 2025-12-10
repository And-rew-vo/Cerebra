package com.cerebra.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.cerebra.app.ui.navigation.Screen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<MainUiState> = combine(
        userPreferencesRepository.isDarkMode,
        userPreferencesRepository.currentUserId
    ) { isDark, userId ->
        MainUiState(
            isLoading = false,
            isDarkMode = isDark,
            startDestination = if (userId != null) Screen.Home.route else Screen.Welcome.route
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState(isLoading = true)
    )
}

data class MainUiState(
    val isLoading: Boolean,
    val isDarkMode: Boolean = false,
    val startDestination: String = Screen.Welcome.route
)
