package com.iti.linguaquest.features.dailymission.data.remote

data class VerifyMissionDto(
    val isMatch: Boolean,
    val xpEarned: Int,
    val coinsEarned: Int
)
