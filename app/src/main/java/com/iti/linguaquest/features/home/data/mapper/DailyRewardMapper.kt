package com.iti.linguaquest.features.home.data.mapper


import com.iti.linguaquest.features.home.data.dataSource.remote.dto.ClaimDailyRewardResponseDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.DailyRewardStatusDto
import com.iti.linguaquest.features.home.domain.model.DailyRewardClaimResult
import com.iti.linguaquest.features.home.domain.model.DailyRewardStatus

fun DailyRewardStatusDto.toDomain() = DailyRewardStatus(
    claimedToday, currentDay, cycleLength, rewardCoins, rewardXp, streakDays
)

fun ClaimDailyRewardResponseDto.toDomain() = DailyRewardClaimResult(
    coinsAwarded, xpAwarded, newCoinsBalance, newXpBalance, newStreakDays, nextDay
)