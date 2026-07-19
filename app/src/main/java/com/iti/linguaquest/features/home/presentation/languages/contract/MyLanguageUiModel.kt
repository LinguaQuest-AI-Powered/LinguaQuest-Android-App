package com.iti.linguaquest.features.home.presentation.languages.contract

data class MyLanguageUiModel(
    val id: Int,
    val name: String,
    val level: Int,
    val isCurrent: Boolean,
    val flagEmoji: String
)