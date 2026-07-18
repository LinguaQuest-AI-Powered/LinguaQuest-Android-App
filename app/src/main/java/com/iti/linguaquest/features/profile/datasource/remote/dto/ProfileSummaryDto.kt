package com.iti.linguaquest.features.profile.datasource.remote.dto


data class ProfileSummaryDto(
    val id: Int,
    val username: String,
    val name: String,
    val photoUrl: String,
    val level: Int,
    val stats: ProfileStatsDto,
    val currentLanguageJourney: LanguageJourneyDto,
    val achievementsSummary: AchievementsSummaryDto,
    val leaderboardSummary: LeaderboardSummaryDto
)

data class ProfileStatsDto(
    val coins: Int,
    val totalXp: Int,
    val streakDays: Int,
    val worldsCount: Int
)

data class LanguageJourneyDto(
    val languageId: Int,
    val name: String,
    val code: String,
    val level: Int,
    val journeyLabel: String,
    val currentXp: Int,
    val nextMilestoneXp: Int
)

data class AchievementPreviewDto(
    val id: Int,
    val name: String,
    val description: String,
    val iconUrl: String,
    val status: String,
    val progressPercent: Int
)

data class AchievementsSummaryDto(
    val earnedCount: Int,
    val totalCount: Int,
    val preview: List<AchievementPreviewDto>
)

data class LeaderboardPreviewEntryDto(
    val rank: Int,
    val userId: Int,
    val username: String,
    val photoUrl: String,
    val level: Int,
    val xp: Int,
    val isCurrentUser: Boolean
)

data class LeaderboardSummaryDto(
    val myRank: Int,
    val preview: List<LeaderboardPreviewEntryDto>
)