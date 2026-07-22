package com.iti.linguaquest.features.setting.presentation.contract

import java.time.DayOfWeek

data class ReminderState(
    val enabled: Boolean = false,
    val hour: Int = 8,
    val minute: Int = 0,
    val selectedDays: Set<DayOfWeek> = DayOfWeek.entries.toSet(),
    val showTimePicker: Boolean = false,
    val showRepeatSheet: Boolean = false
) {
    val repeatLabel: String
        get() = when {
            selectedDays.size == 7 -> "Every Day"
            selectedDays == setOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
            ) -> "Weekdays"
            selectedDays == setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY) -> "Weekends"
            selectedDays.size == 1 -> selectedDays.first().name.lowercase()
                .replaceFirstChar { it.uppercase() }
            else -> "${selectedDays.size} days"
        }

    val timeLabel: String
        get() {
            val period = if (hour < 12) "AM" else "PM"
            val displayHour = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }
            val displayMinute = minute.toString().padStart(2, '0')
            return "$displayHour:$displayMinute $period"
        }
}
