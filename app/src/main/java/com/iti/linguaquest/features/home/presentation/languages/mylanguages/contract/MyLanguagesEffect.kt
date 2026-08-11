package com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface MyLanguagesEffect {
    data object NavigateToAddLanguages : MyLanguagesEffect
    data object DismissSheet : MyLanguagesEffect
    data object SwitchingLanguage : MyLanguagesEffect
    data class LanguageSwitchFailed(val error: UiText) : MyLanguagesEffect
}
