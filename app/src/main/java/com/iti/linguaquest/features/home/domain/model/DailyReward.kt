package com.iti.linguaquest.features.home.domain.model


data class DailyRewardStatus(
    val claimedToday: Boolean,
    val currentDay: Int,
    val cycleLength: Int,
    val rewardCoins: Int,
    val rewardXp: Int?
)

data class DailyRewardClaimResult(
    val coinsAwarded: Int,
    val xpAwarded: Int?,
    val newCoinsBalance: Int,
    val newXpBalance: Int,
    val nextDay: Int
)