package com.iti.linguaquest.features.setting.system

import java.time.DayOfWeek

data class ReminderSettings(
    val enabled: Boolean,
    val hour: Int,
    val minute: Int,
    val selectedDays: Set<DayOfWeek>
)

interface AlarmScheduler {
    fun schedule(settings: ReminderSettings)
    fun cancel()
}