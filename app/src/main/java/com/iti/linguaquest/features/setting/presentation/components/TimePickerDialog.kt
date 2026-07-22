package com.iti.linguaquest.features.setting.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.setting.presentation.contract.ReminderIntent
import com.iti.linguaquest.features.setting.presentation.contract.ReminderState
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    state: ReminderState,
    onIntent: (ReminderIntent) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = state.hour,
        initialMinute = state.minute,
        is24Hour = false
    )

    AlertDialog(
        onDismissRequest = { onIntent(ReminderIntent.DismissTimePicker) },
        containerColor = MaterialTheme.colorScheme.background,
        title = {
            Text(
                text = stringResource(R.string.select_reminder_time),

                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onIntent(
                        ReminderIntent.SelectTime(
                            hour = timePickerState.hour,
                            minute = timePickerState.minute
                        )
                    )
                }
            ) {
                Text(    text = stringResource(R.string.save),
                    color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = { onIntent(ReminderIntent.DismissTimePicker) }) {
                Text(    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
