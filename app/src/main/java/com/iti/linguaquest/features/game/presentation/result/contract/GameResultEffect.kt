package com.iti.linguaquest.features.game.presentation.result.contract

sealed interface GameResultEffect {
    data object NavigateToCamera : GameResultEffect
    data class ApplyHintAndRetry(val hint: String) : GameResultEffect
    data object NavigateToNextLevel : GameResultEffect
    object NavigateToExit : GameResultEffect
}