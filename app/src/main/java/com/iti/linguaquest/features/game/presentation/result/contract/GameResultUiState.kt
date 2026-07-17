package com.iti.linguaquest.features.game.presentation.result.contract

sealed interface GameResultUiState {
    object Processing : GameResultUiState

    data class Success(
        val xpAwarded: Int = 0,
        val coinsAwarded: Int = 0,
        val currentLevel: Int = 12,
        val progressPercent: Float = 0.8f
    ) : GameResultUiState

    data class Failure(
        val reason: String = "Item not recognized."
    ) : GameResultUiState

    data class Error(
        val errorMessage: String = "Network error occurred."
    ) : GameResultUiState
}