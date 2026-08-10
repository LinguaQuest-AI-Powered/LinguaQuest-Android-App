package com.iti.linguaquest.features.leaderboard.presentation.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.leaderboard.domain.model.Leaderboard
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardScope

import com.iti.linguaquest.core.sharedComponents.state.DataStatus

data class LeaderboardState(
    val dataStatus: DataStatus = DataStatus.Loading,
    val isLoadingMore: Boolean = false,
    val leaderboard: Leaderboard? = null,
    val scope: LeaderboardScope = LeaderboardScope.GLOBAL,
    val languageId: Int? = null,
    val currentPage: Int = 0,
    val endReached: Boolean = false
)