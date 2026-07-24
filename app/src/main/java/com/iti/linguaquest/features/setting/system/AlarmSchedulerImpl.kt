package com.iti.linguaquest.features.setting.system

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.util.Calendar
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(settings: ReminderSettings) {
        cancel()

        if (!settings.enabled || settings.selectedDays.isEmpty()) return

        settings.selectedDays.forEach { dayOfWeek ->
            val pendingIntent = buildPendingIntent(dayOfWeek.value)
            val triggerAt = nextTriggerMillis(settings.hour, settings.minute, dayOfWeek)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                alarmManager?.canScheduleExactAlarms() == false
            ) {
                 alarmManager?.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerAt,
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
                )
            } else {
                alarmManager?.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAt,
                    pendingIntent
                )
            }
        }
    }

    override fun cancel() {
        DayOfWeek.entries.forEach { dayOfWeek ->
            val pendingIntent = buildPendingIntent(dayOfWeek.value)
            alarmManager?.cancel(pendingIntent)
        }
    }

    private fun buildPendingIntent(requestCode: Int): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ReminderReceiver.ACTION_REMINDER
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun nextTriggerMillis(hour: Int, minute: Int, dayOfWeek: DayOfWeek): Long {
        val now = Calendar.getInstance()
        val trigger = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            val calDay = when (dayOfWeek) {
                DayOfWeek.MONDAY -> Calendar.MONDAY
                DayOfWeek.TUESDAY -> Calendar.TUESDAY
                DayOfWeek.WEDNESDAY -> Calendar.WEDNESDAY
                DayOfWeek.THURSDAY -> Calendar.THURSDAY
                DayOfWeek.FRIDAY -> Calendar.FRIDAY
                DayOfWeek.SATURDAY -> Calendar.SATURDAY
                DayOfWeek.SUNDAY -> Calendar.SUNDAY
            }
            set(Calendar.DAY_OF_WEEK, calDay)
            if (before(now)) add(Calendar.WEEK_OF_YEAR, 1)
        }
        return trigger.timeInMillis
    }
}
