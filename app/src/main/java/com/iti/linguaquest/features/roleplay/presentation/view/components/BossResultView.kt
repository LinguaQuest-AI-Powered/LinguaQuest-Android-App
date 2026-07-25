package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayAssessmentResult

@Composable
fun BossResultView(
    result: RoleplayAssessmentResult, 
    onAdvanceToNextWorld: () -> Unit,
    onRetryStage: () -> Unit
) {
    val soundPlayer = LocalSoundPlayer.current
    LaunchedEffect(result.isTaskCompleted) {
        if (result.isTaskCompleted) {
            soundPlayer.play(AppSound.SUCCESS)
        } else {
            soundPlayer.play(AppSound.FAIL)
        }
    }

    if (result.isTaskCompleted) {
        BossSuccessView(
            result = result,
            onAdvanceToNextWorld = onAdvanceToNextWorld
        )
    } else {
        BossFailView(
            result = result,
            onRetryStage = onRetryStage
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BossResultSuccessPreview() {
    LinguaQuestTheme {
        BossResultView(
            result = RoleplayAssessmentResult(
                isTaskCompleted = true,
                fluencyScore = 85,
                feedbackMessage = "Great job! You spoke very clearly and naturally."
            ),
            onAdvanceToNextWorld = {},
            onRetryStage = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BossResultFailPreview() {
    LinguaQuestTheme {
        BossResultView(
            result = RoleplayAssessmentResult(
                isTaskCompleted = false,
                fluencyScore = 45,
                feedbackMessage = "You were a bit hard to understand. Try speaking a bit slower."
            ),
            onAdvanceToNextWorld = {},
            onRetryStage = {}
        )
    }
}
