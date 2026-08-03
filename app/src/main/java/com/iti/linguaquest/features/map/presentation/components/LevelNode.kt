package com.iti.linguaquest.features.map.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private const val INTRO_DURATION = 1500
private const val DOT_COUNT = 8

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

    val interactionSource = remember { MutableInteractionSource() }
    var showIntro by remember { androidx.compose.runtime.mutableStateOf(true) }
    val introScale = remember { Animatable(0.9f) }

    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    LaunchedEffect(Unit) {
        launch {
            introScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
        }
        launch {
            kotlinx.coroutines.delay(INTRO_DURATION.toLong())
            showIntro = false
        }
    }

    Box(
        modifier = Modifier
            .absoluteOffset(
                x = offsetX,
                y = offsetY
            )
            .size(100.dp)
            .scale(if (showIntro) introScale.value else 1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = status != LevelStatus.LOCKED
            ) { onClick() }
    ) {
        if (showIntro) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.align(Alignment.Center)
            ) {
                // Large soft outer glow (Sun Corona)
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(if (showIntro) glowPulse else 1f)
                        .alpha(if (showIntro) glowPulse * 0.7f else 1f)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.6f),
                                    Color.White.copy(alpha = 0.3f),
                                    Color.White.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Intense inner core glow (Sun core)
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(if (showIntro) glowPulse else 1f)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White,
                                    Color.White.copy(alpha = 0.9f),
                                    Color.White.copy(alpha = 0.5f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Star bubble image (breathes slightly to enhance glow effect)
                Image(
                    painter = painterResource(id = R.drawable.lingo_start),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .scale(if (showIntro) 0.9f + (glowPulse - 0.6f) * 0.4f else 1f)
                )
            }
        }

        val circleSize = if (status == LevelStatus.CURRENT) 64.dp else 56.dp
        val backgroundBrush = when (status) {
            LevelStatus.COMPLETED -> Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f),
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
                )
            )
            LevelStatus.CURRENT -> Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                )
            )
            LevelStatus.LOCKED -> Brush.linearGradient(
                colors = listOf(
                    LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.4f),
                    LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.1f)
                )
            )
        }
        
        val borderModifier = if (status == LevelStatus.CURRENT) {
            Modifier.border(3.dp, LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.9f), CircleShape)
        } else if (status == LevelStatus.COMPLETED) {
            Modifier.border(2.dp, LinguaQuestTheme.colors.SuccessAccent.copy(alpha = 0.7f), CircleShape)
        } else {
            Modifier.border(1.dp, LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.3f), CircleShape)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(if (showIntro) 0f else 1f)
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
                val context = androidx.compose.ui.platform.LocalContext.current
                val prefs = remember { context.getSharedPreferences("linguaquest_prefs", android.content.Context.MODE_PRIVATE) }
                val chestKey = "chest_opened_$levelNumber"
                val hasOpened = remember { prefs.getBoolean(chestKey, false) }
                
                var currentFrame by remember { 
                    androidx.compose.runtime.mutableIntStateOf(
                        if (status == LevelStatus.COMPLETED && hasOpened) 6 else 0
                    ) 
                }
                
                LaunchedEffect(status) {
                    if (status == LevelStatus.COMPLETED && !hasOpened) {
                        kotlinx.coroutines.delay(500) // Small delay before animating
                        for (i in 1..6) {
                            currentFrame = i
                            kotlinx.coroutines.delay(150) // 150ms per frame
                        }
                        prefs.edit().putBoolean(chestKey, true).apply()
                    }
                }
                
                val chestImageRes = when(currentFrame) {
                    0 -> R.drawable.ic_treasure_chest_close
                    1 -> R.drawable.lingo_map_1
                    2 -> R.drawable.lingo_map_2
                    3 -> R.drawable.lingo_map_3
                    4 -> R.drawable.lingo_map_4
                    5 -> R.drawable.lingo_map_5
                    else -> R.drawable.lingo_map_6
                }

                Image(
                    painter = painterResource(id = chestImageRes),
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
                        .background(backgroundBrush)
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
    }
}