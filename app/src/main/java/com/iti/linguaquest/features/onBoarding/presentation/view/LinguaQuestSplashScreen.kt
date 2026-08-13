package com.iti.linguaquest.features.onBoarding.presentation.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.onBoarding.presentation.view.components.OrbitingSparklesView
import com.iti.linguaquest.features.onBoarding.presentation.view.components.RippleRingsView
import com.iti.linguaquest.features.onBoarding.presentation.view.components.ShimmerLogoView
import com.iti.linguaquest.features.onBoarding.presentation.view.components.SplashVideoView
import com.iti.linguaquest.features.onBoarding.presentation.view.components.TwinklingStarsView
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LinguaQuestSplashScreen(
    modifier: Modifier = Modifier,
    shouldStop: Boolean = false
) {
    var circleVisible by remember { mutableStateOf(false) }
    var logoVisible by remember { mutableStateOf(false) }
    var bottomTextVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        logoVisible = true
        circleVisible = true
        delay(700.milliseconds)
        bottomTextVisible = true
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

    val bottomTextOffsetY by animateFloatAsState(
        targetValue = if (bottomTextVisible) 0f else 50f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "bottom_text_offset"
    )
    val bottomTextAlpha by animateFloatAsState(
        targetValue = if (bottomTextVisible) 1f else 0f,
        animationSpec = tween(800, easing = LinearEasing),
        label = "bottom_text_alpha"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (shouldStop) 0f else 1f,
        animationSpec = tween(800, easing = LinearEasing),
        label = "content_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = LinguaQuestTheme.colors.splashBackgroundSolid)
    ) {
        Box(modifier = Modifier.fillMaxSize().graphicsLayer { alpha = contentAlpha }) {
            RippleRingsView(modifier = Modifier.align(Alignment.Center))

            ShimmerLogoView(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth(0.88f)
                    .wrapContentHeight()
                    .graphicsLayer {
                        translationY = logoOffsetY
                        alpha = logoAlpha
                    }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 20.dp)
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

                TwinklingStarsView(modifier = Modifier.fillMaxSize())
                OrbitingSparklesView(modifier = Modifier.fillMaxSize())

                SplashVideoView(
                    modifier = Modifier
                        .fillMaxSize(0.72f)
                        .align(Alignment.Center),
                    shouldStop = shouldStop
                )
            }

            Text(
                text = stringResource(id = R.string.app_name),
                color = LinguaQuestTheme.colors.whiteColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
                    .graphicsLayer {
                        translationY = bottomTextOffsetY
                        alpha = bottomTextAlpha
                    }
            )
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