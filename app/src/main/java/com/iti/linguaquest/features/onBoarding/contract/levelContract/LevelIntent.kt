package com.iti.linguaquest.features.onBoarding.contract.levelContract

sealed interface LevelIntent {
    data class SelectLevel(val level: ProficiencyLevel) : LevelIntent
    data object ContinueClicked : LevelIntent
}