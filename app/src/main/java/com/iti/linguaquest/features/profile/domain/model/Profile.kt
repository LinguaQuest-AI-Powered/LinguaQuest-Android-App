package com.iti.linguaquest.features.profile.domain.model


data class ProfileSummary(
    val id: Int,
    val username: String,
    val photoUrl: String?,
    val level: Int,
    val stats: ProfileStats,
    val languageJourney: LanguageJourney,
    val achievementsSummary: AchievementsSummary,
    val leaderboardSummary: LeaderboardSummary
)

data class ProfileStats(
    val coins: Int,
    val totalXp: Int,
    val streakDays: Int,
    val worldsCount: Int
)

data class LanguageJourney(
    val languageId: Int,
    val name: String,
    val code: String,
    val level: Int,
    val journeyLabel: String,
    val currentXp: Int,
    val nextMilestoneXp: Int
)

enum class AchievementStatus { EARNED, IN_PROGRESS, LOCKED }

data class AchievementPreview(
    val id: Int,
    val name: String,
    val description: String,
    val iconUrl: String,
    val status: AchievementStatus,
    val progressPercent: Int
)

data class AchievementsSummary(
    val earnedCount: Int,
    val totalCount: Int,
    val preview: List<AchievementPreview>
)

data class LeaderboardPreviewEntry(
    val rank: Int,
    val userId: Int,
    val username: String,
    val photoUrl: String?,
    val level: Int,
    val xp: Int,
    val isCurrentUser: Boolean
)

data class LeaderboardSummary(
    val myRank: Int,
    val preview: List<LeaderboardPreviewEntry>
)