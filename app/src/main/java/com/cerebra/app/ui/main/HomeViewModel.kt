package com.cerebra.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebra.app.data.local.entity.TextDocument
import com.cerebra.app.data.repository.CerebraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CerebraRepository
) : ViewModel() {
    
    private val _unfinishedTexts = MutableStateFlow<List<TextDocument>>(emptyList())
    val unfinishedTexts: StateFlow<List<TextDocument>> = _unfinishedTexts

    fun loadUnfinishedTexts(userId: Int) {
        viewModelScope.launch {
            repository.getUnfinishedTexts(userId).collect {
                _unfinishedTexts.value = it
            }
        }
    }
}
