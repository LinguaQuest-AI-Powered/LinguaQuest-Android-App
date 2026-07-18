package com.iti.linguaquest.features.review.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R

@Composable
fun ReviewSectionCard(
    emoji: String,
    label: String,
    content: String,
    accentColor: Color,
    background: Color,
    isSpeaking: Boolean,
    pulseScale: Float,
    onSpeak: () -> Unit,
    modifier: Modifier = Modifier,
    contentStyle: FontStyle = FontStyle.Normal
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
         Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.weight(1f)
            )
             SpeakIconButton(
                isSpeaking = isSpeaking,
                pulseScale = pulseScale,
                onClick = onSpeak,
                size = 34.dp,
                iconSize = 16.dp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

         Box(
            modifier = Modifier
                .width(32.dp)
                .height(2.dp)
                .background(accentColor, RoundedCornerShape(1.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = content,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            fontStyle = contentStyle,
            lineHeight = 22.sp
        )
    }
}


@Composable
private fun SpeakIconButton(
    isSpeaking: Boolean,
    pulseScale: Float,
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp = 44.dp,
    iconSize: androidx.compose.ui.unit.Dp = 22.dp
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(size)
            .then(if (isSpeaking) Modifier.scale(pulseScale) else Modifier)
            .background(
                color = if (isSpeaking) MaterialTheme.colorScheme.tertiary
                else MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = if (isSpeaking) Icons.AutoMirrored.Filled.VolumeUp
            else Icons.AutoMirrored.Filled.VolumeOff,
            contentDescription = if (isSpeaking)
                stringResource(R.string.review_stop_speaking_description)
            else
                stringResource(R.string.review_speak_description),
            tint = if (isSpeaking) MaterialTheme.colorScheme.onTertiary
            else MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(iconSize)
        )
    }
}

