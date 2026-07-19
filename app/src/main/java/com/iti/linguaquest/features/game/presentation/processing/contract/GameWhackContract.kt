package com.iti.linguaquest.features.game.presentation.processing.contract

data class GameWhackState(
    val currentCoins: Int = 0,
    val isGameActive: Boolean = false,
    val lingoXPosition: Float = 0f,
    val lingoYPosition: Float = 0f,
    val isLingoVisible: Boolean = false
)

sealed interface GameWhackIntent {
    object StartGame : GameWhackIntent
    object LingoWhacked : GameWhackIntent
    object StopGame : GameWhackIntent
}