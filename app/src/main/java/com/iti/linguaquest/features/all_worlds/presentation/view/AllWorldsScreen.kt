package com.iti.linguaquest.features.all_worlds.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsEffect
import com.iti.linguaquest.features.all_worlds.presentation.view.components.AllWorldsContent
import com.iti.linguaquest.features.all_worlds.presentation.viewModel.AllWorldsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AllWorldsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToWorldDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AllWorldsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AllWorldsEffect.NavigateBack -> onNavigateBack()
                is AllWorldsEffect.NavigateToWorldDetails -> onNavigateToWorldDetails(effect.worldId)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F6))
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            AllWorldsContent(
                state = state,
                onIntent = viewModel::onIntent
            )
        }
    }
}
