package com.iti.linguaquest.features.game.presentation.level.contract

data class LevelState(
    val isLoading: Boolean = false,
    val worldId: Int = 1,
    val levelNumber: Int = 1,
    val coinCount: Int = 1250,
    val wordToGuess: String = "PAN",
    val languageCode: String = "en",
    val isBottomSheetVisible: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null
)
