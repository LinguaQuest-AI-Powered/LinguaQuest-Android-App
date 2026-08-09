package com.iti.linguaquest.features.all_worlds.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsEffect
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsIntent
import com.iti.linguaquest.features.all_worlds.presentation.view.components.AllWorldsContent
import com.iti.linguaquest.features.all_worlds.presentation.viewModel.AllWorldsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AllWorldsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToWorldDetails: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AllWorldsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AllWorldsEffect.NavigateBack -> onNavigateBack()
                is AllWorldsEffect.NavigateToWorldDetails -> onNavigateToWorldDetails(effect.worldId, effect.totalLevels)
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
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
            AllWorldsContent(
                state = state,
                onIntent = viewModel::onIntent
            )
        }

        if (state.isLoading) {
            LoadingView(onDismissRequest = onNavigateBack)
        }

        if (state.hasError) {
            ErrorView(
                message = state.errorMessage ?: UiText.StringResource(R.string.error_generic),
                onRetry = { viewModel.onIntent(AllWorldsIntent.OnRetry) },
                onDismissRequest = onNavigateBack
            )
        }
    }
}
