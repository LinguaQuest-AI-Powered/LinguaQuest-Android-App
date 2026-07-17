package com.iti.linguaquest.features.game.presentation.result.contract

sealed interface GameResultUiState {
    object Processing : GameResultUiState

    data class Success(
        val xpAwarded: Int = 0,
        val coinsAwarded: Int = 0
    ) : GameResultUiState

    data class Failure(
        val reason: String = "Item not recognized."
    ) : GameResultUiState

    data class Error(
        val errorMessage: String = "Network error occurred."
    ) : GameResultUiState
}