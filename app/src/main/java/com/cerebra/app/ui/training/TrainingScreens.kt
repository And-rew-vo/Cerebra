package com.cerebra.app.ui.training

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cerebra.app.ui.theme.CorrectGreen
import com.cerebra.app.ui.theme.ErrorRed
import com.cerebra.app.ui.theme.NeutralVariant

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    onNavigateBack: () -> Unit,
    viewModel: TrainingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveProgress()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.text?.title ?: "Тренировка") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.saveProgress()
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Text("Progress: ${uiState.progress}%", modifier = Modifier.padding(end = 16.dp))
                }
            )
        },
        floatingActionButton = {
            if (uiState.isComplete) {
                FloatingActionButton(onClick = {
                    viewModel.saveProgress()
                    onNavigateBack()
                }, containerColor = CorrectGreen) {
                    Icon(Icons.Default.Check, contentDescription = "Finish")
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.tokens.forEach { token ->
                        if (token.isHidden) {
                            HiddenWordInput(
                                userValue = uiState.userInputs[token.index] ?: "",
                                isCorrect = uiState.validationStatus[token.index],
                                onValueChange = { viewModel.onInputChange(token.index, it) }
                            )
                        } else {
                            Text(
                                text = token.displayValue,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HiddenWordInput(
    userValue: String,
    isCorrect: Boolean?,
    onValueChange: (String) -> Unit
) {
    val backgroundColor = when (isCorrect) {
        true -> CorrectGreen.copy(alpha = 0.3f)
        false -> ErrorRed.copy(alpha = 0.3f)
        else -> NeutralVariant
    }
    
    val borderColor = when (isCorrect) {
        true -> CorrectGreen
        false -> ErrorRed
        else -> Color.Gray
    }

    // Dynamic width based on input length or fixed min width?
    // Let's use basic wrapper
    BasicTextFieldWrapper(
        value = userValue,
        onValueChange = onValueChange,
        backgroundColor = backgroundColor,
        borderColor = borderColor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTextFieldWrapper(
    value: String,
    onValueChange: (String) -> Unit,
    backgroundColor: Color,
    borderColor: Color
) {
    // Custom small text field
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.width(100.dp), // Fixed width for simplicity in MVP, could be dynamic
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor
        ),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge
    )
}
