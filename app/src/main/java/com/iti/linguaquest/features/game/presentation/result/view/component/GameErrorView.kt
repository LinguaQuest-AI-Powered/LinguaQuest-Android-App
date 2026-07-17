package com.iti.linguaquest.features.game.presentation.result.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultUiState


@Composable
fun GameErrorView(
    state: GameResultUiState.Error,
    onRetry: () -> Unit,
    onExit: () -> Unit
) {
    Column {
        Text("Connection Error")
        Text("Details: ${state.errorMessage}")
        Button(onClick = onRetry) { Text("Retry Connection / Go Back") }
        Button(onClick = onExit) { Text("Exit Game") }
    }
}