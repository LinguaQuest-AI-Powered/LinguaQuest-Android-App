package com.iti.linguaquest.features.home.data.dataSource.remote.dto


data class DailyRewardStatusDto(
    val claimedToday: Boolean,
    val currentDay: Int,
    val cycleLength: Int,
    val rewardCoins: Int,
    val rewardXp: Int?,
    val streakDays: Int
)

data class ClaimDailyRewardResponseDto(
    val coinsAwarded: Int,
    val xpAwarded: Int?,
    val newCoinsBalance: Int,
    val newXpBalance: Int,
    val newStreakDays: Int,
    val nextDay: Int
)