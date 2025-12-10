package com.cerebra.app.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.UserEntity
import com.cerebra.app.data.repository.UserPreferencesRepository
import com.cerebra.app.domain.repository.AuthRepository
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
    val user: UserEntity? = null,
    // Validation Errors
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.currentUserId.collect { userId ->
                if (userId != null) {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                    try {
                        val user = authRepository.getUserById(userId)
                        if (user != null) {
                            _uiState.value = _uiState.value.copy(
                                isLoggedIn = true, 
                                isLoading = false,
                                user = user
                            )
                        } else {
                            // User ID exists but user not found in DB? Inconsistency.
                            // Maybe logout or just stop loading.
                            _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = false)
                        }
                    } catch (e: Exception) {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                    }
                } else {
                     // Ensure state is reset if ID is null (logout)
                     _uiState.value = AuthUiState() 
                }
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        if (!validateRegistration(name, email, password)) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val newUser = UserEntity(name = name, email = email, password = password)
                // Check if user exists happens in Repo/DAO (constraint) usually
                authRepository.registerUser(newUser)
                login(email, password)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Ошибка регистрации: ${e.message}"
                )
            }
        }
    }

    fun login(email: String, password: String) {
        if (!validateLogin(email, password)) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val user = authRepository.loginUser(email)
                if (user != null && user.password == password) {
                    userPreferencesRepository.saveUserId(user.id)
                    _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true, user = user)
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Неверный email или пароль")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Ошибка входа: ${e.message}")
            }
        }
    }

    private fun validateLogin(email: String, password: String): Boolean {
        var isValid = true
        var emailError: String? = null
        var passwordError: String? = null

        if (email.isBlank()) {
            emailError = "Введите email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Неверный формат почты"
            isValid = false
        }

        if (password.isBlank()) {
            passwordError = "Введите пароль"
            isValid = false
        }

        _uiState.value = _uiState.value.copy(emailError = emailError, passwordError = passwordError)
        return isValid
    }

    private fun validateRegistration(name: String, email: String, password: String): Boolean {
        var isValid = true
        var nameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null

        if (name.isBlank()) {
            nameError = "Введите имя"
            isValid = false
        }

        if (email.isBlank()) {
            emailError = "Введите email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Неверный формат почты"
            isValid = false
        }

        if (password.length < 6) {
            passwordError = "Пароль должен быть не менее 6 символов"
            isValid = false
        }

        _uiState.value = _uiState.value.copy(
            nameError = nameError,
            emailError = emailError,
            passwordError = passwordError
        )
        return isValid
    }

    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.clearUserId()
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null, nameError = null, emailError = null, passwordError = null)
    }
}
