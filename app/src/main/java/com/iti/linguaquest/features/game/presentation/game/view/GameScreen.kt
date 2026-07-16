package com.iti.linguaquest.features.game.presentation.game.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun GameScreen(
    sharedViewModel: GameSharedViewModel,
    onStartCamera: () -> Unit,
    onExit: () -> Unit
) {
    val sharedState by sharedViewModel.sharedState.collectAsState()

    // Simulate fetching the level data and populating the shared bus
    LaunchedEffect(Unit) {
        if (sharedState.targetWord.isEmpty()) {
            // This would normally come from a LobbyViewModel fetching from a Repository
            sharedViewModel.setTargetWord("Apple")
        }
    }

    Column {
        Text("Game Lobby Screen")
        Text("Level ID: ${sharedState.levelId}")
        Text("Look for: ${sharedState.targetWord}")

        Button(onClick = onStartCamera) { Text("Start Hunt") }
        Button(onClick = { sharedViewModel.useHint() }) { Text("Buy Hint") }
        Button(onClick = onExit) { Text("Exit") }
    }
}