package com.iti.linguaquest.features.game.presentation.result.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface GameResultUiState {

    data class Success(
        val xpAwarded: Int = 0,
        val coinsAwarded: Int = 0,
        val currentLevel: Int = 12,
        val progressPercent: Float = 0.8f
    ) : GameResultUiState

    data class Failure(
        val reason: UiText = UiText.DynamicString("Item not recognized.")
    ) : GameResultUiState

    data class Error(
        val errorMessage: UiText = UiText.DynamicString("Network error occurred.")
    ) : GameResultUiState
}