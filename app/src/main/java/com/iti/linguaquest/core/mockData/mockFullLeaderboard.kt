package com.iti.linguaquest.core.mockData
import com.iti.linguaquest.R
import com.iti.linguaquest.features.profile.presentation.model.LeaderboardEntry

val mockFullLeaderboard = listOf(
    LeaderboardEntry(1, "Marco Polo", "Legend", 4250, R.drawable.ic_leaderboard_1),
    LeaderboardEntry(2, "Amelia", "Explorer", 3890, R.drawable.ic_leaderboard_2),
    LeaderboardEntry(3, "Ibn Battuta", "Traveler", 3420, R.drawable.ic_leaderboard_3),
    LeaderboardEntry(98, "Ferdinand M.", "Novice", 2900, R.drawable.lingo_splash_2),
    LeaderboardEntry(99, "Sacagawea", "Guide", 2750, R.drawable.lingo_splash_3),
    LeaderboardEntry(100, "Explorer Sam", "Adventurer", 3150, R.drawable.lingo_splash_4, isCurrentUser = true),
    LeaderboardEntry(101, "Zheng He", "Admiral", 2600, R.drawable.lingo_splash_5),
    LeaderboardEntry(102, "Xuanzang", "Monk", 2550, R.drawable.lingo_splash_6)
)
