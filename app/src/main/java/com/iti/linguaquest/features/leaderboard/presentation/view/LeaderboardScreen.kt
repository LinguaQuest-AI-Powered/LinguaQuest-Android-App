package com.iti.linguaquest.features.leaderboard.presentation

import androidx.compose.runtime.Composable


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.linguaquest.core.sharedComponents.LoadingView
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(state.errorMessage!!)
            }
        }

        state.leaderboard != null -> {

            LeaderboardContent(
                leaderboard = state.leaderboard!!,
                onBack = onBack
            )
        }
    }
}