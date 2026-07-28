package com.iti.linguaquest.features.voicegame.presentation.view.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameIntent
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameState
import com.iti.linguaquest.features.voicegame.presentation.view.components.SpeechBubble
import com.iti.linguaquest.features.voicegame.presentation.viewModel.VoiceGameViewModel

import androidx.compose.ui.text.font.FontStyle
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon

@Composable
fun IdlePhaseContent(state: VoiceGameState, viewModel: VoiceGameViewModel) {
    SpeechBubble(stringResource(R.string.voice_idle_yo_can_do_it))
    Spacer(Modifier.height(8.dp))
    AppMascotGradientBox(
        imageRes = R.drawable.lingo_initial_state_voice,
        mascotOverlapHeight = 70.dp,
        mascotSize = 180.dp
    ) {
        Text(stringResource(R.string.voice_idle_pronounce_this), style = AppTextStyles.Caption, color = LinguaQuestTheme.colors.iconsColor)
        Spacer(Modifier.height(8.dp))

        if (state.isLoadingSentence) {
            LingoSpinningIcon(
                size = 32.dp
            )
        } else {
            Text(
                text = state.sentence,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                color = LinguaQuestTheme.colors.blackColor
            )
            state.phonetic?.let { phonetic ->
                Spacer(Modifier.height(4.dp))
                Text(
                    text = phonetic,
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                    textAlign = TextAlign.Center,
                    color = LinguaQuestTheme.colors.iconsColor
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.clickable(enabled = !state.isLoadingSentence && state.sentence.isNotBlank()) {
                viewModel.onIntent(VoiceGameIntent.ListenClicked)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = LinguaQuestTheme.colors.iconsColor, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(4.dp))
            Text(stringResource(R.string.voice_idle_listen), color = LinguaQuestTheme.colors.iconsColor, fontWeight = FontWeight.Bold)
        }
    }

    Spacer(Modifier.height(24.dp))
    Text(stringResource(R.string.voice_idle_tap_hold_record), color = LinguaQuestTheme.colors.iconsColor)
    Spacer(Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .size(88.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(enabled = !state.isLoadingSentence && state.sentence.isNotBlank()) {
                viewModel.onIntent(VoiceGameIntent.RecordClicked)
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Mic, contentDescription = "Record", tint = LinguaQuestTheme.colors.iconsColor, modifier = Modifier.size(36.dp))
    }

    Spacer(Modifier.height(20.dp))
    AppOutlinedButton(
        text = stringResource(R.string.voice_idle_skip),
        onClick = { viewModel.onIntent(VoiceGameIntent.SkipClicked) },
        color = AppColors.DialogSecondaryButtonOutline
    )
    Spacer(Modifier.height(16.dp))
}