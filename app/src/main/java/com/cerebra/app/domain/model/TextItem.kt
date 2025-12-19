package com.cerebra.app.domain.model

data class TextItem(
    val id: String,
    val title: String,
    val author: String,
    val content: String,
    val isLocal: Boolean = false
)
