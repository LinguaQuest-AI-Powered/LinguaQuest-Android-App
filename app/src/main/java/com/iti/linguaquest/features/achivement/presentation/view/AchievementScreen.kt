package com.iti.linguaquest.features.achivement.presentation.view

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

    when {
        state.isLoading && state.achievements.isEmpty() -> {
            LoadingView(modifier = modifier)
        }

        state.errorMessage != null && state.achievements.isEmpty() -> {
            ErrorView(
                modifier = modifier,
                message = state.errorMessage!!.asString(),
                onRetry = { viewModel.onIntent(AchievementIntent.LoadAchievements) }
            )
        }

        else -> {
            OfflineAwareContent(isOnline = isOnline) {
                AchievementContent(
                    modifier = modifier,
                    achievements = state.achievements,
                    earnedCount = state.earnedCount,
                    inProgressCount = state.inProgressCount,
                    xpGained = state.xpEarned,
                    onBackClick = onBackClick,
                    onClaimClick = { /* Handle claim */ }
                )
            }
        }
    }
}
