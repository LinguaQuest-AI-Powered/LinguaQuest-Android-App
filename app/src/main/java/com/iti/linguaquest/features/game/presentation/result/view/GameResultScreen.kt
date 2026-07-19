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
import com.iti.linguaquest.features.game.presentation.result.view.component.GameSuccessView
import com.iti.linguaquest.features.game.presentation.result.viewmodel.GameResultViewModel
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel
import com.iti.linguaquest.features.game.presentation.shared.VerificationOutcome

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

    LaunchedEffect(sharedState.verificationOutcome) {
        val mappedState = when (val outcome = sharedState.verificationOutcome) {
            is VerificationOutcome.Success -> GameResultUiState.Success(
                xpAwarded = outcome.xpAwarded,
                coinsAwarded = outcome.coinsAwarded
            )
            is VerificationOutcome.Failure -> GameResultUiState.Failure(reason = outcome.reason)
            is VerificationOutcome.Error -> GameResultUiState.Error(errorMessage = outcome.errorMessage)
            VerificationOutcome.Idle -> GameResultUiState.Error(com.iti.linguaquest.core.sharedComponents.text.UiText.DynamicString("Invalid state. No outcome generated."))
        }
        viewModel.setInitialResult(mappedState)
    }

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

    when (val currentState = state) {
        is GameResultUiState.Success -> {
            GameSuccessView(
                xpGained = currentState.xpAwarded,
                coinsGained = currentState.coinsAwarded,
                currentLevel = currentState.currentLevel,
                progressPercent = currentState.progressPercent,
                onNextLevelClick = { viewModel.onIntent(GameResultIntent.NextLevelClicked) }
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