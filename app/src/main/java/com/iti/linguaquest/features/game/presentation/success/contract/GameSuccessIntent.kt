package com.iti.linguaquest.features.game.presentation.success.contract

sealed interface GameSuccessIntent {
    object NextLevelClicked : GameSuccessIntent
    object ExitClicked : GameSuccessIntent
}