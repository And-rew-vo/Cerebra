package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.User
import com.cerebra.app.data.repository.CerebraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: CerebraRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile(user: User) {
        viewModelScope.launch {
            launch {
                repository.getTextCount(user.id).collect { count ->
                    _uiState.value = _uiState.value.copy(textCount = count)
                }
            }
            launch {
                repository.getAverageProgress(user.id).collect { avg ->
                    _uiState.value = _uiState.value.copy(averageProgress = avg ?: 0f)
                }
            }
            _uiState.value = _uiState.value.copy(user = user)
        }
    }
}
