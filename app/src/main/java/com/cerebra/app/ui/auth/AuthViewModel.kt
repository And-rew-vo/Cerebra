package com.cerebra.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.User
import com.cerebra.app.data.repository.CerebraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val user: User? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: CerebraRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Check if user exists logic could go here, but Repository/DAO should handle errors ideally
                // For MVP, just try insert
                val newUser = User(name = name, email = email, passwordHash = password) // In real app, hash this!
                repository.registerUser(newUser)
                login(email, password) // Auto login
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Ошибка регистрации")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val user = repository.loginUser(email)
                if (user != null && user.passwordHash == password) {
                    _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true, user = user)
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Неверный email или пароль")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Ошибка входа")
            }
        }
    }
    
    fun logout() {
        _uiState.value = AuthUiState() // Reset
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
