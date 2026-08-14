package com.iti.linguaquest.core.sharedComponents

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private val LINGO_SPLASH_FRAMES = listOf(
    R.drawable.lingo_splash_1,
    R.drawable.lingo_splash_2,
    R.drawable.lingo_splash_3,
    R.drawable.lingo_splash_4,
    R.drawable.lingo_splash_5,
    R.drawable.lingo_splash_6,
    R.drawable.lingo_splash_7,
    R.drawable.lingo_splash_8,
    R.drawable.lingo_splash_9,
    R.drawable.lingo_splash_10
)

@Composable
fun LingoLoadingAnimation(
    modifier: Modifier = Modifier,
    size: Dp = 110.dp,
    useSpriteAnimation: Boolean = true
) {
    var frameIndex by remember { mutableIntStateOf(0) }

    if (useSpriteAnimation) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(120L.milliseconds)
                frameIndex = (frameIndex + 1) % LINGO_SPLASH_FRAMES.size
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "lingo_bounce")

    val translateY by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translateY"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val drawableRes = if (useSpriteAnimation) {
            LINGO_SPLASH_FRAMES[frameIndex]
        } else {
            R.drawable.lingo_searching
        }

        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = "Lingo Animated Mascot",
            modifier = Modifier
                .size(size)
                .graphicsLayer {
                    translationY = translateY
                    scaleX = scale
                    scaleY = scale
                }
        )

        Spacer(modifier = Modifier.height(12.dp))

        LingoBouncingDots()
    }
}

@Composable
fun LingoSpinningIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    imageRes: Int = R.drawable.linguaquest_circle
) {
    val infiniteTransition = rememberInfiniteTransition(label = "lingo_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Loading",
        modifier = modifier
            .size(size)
            .graphicsLayer {
                rotationZ = rotation
            }
    )
}

@Composable
fun LingoBouncingDots(
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    dotColor: Color = LinguaQuestTheme.colors.BrownText
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    val dot1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, delayMillis = 0, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )

    val dot2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, delayMillis = 150, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )

    val dot3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, delayMillis = 300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dotSize)
                .graphicsLayer { alpha = dot1Alpha }
                .background(dotColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(dotSize)
                .graphicsLayer { alpha = dot2Alpha }
                .background(dotColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(dotSize)
                .graphicsLayer { alpha = dot3Alpha }
                .background(dotColor, CircleShape)
        )
    }
}
