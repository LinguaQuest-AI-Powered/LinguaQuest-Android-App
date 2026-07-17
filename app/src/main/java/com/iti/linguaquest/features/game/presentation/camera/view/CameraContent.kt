package com.iti.linguaquest.features.game.presentation.camera.view

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
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
    isFlashEnabled: Boolean,
    cameraController: LifecycleCameraController,
    onBackClicked: () -> Unit,
    onToggleFlash: () -> Unit,
    onFlipCamera: () -> Unit,
    onCaptureClicked: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    if (hasPermission) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LinguaQuestTheme.colors.blackColor)
        ) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        this.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            val strokeColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.8f)
                    .aspectRatio(0.8f)
                    .drawBehind {
                        drawRoundRect(
                            color = strokeColor,
                            style = Stroke(
                                width = 3.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f), 0f)
                            ),
                            cornerRadius = CornerRadius(24.dp.toPx())
                        )
                    }
            )

            CameraTopBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                targetWord = targetWord,
                onBackClicked = onBackClicked,
                sideOptions = {
                    CameraSideOptions(
                        isFlashEnabled = isFlashEnabled,
                        onToggleFlash = onToggleFlash,
                        onFlipCamera = onFlipCamera,
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
            Text(
                text = "Camera permission is required.",
                color = LinguaQuestTheme.colors.whiteColor
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = onBackClicked) {
                Text("Go Back")
            }
        }
    }
}