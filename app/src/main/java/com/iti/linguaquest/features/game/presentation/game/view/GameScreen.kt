package com.iti.linguaquest.features.game.presentation.game.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun GameScreen(
    sharedViewModel: GameSharedViewModel,
    onStartCamera: () -> Unit,
    onExit: () -> Unit
) {
    Column {
        Text("Game Lobby Screen")
        Button(onClick = onStartCamera) { Text("Start Hunt") }
        Button(onClick = onExit) { Text("Exit") }
    }
}