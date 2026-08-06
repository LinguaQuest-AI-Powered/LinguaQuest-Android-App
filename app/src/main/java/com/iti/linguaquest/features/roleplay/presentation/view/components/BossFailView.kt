package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult

@Composable
fun BossFailView(
    result: BossEvaluationResult,
    onRetryStage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val feedbackText = when (result.feedback_message) {
        "ERROR_NO_SPEECH" -> stringResource(R.string.roleplay_no_speech)
        "ERROR_SHORT_CONVERSATION" -> stringResource(R.string.roleplay_short_conversation)
        else -> result.feedback_message
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppMascotGradientBox(
            imageRes = R.drawable.lingo_sad,
            mascotSize = 160.dp,
            mascotOverlapHeight = 55.dp
        ) {
            Text(
                text = stringResource(R.string.roleplay_stage_failed),
                style = AppTextStyles.ScreenTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = LinguaQuestTheme.colors.BrownText
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${result.fluency_score}%",
                style = AppTextStyles.DialogMessage,
                fontWeight = FontWeight.ExtraBold,
                color = LinguaQuestTheme.colors.ErrorAccent,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (result.grammar_score > 0 || result.vocabulary_score > 0) {
                BossScoreBreakdownRow(
                    fluencyScore = result.fluency_score,
                    grammarScore = result.grammar_score,
                    vocabularyScore = result.vocabulary_score,
                    showFluency = false
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            BossFeedbackBox(
                feedbackMessage = feedbackText,
                improvements = result.improvements,
                modifier = Modifier.weight(1f, fill = false)
            )

            Spacer(modifier = Modifier.height(24.dp))

            AppButton3D(
                text = stringResource(R.string.roleplay_try_again),
                onClick = onRetryStage
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BossFailViewPreview() {
    LinguaQuestTheme {
        BossFailView(
            result = BossEvaluationResult(
                task_completed = false,
                fluency_score = 42,
                grammar_score = 45,
                vocabulary_score = 38,
                feedback_message = "You didn't reach an agreement on the price before ending the conversation.",
                improvements = listOf("Try counter-offering with a specific price", "Use polite phrases when bargaining")
            ),
            onRetryStage = {}
        )
    }
}
