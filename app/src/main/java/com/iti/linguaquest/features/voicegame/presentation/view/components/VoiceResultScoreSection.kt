package com.iti.linguaquest.features.voicegame.presentation.view.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun VoiceResultScoreSection(
    rating: Int,
    isPassed: Boolean,
    correctWords: List<String>,
    wrongWords: List<String>,
    coinsAwarded: Int
) {
    ScoreCircle(rating, isPassed)

    Spacer(Modifier.height(20.dp))
    Text(
        stringResource(R.string.voice_result_sentence_review),
        style = MaterialTheme.typography.labelMedium,
        color = LinguaQuestTheme.colors.blackColor
    )
    Spacer(Modifier.height(10.dp))
    WordChipsRow(correctWords, wrongWords)

    if (isPassed && coinsAwarded > 0) {
        Spacer(Modifier.height(16.dp))
        Text(
            "+$coinsAwarded ${stringResource(R.string.voice_result_coins_earned)}",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
