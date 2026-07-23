package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun PushToTalkButton(
    isRecording: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isRecording) LinguaQuestTheme.colors.ErrorAccent
    else MaterialTheme.colorScheme.primary

    val icon = if (isRecording) Icons.Default.Stop else Icons.Default.Mic
    val contentDescription = if (isRecording) "Stop recording" else "Start recording"

    if (isRecording) {
        val infiniteTransition = rememberInfiniteTransition(label = "recording_pulse")
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.12f,
            animationSpec = infiniteRepeatable(
                tween(600, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            ),
            label = "pulseScale"
        )
        Box(
            modifier = modifier
                .size(96.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(3.dp, LinguaQuestTheme.colors.ErrorAccent.copy(alpha = 0.3f), CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = contentDescription, tint = Color.White, modifier = Modifier.size(40.dp))
        }
    } else {
        Box(
            modifier = modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = contentDescription, tint = LinguaQuestTheme.colors.iconsColor, modifier = Modifier.size(36.dp))
        }
    }
}
