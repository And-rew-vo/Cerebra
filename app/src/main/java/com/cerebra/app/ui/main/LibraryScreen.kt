package com.cerebra.app.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cerebra.app.data.local.entity.UserEntity
import com.cerebra.app.ui.navigation.Screen

@Composable
fun LibraryScreen(
    // user removed as it was unused
    onNavigateToTraining: (Int) -> Unit,
    onNavigateToAddText: () -> Unit,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val texts by viewModel.texts.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddText) {
                Icon(Icons.Default.Add, contentDescription = "Добавить текст")
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
}
