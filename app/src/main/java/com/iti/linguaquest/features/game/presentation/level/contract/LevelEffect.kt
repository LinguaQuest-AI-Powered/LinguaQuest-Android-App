package com.iti.linguaquest.features.game.presentation.level.contract

sealed interface LevelEffect {
    data object NavigateBack : LevelEffect
    data object LaunchCamera : LevelEffect
    data class PlaySound(val word: String, val languageCode: String) : LevelEffect
    data class HintRetrieved(val hint: String) : LevelEffect
    data object SkipLevel : LevelEffect
}
