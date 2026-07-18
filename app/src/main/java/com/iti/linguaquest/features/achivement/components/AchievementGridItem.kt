package com.iti.linguaquest.features.achivement.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.features.achivement.model.AchievementItem
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AchievementGridItem(
    item: AchievementItem,
    index: Int,
    modifier: Modifier = Modifier
) {
     var visible by remember { mutableStateOf(index >= 8) }

    LaunchedEffect(Unit) {
        if (index < 8) {
            val baseDelay = 100L
            val itemDelay = baseDelay + (index * 50L)
        delay(itemDelay.milliseconds)
            visible = true
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.5f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "scale"
    )
    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .alpha(alphaAnim)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .background(AppColors.AchievementCardBorder, RoundedCornerShape(16.dp))
            .padding(bottom = 3.dp)
            .background(AppColors.White, RoundedCornerShape(16.dp))
            .border(1.dp, AppColors.AchievementCardBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
             Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (item.isEarned) AppColors.AchievementCyanBackground else AppColors.SecondaryColor,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                ImageWrapper(
                    model = item.iconRes,
                    contentDescription = item.title,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (item.isEarned) AppColors.BrownText else AppColors.BrownText.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (item.isEarned && item.dateEarned != null) {
                Box(
                    modifier = Modifier
                        .background(AppColors.AchievementCyanBackground, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.dateEarned,
                        color = AppColors.AchievementCyanText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                 Box(
                    modifier = Modifier
                        .background(AppColors.SecondaryColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Locked",
                        color = AppColors.TitleAndCaptionColor.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
