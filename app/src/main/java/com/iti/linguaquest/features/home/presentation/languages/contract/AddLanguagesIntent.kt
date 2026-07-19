package com.iti.linguaquest.features.home.presentation.languages.contract

sealed interface AddLanguagesIntent {
    data object BackClicked : AddLanguagesIntent
    data class SearchQueryChanged(val query: String) : AddLanguagesIntent
    data class LanguageToggled(val languageId: Int) : AddLanguagesIntent
    data object AddSelectedClicked : AddLanguagesIntent
}