package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun BossScoreBreakdownRow(
    fluencyScore: Int,
    grammarScore: Int = 0,
    vocabularyScore: Int = 0,
    showFluency: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showFluency) {
            ScoreMetricItem(
                label = stringResource(R.string.roleplay_overall_fluency),
                score = fluencyScore,
                modifier = Modifier.weight(1f)
            )
        }
        if (grammarScore > 0) {
            ScoreMetricItem(
                label = stringResource(R.string.roleplay_grammar),
                score = grammarScore,
                modifier = Modifier.weight(1f)
            )
        }
        if (vocabularyScore > 0) {
            ScoreMetricItem(
                label = stringResource(R.string.roleplay_vocabulary),
                score = vocabularyScore,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BossScoreBreakdownRowPreview() {
    LinguaQuestTheme {
        BossScoreBreakdownRow(
            fluencyScore = 90,
            grammarScore = 85,
            vocabularyScore = 80
        )
    }
}
