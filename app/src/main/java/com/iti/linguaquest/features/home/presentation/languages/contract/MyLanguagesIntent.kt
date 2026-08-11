package com.iti.linguaquest.features.home.presentation.languages.contract

sealed interface MyLanguagesIntent {
    data class RequestSetActiveLanguage(val language: MyLanguageUiModel) : MyLanguagesIntent
    data object ConfirmSetActiveLanguage : MyLanguagesIntent
    data object DismissSetActiveDialog : MyLanguagesIntent
    data class RequestRemoveLanguage(val language: MyLanguageUiModel) : MyLanguagesIntent
    data object ConfirmRemoveLanguage : MyLanguagesIntent
    data object DismissRemoveDialog : MyLanguagesIntent
    data object AddNewLanguageClicked : MyLanguagesIntent
    data object ToggleEditMode : MyLanguagesIntent
    data object Dismiss : MyLanguagesIntent
}
