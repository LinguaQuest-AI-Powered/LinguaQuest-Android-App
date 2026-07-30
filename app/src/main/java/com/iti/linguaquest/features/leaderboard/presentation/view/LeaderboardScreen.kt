package com.iti.linguaquest.features.leaderboard.presentation.view

import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.features.leaderboard.presentation.contract.LeaderboardIntent
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.features.leaderboard.presentation.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    onBack: () -> Unit,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    val state by viewModel.state.collectAsState()

    when {

        state.isLoading -> {
            LoadingView()
        }

        state.errorMessage != null -> {
            ErrorView(
                message = state.errorMessage!!,
                onRetry = { viewModel.onIntent(LeaderboardIntent.LoadLeaderboard) }
            )
        }

        state.leaderboard != null -> {
            OfflineAwareContent(isOnline = isOnline) {
                LeaderboardContent(
                    leaderboard = state.leaderboard!!,
                    onBack = onBack,
                    onLoadMore = { viewModel.onIntent(LeaderboardIntent.LoadMore) },
                    isLoadingMore = state.isLoadingMore,
                    endReached = state.endReached
                )
            }
        }
    }
}

