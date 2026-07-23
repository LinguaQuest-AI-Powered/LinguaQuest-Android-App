package com.iti.linguaquest.features.setting.presentation.contract

import android.content.Context
import com.iti.linguaquest.R
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

data class ReminderState(
    val enabled: Boolean = false,
    val hour: Int = 8,
    val minute: Int = 0,
    val selectedDays: Set<DayOfWeek> = DayOfWeek.entries.toSet(),
    val showTimePicker: Boolean = false,
    val showRepeatSheet: Boolean = false
) {
     fun getRepeatLabel(context: Context): String = when {
        selectedDays.size == 7 -> context.getString(R.string.every_day)

        selectedDays == setOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        ) -> context.getString(R.string.weekdays)

        selectedDays == setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY) -> context.getString(R.string.weekends)

         selectedDays.size == 1 -> selectedDays.first().getDisplayName(TextStyle.FULL, Locale.getDefault())

        else -> context.getString(R.string.days_count, selectedDays.size)
    }

     val timeLabel: String
        get() = LocalTime.of(hour, minute).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
}