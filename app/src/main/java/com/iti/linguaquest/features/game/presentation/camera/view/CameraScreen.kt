package com.iti.linguaquest.features.game.presentation.camera.view

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.linguaquest.features.game.presentation.camera.CameraViewModel
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraEffect
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraIntent
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun CameraScreen(
    sharedViewModel: GameSharedViewModel,
    onSubmitPhoto: () -> Unit,
    onBack: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    // 1. Observe States
    val sharedState by sharedViewModel.sharedState.collectAsState()
    val cameraState by viewModel.state.collectAsState()

    // 2. Listen for Effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CameraEffect.NavigateToProcessing -> {
                    // BRIDGE: Save the data to the SharedViewModel before navigating!
                    sharedViewModel.setCapturedImage(effect.imageUri)
                    onSubmitPhoto()
                }
                CameraEffect.NavigateBack -> {
                    onBack()
                }
            }
        }
    }

    // 3. UI (Simulating the intents)
    Column {
        Text("Camera Screen")
        Text("Target Word: ${sharedState.targetWord}")
        Text("Flash Enabled: ${cameraState.isFlashEnabled}")

        Button(onClick = {
            // Mocking a captured URI
            val mockUri = Uri.parse("content://media/external/images/media/1")
            viewModel.onIntent(CameraIntent.CapturePhoto(mockUri))
        }) {
            Text("Simulate Capture & Submit")
        }

        Button(onClick = { viewModel.onIntent(CameraIntent.ToggleFlash) }) {
            Text("Toggle Flash")
        }

        Button(onClick = { viewModel.onIntent(CameraIntent.BackClicked) }) {
            Text("Back to Lobby")
        }
    }
}