package com.iti.linguaquest.features.game.presentation.processing.view.component

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameProcessingView(
    imageUri: Uri?,
    onStartGameClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Future: Render the `imageUri` as a blurred background here

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "The AI is verifying your photo...")

            Spacer(modifier = Modifier.height(48.dp))

            // The call to action for the mini-game
            Button(onClick = onStartGameClicked) {
                Text("Tap to Play While You Wait!")
            }
        }
    }
}