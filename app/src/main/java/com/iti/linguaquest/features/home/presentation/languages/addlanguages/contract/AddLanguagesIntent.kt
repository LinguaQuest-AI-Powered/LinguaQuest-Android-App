package com.iti.linguaquest.features.home.presentation.languages.addlanguages.contract

sealed interface AddLanguagesIntent {
    data object BackClicked : AddLanguagesIntent
    data class SearchQueryChanged(val query: String) : AddLanguagesIntent
    data class LanguageToggled(val languageId: Int) : AddLanguagesIntent
    data class RequestRemoveLanguage(val language: LanguageUiItem) : AddLanguagesIntent
    data object ConfirmRemoveLanguage : AddLanguagesIntent
    data object DismissRemoveDialog : AddLanguagesIntent
    data object AddSelectedClicked : AddLanguagesIntent
}
