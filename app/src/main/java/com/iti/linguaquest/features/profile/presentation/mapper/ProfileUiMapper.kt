package com.iti.linguaquest.features.profile.presentation.mapper


import com.iti.linguaquest.R
import com.iti.linguaquest.features.profile.domain.model.AchievementPreview
import com.iti.linguaquest.features.profile.domain.model.LeaderboardPreviewEntry
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.presentation.model.Achievement
import com.iti.linguaquest.features.profile.presentation.model.LeaderboardEntry
import com.iti.linguaquest.features.profile.presentation.model.ProfileState

// TODO: temporary until backend images are live
private fun localFlagFor(code: String): Int = when (code) {
    "es" -> R.drawable.flag_spain
    "fr" -> R.drawable.flag_france
    "ge" -> R.drawable.flag_germany
    "ja" -> R.drawable.flag_japan
    else -> R.drawable.flag_spain
}

private fun localAvatarPlaceholder(): Int = R.drawable.lingo

private fun localAchievementIconFor(name: String): Int = when (name) {
    "Wild Explorer" -> R.drawable.achievement_cup
    else -> R.drawable.achievement_cup
}

private fun placeholderTitleFor(level: Int): String = "Level $level Explorer"

fun ProfileSummary.toProfileState(): ProfileState = ProfileState(
    userName = username,
    level = level,
    avatarUrl = photoUrl.ifBlank { null },
    coins = stats.coins,
    totalXp = stats.totalXp,
    streakDays = stats.streakDays,
    worldsCount = stats.worldsCount,
    learningLanguageName = languageJourney.name,
    learningLanguageFlagRes = localFlagFor(languageJourney.code),
    proficiencyLabel = languageJourney.journeyLabel,
    currentMilestoneXp = languageJourney.currentXp,
    targetMilestoneXp = languageJourney.nextMilestoneXp,
    achievements = achievementsSummary.preview.map { it.toUiAchievement() },
    nearbyLeaderboard = leaderboardSummary.preview.map { it.toUiLeaderboardEntry() }
)

private fun AchievementPreview.toUiAchievement() = Achievement(
    id = id.toString(),
    title = name,
    iconRes = localAchievementIconFor(name),
    progressLabel = description
)

private fun LeaderboardPreviewEntry.toUiLeaderboardEntry() = LeaderboardEntry(
    rank = rank,
    name = username,
    title = placeholderTitleFor(level),
    xp = xp,
    avatarUrl = localAvatarPlaceholder(),
    isCurrentUser = isCurrentUser
)