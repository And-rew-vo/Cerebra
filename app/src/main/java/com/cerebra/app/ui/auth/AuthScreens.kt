package com.cerebra.app.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cerebra", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Запоминайте тексты эффективно.", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Регистрация")
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Вход", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                viewModel.clearError() 
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.emailError != null,
            supportingText = {
                if (uiState.emailError != null) {
                    Text(uiState.emailError!!)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                viewModel.clearError()
            },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.passwordError != null,
            supportingText = {
                 if (uiState.passwordError != null) {
                    Text(uiState.passwordError!!)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.error != null) {
            Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = { viewModel.login(email, password) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Войти")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onNavigateToRegister) {
            Text("Нет аккаунта? Зарегистрироваться")
        }
    }
}

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { 
                name = it
                viewModel.clearError()
            },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.nameError != null,
            supportingText = { uiState.nameError?.let { Text(it) } }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                viewModel.clearError()
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.emailError != null,
            supportingText = { uiState.emailError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                viewModel.clearError()
            },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.passwordError != null,
            supportingText = { uiState.passwordError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.error != null) {
            Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = { viewModel.register(name, email, password) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Зарегистрироваться")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onNavigateToLogin) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}
