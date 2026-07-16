package com.iti.linguaquest.features.game.presentation.failure.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun GameFailureScreen(
    sharedViewModel: GameSharedViewModel,
    onRetry: () -> Unit,
    onExit: () -> Unit
) {
    Column {
        Text("Failure Screen")
        Button(onClick = onRetry) { Text("Try Again") }
        Button(onClick = onExit) { Text("Give Up") }
    }
}