package com.cerebra.app.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cerebra.app.data.local.entity.UserEntity

@Composable
fun ProfileScreen(
    user: UserEntity,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(user) {
        viewModel.loadProfile(user)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Профиль", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(uiState.user?.name ?: user.name, style = MaterialTheme.typography.headlineSmall)
        Text(uiState.user?.email ?: user.email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Статистика", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Всего текстов:")
                    Text("${uiState.textCount}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(), 
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Темная тема")
            Switch(
                checked = uiState.isDarkMode,
                onCheckedChange = { viewModel.toggleTheme(it) }
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выйти")
        }
    }
}
