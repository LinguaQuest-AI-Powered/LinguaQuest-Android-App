package com.iti.linguaquest.features.onBoarding.presentation.view.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun OnboardingHeroView(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_anim")

    val mascotBobbing by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mascot_bobbing"
    )

    val shadowScale by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shadow_scale"
    )

    val starRotation by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_rotation"
    )

    val starFloat by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_float"
    )

    val starScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_scale"
    )

    val globeFloat by infiniteTransition.animateFloat(
        initialValue = 5f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "globe_float"
    )

    val globeRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "globe_rotation"
    )

    val sparkleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle_alpha"
    )

    val starColor = LinguaQuestTheme.colors.LeaderboardGold
    val globeColor = LinguaQuestTheme.colors.AchievementCyanText.copy(alpha = 0.75f)
    val shadowColor = LinguaQuestTheme.colors.blackColor

    Box(
        modifier = modifier
            .fillMaxWidth(0.92f)
            .aspectRatio(1.05f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-14).dp)
                .width(150.dp)
                .height(18.dp)
                .graphicsLayer {
                    scaleX = shadowScale
                    scaleY = shadowScale
                }
        ) {
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        shadowColor.copy(alpha = 0.16f),
                        shadowColor.copy(alpha = 0.06f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.width / 2f
                )
            )
        }

        Canvas(
            modifier = Modifier
                .align(BiasAlignment(-0.78f, -0.75f))
                .size(34.dp)
                .graphicsLayer {
                    translationY = starFloat.dp.toPx()
                    rotationZ = starRotation
                    scaleX = starScale
                    scaleY = starScale
                }
        ) {
            val path = Path()
            val points = 5
            val outerRadius = size.minDimension / 2f * 0.92f
            val innerRadius = outerRadius * 0.44f
            val cx = size.width / 2f
            val cy = size.height / 2f

            for (i in 0 until points * 2) {
                val r = if (i % 2 == 0) outerRadius else innerRadius
                val angle = (i * PI / points - PI / 2).toFloat()
                val x = cx + r * cos(angle)
                val y = cy + r * sin(angle)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            drawPath(
                path = path,
                color = starColor,
                style = Stroke(
                    width = 2.5.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        Canvas(
            modifier = Modifier
                .align(BiasAlignment(0.86f, -0.08f))
                .size(32.dp)
                .graphicsLayer {
                    translationY = globeFloat.dp.toPx()
                }
        ) {
            val strokeWidth = 2.dp.toPx()
            val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
            val radius = (size.minDimension - strokeWidth) / 2f
            val centerOffset = Offset(size.width / 2f, size.height / 2f)

            drawCircle(
                color = globeColor,
                radius = radius,
                center = centerOffset,
                style = stroke
            )

            drawLine(
                color = globeColor,
                start = Offset(centerOffset.x - radius, centerOffset.y),
                end = Offset(centerOffset.x + radius, centerOffset.y),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )

            drawLine(
                color = globeColor,
                start = Offset(centerOffset.x, centerOffset.y - radius),
                end = Offset(centerOffset.x, centerOffset.y + radius),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )

            val innerWidth = radius * 0.65f
            drawOval(
                color = globeColor,
                topLeft = Offset(centerOffset.x - innerWidth, centerOffset.y - radius),
                size = Size(innerWidth * 2f, radius * 2f),
                style = stroke
            )
        }

        Canvas(
            modifier = Modifier
                .align(BiasAlignment(0.72f, -0.72f))
                .size(16.dp)
                .graphicsLayer {
                    alpha = sparkleAlpha
                    rotationZ = globeRotation * 0.5f
                }
        ) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val arm = size.minDimension / 2f
            val path = Path().apply {
                moveTo(cx, cy - arm)
                quadraticTo(cx, cy, cx + arm, cy)
                quadraticTo(cx, cy, cx, cy + arm)
                quadraticTo(cx, cy, cx - arm, cy)
                quadraticTo(cx, cy, cx, cy - arm)
                close()
            }
            drawPath(
                path = path,
                color = starColor.copy(alpha = 0.85f)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.lingo_onboarding),
            contentDescription = stringResource(R.string.cd_linguaquest_mascot),
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = mascotBobbing.dp.toPx()
                },
            contentScale = ContentScale.Fit
        )
    }
}
