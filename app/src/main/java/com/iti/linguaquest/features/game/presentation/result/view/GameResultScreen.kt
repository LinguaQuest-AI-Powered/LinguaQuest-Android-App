package com.iti.linguaquest.features.game.presentation.result.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultEffect
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultIntent
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultUiState
import com.iti.linguaquest.features.game.presentation.result.view.component.GameErrorView
import com.iti.linguaquest.features.game.presentation.result.view.component.GameFailView
import com.iti.linguaquest.features.game.presentation.result.view.component.GameProcessingView
import com.iti.linguaquest.features.game.presentation.result.view.component.GameSuccessView
import com.iti.linguaquest.features.game.presentation.result.viewmodel.GameResultViewModel
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun GameResultScreen(
    sharedViewModel: GameSharedViewModel,
    onNavigateToCamera: () -> Unit,
    onNavigateToNextLevel: () -> Unit,
    onExit: () -> Unit,
    viewModel: GameResultViewModel = hiltViewModel()
) {
    val sharedState by sharedViewModel.sharedState.collectAsState()
    val state by viewModel.state.collectAsState()

    // Handle One-Time Effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GameResultEffect.NavigateToCamera -> onNavigateToCamera()
                GameResultEffect.ApplyHintAndRetry -> {
                    sharedViewModel.useHint()
                    onNavigateToCamera()
                }
                GameResultEffect.NavigateToNextLevel -> onNavigateToNextLevel()
                GameResultEffect.NavigateToExit -> onExit()
            }
        }
    }

    // Route the UI based on the current state
    when (val currentState = state) {
        is GameResultUiState.Processing -> {
            GameProcessingView(
                capturedUri = sharedState.capturedImageUri,
                onSimulateSuccess = { viewModel.onIntent(GameResultIntent.SimulateAiSuccess) },
                onSimulateFailure = { viewModel.onIntent(GameResultIntent.SimulateAiFailure) },
                onSimulateError = { viewModel.onIntent(GameResultIntent.SimulateNetworkError) }
            )
        }
        is GameResultUiState.Success -> {
            GameSuccessView(
                state = currentState,
                targetWord = sharedState.targetWord,
                onNextLevel = { viewModel.onIntent(GameResultIntent.NextLevelClicked) },
                onExit = { viewModel.onIntent(GameResultIntent.ExitClicked) }
            )
        }
        is GameResultUiState.Failure -> {
            GameFailView(
                state = currentState,
                targetWord = sharedState.targetWord,
                isHintUsed = sharedState.isHintUsed,
                onRetry = { viewModel.onIntent(GameResultIntent.RetryClicked) },
                onBuyHint = { viewModel.onIntent(GameResultIntent.BuyHintClicked) },
                onExit = { viewModel.onIntent(GameResultIntent.ExitClicked) }
            )
        }
        is GameResultUiState.Error -> {
            GameErrorView(
                state = currentState,
                onRetry = { viewModel.onIntent(GameResultIntent.RetryClicked) },
                onExit = { viewModel.onIntent(GameResultIntent.ExitClicked) }
            )
        }
    }
}