package com.iti.linguaquest.features.onBoarding.contract

sealed interface LevelEffect {
    data object NavigateToHome : LevelEffect
}