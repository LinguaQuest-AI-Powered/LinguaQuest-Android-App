package com.iti.linguaquest.features.setting.presentation.contract

import java.time.DayOfWeek

sealed interface ReminderIntent {
    data class ToggleReminder(val enabled: Boolean) : ReminderIntent
    data object ShowTimePicker : ReminderIntent
    data object DismissTimePicker : ReminderIntent
    data class SelectTime(val hour: Int, val minute: Int) : ReminderIntent
    data object ShowRepeatSheet : ReminderIntent
    data object DismissRepeatSheet : ReminderIntent
    data class SelectPreset(val preset: RepeatPreset) : ReminderIntent
    data class ToggleDay(val day: DayOfWeek) : ReminderIntent
    data object SaveRepeat : ReminderIntent
}

enum class RepeatPreset { EVERY_DAY, WEEKDAYS, WEEKENDS, CUSTOM }
