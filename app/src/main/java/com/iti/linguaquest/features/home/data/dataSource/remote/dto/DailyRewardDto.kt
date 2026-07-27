package com.iti.linguaquest.features.home.data.dataSource.remote.dto


import com.google.gson.annotations.SerializedName

data class DailyRewardStatusDto(
    @SerializedName("claimedToday")
    val claimedToday: Boolean,
    
    @SerializedName("currentDay")
    val currentDay: Int,
    
    @SerializedName("cycleLength")
    val cycleLength: Int,
    
    @SerializedName("rewardCoins")
    val rewardCoins: Int,
    
    @SerializedName("rewardXp")
    val rewardXp: Int?
)

data class ClaimDailyRewardResponseDto(
    val coinsAwarded: Int,
    val xpAwarded: Int?,
    val newCoinsBalance: Int,
    val newXpBalance: Int,
    val nextDay: Int
)