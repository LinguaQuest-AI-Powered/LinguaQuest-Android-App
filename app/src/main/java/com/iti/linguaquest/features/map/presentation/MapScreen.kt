package com.iti.linguaquest.features.map.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.map.presentation.components.LevelStatus
import com.iti.linguaquest.features.map.presentation.components.MapContent
import com.iti.linguaquest.features.map.presentation.contract.MapEffect
import com.iti.linguaquest.features.map.presentation.contract.MapIntent
import com.iti.linguaquest.features.map.presentation.contract.MapLevelUiModel
import com.iti.linguaquest.features.map.presentation.contract.MapState
import com.iti.linguaquest.features.map.presentation.viewmodel.MapViewModel

@Composable
fun MapScreen(
    worldId: Int,
    onBack: () -> Unit = {},
    onNavigateToLevel: (Int, Int, String?) -> Unit = { _, _, _ -> },
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
                is MapEffect.NavigateToLevel -> onNavigateToLevel(effect.levelId, effect.levelOrder, effect.targetWord)
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
        MapContent(
            state = state,
            onLevelClick = onLevelClick,
            modifier = Modifier.fillMaxSize()
        )

        if (state.isLoading && state.levels.isEmpty()) {
            LoadingView(onDismissRequest = onBackClick)
        }

        if (state.hasError && state.levels.isEmpty()) {
            ErrorView(
                message = state.errorMessage ?: stringResource(R.string.error_generic),
                onRetry = onRetry,
                onDismissRequest = onBackClick
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(44.dp)
                    .background(LinguaQuestTheme.colors.whiteColor, CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            if (!state.isLoading) {
                Text(
                    text = state.worldTitle.asString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.whiteColor,
                        fontSize = 22.sp
                    )
                )
            }
        }
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