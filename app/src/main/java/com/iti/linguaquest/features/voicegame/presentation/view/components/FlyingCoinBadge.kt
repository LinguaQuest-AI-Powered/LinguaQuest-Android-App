package com.iti.linguaquest.features.voicegame.presentation.view.components


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import kotlin.math.roundToInt

@Composable
fun FlyingCoinBadge(
    coinsAwarded: Int,
    sourceOffset: Offset,
    targetOffset: Offset,
    containerOrigin: Offset,
    onLanded: () -> Unit
) {
    val flightOffset = remember { Animatable(sourceOffset, Offset.VectorConverter) }

    LaunchedEffect(sourceOffset, targetOffset) {
        val peak = Offset(
            x = (sourceOffset.x + targetOffset.x) / 2f,
            y = minOf(sourceOffset.y, targetOffset.y) - 120f
        )

        flightOffset.animateTo(
            targetValue = targetOffset,
            animationSpec = keyframes {
                durationMillis = 1500

                sourceOffset at 0
                peak at 800 using FastOutSlowInEasing
                targetOffset at 1500 using FastOutSlowInEasing
            }
        )

        onLanded()
    }

    val local = flightOffset.value - containerOrigin

    Row(
        modifier = Modifier
            .offset { IntOffset(local.x.roundToInt(), local.y.roundToInt()) }
            .clip(RoundedCornerShape(50))
            .background(LinguaQuestTheme.colors.whiteColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.MonetizationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(2.dp))
        Text(
            "+$coinsAwarded",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}