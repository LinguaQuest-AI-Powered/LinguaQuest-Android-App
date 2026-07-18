package com.iti.linguaquest.features.leaderboard.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.features.profile.presentation.model.LeaderboardEntry
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

fun Modifier.coloredShadow(
    color: Color,
    borderRadius: Dp = 16.dp,
    blurRadius: Dp = 16.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp
): Modifier = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            asFrameworkPaint().apply {
                isAntiAlias = true
                this.color = android.graphics.Color.TRANSPARENT
                setShadowLayer(
                    blurRadius.toPx(),
                    offsetX.toPx(),
                    offsetY.toPx(),
                    color.copy(alpha = 0.35f).toArgb()
                )
            }
        }
        canvas.drawRoundRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
            radiusX = borderRadius.toPx(),
            radiusY = borderRadius.toPx(),
            paint = paint
        )
    }
}

@Composable
fun PodiumSection(topThree: List<LeaderboardEntry>) {
    val first = topThree.find { it.rank == 1 }
    val second = topThree.find { it.rank == 2 }
    val third = topThree.find { it.rank == 3 }

    var parrotVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(1800.milliseconds)
        parrotVisible = true
    }

    val parrotScale by animateFloatAsState(
        targetValue = if (parrotVisible) 1f else 0.5f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow)
    )
    val parrotAlpha by animateFloatAsState(
        targetValue = if (parrotVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        ImageWrapper(
            model = R.drawable.lingo_leaderboard,
            contentDescription = null,
            modifier = Modifier
                .size(175.dp)
                .zIndex(4f)
                .graphicsLayer {
                    scaleX = parrotScale
                    scaleY = parrotScale
                    alpha = parrotAlpha
                }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 135.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                if (second != null) {
                    PodiumItem(
                        entry = second,
                        rankColor = AppColors.LeaderboardBlue,
                        cardHeight = 130.dp,
                        delayMillis = 600
                    )
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                if (first != null) {
                    PodiumItem(
                        entry = first,
                        rankColor = AppColors.LeaderboardGold,
                        cardHeight = 165.dp,
                        isFirst = true,
                        delayMillis = 1200
                    )
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                if (third != null) {
                    PodiumItem(
                        entry = third,
                        rankColor = AppColors.LeaderboardBronze,
                        cardHeight = 130.dp,
                        delayMillis = 0
                    )
                }
            }
        }
    }
}
