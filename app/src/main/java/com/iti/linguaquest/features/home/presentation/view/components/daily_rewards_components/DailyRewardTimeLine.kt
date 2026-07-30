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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme


@Composable
fun DailyRewardTimeline(
    modifier: Modifier = Modifier,
    currentDay: Int,
    cycleLength: Int = 5
) {
    val maxVisibleNodes = 5
    val totalNodes = minOf(cycleLength.coerceAtLeast(1), maxVisibleNodes)
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = LinguaQuestTheme.colors.DailyRewardInactiveLine

    val startDay = maxOf(1, minOf(currentDay - 2, (cycleLength - totalNodes + 1).coerceAtLeast(1)))
    val endDay = minOf(cycleLength.coerceAtLeast(1), startDay + totalNodes - 1)
    val displayedDays = (startDay..endDay).toList()

    val activeNodesCount = displayedDays.count { it < currentDay }

    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        startAnimation = true
    }

    val animatedLineProgress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "lineProgress"
    )

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        val isRtl = androidx.compose.ui.platform.LocalLayoutDirection.current == androidx.compose.ui.unit.LayoutDirection.Rtl
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
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

            if (activeNodesCount > 0) {
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            for ((index, day) in displayedDays.withIndex()) {
                val isCompleted = day < currentDay

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
                    modifier = Modifier.width(56.dp)
                ) {
                    Box(
                        modifier = Modifier.height(48.dp).scale(nodeScale),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            isCompleted -> CompletedDayNode()
                            day == currentDay -> CurrentDayNode()
                            else -> LockedDayNode()
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(id = R.string.day_format, day),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (day == currentDay) LinguaQuestTheme.colors.DailyRewardActiveText else LinguaQuestTheme.colors.DailyRewardInactiveText,
                            fontWeight = if (day == currentDay) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
