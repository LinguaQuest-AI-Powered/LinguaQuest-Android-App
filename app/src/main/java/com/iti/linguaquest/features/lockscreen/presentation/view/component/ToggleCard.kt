package com.iti.linguaquest.features.lockscreen.presentation.view.component
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureState
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenState

@Composable
  fun ToggleCard(
    state: LockScreenState,
    onToggle: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.lockscreen_vocabulary_status_label),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = LinguaQuestTheme.colors.titleAndCationsColor
                )
                Text(
                    text = when {
                        state.featureState == LockScreenFeatureState.DISABLED -> stringResource(R.string.lockscreen_vocabulary_state_disabled)
                        state.pendingGeneration -> stringResource(R.string.lockscreen_vocabulary_state_generating)
                        state.featureState == LockScreenFeatureState.ERROR -> stringResource(R.string.lockscreen_vocabulary_state_error)
                        else -> stringResource(R.string.lockscreen_vocabulary_state_active)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = state.featureState != LockScreenFeatureState.DISABLED &&
                        state.featureState != LockScreenFeatureState.DISABLING,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AppColors.White,
                    checkedTrackColor = AppColors.OrangeActive,
                    uncheckedThumbColor = AppColors.White,
                    uncheckedTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                )
            )
        }
    }
}