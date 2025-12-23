package com.cerebra.app.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cerebra.app.data.local.entity.UserEntity

@Composable
fun HomeScreen(
    user: UserEntity,
    onNavigateToTraining: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val texts by viewModel.unfinishedTexts.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Главная", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Привет, ${user.name}!", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Продолжить обучение", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(16.dp))
        
        if (texts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text("Нет активных тренировок")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(texts) { text ->
                    TextItemCard(
                        text = text,
                        onClick = { onNavigateToTraining(text.id) }
                    )
                }
            }
        }
    }
}
