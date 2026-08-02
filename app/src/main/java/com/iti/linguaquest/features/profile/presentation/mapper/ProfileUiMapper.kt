package com.iti.linguaquest.features.profile.presentation.mapper


import com.iti.linguaquest.R
import com.iti.linguaquest.core.utils.toFlagEmoji
import com.iti.linguaquest.features.profile.domain.model.AchievementPreview
import com.iti.linguaquest.features.profile.domain.model.LeaderboardPreviewEntry
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.presentation.model.Achievement
import com.iti.linguaquest.features.profile.presentation.model.LeaderboardEntry
import com.iti.linguaquest.features.profile.presentation.model.ProfileState

private fun localAvatarPlaceholder(): Int = R.drawable.lingo

private fun localAchievementIconFor(name: String): Int = when (name) {
    "Wild Explorer" -> R.drawable.achievement_cup
    else -> R.drawable.achievement_cup
}

private fun placeholderTitleFor(level: Int): String = "Level $level Explorer"

fun ProfileSummary.toProfileState(): ProfileState = ProfileState(
    userName = username,
    level = level,
    avatarUrl = photoUrl?.ifBlank { null },
    coins = stats.coins,
    totalXp = stats.totalXp,
    streakDays = stats.streakDays,
    worldsCount = stats.worldsCount,
    learningLanguageName = languageJourney.name,
    learningLanguageFlag = languageJourney.code.toFlagEmoji(),
    proficiencyLabel = languageJourney.journeyLabel,
    currentMilestoneXp = languageJourney.currentXp,
    targetMilestoneXp = languageJourney.nextMilestoneXp,
    achievements = achievementsSummary.preview.map { it.toUiAchievement() },
    nearbyLeaderboard = leaderboardSummary.preview.map { it.toUiLeaderboardEntry() }
)

private fun AchievementPreview.toUiAchievement() = Achievement(
    id = id.toString(),
    title = name,
    icon = iconUrl.ifBlank { localAchievementIconFor(name) },
    progressLabel = description
)

private fun LeaderboardPreviewEntry.toUiLeaderboardEntry() = LeaderboardEntry(
    rank = rank,
    name = username,
    title = placeholderTitleFor(level),
    xp = xp,
    avatarUrl = photoUrl?.ifBlank { null } ?: localAvatarPlaceholder(),
    isCurrentUser = isCurrentUser
)