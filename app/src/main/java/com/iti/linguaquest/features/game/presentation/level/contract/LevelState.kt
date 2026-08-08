package com.iti.linguaquest.features.game.presentation.level.contract

data class LevelState(
    val isLoading: Boolean = true,
    val isHintLoading: Boolean = false,
    val worldId: Int = 1,
    val levelId: Int = 1,
    val levelOrder: Int = 1,
    val coinCount: Int = 1250,
    val wordToGuess: String = "PAN",
    val languageCode: String = "en",
    val isLevelReady: Boolean = false,
    val isChangeWordAvailable: Boolean = true,
    val isBottomSheetVisible: Boolean = false,
    val isChangeWordDialogVisible: Boolean = false,
    val isChangeWordUsed: Boolean = false,
    val hintText: String? = null
)
