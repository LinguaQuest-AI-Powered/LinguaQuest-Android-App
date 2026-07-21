package com.iti.linguaquest.features.map.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R

@Composable
fun LevelNode(
    levelNumber: Int,
    status: LevelStatus,
    stars: Int,
    offsetX: Dp,
    offsetY: Dp,
    isLastLevel: Boolean = false,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "halo")

    // Pulse (scale) instead of vertical movement, so CURRENT / last-level nodes
    // stay anchored exactly on the map path while still drawing attention.
    val pulseScale by if (status == LevelStatus.CURRENT || isLastLevel) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isLastLevel) 1.15f else 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = if (isLastLevel) 800 else 1000,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseScale"
        )
    } else {
        remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    }

    Box(
        modifier = Modifier
            .absoluteOffset(x = offsetX, y = offsetY)
            .size(100.dp)
            .clickable(enabled = status != LevelStatus.LOCKED) { onClick() }
    ) {
        val circleSize = if (status == LevelStatus.CURRENT) 70.dp else 60.dp
        val backgroundColor = when (status) {
            LevelStatus.COMPLETED -> MaterialTheme.colorScheme.tertiary
            LevelStatus.CURRENT -> MaterialTheme.colorScheme.primary
            LevelStatus.LOCKED -> LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.4f)
        }
        val borderModifier = if (status == LevelStatus.CURRENT) {
            Modifier.border(4.dp, LinguaQuestTheme.colors.whiteColor, CircleShape)
        } else if (status == LevelStatus.COMPLETED) {
            Modifier.border(2.dp, LinguaQuestTheme.colors.SuccessAccent, CircleShape)
        } else {
            Modifier
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            if (status == LevelStatus.CURRENT) {
                val haloSize = if (isLastLevel) 220.dp else 120.dp
                Box(
                    modifier = Modifier
                        .size(haloSize)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.6f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            if (isLastLevel) {
                Image(
                    painter = painterResource(id = R.drawable.ic_treasure_chest),
                    contentDescription = "Treasure",
                    modifier = Modifier
                        .size(if (status == LevelStatus.CURRENT) 180.dp else 150.dp)
                        .scale(pulseScale)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .then(borderModifier),
                    contentAlignment = Alignment.Center
                ) {
                    if (status == LevelStatus.LOCKED) {
                        Icon(
                            painter = painterResource(id = R.drawable.lock),
                            contentDescription = "Locked",
                            tint = LinguaQuestTheme.colors.whiteColor,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = levelNumber.toString(),
                            color = LinguaQuestTheme.colors.whiteColor,
                            fontSize = if (status == LevelStatus.CURRENT) 32.sp else 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (status == LevelStatus.COMPLETED) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (i in 1..3) {
                    val isFilled = i <= stars
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Star",
                        tint = if (isFilled) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}