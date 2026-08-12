package com.iti.linguaquest.features.onBoarding.presentation.view.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun ShimmerLogoView(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerProgress by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, delayMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_progress"
    )

    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.linguaquest_logo),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            contentScale = ContentScale.FillWidth
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer(alpha = 0.99f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.linguaquest_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                contentScale = ContentScale.FillWidth
            )
            
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        blendMode = BlendMode.SrcIn
                    }
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.7f),
                                Color.Transparent
                            ),
                            start = Offset(x = shimmerProgress * 1000f - 200f, y = 0f),
                            end = Offset(x = shimmerProgress * 1000f + 200f, y = 1000f)
                        )
                    )
            )
        }
    }
}
