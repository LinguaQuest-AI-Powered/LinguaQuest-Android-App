package com.iti.linguaquest.core.sharedComponents.offline

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun OfflineStateView(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    retryLabel: String? = null,
    onRetry: (() -> Unit)? = null
) {
    val resolvedTitle = title ?: stringResource(R.string.no_internet_title)
    val resolvedSubtitle = subtitle ?: stringResource(R.string.no_internet_subtitle)
    val resolvedRetryLabel = retryLabel ?: stringResource(R.string.retry_label)

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(120.milliseconds)
        visible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "offline_anim")
    val floatOffsetDp by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    val imageContentDescription = stringResource(R.string.no_internet_image_desc)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(500)) +
                    scaleIn(
                        initialScale = 0.85f,
                        animationSpec = tween(
                            500,
                            easing = FastOutSlowInEasing
                        )
                    )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MessageBubble(
                    title = resolvedTitle,
                    subtitle = resolvedSubtitle
                )

                Spacer(modifier = Modifier.height(28.dp))

                Image(
                    painter = painterResource(id = R.drawable.lingo_offline_mode),
                    contentDescription = imageContentDescription,
                    modifier = Modifier
                        .size(280.dp)
                        .offset(y = floatOffsetDp.dp),
                    contentScale = ContentScale.Fit
                )

                if (onRetry != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onRetry,
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(resolvedRetryLabel)
                    }
                }
            }
        }
    }
}

private const val BUBBLE_CORNER_RADIUS_DP = 20
private const val BUBBLE_TAIL_WIDTH_DP = 22
private const val BUBBLE_TAIL_HEIGHT_DP = 12
private const val BUBBLE_TAIL_POSITION = 0.2f
private const val BUBBLE_BORDER_WIDTH_DP = 3

@Composable
fun MessageBubble(
    title: String,
    subtitle: String?
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

    val borderColorA = MaterialTheme.colorScheme.tertiary
    val borderColorB = MaterialTheme.colorScheme.secondary
    val borderColorC = MaterialTheme.colorScheme.primaryContainer

    Box(
        modifier = Modifier
            .widthIn(max = 280.dp)
            .clip(
                ChatBubbleShape(
                    cornerRadius = BUBBLE_CORNER_RADIUS_DP.dp,
                    tailWidth = BUBBLE_TAIL_WIDTH_DP.dp,
                    tailHeight = BUBBLE_TAIL_HEIGHT_DP.dp,
                    tailPosition = BUBBLE_TAIL_POSITION
                )
            )
            .background(MaterialTheme.colorScheme.primary)
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
                    tailPosition = BUBBLE_TAIL_POSITION
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
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .padding(bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle?:" ",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
            )
        }
    }
}

private fun buildChatBubblePath(
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

private class ChatBubbleShape(
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

@Composable
fun OfflineAwareContent(
    isOnline: Boolean,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Crossfade(
        targetState = isOnline,
        animationSpec = tween(400),
        label = "offline_crossfade",
        modifier = modifier
    ) { online ->
        if (online) {
            content()
        } else {
            OfflineStateView(onRetry = onRetry)
        }
    }
}