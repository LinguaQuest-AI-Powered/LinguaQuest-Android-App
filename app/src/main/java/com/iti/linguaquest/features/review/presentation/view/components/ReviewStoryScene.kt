package com.iti.linguaquest.features.review.presentation.view.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReviewStoryScene(
    @DrawableRes mascotRes: Int,
    title: String,
    message: String,
    accentColor: Color,
    flipped: Boolean,
    visible: Boolean,
    modifier: Modifier = Modifier,
    contentStyle: FontStyle = FontStyle.Normal,
    showSpeakButton: Boolean = true,
    isSpeaking: Boolean = false,
    onSpeak: (() -> Unit)? = null
) {
    androidx.compose.animation.AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(420)) + slideInHorizontally(
            animationSpec = tween(620),
            initialOffsetX = { if (flipped) it / 3 else -it / 3 }
        )
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = if (flipped) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!flipped) {
                StoryMascot(mascotRes = mascotRes, accentColor = accentColor)
                Spacer(modifier = Modifier.width(12.dp))
                StoryBubble(
                    title = title,
                    message = message,
                    accentColor = accentColor,
                    tailOnStart = true,
                    contentStyle = contentStyle,
                    showSpeakButton = showSpeakButton,
                    isSpeaking = isSpeaking,
                    onSpeak = onSpeak
                )
            } else {
                StoryBubble(
                    title = title,
                    message = message,
                    accentColor = accentColor,
                    tailOnStart = false,
                    contentStyle = contentStyle,
                    showSpeakButton = showSpeakButton,
                    isSpeaking = isSpeaking,
                    onSpeak = onSpeak
                )
                Spacer(modifier = Modifier.width(12.dp))
                StoryMascot(mascotRes = mascotRes, accentColor = accentColor)
            }
        }
    }
}

@Composable
private fun StoryMascot(
    @DrawableRes mascotRes: Int,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(118.dp)
            .background(
                color = accentColor.copy(alpha = 0.08f),
                shape = RoundedCornerShape(30.dp)
            )
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.08f),
                shape = RoundedCornerShape(30.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = mascotRes),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun StoryBubble(
    title: String,
    message: String,
    accentColor: Color,
    tailOnStart: Boolean,
    showSpeakButton: Boolean,
    isSpeaking: Boolean,
    onSpeak: (() -> Unit)?,
    contentStyle: FontStyle,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp,
            tonalElevation = 0.dp,
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier
                            .background(
                                color = accentColor.copy(alpha = 0.10f),
                                shape = RoundedCornerShape(999.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (showSpeakButton && onSpeak != null) {
                        StorySpeakButton(
                            isSpeaking = isSpeaking,
                            onClick = onSpeak
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp,
                    fontStyle = contentStyle,
                    lineHeight = 23.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .align(
                    if (tailOnStart) Alignment.BottomStart else Alignment.BottomEnd
                )
                .offset(
                    x = if (tailOnStart) (-8).dp else 8.dp,
                    y = 6.dp
                )
                .size(18.dp)
                .rotate(45f)
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                )
        )
    }
}

@Composable
private fun StorySpeakButton(
    isSpeaking: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
            modifier = Modifier
            .size(36.dp)
            .background(
                color = if (isSpeaking) MaterialTheme.colorScheme.tertiary
                else MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = null,
            tint = if (isSpeaking) MaterialTheme.colorScheme.onTertiary
            else MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(18.dp)
        )
    }
}
