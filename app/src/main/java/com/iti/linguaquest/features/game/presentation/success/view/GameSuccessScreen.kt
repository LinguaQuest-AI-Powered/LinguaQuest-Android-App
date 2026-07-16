package com.iti.linguaquest.features.game.presentation.success.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel
import com.iti.linguaquest.features.game.presentation.success.contract.GameSuccessEffect
import com.iti.linguaquest.features.game.presentation.success.contract.GameSuccessIntent
import com.iti.linguaquest.features.game.presentation.success.viewmodel.SuccessViewModel

@Composable
fun GameSuccessScreen(
    sharedViewModel: GameSharedViewModel,
    onNextLevel: () -> Unit,
    onExit: () -> Unit,
    viewModel: SuccessViewModel = hiltViewModel()
) {
    val sharedState by sharedViewModel.sharedState.collectAsState()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GameSuccessEffect.NavigateToNextLevel -> onNextLevel()
                GameSuccessEffect.NavigateToExit -> onExit()
            }
        }
    }

    Column {
        Text("Victory Screen!")
        Text("You found the: ${sharedState.targetWord}")
        Text("Rewards: +${state.xpAwarded} XP, +${state.coinsAwarded} Coins")

        Button(onClick = { viewModel.onIntent(GameSuccessIntent.NextLevelClicked) }) {
            Text("Next Level")
        }
        Button(onClick = { viewModel.onIntent(GameSuccessIntent.ExitClicked) }) {
            Text("Return to Home")
        }
    }
}