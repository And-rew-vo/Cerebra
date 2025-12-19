package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.data.repository.UserPreferencesRepository
import com.cerebra.app.domain.model.TextItem
import com.cerebra.app.domain.repository.PoetryRepository
import com.cerebra.app.domain.repository.TextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: TextRepository,
    private val poetryRepository: PoetryRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _texts = MutableStateFlow<List<TextEntity>>(emptyList())
    val texts: StateFlow<List<TextEntity>> = _texts

    private val _commonTexts = MutableStateFlow<List<TextItem>>(emptyList())
    val commonTexts: StateFlow<List<TextItem>> = _commonTexts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        viewModelScope.launch {
            userPreferencesRepository.currentUserId.collect { userId ->
                userId?.let {
                    repository.getAllTexts(it).collect { list ->
                        _texts.value = list
                    }
                }
            }
        }
        refreshCommonTexts()
    }

    fun refreshCommonTexts() {
        viewModelScope.launch {
            _isLoading.value = true
            val query = _searchQuery.value
            _commonTexts.value = if (query.isBlank()) {
                poetryRepository.getCommonPoems()
            } else {
                poetryRepository.searchPoems(query)
            }
            _isLoading.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        refreshCommonTexts()
    }
}
