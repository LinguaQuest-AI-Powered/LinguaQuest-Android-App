package com.iti.linguaquest.features.game.presentation.shared

import android.net.Uri

sealed interface VerificationOutcome {
    object Idle : VerificationOutcome
    data class Success(val xpAwarded: Int, val coinsAwarded: Int) : VerificationOutcome
    data class Failure(val reason: String) : VerificationOutcome
    data class Error(val errorMessage: String) : VerificationOutcome
}

data class GameSharedState(
    val levelId: Int = -1,
    val targetWord: String = "",
    val capturedImageUri: Uri? = null,
    val isHintUsed: Boolean = false,
    val verificationOutcome: VerificationOutcome = VerificationOutcome.Idle // Added outcome
)