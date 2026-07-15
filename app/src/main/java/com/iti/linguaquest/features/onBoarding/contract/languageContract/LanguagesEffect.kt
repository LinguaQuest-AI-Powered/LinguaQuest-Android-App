package com.iti.linguaquest.features.onBoarding.contract.languageContract

sealed interface LanguagesEffect {
    data object NavigateToLevelScreen : LanguagesEffect
}