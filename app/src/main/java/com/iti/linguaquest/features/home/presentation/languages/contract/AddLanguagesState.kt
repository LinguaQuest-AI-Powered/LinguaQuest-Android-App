package com.iti.linguaquest.features.home.presentation.languages.contract

data class AddLanguagesState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val availableLanguages: List<LanguageUiItem> = emptyList(),
    val selectedLanguageIds: Set<Int> = emptySet()
)

data class LanguageUiItem(
    val id: Int,
    val name: String,
    val flagEmoji: String
)