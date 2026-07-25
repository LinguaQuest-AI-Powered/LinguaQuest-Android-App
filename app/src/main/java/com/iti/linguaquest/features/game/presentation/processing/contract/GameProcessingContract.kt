package com.iti.linguaquest.features.game.presentation.processing.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText

data class GameProcessingState(
    val isProcessing: Boolean = true
)

sealed interface GameProcessingIntent {
    object StartProcessing : GameProcessingIntent
}

sealed interface GameProcessingEffect {
    data class NavigateToSuccess(
        val xp: Int,
        val coins: Int,
        val level: Int = 1,
        val progressPercentage: Int = 0
    ) : GameProcessingEffect
    data class NavigateToFailure(val reason: UiText) : GameProcessingEffect
    data class NavigateToError(val errorMessage: UiText) : GameProcessingEffect
}