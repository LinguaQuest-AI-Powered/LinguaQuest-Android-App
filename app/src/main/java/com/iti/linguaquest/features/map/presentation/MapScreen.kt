package com.iti.linguaquest.features.map.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.map.presentation.components.MapContent
import com.iti.linguaquest.features.map.presentation.contract.MapEffect
import com.iti.linguaquest.features.map.presentation.contract.MapIntent
import com.iti.linguaquest.features.map.presentation.viewmodel.MapViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.map.presentation.components.LevelStatus
import com.iti.linguaquest.features.map.presentation.contract.MapLevelUiModel
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.features.map.presentation.contract.MapState

@Composable
fun MapScreen(
    worldId: Int,
    onBack: () -> Unit = {},
    onNavigateToLevel: (Int) -> Unit = {},
    viewModel: MapViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, worldId) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadLevels(worldId, force = true)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                MapEffect.NavigateBack -> onBack()
                is MapEffect.NavigateToLevel -> onNavigateToLevel(effect.levelId)
            }
        }
    }
    OfflineAwareContent(isOnline = isOnline) {
        MapScreenContent(
            state = state,
            onLevelClick = { viewModel.onIntent(MapIntent.LevelClicked(it)) },
            onBackClick = { viewModel.onIntent(MapIntent.BackClicked) },
            onRetry = { viewModel.onIntent(MapIntent.Retry) }
        )
    }
}

@Composable
fun MapScreenContent(
    state: MapState,
    onLevelClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onRetry: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            state.isLoading -> {
                LoadingView(modifier = Modifier.fillMaxSize())
            }
            state.hasError && state.levels.isEmpty() -> {
                ErrorView(
                    message = state.errorMessage ?: stringResource(R.string.error_generic),
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                MapContent(
                    state = state,
                    onLevelClick = onLevelClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        LinguaQuestScreenTopBar(
            title = if (state.isLoading) "" else state.worldTitle.asString(),
            onBackClicked = onBackClick,
            isTitleCentered = false,
            titleColor = LinguaQuestTheme.colors.BrownText,
            modifier = Modifier
                .background(LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.5f))
                .padding(top = 16.dp)
        )
    }
}

@Preview
@Composable
fun MapScreenPreview(){
    val mockLevels = listOf(
        MapLevelUiModel(1, LevelStatus.COMPLETED, 3),
        MapLevelUiModel(2, LevelStatus.COMPLETED, 3),
        MapLevelUiModel(3, LevelStatus.COMPLETED, 3)
    )
    MapScreenContent(
        state = MapState(
            isLoading = false,
            worldTitle = UiText.DynamicString("Kitchen World"),
            levels = mockLevels,
            currentLevelIndex = 1
        ),
        onLevelClick = {},
        onBackClick = {}
    )
}