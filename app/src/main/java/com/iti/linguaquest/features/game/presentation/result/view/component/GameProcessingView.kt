package com.iti.linguaquest.features.game.presentation.result.view.component

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultUiState

@Composable
fun GameProcessingView(
    capturedUri: Uri?,
    onSimulateSuccess: () -> Unit,
    onSimulateFailure: () -> Unit,
    onSimulateError: () -> Unit
) {
    Column {
        Text("Processing Screen")
        Text("Processing URI: $capturedUri")
        Button(onClick = onSimulateSuccess) { Text("Simulate AI Success") }
        Button(onClick = onSimulateFailure) { Text("Simulate AI Failure") }
        Button(onClick = onSimulateError) { Text("Simulate Error") }
    }
}