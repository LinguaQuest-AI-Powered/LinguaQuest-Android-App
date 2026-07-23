package com.iti.linguaquest.features.home.presentation.languages.contract

sealed interface MyLanguagesIntent {
    data object LoadMyLanguages : MyLanguagesIntent
    data class SetActiveLanguage(val languageId: Int) : MyLanguagesIntent
    data object AddNewLanguageClicked : MyLanguagesIntent
    data object Dismiss : MyLanguagesIntent
}
