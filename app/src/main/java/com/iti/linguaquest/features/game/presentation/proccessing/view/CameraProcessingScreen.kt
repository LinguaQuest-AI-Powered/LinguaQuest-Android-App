package com.iti.linguaquest.features.game.presentation.proccessing.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun CameraProcessingScreen(
    sharedViewModel: GameSharedViewModel,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    Column {
        Text("Processing Screen (Animations)")
        Button(onClick = onSuccess) { Text("Simulate Success") }
        Button(onClick = onFailure) { Text("Simulate Failure") }
    }
}