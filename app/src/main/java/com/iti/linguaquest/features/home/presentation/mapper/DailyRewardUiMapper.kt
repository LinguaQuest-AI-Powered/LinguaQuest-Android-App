package com.iti.linguaquest.features.home.presentation.mapper


import com.iti.linguaquest.features.home.domain.model.DailyRewardStatus

data class DailyRewardUi(
    val claimedToday: Boolean,
    val currentDay: Int,
    val cycleLength: Int,
    val rewardCoins: Int,
    val rewardXp: Int? = null
)

fun DailyRewardStatus.toUi() = DailyRewardUi(
    claimedToday = claimedToday,
    currentDay = currentDay,
    cycleLength = cycleLength,
    rewardCoins = rewardCoins,
    rewardXp = rewardXp
)