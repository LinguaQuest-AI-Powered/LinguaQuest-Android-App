package com.iti.linguaquest.features.game.presentation.camera.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun CameraScreen(
    sharedViewModel: GameSharedViewModel,
    onSubmitPhoto: () -> Unit,
    onBack: () -> Unit
) {
    Column {
        Text("Camera Screen")
        Button(onClick = onSubmitPhoto) { Text("Capture & Submit") }
        Button(onClick = onBack) { Text("Back to Lobby") }
    }
}