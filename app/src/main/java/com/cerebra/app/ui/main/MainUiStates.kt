package com.cerebra.app.ui.main

import com.cerebra.app.data.local.entity.TextDocument
import com.cerebra.app.data.local.entity.User

data class HomeUiState(
    val unfinishedTexts: List<TextDocument> = emptyList(),
    val isLoading: Boolean = false
)

data class LibraryUiState(
    val allTexts: List<TextDocument> = emptyList(),
    val isLoading: Boolean = false
)

data class ProfileUiState(
    val user: User? = null,
    val textCount: Int = 0,
    val averageProgress: Float = 0f
)
