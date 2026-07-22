package com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract

import com.iti.linguaquest.features.home.domain.model.LanguageOption

sealed interface LanguagesIntent {
    data class SelectNativeLanguage(val language: LanguageOption) : LanguagesIntent
    data class SelectTargetLanguage(val language: LanguageOption) : LanguagesIntent
    data object ToggleNativeDropdown : LanguagesIntent
    data object ToggleTargetDropdown : LanguagesIntent
    data object ContinueClicked : LanguagesIntent
}