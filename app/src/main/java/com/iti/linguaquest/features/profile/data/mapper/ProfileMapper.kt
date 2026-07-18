package com.iti.linguaquest.features.profile.data.mapper


import com.iti.linguaquest.features.profile.data.remote.dto.AchievementPreviewDto
import com.iti.linguaquest.features.profile.data.remote.dto.AchievementsSummaryDto
import com.iti.linguaquest.features.profile.data.remote.dto.LanguageJourneyDto
import com.iti.linguaquest.features.profile.data.remote.dto.LeaderboardPreviewEntryDto
import com.iti.linguaquest.features.profile.data.remote.dto.LeaderboardSummaryDto
import com.iti.linguaquest.features.profile.data.remote.dto.ProfileStatsDto
import com.iti.linguaquest.features.profile.data.remote.dto.ProfileSummaryDto
import com.iti.linguaquest.features.profile.domain.model.AchievementPreview
import com.iti.linguaquest.features.profile.domain.model.AchievementStatus
import com.iti.linguaquest.features.profile.domain.model.AchievementsSummary
import com.iti.linguaquest.features.profile.domain.model.LanguageJourney
import com.iti.linguaquest.features.profile.domain.model.LeaderboardPreviewEntry
import com.iti.linguaquest.features.profile.domain.model.LeaderboardSummary
import com.iti.linguaquest.features.profile.domain.model.ProfileStats
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary

fun ProfileSummaryDto.toDomain(): ProfileSummary = ProfileSummary(
    id = id,
    username = username,
    name = name,
    photoUrl = photoUrl,
    level = level,
    stats = stats.toDomain(),
    languageJourney = currentLanguageJourney.toDomain(),
    achievementsSummary = achievementsSummary.toDomain(),
    leaderboardSummary = leaderboardSummary.toDomain()
)

private fun ProfileStatsDto.toDomain() = ProfileStats(coins, totalXp, streakDays, worldsCount)

private fun LanguageJourneyDto.toDomain() = LanguageJourney(
    languageId, name, code, level, journeyLabel, currentXp, nextMilestoneXp
)

private fun AchievementPreviewDto.toDomain() = AchievementPreview(
    id = id,
    name = name,
    description = description,
    iconUrl = iconUrl,
    status = runCatching { AchievementStatus.valueOf(status) }.getOrDefault(AchievementStatus.LOCKED),
    progressPercent = progressPercent
)

private fun AchievementsSummaryDto.toDomain() = AchievementsSummary(
    earnedCount, totalCount, preview.map { it.toDomain() }
)

private fun LeaderboardPreviewEntryDto.toDomain() = LeaderboardPreviewEntry(
    rank, userId, username, photoUrl, level, xp, isCurrentUser
)

private fun LeaderboardSummaryDto.toDomain() = LeaderboardSummary(
    myRank, preview.map { it.toDomain() }
)