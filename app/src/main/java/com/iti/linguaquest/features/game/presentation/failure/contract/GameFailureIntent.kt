package com.iti.linguaquest.features.game.presentation.failure.contract

sealed interface GameFailureIntent {
    object RetryClicked : GameFailureIntent
    object BuyHintClicked : GameFailureIntent
    object ExitClicked : GameFailureIntent
}