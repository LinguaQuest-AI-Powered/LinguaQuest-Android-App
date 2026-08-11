package com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract

data class MyLanguageUiModel(
    val id: Int,
    val name: String,
    val flagEmoji: String,
    val level: Int,
    val isCurrent: Boolean = false
)
