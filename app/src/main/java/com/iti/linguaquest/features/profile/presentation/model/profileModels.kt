package com.iti.linguaquest.features.profile.presentation.model

import androidx.annotation.DrawableRes

data class ProfileState(
    val userName: String = "",
    val level: Int = 0,
    val avatarUrl: Any? = null,
    val coins: Int = 0,
    val totalXp: Int = 0,
    val streakDays: Int = 0,
    val worldsCount: Int = 0,
    val learningLanguageName: String = "",
    @DrawableRes val learningLanguageFlagRes: Int? = null,
    val proficiencyLabel: String = "",
    val currentMilestoneXp: Int = 0,
    val targetMilestoneXp: Int = 0,
    val achievements: List<Achievement> = emptyList(),
    val nearbyLeaderboard: List<LeaderboardEntry> = emptyList()
)

data class Achievement(val id: String, val title: String, val iconRes: Int, val progressLabel: String)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val title: String,
    val xp: Int,
    val avatarUrl: Int? = null,
    val isCurrentUser: Boolean = false
)