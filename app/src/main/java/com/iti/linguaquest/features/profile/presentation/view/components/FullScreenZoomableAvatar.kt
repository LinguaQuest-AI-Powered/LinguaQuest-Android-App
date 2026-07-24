package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds
import com.iti.linguaquest.R

@Composable
 fun FullScreenZoomableAvatar(
    model: Any?,
    contentDescription: String?,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var hasInteracted by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }

    fun close() {
        visible = false
    }

    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(200.milliseconds)
            onDismiss()
        }
    }

    LaunchedEffect(visible) {
        if (visible) {
            delay(350.milliseconds)
            showHint = true
            delay(2200.milliseconds)
            showHint = false
        }
    }

    val pulseScale = remember { Animatable(1f) }
    LaunchedEffect(visible) {
        if (visible) {
            delay(350.milliseconds)
            pulseScale.animateTo(1.04f, tween(220))
            pulseScale.animateTo(1f, tween(220))
        }
    }

    val contentScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.85f,
        animationSpec = tween(220),
        label = "contentScale"
    )
    val contentAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(200),
        label = "contentAlpha"
    )
    val scrimAlpha by animateFloatAsState(
        targetValue = if (visible) (1f - (abs(dragOffsetY) / 900f).coerceIn(0f, 0.85f)) else 0f,
        animationSpec = tween(200),
        label = "scrimAlpha"
    )
    val hintAlpha by animateFloatAsState(
        targetValue = if (showHint && !hasInteracted) 1f else 0f,
        animationSpec = tween(250),
        label = "hintAlpha"
    )

    Dialog(
        onDismissRequest = { close() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlpha))
        ) {
            AsyncImage(
                model = model,
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .graphicsLayer(
                        scaleX = scale * contentScale * pulseScale.value,
                        scaleY = scale * contentScale * pulseScale.value,
                        translationX = offset.x,
                        translationY = offset.y + dragOffsetY,
                        alpha = contentAlpha
                    )
                    .pointerInput(scale) {
                        if (scale <= 1f) {
                            detectDragGestures(
                                onDragEnd = {
                                    if (abs(dragOffsetY) > 180f) {
                                        close()
                                    } else {
                                        dragOffsetY = 0f
                                    }
                                },
                                onDragCancel = { dragOffsetY = 0f }
                            ) { change, dragAmount ->
                                if (!hasInteracted) hasInteracted = true
                                change.consume()
                                dragOffsetY += dragAmount.y
                            }
                        } else {
                            detectTransformGestures { _, pan, zoom, _ ->
                                if (!hasInteracted) hasInteracted = true
                                val newScale = (scale * zoom).coerceIn(1f, 5f)
                                scale = newScale
                                offset = if (newScale > 1f) offset + pan else Offset.Zero
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                hasInteracted = true
                                scale = if (scale > 1f) 1f else 2.5f
                                offset = Offset.Zero
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            if (zoom != 1f) {
                                hasInteracted = true
                                val newScale = (scale * zoom).coerceIn(1f, 5f)
                                scale = newScale
                                offset = if (newScale > 1f) offset + pan else Offset.Zero
                            }
                        }
                    }
            )

            IconButton(
                onClick = { close() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .graphicsLayer(alpha = contentAlpha)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = Color.White,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
                    .graphicsLayer(alpha = hintAlpha)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.tap_to_zoom),
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }
    }
}