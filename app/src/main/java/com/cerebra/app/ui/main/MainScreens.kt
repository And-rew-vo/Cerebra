package com.cerebra.app.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cerebra.app.data.local.entity.TextDocument
import com.cerebra.app.data.local.entity.User

@Composable
fun HomeScreen(
    user: User,
    onNavigateToTraining: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val texts by viewModel.unfinishedTexts.collectAsState()
    
    LaunchedEffect(user.id) {
        viewModel.loadUnfinishedTexts(user.id)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Главная", style = MaterialTheme.typography.headlineMedium)
        Text("Продолжить обучение", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(16.dp))
        
        if (texts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Нет активных тренировок")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(texts) { text ->
                    TextItemCard(text, onClick = { onNavigateToTraining(text.id) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    user: User,
    onNavigateToTraining: (Int) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val texts by viewModel.texts.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(user.id) {
        viewModel.loadTexts(user.id)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Text")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Библиотека", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            
            if (texts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Библиотека пуста. Добавьте текст!")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(texts) { text ->
                       TextItemCard(text, onClick = { onNavigateToTraining(text.id) })
                    }
                }
            }
        }
    }
    
    if (showDialog) {
        AddTextDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, content ->
                viewModel.addText(user.id, title, content)
                showDialog = false
            }
        )
    }
}

@Composable
fun TextItemCard(text: TextDocument, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = text.progress / 100f,
                modifier = Modifier.fillMaxWidth()
            )
            Text("${text.progress}%", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.End))
        }
    }
}

@Composable
fun AddTextDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить текст") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Текст") },
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    maxLines = 10
                )
            }
        },
        confirmButton = {
            Button(onClick = { 
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    onConfirm(title, content) 
                }
            }) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun ProfileScreen(
    user: User,
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
        
        Text(uiState.user?.name ?: "User", style = MaterialTheme.typography.headlineSmall)
        Text(uiState.user?.email ?: "", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Статистика", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Всего текстов:")
                    Text("${uiState.textCount}")
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Средний прогресс:")
                    Text("${uiState.averageProgress.toInt()}%")
                }
            }
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
