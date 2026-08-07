package com.iti.linguaquest.core.sharedComponents

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

private const val BUBBLE_CORNER_RADIUS_DP = 20
private const val BUBBLE_TAIL_WIDTH_DP = 22
private const val BUBBLE_TAIL_HEIGHT_DP = 12
private const val BUBBLE_TAIL_POSITION = 0.2f
private const val BUBBLE_BORDER_WIDTH_DP = 3

@Composable
fun MessageBubble(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    tailPosition: Float = BUBBLE_TAIL_POSITION
) {
    val borderTransition = rememberInfiniteTransition(label = "bubble_border")
    val borderShift by borderTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "border_shift"
    )

    val borderColorA = LinguaQuestTheme.colors.splashTopLeftColor
    val borderColorB = LinguaQuestTheme.colors.OrangeActive
    val borderColorC = LinguaQuestTheme.colors.ShadowOrange

    Box(
        modifier = modifier
            .widthIn(max = 300.dp)
            .clip(
                ChatBubbleShape(
                    cornerRadius = BUBBLE_CORNER_RADIUS_DP.dp,
                    tailWidth = BUBBLE_TAIL_WIDTH_DP.dp,
                    tailHeight = BUBBLE_TAIL_HEIGHT_DP.dp,
                    tailPosition = tailPosition
                )
            )
            .background(LinguaQuestTheme.colors.whiteColor)
            .drawWithContent {
                drawContent()

                val cornerPx = BUBBLE_CORNER_RADIUS_DP.dp.toPx()
                val tailWPx = BUBBLE_TAIL_WIDTH_DP.dp.toPx()
                val tailHPx = BUBBLE_TAIL_HEIGHT_DP.dp.toPx()
                val strokePx = BUBBLE_BORDER_WIDTH_DP.dp.toPx()

                val bubblePath = buildChatBubblePath(
                    size = size,
                    cornerRadiusPx = cornerPx,
                    tailWidthPx = tailWPx,
                    tailHeightPx = tailHPx,
                    tailPosition = tailPosition
                )
                val span = size.width + size.height
                val travel = borderShift * span
                val start = Offset(travel - span, 0f)
                val end = Offset(travel, size.height)

                drawPath(
                    path = bubblePath,
                    brush = Brush.linearGradient(
                        colors = listOf(borderColorA, borderColorB, borderColorC, borderColorB, borderColorA),
                        start = start,
                        end = end
                    ),
                    style = Stroke(
                        width = strokePx,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .padding(bottom = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = LinguaQuestTheme.colors.BrownText
            )

            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
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

        moveTo(tipX - tailWidthPx / 2f, bodyHeight - 1f)
        lineTo(tipX + tailWidthPx / 2f, bodyHeight - 1f)
        lineTo(tipX - tailWidthPx / 4f, size.height)
        close()
    }
}

internal class ChatBubbleShape(
    private val cornerRadius: Dp,
    private val tailWidth: Dp,
    private val tailHeight: Dp,
    private val tailPosition: Float = 0.2f
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
