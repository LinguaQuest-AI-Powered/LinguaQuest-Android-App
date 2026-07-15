package com.iti.linguaquest.features.onBoarding.contract

sealed interface LanguagesEffect {
    data object NavigateToLevelScreen : LanguagesEffect
}