package com.iti.linguaquest.features.game.presentation.success.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun GameSuccessScreen(
    sharedViewModel: GameSharedViewModel,
    onNextLevel: () -> Unit,
    onExit: () -> Unit
) {
    Column {
        Text("Victory Screen!")
        Button(onClick = onNextLevel) { Text("Next Level") }
        Button(onClick = onExit) { Text("Return to Home") }
    }
}