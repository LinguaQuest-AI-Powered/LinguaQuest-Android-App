package com.iti.linguaquest.features.leaderboard

import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.mockData.mockFullLeaderboard


@Composable
fun LeaderboardScreen(onBack: () -> Unit) {
    LeaderboardContent(
        entries = mockFullLeaderboard,
        onBack = onBack
    )
}
