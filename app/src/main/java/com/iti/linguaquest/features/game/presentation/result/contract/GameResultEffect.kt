package com.iti.linguaquest.features.game.presentation.result.contract

sealed interface GameResultEffect {
    object NavigateToCamera : GameResultEffect
    object ApplyHintAndRetry : GameResultEffect
    object NavigateToNextLevel : GameResultEffect
    object NavigateToExit : GameResultEffect
}