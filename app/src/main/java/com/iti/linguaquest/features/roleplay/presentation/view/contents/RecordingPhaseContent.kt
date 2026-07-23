package com.iti.linguaquest.features.roleplay.presentation.view.contents

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import com.iti.linguaquest.features.roleplay.presentation.view.components.PushToTalkButton
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel

@Composable
fun RecordingPhaseContent(state: RoleplayState, viewModel: RoleplayViewModel) {
    Spacer(Modifier.height(24.dp))

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(LinguaQuestTheme.colors.ErrorAccent.copy(alpha = 0.2f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(LinguaQuestTheme.colors.ErrorAccent)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            formatRecordingElapsed(state.recordingElapsedSeconds),
            fontWeight = FontWeight.Bold,
            color = LinguaQuestTheme.colors.ErrorAccent
        )
    }

    Spacer(Modifier.height(24.dp))
    Text(
        "Speak now...",
        fontWeight = FontWeight.SemiBold,
        color = LinguaQuestTheme.colors.iconsColor
    )
    Spacer(Modifier.height(16.dp))

    PushToTalkButton(
        isRecording = true,
        onClick = { viewModel.onIntent(RoleplayIntent.StopRecordingClicked) }
    )

    Spacer(Modifier.height(24.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        AppOutlinedButton(
            text = "Cancel",
            onClick = { viewModel.onIntent(RoleplayIntent.StopRecordingClicked) },
            modifier = Modifier.weight(1f),
            color = AppColors.DialogSecondaryButtonOutline
        )
        AppButton(
            text = "Done",
            onClick = { viewModel.onIntent(RoleplayIntent.StopRecordingClicked) },
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(Modifier.height(16.dp))
}

private fun formatRecordingElapsed(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "%d:%02d".format(m, s)
}
