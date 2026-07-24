package com.iti.linguaquest.features.game.domain.model

data class VerifyLevelResult(
    val isMatch: Boolean,
    val xpEarned: Int,
    val coinsEarned: Int,
    val level: Int,
    val levelProgressPercentage: Int
)
