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
import androidx.compose.ui.graphics.toArgb
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
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun BossSuccessView(
    result: BossEvaluationResult,
    onAdvanceToNextWorld: () -> Unit,
    modifier: Modifier = Modifier
) {
    val confettiColors = listOf(
        LinguaQuestTheme.colors.OrangeActive.toArgb(),
        LinguaQuestTheme.colors.splashTopLeftColor.toArgb(),
        LinguaQuestTheme.colors.whiteColor.toArgb()
    )

    val party = Party(
        speed = 0f,
        maxSpeed = 30f,
        damping = 0.9f,
        spread = 360,
        colors = confettiColors,
        position = Position.Relative(0.5, 0.25),
        emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(200)
    )

    val starTitleRes = when (result.stars) {
        3 -> R.string.roleplay_stars_3
        2 -> R.string.roleplay_stars_2
        else -> R.string.roleplay_stars_1
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = listOf(party)
        )

        AppMascotGradientBox(
            imageRes = R.drawable.lingo_success,
            mascotSize = 160.dp,
            mascotOverlapHeight = 55.dp
        ) {
            Text(
                text = stringResource(R.string.roleplay_victory),
                style = AppTextStyles.ScreenTitle,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                color = LinguaQuestTheme.colors.BrownText
            )

            Spacer(modifier = Modifier.height(8.dp))

            StarRatingRow(stars = result.stars)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(starTitleRes),
                style = AppTextStyles.DialogMessage,
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.OrangeActive,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            BossScoreBreakdownRow(
                fluencyScore = result.fluency_score,
                grammarScore = result.grammar_score,
                vocabularyScore = result.vocabulary_score
            )

            Spacer(modifier = Modifier.height(12.dp))

            BossFeedbackBox(
                feedbackMessage = result.feedback_message,
                strengths = result.strengths,
                improvements = result.improvements,
                modifier = Modifier.weight(1f, fill = false)
            )

            Spacer(modifier = Modifier.height(16.dp))

            BossRewardRow(
                xpEarned = result.xp_earned,
                coinsEarned = result.coins_earned
            )

            Spacer(modifier = Modifier.height(24.dp))

            AppButton3D(
                text = stringResource(R.string.roleplay_next_world),
                onClick = onAdvanceToNextWorld
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BossSuccessViewPreview() {
    LinguaQuestTheme {
        BossSuccessView(
            result = BossEvaluationResult(
                task_completed = true,
                fluency_score = 92,
                grammar_score = 95,
                vocabulary_score = 88,
                stars = 3,
                xp_earned = 200,
                coins_earned = 75,
                feedback_message = "Excellent performance! You bargained effectively and kept a natural conversation pace.",
                strengths = listOf("Used polite expressions correctly", "Clear negotiation flow"),
                improvements = listOf("Try using varied vocabulary for prices")
            ),
            onAdvanceToNextWorld = {}
        )
    }
}
