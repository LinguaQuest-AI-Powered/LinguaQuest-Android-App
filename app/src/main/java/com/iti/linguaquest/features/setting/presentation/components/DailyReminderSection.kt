package com.iti.linguaquest.features.setting.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.features.setting.presentation.contract.ReminderIntent
import com.iti.linguaquest.features.setting.presentation.contract.ReminderState
import androidx.compose.ui.res.stringResource

@Composable
fun DailyReminderSection(
    state: ReminderState,
    onIntent: (ReminderIntent) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    SettingSectionContainer(
        title = stringResource(R.string.daily_reminder),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (enabled) 1f else 0.45f)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bell_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = stringResource(R.string.daily_reminder),
                    color = LocalLinguaQuestColors.current.titleAndCationsColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Switch(
                checked = state.enabled,
                enabled = enabled,
                onCheckedChange = { onIntent(ReminderIntent.ToggleReminder(it)) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LinguaQuestTheme.colors.whiteColor,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = LinguaQuestTheme.colors.whiteColor,
                    uncheckedTrackColor = Color.LightGray,
                    uncheckedBorderColor = Color.Transparent
                ),
                modifier = Modifier.height(24.dp)
            )
        }

        AnimatedVisibility(
            visible = state.enabled,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                SettingItem(
                    icon = painterResource(id = R.drawable.ic_bell_icon),
                    title = stringResource(R.string.reminder_time),
                    value = state.timeLabel,
                    valueColor = LocalLinguaQuestColors.current.BrownText,
                    iconTint = MaterialTheme.colorScheme.tertiary,
                    onClick = { onIntent(ReminderIntent.ShowTimePicker) },
                    enabled = enabled
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                SettingItem(
                    icon = painterResource(id = R.drawable.ic_bell_icon),
                    title = stringResource(R.string.repeat),
                    value = state.repeatLabel,
                    valueColor = LocalLinguaQuestColors.current.BrownText,
                    iconTint = MaterialTheme.colorScheme.tertiary,
                    onClick = { onIntent(ReminderIntent.ShowRepeatSheet) },
                    enabled = enabled
                )
            }
        }
    }
}