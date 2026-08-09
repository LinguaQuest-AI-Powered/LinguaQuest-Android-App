package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme


import androidx.compose.ui.platform.LocalDensity

@Composable
fun DailyRewardTimeline(
    modifier: Modifier = Modifier,
    currentDay: Int,
    cycleLength: Int = 5
) {
    val totalNodes = cycleLength.coerceAtLeast(1)
    val safeCurrentDay = currentDay.coerceIn(1, totalNodes)
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = LinguaQuestTheme.colors.DailyRewardInactiveLine

    val displayedDays = (1..totalNodes).toList()
    val activeNodesCount = (safeCurrentDay - 1).coerceIn(0, totalNodes - 1)

    val scrollState = rememberScrollState()
    val nodeWidth = 56.dp
    val nodeSpacing = 16.dp
    val density = LocalDensity.current

    var containerWidth by remember { mutableStateOf(0) }
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(safeCurrentDay, totalNodes, containerWidth) {
        startAnimation = true
        if (totalNodes > 1 && safeCurrentDay > 1 && containerWidth > 0) {
            val itemWidthPx = with(density) { (nodeWidth + nodeSpacing).toPx() }
            val targetPx = (safeCurrentDay - 1) * itemWidthPx
            val halfViewportPx = containerWidth / 2f
            val scrollToPx = (targetPx - halfViewportPx + itemWidthPx / 2f).coerceAtLeast(0f)
            scrollState.animateScrollTo(scrollToPx.toInt(), animationSpec = tween(800, easing = FastOutSlowInEasing))
        }
    }

    val animatedLineProgress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "lineProgress"
    )
    val totalWidth = if (totalNodes <= 1) nodeWidth else (nodeWidth * totalNodes + nodeSpacing * (totalNodes - 1))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { containerWidth = it.width }
            .horizontalScroll(scrollState),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier.width(totalWidth),
            contentAlignment = Alignment.TopStart
        ) {
            val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
            Canvas(
                modifier = Modifier
                    .width(totalWidth)
                    .padding(horizontal = 28.dp)
                    .height(48.dp)
                    .scale(scaleX = if (isRtl) -1f else 1f, scaleY = 1f)
            ) {
                val yOffset = size.height / 2

                drawLine(
                    color = inactiveColor,
                    start = Offset(0f, yOffset),
                    end = Offset(size.width, yOffset),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )

                if (activeNodesCount > 0 && totalNodes > 1) {
                    val segmentWidth = size.width / (totalNodes - 1)
                    val activeEnd = segmentWidth * activeNodesCount * animatedLineProgress
                    drawLine(
                        color = activeColor,
                        start = Offset(0f, yOffset),
                        end = Offset(activeEnd, yOffset),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            Row(
                modifier = Modifier.width(totalWidth),
                horizontalArrangement = Arrangement.spacedBy(nodeSpacing),
                verticalAlignment = Alignment.Top
            ) {
                for ((index, day) in displayedDays.withIndex()) {
                    val isCompleted = day < safeCurrentDay

                    val nodeScale by animateFloatAsState(
                        targetValue = if (startAnimation) 1f else 0f,
                        animationSpec = tween(
                            durationMillis = 400,
                            delayMillis = index * 150,
                            easing = FastOutSlowInEasing
                        ),
                        label = "nodeScale"
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(nodeWidth)
                    ) {
                        Box(
                            modifier = Modifier.height(48.dp).scale(nodeScale),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                isCompleted -> CompletedDayNode()
                                day == safeCurrentDay -> CurrentDayNode()
                                else -> LockedDayNode()
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = stringResource(id = R.string.day_format, day),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (day == safeCurrentDay) LinguaQuestTheme.colors.DailyRewardActiveText else LinguaQuestTheme.colors.DailyRewardInactiveText,
                                fontWeight = if (day == safeCurrentDay) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
