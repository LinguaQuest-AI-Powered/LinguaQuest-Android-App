package com.iti.linguaquest.features.leaderboard.presentation.contract

import com.iti.linguaquest.features.leaderboard.domain.model.Leaderboard
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardScope

data class LeaderboardState(
    val isLoading: Boolean = false,
    val leaderboard: Leaderboard? = null,
    val scope: LeaderboardScope = LeaderboardScope.GLOBAL,
    val errorMessage: String? = null
)