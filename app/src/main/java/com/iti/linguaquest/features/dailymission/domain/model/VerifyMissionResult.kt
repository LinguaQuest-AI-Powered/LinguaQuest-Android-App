package com.iti.linguaquest.features.dailymission.domain.model

data class VerifyMissionResult(
    val isMatch: Boolean,
    val xpEarned: Int,
    val coinsEarned: Int
)
