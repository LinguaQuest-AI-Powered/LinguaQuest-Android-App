package com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract

sealed interface LanguagesEffect {
    data object NavigateToLevelScreen : LanguagesEffect
}