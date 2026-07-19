package com.iti.linguaquest.features.game.presentation.shared

import android.net.Uri
import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface VerificationOutcome {
    object Idle : VerificationOutcome
    data class Success(val xpAwarded: Int, val coinsAwarded: Int) : VerificationOutcome
    data class Failure(val reason: UiText) : VerificationOutcome
    data class Error(val errorMessage: UiText) : VerificationOutcome
}

data class GameSharedState(
    val levelId: Int = -1,
    val targetWord: UiText = UiText.DynamicString(""),
    val capturedImageUri: Uri? = null,
    val isHintUsed: Boolean = false,
    val verificationOutcome: VerificationOutcome = VerificationOutcome.Idle
)