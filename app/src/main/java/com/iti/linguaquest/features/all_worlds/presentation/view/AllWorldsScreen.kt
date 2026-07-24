package com.iti.linguaquest.features.all_worlds.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsEffect
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsIntent
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading) {
            LoadingView()
        } else if (state.hasError) {
            ErrorView(
                message = state.errorMessage ?: stringResource(R.string.error_generic),
                onRetry = { viewModel.onIntent(AllWorldsIntent.OnRetry) }
            )
        } else {
            AllWorldsContent(
                state = state,
                onIntent = viewModel::onIntent
            )
        }
    }
}
