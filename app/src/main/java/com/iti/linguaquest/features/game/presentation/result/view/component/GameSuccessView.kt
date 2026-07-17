package com.iti.linguaquest.features.game.presentation.result.view.component

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultUiState


@Composable
fun GameSuccessView(
    state: GameResultUiState.Success,
    targetWord: String,
    onNextLevel: () -> Unit,
    onExit: () -> Unit
) {
    Column {
        Text("Victory Screen!")
        Text("You found the: $targetWord")
        Text("Rewards: +${state.xpAwarded} XP, +${state.coinsAwarded} Coins")
        Button(onClick = onNextLevel) { Text("Next Level") }
        Button(onClick = onExit) { Text("Return to Home") }
    }
}