package com.iti.linguaquest.features.setting.presentation.utils
import com.iti.linguaquest.features.setting.presentation.contract.ReminderState
import com.iti.linguaquest.features.setting.system.ReminderSettings
import java.time.DayOfWeek

fun parseTime(timeStr: String): Pair<Int, Int> {
        return try {
            val parts = timeStr.split(":")
            Pair(parts[0].toInt(), parts[1].toInt())
        } catch (e: Exception) {
            Pair(8, 0)
        }
    }
      fun parseDays(daysStr: String): Set<DayOfWeek> {
        return try {
            daysStr.split(",")
                .mapNotNull { it.trim().toIntOrNull() }
                .mapNotNull { DayOfWeek.entries.getOrNull(it - 1) }
                .toSet()
                .ifEmpty { DayOfWeek.entries.toSet() }
        } catch (e: Exception) {
            DayOfWeek.entries.toSet()
        }
    }


      fun ReminderState.toReminderSettings() = ReminderSettings(
        enabled = enabled,
        hour = hour,
        minute = minute,
        selectedDays = selectedDays
    )