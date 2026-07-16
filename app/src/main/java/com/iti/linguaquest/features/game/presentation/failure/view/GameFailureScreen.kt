package com.iti.linguaquest.features.game.presentation.failure.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.iti.linguaquest.features.game.presentation.failure.contract.GameFailureEffect
import com.iti.linguaquest.features.game.presentation.failure.contract.GameFailureIntent
import com.iti.linguaquest.features.game.presentation.failure.viewmodel.GameFailureViewModel
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun GameFailureScreen(
    sharedViewModel: GameSharedViewModel,
    onRetry: () -> Unit,
    onExit: () -> Unit,
    viewModel: GameFailureViewModel = hiltViewModel()
) {
    val sharedState by sharedViewModel.sharedState.collectAsState()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GameFailureEffect.NavigateToRetry -> onRetry()
                GameFailureEffect.ApplyHint -> {
                    // Update the shared bus, then immediately navigate back to the camera
                    sharedViewModel.useHint()
                    onRetry()
                }
                GameFailureEffect.NavigateToExit -> onExit()
            }
        }
    }

    Column {
        Text("Failure Screen")
        Text("Target Word: ${sharedState.targetWord}")
        Text("Reason: ${state.reason}")
        Text("Hint Used: ${sharedState.isHintUsed}")

        Button(onClick = { viewModel.onIntent(GameFailureIntent.RetryClicked) }) {
            Text("Try Again")
        }

        // Only show hint button if they haven't used it yet
        if (!sharedState.isHintUsed) {
            Button(onClick = { viewModel.onIntent(GameFailureIntent.BuyHintClicked) }) {
                Text("Buy Hint & Retry")
            }
        }

        Button(onClick = { viewModel.onIntent(GameFailureIntent.ExitClicked) }) {
            Text("Give Up")
        }
    }
}