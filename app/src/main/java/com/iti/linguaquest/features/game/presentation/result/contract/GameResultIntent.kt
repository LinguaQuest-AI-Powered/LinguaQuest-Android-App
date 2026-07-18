package com.iti.linguaquest.features.game.presentation.result.contract

sealed interface GameResultIntent {
    // User Action Intents
    object RetryClicked : GameResultIntent
    object BuyHintClicked : GameResultIntent
    object NextLevelClicked : GameResultIntent
    object ExitClicked : GameResultIntent
}