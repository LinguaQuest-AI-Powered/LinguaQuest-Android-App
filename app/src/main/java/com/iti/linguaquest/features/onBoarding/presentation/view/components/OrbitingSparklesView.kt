package com.iti.linguaquest.features.onBoarding.presentation.view.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun OrbitingSparklesView(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "sparkles")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkle_progress"
    )

    Box(modifier = modifier) {
        val sparkleCount = 8
        val baseRadius = 130f
        
        for (i in 0 until sparkleCount) {
            val angleOffset = (i.toFloat() / sparkleCount) * 2 * Math.PI.toFloat()
            val radiusMultiplier = 1f + ((i % 3) - 1) * 0.1f 
            val radius = baseRadius * radiusMultiplier
            val angle = angleOffset + (progress * 2 * Math.PI.toFloat() * (if (i % 2 == 0) 1 else -1))
            
            val xOffset = cos(angle) * radius
            val yOffset = sin(angle) * radius
            
            val opacityPhase = (progress * 5f + i) % 1f
            val opacity = if (opacityPhase > 0.5f) (1f - opacityPhase) * 2f else opacityPhase * 2f

            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = LinguaQuestTheme.colors.whiteColor,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = xOffset.dp, y = yOffset.dp)
                    .size(if (i % 2 == 0) 16.dp else 10.dp)
                    .alpha(opacity)
            )
        }
    }
}
