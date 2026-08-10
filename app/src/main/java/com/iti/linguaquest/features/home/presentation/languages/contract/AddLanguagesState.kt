package com.iti.linguaquest.features.home.presentation.languages.contract

import com.iti.linguaquest.core.sharedComponents.state.DataStatus

data class AddLanguagesState(
    val dataStatus: DataStatus = DataStatus.Loading,
    val isAdding: Boolean = false,
    val searchQuery: String = "",
    val availableLanguages: List<LanguageUiItem> = emptyList(),
    val selectedLanguageIds: Set<Int> = emptySet(),
    val isRemoving: Boolean = false,
    val languagePendingRemoval: LanguageUiItem? = null
) {
    val hasData: Boolean get() = availableLanguages.isNotEmpty()
}

data class LanguageUiItem(
    val id: Int,
    val name: String,
    val flagEmoji: String,
    val isAdded: Boolean = false
)