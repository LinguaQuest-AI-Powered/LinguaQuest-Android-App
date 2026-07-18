package com.iti.linguaquest.features.game.presentation.processing.view.component

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    Box(modifier = modifier.fillMaxSize()) {

        // 1. Background Image with Dark Tint
        ProcessingBackground(imageUri = imageUri)

        // 2. Center: The Interactive Darting Mascot
        FloatingLingo(
            onLingoTapped = onStartGameClicked,
            modifier = Modifier.align(Alignment.Center)
        )

        // 3. Bottom: Fixed Hint and Rotating Status
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 80.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {


            ProcessingStatusText()

            Spacer(modifier = Modifier.height(16.dp))

            GameHintText()


        }
    }
}