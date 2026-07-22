package com.iti.linguaquest.features.setting.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.iti.linguaquest.core.preferences.domain.repository.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        scope.launch {
            try {
                scheduleReminderFromPreferences()
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun scheduleReminderFromPreferences() {
        val enabled = userPreferencesRepository.reminderEnabled.first()
        if (!enabled) return

        val timeStr = userPreferencesRepository.reminderTime.first()
        val daysStr = userPreferencesRepository.reminderDays.first()
        val (hour, minute) = parseTime(timeStr)
        val selectedDays = parseDays(daysStr)

        alarmScheduler.schedule(
            ReminderSettings(
                enabled = true,
                hour = hour,
                minute = minute,
                selectedDays = selectedDays
            )
        )
    }

    private fun parseTime(timeStr: String): Pair<Int, Int> {
        return runCatching {
            val parts = timeStr.split(":")
            parts[0].toInt() to parts[1].toInt()
        }.getOrElse {
            8 to 0
        }
    }

    private fun parseDays(daysStr: String): Set<DayOfWeek> {
        return daysStr
            .split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .mapNotNull { ordinal -> DayOfWeek.entries.getOrNull(ordinal - 1) }
            .toSet()
            .ifEmpty { DayOfWeek.entries.toSet() }
    }
}
