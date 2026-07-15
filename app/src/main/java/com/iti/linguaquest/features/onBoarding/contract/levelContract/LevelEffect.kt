package com.iti.linguaquest.features.onBoarding.contract.levelContract

sealed interface LevelEffect {
    data object NavigateToHome : LevelEffect
}