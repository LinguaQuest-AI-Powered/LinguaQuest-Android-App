package com.iti.linguaquest.features.auth.share.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.shake(shakeTrigger: Int): Modifier = composed {
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(shakeTrigger) {
        if (shakeTrigger <= 0) return@LaunchedEffect

        shakeOffset.snapTo(0f)
        shakeOffset.animateTo(
            targetValue = 0f,
            animationSpec = keyframes {
                durationMillis = 300
                0f at 0
                10f at 60
                10f at 120
                7f at 180
                4.5f at 240
                0f at 300
            }
        )
    }

    this.graphicsLayer {
        translationX = shakeOffset.value
    }
}
