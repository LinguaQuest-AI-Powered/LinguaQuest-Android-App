package com.iti.linguaquest.features.onBoarding.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LinguaQuestSplashScreen(modifier: Modifier = Modifier) {
    val frames = listOf(
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

    var currentFrameIndex by remember { mutableIntStateOf(0) }
    var circleVisible by remember { mutableStateOf(false) }
    var logoVisible by remember { mutableStateOf(false) }
    var birdOffsetY by remember { mutableFloatStateOf(-80f) }

    LaunchedEffect(Unit) {
        logoVisible = true
        circleVisible = true
        delay(200.milliseconds)
        while (currentFrameIndex < frames.size - 1) {
            delay(120.milliseconds)
            currentFrameIndex++
            birdOffsetY = when (currentFrameIndex) {
                0 -> -80f
                1 -> -60f
                2 -> -40f
                3 -> -20f
                4 -> -10f
                else -> 0f
            }
        }
        delay(400.milliseconds)
    }

    val circleScale by animateFloatAsState(
        targetValue = if (circleVisible) 1f else 0.2f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "circle_scale"
    )
    val circleAlpha by animateFloatAsState(
        targetValue = if (circleVisible) 1f else 0f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "circle_alpha"
    )

    val logoOffsetY by animateFloatAsState(
        targetValue = if (logoVisible) 0f else -100f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "logo_offset"
    )
    val logoAlpha by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0f,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "logo_alpha"
    )

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            LinguaQuestTheme.colors.splashTopLeftColor,
            LinguaQuestTheme.colors.splashBottomRightColor
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.linguaquest_circle),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = circleScale
                            scaleY = circleScale
                            alpha = circleAlpha
                        },
                    contentScale = ContentScale.Fit
                )

                Image(
                    painter = painterResource(id = frames[currentFrameIndex]),
                    contentDescription = "LinguaQuest mascot animation",
                    modifier = Modifier
                        .fillMaxSize(0.72f)
                        .align(BiasAlignment(horizontalBias = -0.12f, verticalBias = 0f))
                        .graphicsLayer {
                            translationY = birdOffsetY
                        },
                    contentScale = ContentScale.Fit
                )
            }


            Image(
                painter = painterResource(id = R.drawable.linguaquest_logo),
                contentDescription = "LinguaQuest",
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .wrapContentHeight()
                    .graphicsLayer {
                        translationY = logoOffsetY
                        alpha = logoAlpha
                    },
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.weight(1.2f))
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 890)
@Composable
private fun LinguaQuestSplashScreenPreview() {
    LinguaQuestSplashScreen()
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun LinguaQuestSplashScreenPreviewLarge() {
    LinguaQuestSplashScreen()
}