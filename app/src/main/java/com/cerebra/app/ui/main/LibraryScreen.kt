package com.cerebra.app.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
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
    onNavigateToTraining: (Int) -> Unit,
    onNavigateToAddText: () -> Unit,
    onNavigateToCommonText: (String) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val texts by viewModel.texts.collectAsState()
    val commonTexts by viewModel.commonTexts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddText) {
                Icon(Icons.Default.Add, contentDescription = "Добавить текст")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
        ) {
            Text("Библиотека", style = MaterialTheme.typography.headlineMedium)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {

                if (texts.isNotEmpty()) {
                    item {
                        Text(
                            "Мои тексты",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(texts) { text ->
                        TextItemCard(
                            text = text,
                            onClick = { onNavigateToTraining(text.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                } else if (searchQuery.isEmpty()) {
                     item {
                        Text("У вас пока нет своих текстов.", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }


                item {
                    Column {
                        Text(
                            "Общая библиотека",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                placeholder = { Text("Поиск...") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { viewModel.refreshCommonTexts() }) {
                                if (isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                } else {
                                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                                }
                            }
                        }
                    }
                }


                if (commonTexts.isEmpty() && !isLoading) {
                    item {
                        Text("Ничего не найдено.", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                items(commonTexts) { item ->
                    CommonTextCard(textItem = item, onClick = {
                         onNavigateToCommonText(item.id)
                    })
                }
            }
        }
    }
}
