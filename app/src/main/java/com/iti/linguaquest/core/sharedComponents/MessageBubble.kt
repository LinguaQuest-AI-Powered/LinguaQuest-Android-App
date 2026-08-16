package com.iti.linguaquest.core.sharedComponents

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

private const val BUBBLE_CORNER_RADIUS_DP = 22
private const val BUBBLE_TAIL_WIDTH_DP = 20
private const val BUBBLE_TAIL_HEIGHT_DP = 14
private const val BUBBLE_TAIL_POSITION = 0.8f
private const val BUBBLE_BORDER_WIDTH_DP = 2.5f
private const val SHADOW_BLUR_RADIUS_DP = 12
private const val SHADOW_OFFSET_Y_DP = 4
private const val SHADOW_ALPHA = 0.12f
private const val ENTRANCE_ANIM_DURATION = 450
private const val ENTRANCE_SLIDE_OFFSET = 16f

@Composable
fun MessageBubble(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    tailPosition: Float = BUBBLE_TAIL_POSITION
) {
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = ENTRANCE_ANIM_DURATION,
                easing = FastOutSlowInEasing
            )
        )
    }

    val borderBrush = Brush.linearGradient(
        colors = listOf(
            LinguaQuestTheme.colors.OrangeActive,
            LinguaQuestTheme.colors.ShadowOrange
        )
    )

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            LinguaQuestTheme.colors.MindReaderCream,
            LinguaQuestTheme.colors.MindReaderBeige
        )
    )

    val shadowColor = LinguaQuestTheme.colors.IconBoxBackground

    val bubbleShape = ChatBubbleShape(
        cornerRadius = BUBBLE_CORNER_RADIUS_DP.dp,
        tailWidth = BUBBLE_TAIL_WIDTH_DP.dp,
        tailHeight = BUBBLE_TAIL_HEIGHT_DP.dp,
        tailPosition = tailPosition
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                alpha = animProgress.value
                translationY = (1f - animProgress.value) * ENTRANCE_SLIDE_OFFSET
            }
            .widthIn(max = 300.dp)
            .drawBehind {
                val cornerPx = BUBBLE_CORNER_RADIUS_DP.dp.toPx()
                val tailWPx = BUBBLE_TAIL_WIDTH_DP.dp.toPx()
                val tailHPx = BUBBLE_TAIL_HEIGHT_DP.dp.toPx()
                val shadowBlurPx = SHADOW_BLUR_RADIUS_DP.dp.toPx()
                val shadowOffsetPx = SHADOW_OFFSET_Y_DP.dp.toPx()

                val shadowPath = buildChatBubblePath(
                    size = size,
                    cornerRadiusPx = cornerPx,
                    tailWidthPx = tailWPx,
                    tailHeightPx = tailHPx,
                    tailPosition = tailPosition
                )

                drawPath(
                    path = shadowPath,
                    color = shadowColor,
                    style = Stroke(
                        width = shadowBlurPx,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
            .clip(bubbleShape)
            .drawBehind {
                drawRect(brush = backgroundBrush)
            }
            .drawWithContent {
                drawContent()

                val cornerPx = BUBBLE_CORNER_RADIUS_DP.dp.toPx()
                val tailWPx = BUBBLE_TAIL_WIDTH_DP.dp.toPx()
                val tailHPx = BUBBLE_TAIL_HEIGHT_DP.dp.toPx()
                val strokePx = BUBBLE_BORDER_WIDTH_DP.dp.toPx()

                val borderPath = buildChatBubblePath(
                    size = size,
                    cornerRadiusPx = cornerPx,
                    tailWidthPx = tailWPx,
                    tailHeightPx = tailHPx,
                    tailPosition = tailPosition
                )

                drawPath(
                    path = borderPath,
                    brush = borderBrush,
                    style = Stroke(
                        width = strokePx,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .padding(bottom = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    lineHeight = 22.sp,
                    letterSpacing = 0.15.sp
                ),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = LinguaQuestTheme.colors.BrownText
            )

            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 18.sp,
                        letterSpacing = 0.1.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = LinguaQuestTheme.colors.BrownText.copy(alpha = 0.65f)
                )
            }
        }
    }
}

internal fun buildChatBubblePath(
    size: Size,
    cornerRadiusPx: Float,
    tailWidthPx: Float,
    tailHeightPx: Float,
    tailPosition: Float
): Path {
    val bodyHeight = size.height - tailHeightPx
    return Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(0f, 0f, size.width, bodyHeight),
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
            )
        )

        val tipX = (size.width * tailPosition)
            .coerceIn(tailWidthPx, size.width - tailWidthPx)

        val tailLeft = tipX - tailWidthPx / 2f
        val tailRight = tipX + tailWidthPx / 2f
        val tailTipX = tipX - tailWidthPx / 6f
        val tailTipY = size.height

        moveTo(tailLeft, bodyHeight - 1f)
        quadraticTo(
            tipX, bodyHeight - 1f,
            tailTipX, tailTipY
        )
        quadraticTo(
            tipX + tailWidthPx / 8f, bodyHeight + tailHeightPx * 0.3f,
            tailRight, bodyHeight - 1f
        )
        close()
    }
}

internal class ChatBubbleShape(
    private val cornerRadius: Dp,
    private val tailWidth: Dp,
    private val tailHeight: Dp,
    private val tailPosition: Float = 0.8f
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = buildChatBubblePath(
            size = size,
            cornerRadiusPx = with(density) { cornerRadius.toPx() },
            tailWidthPx = with(density) { tailWidth.toPx() },
            tailHeightPx = with(density) { tailHeight.toPx() },
            tailPosition = tailPosition
        )
        return Outline.Generic(path)
    }
}

