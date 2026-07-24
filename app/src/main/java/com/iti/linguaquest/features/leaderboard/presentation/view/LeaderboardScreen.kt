package com.iti.linguaquest.features.leaderboard.presentation

import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.features.leaderboard.presentation.contract.LeaderboardIntent
import com.iti.linguaquest.features.leaderboard.presentation.view.LeaderboardContent
import com.iti.linguaquest.features.leaderboard.presentation.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    onBack: () -> Unit,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {

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

            LeaderboardContent(
                leaderboard = state.leaderboard!!,
                onBack = onBack
            )
        }
    }
}