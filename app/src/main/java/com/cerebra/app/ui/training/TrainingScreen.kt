package com.cerebra.app.ui.training

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cerebra.app.domain.Difficulty
import com.cerebra.app.domain.ProcessedToken

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    onNavigateBack: () -> Unit,
    viewModel: TrainingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Тренировка") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                when (uiState.phase) {
                    TrainingPhase.SETUP -> SetupView(
                        difficulty = uiState.difficulty,
                        onDifficultyChange = viewModel::setDifficulty,
                        onStart = { viewModel.startTraining(uiState.difficulty) }
                    )
                    TrainingPhase.TRAINING -> TrainingView(
                        uiState = uiState,
                        onInputChange = viewModel::onInputChange,
                        onNextChunk = viewModel::nextChunk
                    )
                    TrainingPhase.COMPLETED -> CompletedView(onNavigateBack)
                }
            }
        }
    }
}

@Composable
fun SetupView(
    difficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
    onStart: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Настройки тренировки", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Сложность: ${if (difficulty == Difficulty.LOW) "Низкая" else "Высокая"}")
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Низкая")
            Switch(
                checked = difficulty == Difficulty.HIGH,
                onCheckedChange = { isHigh ->
                    onDifficultyChange(if (isHigh) Difficulty.HIGH else Difficulty.LOW) 
                }
            )
            Text("Высокая")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (difficulty == Difficulty.LOW) 
                "Короткие части, скрыто 1-2 слова." 
            else 
                "Длинные части, скрыто ~50% слов.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(48.dp))
        
        Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) {
            Text("Начать")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrainingView(
    uiState: TrainingUiState,
    onInputChange: (Int, String) -> Unit,
    onNextChunk: () -> Unit
) {
    val chunk = uiState.currentChunk ?: return
    val focusManager = LocalFocusManager.current
    
    // Manage FocusRequesters for hidden tokens
    val hiddenTokens = chunk.tokens.filter { it.isHidden }
    // Map index to FocusRequester
    val focusRequesters = remember(chunk.id) { 
        hiddenTokens.associate { it.index to FocusRequester() } 
    }

    // Auto-focus next logic
    LaunchedEffect(uiState.validationStatus) {
        val firstInvalid = hiddenTokens.firstOrNull { token ->
            uiState.validationStatus[token.index] != true
        }
        
        if (firstInvalid != null) {
            // Only focus if the PREVIOUS one was just corrected or it's initial?
            // To avoid stealing focus while typing?
            // Actually, if I am typing in token A, and I finish it correctly, validation updates. 
            // Then I want to jump to token B.
            // Check if user input for firstInvalid is empty?
            // This is tricky. Let's just focus if it's not focused?
            // Better: FocusRequester requestFocus() is safe to call.
            // But we need to know IF we should jump.
            // Simple logic: Always focus the first invalid token IF the previous valid token was just completed?
            // Let's rely on ImeActions "Next" manually or simple AutoFocus when validation passes.
        }
    }
    
    // Better Auto-focus:
    // When input changes and becomes valid, we find the NEXT hidden token and focus it.
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Часть ${uiState.currentChunkIndex + 1} из ${uiState.chunks.size}", 
            style = MaterialTheme.typography.labelLarge
        )
        LinearProgressIndicator(
            progress = (uiState.currentChunkIndex + 1) / uiState.chunks.size.toFloat(),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chunk.tokens.forEach { token ->
                if (token.isHidden) {
                    val isCorrect = uiState.validationStatus[token.index] == true
                    val value = uiState.userInputs[token.index] ?: ""
                    val width = (token.originalWord.length.coerceAtLeast(2) * 14).dp // Approximate width

                    val focusRequester = focusRequesters[token.index] ?: FocusRequester()

                    // Auto-focus trigger: if this token just became valid, try focus next.
                    // This creates a chain reaction.
                    // But we can't do it inside the loop.
                    
                    BasicTextField(
                        value = value,
                        onValueChange = { 
                            if (!isCorrect) onInputChange(token.index, it) 
                        },
                        modifier = Modifier
                            .width(width)
                            .padding(horizontal = 4.dp)
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            color = if (isCorrect) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                innerTextField()
                                Divider(
                                    color = if (isCorrect) Color(0xFF4CAF50) else if (value.isNotEmpty()) Color.Red else MaterialTheme.colorScheme.onSurface,
                                    thickness = 1.dp
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { 
                                focusManager.moveFocus(FocusDirection.Next) 
                            }
                        ),
                        enabled = !isCorrect
                    )
                    
                    // Trigger focus move if correct
                    LaunchedEffect(isCorrect) {
                        if (isCorrect) {
                            // Find next hidden token index > token.index
                            val nextToken = hiddenTokens.firstOrNull { it.index > token.index }
                            if (nextToken != null) {
                                focusRequesters[nextToken.index]?.requestFocus()
                            } else {
                                focusManager.clearFocus()
                            }
                        }
                    }

                } else {
                    Text(
                        text = token.originalWord,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Show Next button if all correct
        val allCorrect = hiddenTokens.all { uiState.validationStatus[it.index] == true }
        if (allCorrect) {
            Button(
                onClick = onNextChunk,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.currentChunkIndex < uiState.chunks.size - 1) "Следующая часть" else "Завершить")
            }
        }
    }
}

@Composable
fun CompletedView(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Поздравляем!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Вы успешно изучили этот текст.", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNavigateBack) {
            Text("Вернуться в библиотеку")
        }
    }
}
