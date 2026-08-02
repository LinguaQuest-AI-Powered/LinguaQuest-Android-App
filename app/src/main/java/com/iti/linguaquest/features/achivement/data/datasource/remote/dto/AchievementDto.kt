package com.iti.linguaquest.features.achivement.data.datasource.remote.dto

data class AchievementsResponseDataDto(
    val earnedCount: Int = 0,
    val inProgressCount: Int = 0,
    val xpEarned: Int = 0,
    val achievements: List<AchievementDto> = emptyList()
)

data class AchievementDto(
    val id: Int,
    val name: String,
    val description: String,
    val iconUrl: String,
    val status: String,
    val progressPercent: Int = 0,
    val targetValue: Int = 0,
    val xpReward: Int = 0,
    val coinReward: Int = 0,
    val earnedAt: String? = null
)
