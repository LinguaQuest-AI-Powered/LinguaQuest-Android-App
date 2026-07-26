package com.iti.linguaquest.features.game.presentation.result.contract

sealed interface GameResultIntent {
    // User Action Intents
    data object RetryClicked : GameResultIntent
    data class BuyHintClicked(val worldId: Int, val levelId: Int) : GameResultIntent
    data object NextLevelClicked : GameResultIntent
    object ExitClicked : GameResultIntent
}