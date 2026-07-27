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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
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
    val density = LocalDensity.current
    val liftPx = with(density) { 70.dp.toPx() }

    val flightOffset = remember { Animatable(sourceOffset, Offset.VectorConverter) }
    var badgeSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(sourceOffset, targetOffset) {
        val peak = Offset(
            x = sourceOffset.x + (targetOffset.x - sourceOffset.x) * 0.85f,
            y = minOf(sourceOffset.y, targetOffset.y) - liftPx
        )

        flightOffset.animateTo(
            targetValue = targetOffset,
            animationSpec = keyframes {
                durationMillis = 1500
                sourceOffset at 0
                peak at 700 using FastOutSlowInEasing
                targetOffset at 1500 using FastOutLinearInEasing
            }
        )

        onLanded()
    }

    val local = flightOffset.value - containerOrigin
    val offsetX = local.x.roundToInt() - badgeSize.width / 2
    val offsetY = local.y.roundToInt() - badgeSize.height / 2

    Row(
        modifier = Modifier
            .offset { IntOffset(offsetX, offsetY) }
            .onSizeChanged { badgeSize = it }
            .clip(RoundedCornerShape(50))
            .background(LinguaQuestTheme.colors.whiteColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text("+$coinsAwarded", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, maxLines = 1, softWrap = false)
    }
}