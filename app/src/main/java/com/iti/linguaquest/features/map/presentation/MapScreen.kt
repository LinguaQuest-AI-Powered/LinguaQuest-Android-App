package com.iti.linguaquest.features.map.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.map.presentation.components.MapContent
import com.iti.linguaquest.features.map.presentation.contract.MapEffect
import com.iti.linguaquest.features.map.presentation.contract.MapIntent
import com.iti.linguaquest.features.map.presentation.viewmodel.MapViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.features.map.presentation.components.LevelStatus
import com.iti.linguaquest.features.map.presentation.contract.MapLevelUiModel
import com.iti.linguaquest.features.map.presentation.contract.MapState

@Composable
fun MapScreen(
    worldId: Int,
    onBack: () -> Unit = {},
    onNavigateToLevel: (Int) -> Unit = {},
    viewModel: MapViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(worldId) {
        viewModel.loadLevels(worldId)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                MapEffect.NavigateBack -> onBack()
                is MapEffect.NavigateToLevel -> onNavigateToLevel(effect.levelNumber)
            }
        }
    }

    MapScreenContent(
        state = state,
        onLevelClick = { viewModel.onIntent(MapIntent.LevelClicked(it)) },
        onBackClick = { viewModel.onIntent(MapIntent.BackClicked) }
    )
}

@Composable
fun MapScreenContent(
    state: MapState,
    onLevelClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MapContent(
            state = state,
            onLevelClick = onLevelClick,
            modifier = Modifier.fillMaxSize()
        )
        LinguaQuestScreenTopBar(
            title = state.worldTitle.asString(),
            onBackClicked = onBackClick,
            isTitleCentered = false,
            titleColor = AppColors.BrownText,
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.5f))
                .padding(top = 16.dp)
        )
    }
}

@Preview
@Composable
fun MapScreenPreview(){
    val mockLevels = listOf(
        MapLevelUiModel(1, LevelStatus.COMPLETED, 3),
        MapLevelUiModel(2, LevelStatus.CURRENT, 0),
        MapLevelUiModel(3, LevelStatus.LOCKED, 0)
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