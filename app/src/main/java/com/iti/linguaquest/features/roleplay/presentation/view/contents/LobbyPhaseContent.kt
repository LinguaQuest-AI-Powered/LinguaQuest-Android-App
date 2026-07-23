package com.iti.linguaquest.features.roleplay.presentation.view.contents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import com.iti.linguaquest.features.roleplay.presentation.view.components.lingoImageForPhase
import com.iti.linguaquest.features.voicegame.presentation.view.components.SpeechBubble
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel

@Composable
fun LobbyPhaseContent(state: RoleplayState, viewModel: RoleplayViewModel) {
    SpeechBubble("Ready for a challenge? \uD83C\uDFAF")
    Spacer(Modifier.height(8.dp))

    AppMascotGradientBox(
        imageRes = lingoImageForPhase(state.phase),
        mascotOverlapHeight = 70.dp,
        mascotSize = 180.dp
    ) {
        Text(
            "Your Mission",
            style = AppTextStyles.Caption,
            color = LinguaQuestTheme.colors.iconsColor
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = state.setting.ifBlank { "Market" },
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            color = LinguaQuestTheme.colors.blackColor
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = state.objectiveText.ifBlank { "Buy 3 apples at the market" },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = LinguaQuestTheme.colors.iconsColor
        )
    }

    Spacer(Modifier.height(32.dp))

    AppButton(
        text = "Start Level",
        onClick = { viewModel.onIntent(RoleplayIntent.StartLevelClicked) },
        variant = ButtonVariant.PRIMARY
    )
    Spacer(Modifier.height(16.dp))
}
