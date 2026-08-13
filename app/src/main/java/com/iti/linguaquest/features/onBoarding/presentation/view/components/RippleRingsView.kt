package com.iti.linguaquest.features.onBoarding.presentation.view.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun RippleRingsView(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "ripple")
    
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple_progress"
    )

    Box(modifier = modifier.size(300.dp), contentAlignment = Alignment.Center) {
        for (i in 0..2) {
            val ringProgress = (progress + (i * 0.33f)) % 1f
            val scale = 0.5f + (ringProgress * 1.0f)
            val opacity = if (ringProgress < 0.2f) {
                ringProgress / 0.2f * 0.6f
            } else {
                (1f - ringProgress) / 0.8f * 0.6f
            }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
                    .alpha(opacity)
                    .border(2.dp, LinguaQuestTheme.colors.whiteColor, CircleShape)
            )
        }
    }
}
