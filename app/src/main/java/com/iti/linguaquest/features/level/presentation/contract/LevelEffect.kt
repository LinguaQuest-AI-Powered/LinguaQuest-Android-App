package com.iti.linguaquest.features.level.presentation.contract

sealed interface LevelEffect {
    data object NavigateBack : LevelEffect
    data object LaunchCamera : LevelEffect
    data object SkipLevel : LevelEffect
    data object PlaySound : LevelEffect
}
