package com.iti.linguaquest.features.voicegame.presentation.view.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameIntent
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameState
import com.iti.linguaquest.core.sharedComponents.MessageBubble
import com.iti.linguaquest.features.voicegame.presentation.view.components.formatElapsed
import com.iti.linguaquest.features.voicegame.presentation.viewModel.VoiceGameViewModel

@Composable
fun RecordingPhaseContent(state: VoiceGameState, viewModel: VoiceGameViewModel) {
    val soundPlayer = LocalSoundPlayer.current
    MessageBubble(title = if (state.isPaused) stringResource(R.string.voice_recording_paused) else stringResource(R.string.voice_recording_listening))
    Spacer(Modifier.height(8.dp))
    AppMascotGradientBox(
        imageRes = R.drawable.lingo_mic,
        mascotOverlapHeight = 70.dp,
        mascotSize = 180.dp
    ) {
        Text(stringResource(R.string.voice_idle_pronounce_this), style = AppTextStyles.Caption, color = LinguaQuestTheme.colors.iconsColor)
        Spacer(Modifier.height(8.dp))
        Text(
            state.sentence,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            color = LinguaQuestTheme.colors.blackColor
        )
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = LinguaQuestTheme.colors.iconsColor, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(4.dp))
            Text(stringResource(R.string.voice_idle_listen), color = LinguaQuestTheme.colors.iconsColor, fontWeight = FontWeight.Bold)
        }
    }

    Spacer(Modifier.height(20.dp))
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(LinguaQuestTheme.colors.ErrorAccent.copy(alpha = 0.2f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(LinguaQuestTheme.colors.ErrorAccent))
        Spacer(Modifier.width(6.dp))
        Text(formatElapsed(state.recordingElapsedSeconds), fontWeight = FontWeight.Bold, color = LinguaQuestTheme.colors.ErrorAccent)
    }

    Spacer(Modifier.height(20.dp))
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(LinguaQuestTheme.colors.ErrorAccent)
            .clickable {
                if (state.isPaused) {
                    soundPlayer.play(AppSound.OPEN_MIC)
                    viewModel.onIntent(VoiceGameIntent.ResumeClicked)
                } else {
                    soundPlayer.play(AppSound.CLOSE_MIC)
                    viewModel.onIntent(VoiceGameIntent.PauseClicked)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            if (state.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
            contentDescription = if (state.isPaused) "Resume" else "Pause",
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )
    }

    Spacer(Modifier.height(20.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        AppOutlinedButton(
            text = stringResource(R.string.voice_recording_cancel),
            onClick = { viewModel.onIntent(VoiceGameIntent.CancelRecordingClicked) },
            modifier = Modifier.weight(1f),
            color = AppColors.DialogSecondaryButtonOutline
        )
        AppButton3D(
            text = stringResource(R.string.voice_recording_done),
            onClick = { viewModel.onIntent(VoiceGameIntent.DoneClicked) },
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(Modifier.height(16.dp))
}