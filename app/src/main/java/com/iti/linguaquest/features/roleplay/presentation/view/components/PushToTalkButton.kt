package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun PushToTalkButton(
    isRecording: Boolean,
    onPressStart: () -> Unit,
    onPressEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val soundPlayer = LocalSoundPlayer.current
    val currentOnPressStart by rememberUpdatedState(onPressStart)
    val currentOnPressEnd by rememberUpdatedState(onPressEnd)

    val backgroundColor by animateColorAsState(
        targetValue = if (isRecording) LinguaQuestTheme.colors.ErrorAccent else MaterialTheme.colorScheme.primary,
        label = "bgColor"
    )
    val iconColor by animateColorAsState(
        targetValue = if (isRecording) LinguaQuestTheme.colors.whiteColor else LinguaQuestTheme.colors.iconsColor,
        label = "iconColor"
    )
    val buttonSize by animateDpAsState(
        targetValue = if (isRecording) 96.dp else 88.dp,
        label = "size"
    )
    val iconSize by animateDpAsState(
        targetValue = if (isRecording) 40.dp else 36.dp,
        label = "iconSize"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "recording_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRecording) 1.12f else 1f,
        animationSpec = infiniteRepeatable(
            tween(600, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val contentDescription = if (isRecording) {
        stringResource(R.string.roleplay_stop_recording)
    } else {
        stringResource(R.string.roleplay_start_recording)
    }

    Box(
        modifier = modifier
            .size(buttonSize)
            .scale(if (isRecording) pulseScale else 1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .then(
                if (isRecording) {
                    Modifier.border(3.dp, LinguaQuestTheme.colors.ErrorAccent.copy(alpha = 0.3f), CircleShape)
                } else {
                    Modifier
                }
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        soundPlayer.play(AppSound.OPEN_MIC)
                        currentOnPressStart()
                        tryAwaitRelease()
                        soundPlayer.play(AppSound.CLOSE_MIC)
                        currentOnPressEnd()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}

