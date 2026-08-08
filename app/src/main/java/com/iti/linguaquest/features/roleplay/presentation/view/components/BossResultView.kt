package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import kotlinx.coroutines.delay

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
            if (result.coins_earned > 0) {
                delay(600)
                soundPlayer.play(AppSound.AddedMoney)
            }
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
                fluency_score = 88,
                grammar_score = 90,
                vocabulary_score = 85,
                stars = 3,
                xp_earned = 200,
                coins_earned = 75,
                feedback_message = "Great job! You used excellent vocabulary.",
                strengths = listOf("Clear pronunciation", "Polite phrasing"),
                improvements = listOf("Practice asking open-ended questions")
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
                grammar_score = 50,
                vocabulary_score = 40,
                feedback_message = "You need to be more clear.",
                improvements = listOf("Speak in complete sentences", "Stick to the target language")
            ),
            onAdvanceToNextWorld = {},
            onRetryStage = {}
        )
    }
}
