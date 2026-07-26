package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult

@Composable
fun BossResultView(
    result: BossEvaluationResult, 
    onAdvanceToNextWorld: () -> Unit,
    onRetryStage: () -> Unit
) {
    val soundPlayer = LocalSoundPlayer.current
    LaunchedEffect(result.task_completed) {
        if (result.task_completed) {
            soundPlayer.play(AppSound.SUCCESS)
        } else {
            soundPlayer.play(AppSound.FAIL)
        }
    }

    if (result.task_completed) {
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
            result = BossEvaluationResult(
                task_completed = true,
                fluency_score = 85,
                feedback_message = "Great job! You used excellent vocabulary."
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
            result = BossEvaluationResult(
                task_completed = false,
                fluency_score = 45,
                feedback_message = "You need to be more clear."
            ),
            onAdvanceToNextWorld = {},
            onRetryStage = {}
        )
    }
}
