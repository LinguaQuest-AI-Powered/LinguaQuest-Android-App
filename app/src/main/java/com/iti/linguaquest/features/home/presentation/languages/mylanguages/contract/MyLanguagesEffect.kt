package com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract

sealed interface MyLanguagesEffect {
    data object NavigateToAddLanguages : MyLanguagesEffect
    data object Dismiss : MyLanguagesEffect
}
