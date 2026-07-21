package com.iti.linguaquest.features.voicegame.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.features.voicegame.presentation.model.VoiceResultUi
import com.iti.linguaquest.features.voicegame.presentation.view.components.ScoreCircle
import com.iti.linguaquest.features.voicegame.presentation.view.components.SpeechBubble
import com.iti.linguaquest.features.voicegame.presentation.view.components.WordChipsRow

private val SuccessGreen = Color(0xFF3E8E5A)


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceResultScreen(
    result: VoiceResultUi,
    onContinue: () -> Unit,
    onRetry: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(Modifier.height(32.dp))

            SpeechBubble(result.advice)
            Spacer(Modifier.height(8.dp))

            AppMascotGradientBox(
                imageRes = if (result.isPassed) R.drawable.lingo_acheviment else R.drawable.lingo_error,
                mascotOverlapHeight = 80.dp,
                mascotSize = 190.dp
            ) {
                Text(
                    if (result.isPassed) stringResource(R.string.voice_result_great_job) else stringResource(
                        R.string.voice_result_not_quite
                    ),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (result.isPassed) SuccessGreen else AppColors.PrimaryColor
                )
                Spacer(Modifier.height(16.dp))

                ScoreCircle(result.rating, result.isPassed)

                Spacer(Modifier.height(20.dp))
                Text(
                    stringResource(R.string.voice_result_sentence_review),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppColors.DialogSecondaryButtonOutline
                )
                Spacer(Modifier.height(10.dp))
                WordChipsRow(result.correctWords, result.wrongWords)

                if (result.isPassed && result.coinsAwarded > 0) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "+${result.coinsAwarded} ${stringResource(R.string.voice_result_coins_earned)}",
                        fontWeight = FontWeight.Bold,
                        color = AppColors.PrimaryColor
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            if (result.isPassed) {
                AppButton(text = stringResource(R.string.voice_result_continue), onClick = onContinue, variant = ButtonVariant.PRIMARY)
                Spacer(Modifier.height(12.dp))
                AppOutlinedButton(
                    text = stringResource(R.string.voice_result_return_home),
                    onClick = onHome,
                    color = AppColors.DialogSecondaryButtonOutline
                )
            } else {
                AppButton(text = stringResource(R.string.voice_result_retry), onClick = onRetry, variant = ButtonVariant.PRIMARY)
                Spacer(Modifier.height(12.dp))
                AppOutlinedButton(
                    text = stringResource(R.string.voice_result_return_home),
                    onClick = onHome,
                    color = AppColors.DialogSecondaryButtonOutline
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
