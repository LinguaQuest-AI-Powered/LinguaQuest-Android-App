package com.iti.linguaquest.features.home.presentation.mapper


import com.iti.linguaquest.features.home.domain.model.DailyRewardStatus

data class DailyRewardUi(
    val claimedToday: Boolean,
    val currentDay: Int,
    val rewardCoins: Int
)

fun DailyRewardStatus.toUi() = DailyRewardUi(
    claimedToday = claimedToday,
    currentDay = currentDay,
    rewardCoins = rewardCoins
)