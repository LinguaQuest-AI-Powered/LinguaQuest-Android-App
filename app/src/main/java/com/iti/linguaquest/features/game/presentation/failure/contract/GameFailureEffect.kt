package com.iti.linguaquest.features.game.presentation.failure.contract

sealed interface GameFailureEffect {
    object NavigateToRetry : GameFailureEffect
    object ApplyHint : GameFailureEffect
    object NavigateToExit : GameFailureEffect
}