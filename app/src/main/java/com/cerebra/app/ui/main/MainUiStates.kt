package com.cerebra.app.ui.main

import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.data.local.entity.UserEntity

data class HomeUiState(
    val unfinishedTexts: List<TextEntity> = emptyList(),
    val isLoading: Boolean = false
)

data class LibraryUiState(
    val allTexts: List<TextEntity> = emptyList(),
    val isLoading: Boolean = false
)

data class ProfileUiState(
    val user: UserEntity? = null,
    val textCount: Int = 0,
    val isDarkMode: Boolean = false
)
