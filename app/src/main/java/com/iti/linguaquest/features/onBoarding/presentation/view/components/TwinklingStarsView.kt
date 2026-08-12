package com.iti.linguaquest.features.onBoarding.presentation.view.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.ui.unit.dp

@Composable
fun TwinklingStarsView(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    
    val star1Opacity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star1_opacity"
    )
    
    val star2Opacity by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star2_opacity"
    )

    Box(modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = LinguaQuestTheme.colors.whiteColor,
            modifier = Modifier
                .align(BiasAlignment(-0.6f, -0.4f))
                .size(36.dp)
                .alpha(star1Opacity)
        )
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = LinguaQuestTheme.colors.whiteColor,
            modifier = Modifier
                .align(BiasAlignment(0.7f, 0.5f))
                .size(24.dp)
                .alpha(star2Opacity)
        )
    }
}
