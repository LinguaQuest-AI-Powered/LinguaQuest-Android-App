package com.iti.linguaquest.features.roleplay.presentation.view.contents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import com.iti.linguaquest.features.roleplay.presentation.view.components.AiSpeechBubble
import com.iti.linguaquest.features.roleplay.presentation.view.components.PushToTalkButton
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel

@Composable
fun IdlePhaseContent(state: RoleplayState, viewModel: RoleplayViewModel) {
    Text(
        "Turn ${state.turnCount}/${state.maxTurns}",
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(Modifier.height(12.dp))

    if (state.aiResponseText.isNotBlank()) {
        AiSpeechBubble(
            aiText = state.aiResponseText,
            translation = state.aiTranslation
        )
    }

    Spacer(Modifier.height(24.dp))
    Text(
        "Tap the mic to respond",
        color = LinguaQuestTheme.colors.iconsColor
    )
    Spacer(Modifier.height(12.dp))

    PushToTalkButton(
        isRecording = false,
        onClick = { viewModel.onIntent(RoleplayIntent.RecordClicked) }
    )
    Spacer(Modifier.height(16.dp))
}
