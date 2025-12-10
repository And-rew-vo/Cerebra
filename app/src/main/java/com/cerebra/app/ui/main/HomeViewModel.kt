package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.data.repository.UserPreferencesRepository
import com.cerebra.app.domain.repository.TextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TextRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    private val _unfinishedTexts = MutableStateFlow<List<TextEntity>>(emptyList())
    val unfinishedTexts: StateFlow<List<TextEntity>> = _unfinishedTexts

    init {
        viewModelScope.launch {
             userPreferencesRepository.currentUserId.collect { userId ->
                userId?.let {
                    repository.getAllTexts(it).collect { list ->
                        _unfinishedTexts.value = list.filter { text ->
                            // Simple filter: Check if percent < 100
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
                }
            }
        }
    }
}
