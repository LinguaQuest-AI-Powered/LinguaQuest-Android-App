package com.iti.linguaquest.features.game.presentation.proccessing.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.iti.linguaquest.features.game.presentation.proccessing.contract.ProcessingEffect
import com.iti.linguaquest.features.game.presentation.proccessing.contract.ProcessingIntent
import com.iti.linguaquest.features.game.presentation.proccessing.viewmodel.ProcessingViewModel
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun CameraProcessingScreen(
    sharedViewModel: GameSharedViewModel,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    viewModel: ProcessingViewModel = hiltViewModel()
) {
    val sharedState by sharedViewModel.sharedState.collectAsState()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ProcessingEffect.NavigateToSuccess -> onSuccess()
                ProcessingEffect.NavigateToFailure -> onFailure()
            }
        }
    }

    Column {
        Text("Processing Screen")
        Text("Status: ${state.statusMessage}")
        Text("Processing URI: ${sharedState.capturedImageUri}")

        Button(onClick = { viewModel.onIntent(ProcessingIntent.SimulateAiSuccess) }) {
            Text("Simulate AI Success")
        }
        Button(onClick = { viewModel.onIntent(ProcessingIntent.SimulateAiFailure) }) {
            Text("Simulate AI Failure")
        }
    }
}