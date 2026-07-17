package com.iti.linguaquest.features.level.presentation.contract

sealed interface LevelEffect {
    data object NavigateBack : LevelEffect
    data object LaunchCamera : LevelEffect
    data class PlaySound(val word: String, val languageCode: String) : LevelEffect
    data object SkipLevel : LevelEffect
}
