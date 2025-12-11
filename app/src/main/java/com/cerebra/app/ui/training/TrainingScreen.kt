package com.cerebra.app.ui.training

import androidx.compose.animation.*
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
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
                },
                actions = {
                    IconButton(onClick = { viewModel.restartTraining() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Начать заново")
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
                        initialTitle = uiState.textEntity?.title ?: "",
                        initialContent = uiState.textEntity?.content ?: "",
                        onDifficultyChange = viewModel::setDifficulty,
                        onStart = { title, content -> viewModel.startTraining(uiState.difficulty, title, content) }
                    )
                    TrainingPhase.TRAINING -> TrainingView(
                        uiState = uiState,
                        onInputChange = viewModel::onInputChange,
                        onNextChunk = viewModel::nextChunk,
                        onRevealHint = viewModel::revealHint
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
    initialTitle: String,
    initialContent: String,
    onDifficultyChange: (Difficulty) -> Unit,
    onStart: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var content by remember { mutableStateOf(initialContent) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Настройки тренировки", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Текст") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp, max = 300.dp),
            minLines = 5,
            maxLines = 15
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        val sliderValue = when(difficulty) {
            Difficulty.LOW -> 0f
            Difficulty.MEDIUM -> 1f
            Difficulty.HIGH -> 2f
        }

        Slider(
            value = sliderValue,
            onValueChange = { 
                val diff = when(it.roundToInt()) {
                    0 -> Difficulty.LOW
                    1 -> Difficulty.MEDIUM
                    else -> Difficulty.HIGH
                }
                onDifficultyChange(diff)
            },
            valueRange = 0f..2f,
            steps = 1
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = when(difficulty) {
                Difficulty.LOW -> "Сложность: Низкая\nКороткие предложения, скрыто 1-2 слова."
                Difficulty.MEDIUM -> "Сложность: Средняя\nКороткие предложения, скрыто ~25% слов."
                Difficulty.HIGH -> "Сложность: Высокая\nДлинные абзацы, скрыто ~50% слов."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { onStart(title, content) }, 
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && content.isNotBlank()
        ) {
            Text("Начать")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrainingView(
    uiState: TrainingUiState,
    onInputChange: (Int, String) -> Unit,
    onNextChunk: () -> Unit,
    onRevealHint: (Int) -> Unit
) {
    val chunk = uiState.currentChunk ?: return
    val focusManager = LocalFocusManager.current
    
    
    val hiddenTokens = chunk.tokens.filter { it.isHidden }
    val focusRequesters = remember(chunk.id) { 
        hiddenTokens.associate { it.index to FocusRequester() } 
    }

    LaunchedEffect(uiState.validationStatus) {
        val firstInvalid = hiddenTokens.firstOrNull { token ->
            uiState.validationStatus[token.index] != true
        }
        
        if (firstInvalid != null) {
        }
    }
    
    
    AnimatedContent(
        targetState = chunk,
        transitionSpec = {
            (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                slideOutHorizontally { width -> -width } + fadeOut())
        },
        label = "ChunkAnimation"
    ) { currentChunk ->
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
                currentChunk.tokens.forEach { token ->
                    if (token.isHidden) {
                        val isCorrect = uiState.validationStatus[token.index] == true
                        val value = uiState.userInputs[token.index] ?: ""
                        val width = (token.originalWord.length.coerceAtLeast(2) * 14).dp

                        val focusRequester = focusRequesters[token.index] ?: FocusRequester()

                        Box {
                            Row(verticalAlignment = Alignment.CenterVertically) {
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
                                
                                if (!isCorrect) {
                                     IconButton(
                                         onClick = { onRevealHint(token.index) },
                                         modifier = Modifier.size(24.dp)
                                     ) {
                                         Icon(
                                             imageVector = Icons.Default.Info, 
                                             contentDescription = "Подсказка",
                                             tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                         )
                                     }
                                }
                            }

                            if (uiState.activeHintTokenIndex == token.index) {
                                Popup(
                                    alignment = Alignment.TopCenter,
                                    onDismissRequest = { onRevealHint(token.index) }
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shadowElevation = 4.dp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    ) {
                                        Text(
                                            text = token.originalWord,
                                            modifier = Modifier.padding(8.dp),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                        
                        LaunchedEffect(isCorrect) {
                            if (isCorrect) {
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
            
            
            val allCorrect = hiddenTokens.isNotEmpty() && hiddenTokens.all { uiState.validationStatus[it.index] == true }
            
            LaunchedEffect(allCorrect) {
                if (allCorrect) {
                    delay(500)
                    onNextChunk()
                }
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
