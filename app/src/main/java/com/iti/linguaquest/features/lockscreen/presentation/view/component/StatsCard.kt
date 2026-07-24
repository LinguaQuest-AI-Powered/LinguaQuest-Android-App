package com.iti.linguaquest.features.lockscreen.presentation.view.component
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenState

@Composable
  fun StatsCard(state: LockScreenState) {
    val dashSymbol = stringResource(R.string.lockscreen_vocabulary_dash)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.lockscreen_vocabulary_status_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = LinguaQuestTheme.colors.titleAndCationsColor
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCardItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.lockscreen_vocabulary_pending_words),
                    value = state.pendingCount.toString(),
                    iconRes = R.drawable.ic_bell_icon
                )
                StatCardItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.lockscreen_vocabulary_batch_size),
                    value = state.batchSize.toString(),
                    iconRes = R.drawable.ic_coin
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCardItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.lockscreen_vocabulary_target_language),
                    value = state.currentTargetLanguage ?: dashSymbol,
                    iconRes = R.drawable.ic_learning_language
                )
                StatCardItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.lockscreen_vocabulary_level),
                    value = state.currentProficiencyLevel ?: dashSymbol,
                    iconRes = R.drawable.ic_timer
                )
            }

            StatCardItem(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.lockscreen_vocabulary_last_generated),
                value = state.lastGenerationTime?.let { formatTime(it) }
                    ?: stringResource(R.string.lockscreen_vocabulary_state_ready),
                iconRes = R.drawable.ic_info_icon
            )
        }
    }
}
