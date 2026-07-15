package com.iti.linguaquest.features.onBoarding.contract

sealed interface LevelIntent {
    data class SelectLevel(val level: ProficiencyLevel) : LevelIntent
    data object ContinueClicked : LevelIntent
}