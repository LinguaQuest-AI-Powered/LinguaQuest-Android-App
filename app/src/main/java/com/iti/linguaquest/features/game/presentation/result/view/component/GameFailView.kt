package com.iti.linguaquest.features.game.presentation.result.view.component

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultUiState

@Composable
fun GameFailView(
    state: GameResultUiState.Failure,
    targetWord: String,
    isHintUsed: Boolean,
    onRetry: () -> Unit,
    onBuyHint: () -> Unit,
    onExit: () -> Unit
) {
    Column {
        Text("Failure Screen")
        Text("Target Word: $targetWord")
        Text("Reason: ${state.reason}")
        Text("Hint Used: $isHintUsed")
        Button(onClick = onRetry) { Text("Try Again") }
        if (!isHintUsed) {
            Button(onClick = onBuyHint) { Text("Buy Hint & Retry") }
        }
        Button(onClick = onExit) { Text("Give Up") }
    }
}