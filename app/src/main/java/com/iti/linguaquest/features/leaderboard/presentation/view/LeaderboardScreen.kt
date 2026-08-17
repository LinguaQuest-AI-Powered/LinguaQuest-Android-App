package com.iti.linguaquest.features.leaderboard.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.core.sharedComponents.state.StatefulContentContainer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.leaderboard.presentation.contract.LeaderboardIntent
import com.iti.linguaquest.features.leaderboard.presentation.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.lingo_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = if (LinguaQuestTheme.colors.isDark) {
                ColorFilter.tint(
                    Color.Black.copy(alpha = 0.75f),
                    BlendMode.SrcOver
                )
            } else null
        )

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

