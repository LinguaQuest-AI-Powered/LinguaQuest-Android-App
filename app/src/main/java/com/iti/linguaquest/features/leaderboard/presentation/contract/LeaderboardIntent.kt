package com.iti.linguaquest.features.leaderboard.presentation.contract

import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardScope

sealed interface LeaderboardIntent {

    data object LoadLeaderboard : LeaderboardIntent

    data class ChangeScope(
        val scope: LeaderboardScope,
        val languageId: Int? = null
    ) : LeaderboardIntent
}