package com.iti.linguaquest.features.map.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.map.presentation.components.MapContent
import com.iti.linguaquest.features.map.presentation.components.MapTopBar
import com.iti.linguaquest.features.map.presentation.contract.MapEffect
import com.iti.linguaquest.features.map.presentation.contract.MapIntent
import com.iti.linguaquest.features.map.presentation.viewmodel.MapViewModel

@Composable
fun MapScreen(
    totalLevels: Int,
    completedLevels: Int,
    onBack: () -> Unit = {},
    viewModel: MapViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(totalLevels, completedLevels) {
        viewModel.loadLevels(totalLevels, completedLevels)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                MapEffect.NavigateBack -> onBack()
                is MapEffect.NavigateToLevel -> {
                    // TODO: navigate to the game / quiz for this level
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MapContent(
            state = state,
            onLevelClick = { viewModel.onIntent(MapIntent.LevelClicked(it)) },
            modifier = Modifier.fillMaxSize()
        )
        MapTopBar(
            title = state.worldTitle,
            onBack = { viewModel.onIntent(MapIntent.BackClicked) }
        )
    }
}
