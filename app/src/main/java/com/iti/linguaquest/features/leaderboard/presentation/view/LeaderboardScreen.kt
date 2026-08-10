package com.iti.linguaquest.features.leaderboard.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.core.sharedComponents.state.StatefulContentContainer
import com.iti.linguaquest.features.leaderboard.presentation.contract.LeaderboardIntent
import com.iti.linguaquest.features.leaderboard.presentation.viewmodel.LeaderboardViewModel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

@Composable
fun LeaderboardScreen(
    onBack: () -> Unit,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        OfflineAwareContent(isOnline = isOnline) {
            StatefulContentContainer(
                dataStatus = state.dataStatus,
                onRetry = { viewModel.onIntent(LeaderboardIntent.LoadLeaderboard) },
                onErrorDismiss = onBack,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.leaderboard != null) {
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
}

