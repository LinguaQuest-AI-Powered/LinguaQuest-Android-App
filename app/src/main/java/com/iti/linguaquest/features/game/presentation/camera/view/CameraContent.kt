package com.iti.linguaquest.features.game.presentation.camera.view

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.game.presentation.camera.view.component.CameraButton
import com.iti.linguaquest.features.game.presentation.camera.view.component.CameraSideOptions
import com.iti.linguaquest.features.game.presentation.camera.view.component.CameraTopBar

@Composable
fun CameraContent(
    hasPermission: Boolean,
    targetWord: String,
    isHintUsed: Boolean,
    cameraController: LifecycleCameraController,
    onBackClicked: () -> Unit,
    onCaptureClicked: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    if (hasPermission) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(LinguaQuestTheme.colors.blackColor)) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        this.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.8f)
                    .aspectRatio(0.8f)
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(24.dp)
                    )
            )

            CameraTopBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                targetWord = targetWord,
                onBackClicked = onBackClicked,
                sideOptions = {
                    CameraSideOptions(
                        onToggleFlash = { /* TODO: Wire Flash */ },
                        onFlipCamera = { /* TODO: Wire Flip Camera */ },
                    )
                },
            )

            CameraButton(
                onCaptureClicked = onCaptureClicked,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Camera permission is required.")
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = onBackClicked) {
                Text("Go Back")
            }
        }
    }
}