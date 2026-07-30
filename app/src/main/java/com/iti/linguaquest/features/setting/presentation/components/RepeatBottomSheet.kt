package com.iti.linguaquest.features.setting.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.features.setting.presentation.contract.ReminderIntent
import com.iti.linguaquest.features.setting.presentation.contract.ReminderState
import com.iti.linguaquest.features.setting.presentation.contract.RepeatPreset
import java.time.DayOfWeek
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatBottomSheet(
    state: ReminderState,
    onIntent: (ReminderIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onIntent(ReminderIntent.DismissRepeatSheet) },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(LinguaQuestTheme.colors.textFieldBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
             Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.repeat),
                    style = AppTextStyles.ScreenTitle.copy(
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.titleAndCationsColor
                    )
                )
                IconButton(onClick = { onIntent(ReminderIntent.DismissRepeatSheet) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = LinguaQuestTheme.colors.titleAndCationsColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val presets = listOf(
                RepeatPreset.EVERY_DAY to stringResource(R.string.every_day),
                RepeatPreset.WEEKDAYS to stringResource(R.string.weekdays),
                RepeatPreset.WEEKENDS to stringResource(R.string.weekends),
                RepeatPreset.CUSTOM to stringResource(R.string.custom)
            )

            presets.forEach { (preset, label) ->
                val isSelected = when (preset) {
                    RepeatPreset.EVERY_DAY -> state.selectedDays.size == 7
                    RepeatPreset.WEEKDAYS -> state.selectedDays == setOf(
                        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
                    )
                    RepeatPreset.WEEKENDS -> state.selectedDays == setOf(
                        DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
                    )
                    RepeatPreset.CUSTOM -> true
                }

                Text(
                    text = label,
                    style = AppTextStyles.LessonTitle.copy(
                        fontWeight = if (isSelected && preset != RepeatPreset.CUSTOM)
                            FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected && preset != RepeatPreset.CUSTOM)
                            MaterialTheme.colorScheme.primary
                        else LinguaQuestTheme.colors.titleAndCationsColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onIntent(ReminderIntent.SelectPreset(preset)) }
                        .padding(vertical = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val orderedDays = listOf(
                DayOfWeek.SUNDAY to stringResource(R.string.day_sun_short),
                DayOfWeek.MONDAY to stringResource(R.string.day_mon_short),
                DayOfWeek.TUESDAY to stringResource(R.string.day_tue_short),
                DayOfWeek.WEDNESDAY to stringResource(R.string.day_wed_short),
                DayOfWeek.THURSDAY to stringResource(R.string.day_thu_short),
                DayOfWeek.FRIDAY to stringResource(R.string.day_fri_short),
                DayOfWeek.SATURDAY to stringResource(R.string.day_sat_short)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                orderedDays.forEach { (day, letter) ->
                    val selected = day in state.selectedDays
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.background
                            )
                            .border(
                                width = 1.dp,
                                color = if (selected) MaterialTheme.colorScheme.primary
                                else LinguaQuestTheme.colors.textFieldBorder,
                                shape = CircleShape
                            )
                            .clickable { onIntent(ReminderIntent.ToggleDay(day)) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letter,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) LinguaQuestTheme.colors.whiteColor
                            else LocalLinguaQuestColors.current.titleAndCationsColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton3D(
                text = stringResource(R.string.save),
                onClick = { onIntent(ReminderIntent.SaveRepeat) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
