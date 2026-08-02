package com.iti.linguaquest.core.appicon.worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.iti.linguaquest.core.appicon.domain.AppIconTiming
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber
import kotlin.time.Duration

@Singleton
class AppIconWorkSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppIconWorkScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleDailyRefresh() {
        val request = PeriodicWorkRequestBuilder<AppIconRefreshWorker>(
            AppIconTiming.PERIODIC_REFRESH_INTERVAL.inWholeMinutes, TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DAILY_REFRESH_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    override fun scheduleAngryWindowCheck() {
        scheduleAlarm(
            delayMillis = AppIconTiming.ANGRY_CHECK_DELAY.inWholeMilliseconds,
            requestCode = STATE_TRANSITION_REQUEST_CODE
        )
    }

    override fun scheduleNextEvaluation(nextDelay: Duration) {
        scheduleAlarm(
            delayMillis = nextDelay.inWholeMilliseconds,
            requestCode = STATE_TRANSITION_REQUEST_CODE
        )
    }

    override fun scheduleBackgroundExitCheck() {
        Timber.d("AppIcon: Scheduling BACKGROUND_EXIT check in ${AppIconTiming.BACKGROUND_EXIT_DELAY.inWholeMilliseconds}ms")
        scheduleAlarm(
            delayMillis = AppIconTiming.BACKGROUND_EXIT_DELAY.inWholeMilliseconds,
            requestCode = BACKGROUND_EXIT_REQUEST_CODE
        )
    }

    override fun cancelBackgroundExitCheck() {
        alarmManager.cancel(buildPendingIntent(BACKGROUND_EXIT_REQUEST_CODE))
    }

    private companion object {
        const val DAILY_REFRESH_WORK_NAME = "app_icon_refresh_work"
        const val ACTION_APP_ICON_REFRESH = "com.iti.linguaquest.ACTION_APP_ICON_REFRESH"
        const val STATE_TRANSITION_REQUEST_CODE = 9101
        const val BACKGROUND_EXIT_REQUEST_CODE = 9102
    }

    private fun scheduleAlarm(delayMillis: Long, requestCode: Int) {
        val safeDelayMillis = delayMillis.coerceAtLeast(0L)
        val pendingIntent = buildPendingIntent(requestCode)
        alarmManager.cancel(pendingIntent)
        val triggerAtMillis = System.currentTimeMillis() + safeDelayMillis

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    private fun buildPendingIntent(requestCode: Int): PendingIntent {
        val intent = Intent(context, AppIconRefreshReceiver::class.java).apply {
            action = ACTION_APP_ICON_REFRESH
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
