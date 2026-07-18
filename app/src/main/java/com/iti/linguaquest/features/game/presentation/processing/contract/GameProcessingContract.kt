package com.iti.linguaquest.features.game.presentation.processing.contract

data class GameProcessingState(
    val isProcessing: Boolean = true
)

sealed interface GameProcessingIntent {
    object StartProcessing : GameProcessingIntent
    object SimulateAiSuccess : GameProcessingIntent
    object SimulateAiFailure : GameProcessingIntent
    object SimulateNetworkError : GameProcessingIntent
}

sealed interface GameProcessingEffect {
    data class NavigateToSuccess(val xp: Int, val coins: Int) : GameProcessingEffect
    data class NavigateToFailure(val reason: String) : GameProcessingEffect
    data class NavigateToError(val errorMessage: String) : GameProcessingEffect
}