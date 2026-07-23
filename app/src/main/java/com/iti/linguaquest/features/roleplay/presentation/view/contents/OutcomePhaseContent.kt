package com.iti.linguaquest.features.roleplay.presentation.view.contents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import com.iti.linguaquest.features.roleplay.presentation.view.components.lingoImageForPhase
import com.iti.linguaquest.features.voicegame.presentation.view.components.SpeechBubble
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel

@Composable
fun OutcomePhaseContent(state: RoleplayState, viewModel: RoleplayViewModel) {
    SpeechBubble(state.feedback.ifBlank { if (state.isPassed) "Great job!" else "Keep practicing!" })
    Spacer(Modifier.height(8.dp))

    AppMascotGradientBox(
        imageRes = lingoImageForPhase(state.phase, state.isPassed),
        mascotOverlapHeight = 80.dp,
        mascotSize = 190.dp
    ) {
        Text(
            if (state.isPassed) "Mission Complete! \uD83C\uDF89" else "Not Quite...",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )

        if (state.isPassed && state.coinsEarned > 0) {
            Spacer(Modifier.height(8.dp))
            Text(
                "+${state.coinsEarned} coins earned",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(4.dp))
        Text(
            "Completed in ${state.turnCount} turns",
            style = MaterialTheme.typography.labelMedium,
            color = LinguaQuestTheme.colors.iconsColor
        )
    }

    Spacer(Modifier.height(24.dp))

    if (state.isPassed) {
        AppButton(
            text = "Continue",
            onClick = { viewModel.onIntent(RoleplayIntent.ReturnHomeClicked) },
            variant = ButtonVariant.PRIMARY
        )
    } else {
        AppButton(
            text = "Retry",
            onClick = { viewModel.onIntent(RoleplayIntent.RetryClicked) },
            variant = ButtonVariant.PRIMARY
        )
    }
    Spacer(Modifier.height(12.dp))
    AppOutlinedButton(
        text = "Return Home",
        onClick = { viewModel.onIntent(RoleplayIntent.ReturnHomeClicked) },
        color = AppColors.DialogSecondaryButtonOutline
    )
    Spacer(Modifier.height(24.dp))
}
