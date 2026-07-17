package com.iti.linguaquest.features.game.presentation.shared

import android.net.Uri

data class GameSharedState(
    val levelId: Int = -1,
    val targetWord: String = "",
    val capturedImageUri: Uri? = null,
    val isHintUsed: Boolean = false
)