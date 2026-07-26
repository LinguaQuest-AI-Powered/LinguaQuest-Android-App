package com.iti.linguaquest.core.sharedComponents.offline

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun NoInternetMiniPopup(
    isOnline: Boolean,
    modifier: Modifier = Modifier,
    message: String? = null,
    onDismiss: () -> Unit
) {
    val resolvedMessage = message ?: stringResource(R.string.no_internet_short)

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    LaunchedEffect(isOnline) {
        if (isOnline && visible) {
            visible = false
        }
    }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(200.milliseconds)
            onDismiss()
        }
    }

     val borderTransition = rememberInfiniteTransition(label = "mini_bubble_border")
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

    val bubbleShape = LeftTailBubbleShape(
        cornerRadius = 16.dp,
        tailWidth = 8.dp,
        tailHeight = 10.dp,
        tailPositionY = 0.3f
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(250)) +
                scaleIn(initialScale = 0.85f, animationSpec = tween(250)),
        exit = fadeOut(animationSpec = tween(200)) +
                scaleOut(targetScale = 0.85f, animationSpec = tween(200)),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        visible = false
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((-22).dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lingo_parrot_pointing),
                    contentDescription = null,
                    modifier = Modifier.size(112.dp),
                    contentScale = ContentScale.Fit
                )

                Box(
                    modifier = Modifier
                        .offset(x = (-18).dp)
                        .widthIn(max = 204.dp)
                        .clip(bubbleShape)
                        .background(LinguaQuestTheme.colors.whiteColor)
                    .drawWithContent {
                        drawContent()

                            val cornerPx = 16.dp.toPx()
                            val tailWPx = 8.dp.toPx()
                            val tailHPx = 10.dp.toPx()
                            val strokePx = 2.5.dp.toPx()

                            val bubblePath = buildLeftTailBubblePath(
                                size = size,
                                cornerRadiusPx = cornerPx,
                                tailWidthPx = tailWPx,
                                tailHeightPx = tailHPx,
                                tailPositionY = 0.3f
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
                        .padding(
                            start = 14.dp,
                            end = 14.dp,
                            top = 10.dp,
                            bottom = 12.dp
                        )
                ) {
                    Text(
                        text = resolvedMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private fun buildLeftTailBubblePath(
    size: Size,
    cornerRadiusPx: Float,
    tailWidthPx: Float,
    tailHeightPx: Float,
    tailPositionY: Float
): Path {
    val bodyLeft = tailWidthPx
    return Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(bodyLeft, 0f, size.width, size.height),
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
            )
        )

        val tipY = (size.height * tailPositionY)
            .coerceIn(tailHeightPx, size.height - tailHeightPx)

        moveTo(bodyLeft + 1f, tipY - tailHeightPx / 2f)
        lineTo(bodyLeft + 1f, tipY + tailHeightPx / 2f)
        lineTo(0f, tipY)
        close()
    }
}

private class LeftTailBubbleShape(
    private val cornerRadius: Dp,
    private val tailWidth: Dp,
    private val tailHeight: Dp,
    private val tailPositionY: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = buildLeftTailBubblePath(
            size = size,
            cornerRadiusPx = with(density) { cornerRadius.toPx() },
            tailWidthPx = with(density) { tailWidth.toPx() },
            tailHeightPx = with(density) { tailHeight.toPx() },
            tailPositionY = tailPositionY
        )
        return Outline.Generic(path)
    }
}
