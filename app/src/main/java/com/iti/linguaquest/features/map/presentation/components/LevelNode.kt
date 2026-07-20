package com.iti.linguaquest.features.map.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.iti.linguaquest.core.theme.AppColors

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
    val floatOffset by if (status == LevelStatus.CURRENT) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -12f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "float"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    val scale by if (isLastLevel) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )
    } else {
        remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    }

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY + floatOffset.dp)
            .size(100.dp)
            .clickable(enabled = status != LevelStatus.LOCKED) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val circleSize = if (status == LevelStatus.CURRENT) 70.dp else 60.dp
            val backgroundColor = when (status) {
                LevelStatus.COMPLETED -> AppColors.Teal
                LevelStatus.CURRENT -> AppColors.PrimaryColor
                LevelStatus.LOCKED -> Color.White.copy(alpha = 0.4f)
            }
            val borderModifier = if (status == LevelStatus.CURRENT) {
                Modifier.border(4.dp, Color.White, CircleShape)
            } else if (status == LevelStatus.COMPLETED) {
                Modifier.border(2.dp, Color(0xFF004D40), CircleShape)
            } else {
                Modifier
            }

            Box(contentAlignment = Alignment.Center) {
                if (status == LevelStatus.CURRENT) {
                    val haloSize = if (isLastLevel) 220.dp else 120.dp
                    Box(
                        modifier = Modifier
                            .size(haloSize)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.6f),
                                        AppColors.PrimaryColor.copy(alpha = 0.5f),
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
                            .scale(scale)
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
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = levelNumber.toString(),
                                color = Color.White,
                                fontSize = if (status == LevelStatus.CURRENT) 32.sp else 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (status == LevelStatus.COMPLETED) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    for (i in 1..3) {
                        val isFilled = i <= stars
                        Icon(
                            painter = painterResource(id = R.drawable.ic_star),
                            contentDescription = "Star",
                            tint = if (isFilled) AppColors.PrimaryColor else Color.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}
