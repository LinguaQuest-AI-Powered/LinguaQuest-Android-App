package com.iti.linguaquest.features.game.presentation.processing.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingEffect
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingIntent
import com.iti.linguaquest.features.game.presentation.processing.contract.GameWhackIntent
import com.iti.linguaquest.features.game.presentation.processing.view.component.GameProcessingView
import com.iti.linguaquest.features.game.presentation.processing.view.component.GameWhackView
import com.iti.linguaquest.features.game.presentation.processing.viewmodel.GameProcessingViewModel
import com.iti.linguaquest.features.game.presentation.processing.viewmodel.GameWhackViewModel
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel
import com.iti.linguaquest.features.game.presentation.shared.VerificationOutcome

@Composable
fun GameProcessingScreen(
    sharedViewModel: GameSharedViewModel,
    onNavigateToResult: () -> Unit,
    modifier: Modifier = Modifier,
    processingViewModel: GameProcessingViewModel = hiltViewModel(),
    whackViewModel: GameWhackViewModel = hiltViewModel()
) {
    val whackState by whackViewModel.state.collectAsState()
    val sharedState by sharedViewModel.sharedState.collectAsState()

    // Handle initial processing start and collect effects
    LaunchedEffect(Unit) {
        // Start the AI verification process automatically when the screen loads
        processingViewModel.onIntent(GameProcessingIntent.StartProcessing)

        processingViewModel.effect.collect { effect ->
            // Map the effect to our new Shared VerificationOutcome
            val outcome = when (effect) {
                is GameProcessingEffect.NavigateToSuccess -> {
                    VerificationOutcome.Success(
                        xpAwarded = effect.xp,
                        // Combine base API coins with the user's mini-game earnings
                        coinsAwarded = effect.coins + whackState.currentCoins
                    )
                }
                is GameProcessingEffect.NavigateToFailure -> {
                    VerificationOutcome.Failure(reason = effect.reason)
                }
                is GameProcessingEffect.NavigateToError -> {
                    VerificationOutcome.Error(errorMessage = effect.errorMessage)
                }
            }

            // 1. Commit the final payload to the data bus
            sharedViewModel.setVerificationOutcome(outcome)

            // 2. Trigger simple navigation to the result screen
            onNavigateToResult()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Base View
        GameProcessingView(
            imageUri = sharedState.capturedImageUri,
            onStartGameClicked = { whackViewModel.onIntent(GameWhackIntent.StartGame) },
            modifier = Modifier.fillMaxSize()
        )

        // Mini-game Overlay
        if (whackState.isGameActive) {
            GameWhackView(
                state = whackState,
                onLingoWhacked = { whackViewModel.onIntent(GameWhackIntent.LingoWhacked) },
                modifier = Modifier.fillMaxSize()
            )
        }

        // ==========================================
        // TEMPORARY DEV OVERLAY FOR SIMULATION
        // ==========================================
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { processingViewModel.onIntent(GameProcessingIntent.SimulateAiSuccess) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green.copy(alpha = 0.8f))
            ) {
                Text("Success")
            }

            Button(
                onClick = { processingViewModel.onIntent(GameProcessingIntent.SimulateAiFailure) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow.copy(alpha = 0.8f))
            ) {
                Text("Fail", color = Color.Black)
            }

            Button(
                onClick = { processingViewModel.onIntent(GameProcessingIntent.SimulateNetworkError) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
            ) {
                Text("Error")
            }
        }
    }
}