package com.iti.linguaquest.features.onBoarding.contract.languageContract

sealed interface LanguagesIntent {
    data class SelectNativeLanguage(val language: LanguageOption) : LanguagesIntent
    data class SelectTargetLanguage(val language: LanguageOption) : LanguagesIntent
    data object ToggleNativeDropdown : LanguagesIntent
    data object ToggleTargetDropdown : LanguagesIntent
    data object ContinueClicked : LanguagesIntent
}