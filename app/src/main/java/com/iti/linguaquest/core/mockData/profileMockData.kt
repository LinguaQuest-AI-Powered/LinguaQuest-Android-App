package com.iti.linguaquest.core.mockData

import com.iti.linguaquest.R
import com.iti.linguaquest.features.profile.presentation.model.Achievement
import com.iti.linguaquest.features.profile.presentation.model.LeaderboardEntry
import com.iti.linguaquest.features.profile.presentation.model.ProfileState

 val mockProfileState = ProfileState(
    userName = "Explorer Alex",
    level = 12,
    avatarUrl = R.drawable.lingo_writing,
    coins = 1250,
    totalXp = 4500,
    streakDays = 7,
    worldsCount = 2,
    learningLanguageName = "French",
    learningLanguageFlagRes = R.drawable.flag_spain,
    proficiencyLabel = "Intermediate Journey",
    currentMilestoneXp = 2450,
    targetMilestoneXp = 3000,
    achievements = listOf(
        Achievement("1", "Wild Explorer", R.drawable.achievement_cup, "Complete 10 lessons in...")
    ),
    nearbyLeaderboard = listOf(
        LeaderboardEntry(99, "Sacagawea", "Guide", 2750, avatarUrl = R.drawable.lingo_writing),
        LeaderboardEntry(100, "Explorer Sam", "Adventurer", 3150, isCurrentUser = true,avatarUrl = R.drawable.lingo_writing),
        LeaderboardEntry(101, "Zheng He", "Admiral", 2600,avatarUrl = R.drawable.lingo_writing)
    )
)