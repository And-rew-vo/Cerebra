package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.data.repository.UserPreferencesRepository
import com.cerebra.app.domain.repository.TextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TextRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val unfinishedTexts: StateFlow<List<TextEntity>> = userPreferencesRepository.currentUserId
        .flatMapLatest { userId ->
            if (userId != null) {
                repository.getAllTexts(userId)
            } else {
                flowOf(emptyList())
            }
        }
        .map { list ->
            list.filter { text ->
                val percent = try {
                    if (text.progress.startsWith("{")) {
                        JSONObject(text.progress).optInt("percent", 0)
                    } else {
                        text.progress.toIntOrNull() ?: 0
                    }
                } catch (e: Exception) { 0 }
                percent < 100
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
