package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId

@Composable
fun RoleplayObjectiveBanner(
    scenario: BossScenario,
    isTimerRunning: Boolean,
    remainingTimeSeconds: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.roleplay_objective_format, scenario.objective),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )

            if (isTimerRunning) {
                Spacer(Modifier.height(6.dp))
                val minutes = remainingTimeSeconds / 60
                val seconds = remainingTimeSeconds % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)
                val timerColor = if (remainingTimeSeconds <= 30) {
                    LinguaQuestTheme.colors.ErrorAccent
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }

                Text(
                    text = timeString,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = timerColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RoleplayObjectiveBannerPreview() {
    LinguaQuestTheme {
        RoleplayObjectiveBanner(
            scenario = BossScenario(
                id = ScenarioId.SCENARIO_MARKET_01,
                bossName = "Sherry",
                roleDescription = "Fruit Vendor",
                objective = "Buy some fresh mangoes.",
                worldId = "2",
                voiceName = "KORE"
            ),
            isTimerRunning = true,
            remainingTimeSeconds = 75,
            modifier = Modifier.padding(16.dp)
        )
    }
}
