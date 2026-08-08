package com.iti.linguaquest.features.achivement.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.features.achivement.presentation.contract.AchievementIntent
import com.iti.linguaquest.features.achivement.presentation.viewmodel.AchievementViewModel

@Composable
fun AchievementScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: AchievementViewModel = hiltViewModel()
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        OfflineAwareContent(isOnline = isOnline) {
            AchievementContent(
                modifier = Modifier.fillMaxSize(),
                achievements = state.achievements,
                earnedCount = state.earnedCount,
                inProgressCount = state.inProgressCount,
                xpGained = state.xpEarned,
                onBackClick = onBackClick,
                onClaimClick = { /* Handle claim */ }
            )
        }

        if (state.isLoading && state.achievements.isEmpty()) {
            LoadingView(onDismissRequest = onBackClick)
        }

        if (state.errorMessage != null && state.achievements.isEmpty()) {
            ErrorView(
                message = state.errorMessage!!,
                onRetry = { viewModel.onIntent(AchievementIntent.LoadAchievements) },
                onDismissRequest = onBackClick
            )
        }
    }
}
