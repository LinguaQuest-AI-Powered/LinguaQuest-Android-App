package com.iti.linguaquest.features.achivement.domain.model

enum class AchievementFilter {
    ALL,
    EARNED,
    LOCKED
}

enum class AchievementStatus {
    LOCKED,
    IN_PROGRESS,
    EARNED
}

data class AchievementsData(
    val earnedCount: Int,
    val inProgressCount: Int,
    val xpEarned: Int,
    val achievements: List<AchievementDomainModel>
)

data class AchievementDomainModel(
    val id: Int,
    val name: String,
    val description: String,
    val iconUrl: String,
    val status: AchievementStatus,
    val progressPercent: Int,
    val targetValue: Int,
    val xpReward: Int,
    val coinReward: Int,
    val earnedAt: String?
)
