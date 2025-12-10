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
        if (userId != null) {
            MainUiState.Authenticated(userId, isDark)
        } else {
            MainUiState.Unauthenticated(isDark)
        }
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainUiState.Loading
    )
}

sealed interface MainUiState {
    val isDarkMode: Boolean

    data object Loading : MainUiState {
        override val isDarkMode: Boolean = false
    }
    data class Authenticated(val userId: Int, override val isDarkMode: Boolean) : MainUiState
    data class Unauthenticated(override val isDarkMode: Boolean) : MainUiState
}
