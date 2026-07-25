package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
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
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppMascotGradientBox(
            imageRes = R.drawable.lingo_sad
        ) {
            Text(
                text = stringResource(R.string.roleplay_stage_failed),
                style = AppTextStyles.ScreenTitle,
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.BrownText
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${result.fluency_score}%",
                style = AppTextStyles.DialogMessage,
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.BrownText,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.5f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = result.feedback_message,
                    style = AppTextStyles.DialogMessage,
                    color = LinguaQuestTheme.colors.BrownText,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = stringResource(R.string.roleplay_try_again),
                onClick = onRetryStage
            )
        }
    }
}
