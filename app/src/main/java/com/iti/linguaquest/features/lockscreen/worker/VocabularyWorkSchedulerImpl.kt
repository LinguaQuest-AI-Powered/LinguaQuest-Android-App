package com.iti.linguaquest.features.lockscreen.worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.iti.linguaquest.features.lockscreen.notification.VocabularyNotificationReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import timber.log.Timber

class VocabularyWorkSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : VocabularyWorkScheduler {

    private val workManager by lazy { WorkManager.getInstance(context) }
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleImmediateNotification() {
        scheduleAlarm(
            delayMillis = 1000L,
            requestCode = IMMEDIATE_ALARM_REQUEST_CODE,
            forceShow = true
        )
    }

    override fun scheduleScreenOffNotification() {
        scheduleScreenOffAlarm(15 * 60 * 1000L) // 15 minutes
    }

    override fun scheduleNotificationWork() {
        scheduleAlarm(15 * 60 * 1000L)
    }

    private fun scheduleAlarm(
        delayMillis: Long,
        requestCode: Int = ALARM_REQUEST_CODE,
        forceShow: Boolean = false
    ) {
        val intent = Intent(context, VocabularyNotificationReceiver::class.java).apply {
            action = VocabularyNotificationReceiver.ACTION_VOCAB_REMINDER
            putExtra(VocabularyNotificationReceiver.EXTRA_FORCE_SHOW, forceShow)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAt = System.currentTimeMillis() + delayMillis

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Timber.d("VocabularyWorkScheduler: Scheduling inexact alarm for $delayMillis ms (forceShow=$forceShow)")
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        } else {
            Timber.d("VocabularyWorkScheduler: Scheduling exact alarm for $delayMillis ms (forceShow=$forceShow)")
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        }
    }

    private fun scheduleScreenOffAlarm(delayMillis: Long) {
        val intent = Intent(context, VocabularyNotificationReceiver::class.java).apply {
            action = VocabularyNotificationReceiver.ACTION_VOCAB_REMINDER
            putExtra(VocabularyNotificationReceiver.EXTRA_FORCE_SHOW, false)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            SCREEN_OFF_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerAt = System.currentTimeMillis() + delayMillis
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Timber.d("VocabularyWorkScheduler: Scheduling inexact screen-off alarm for $delayMillis ms")
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        } else {
            Timber.d("VocabularyWorkScheduler: Scheduling exact screen-off alarm for $delayMillis ms")
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        }
    }

    override fun enqueueGenerationWork() {
        val request = OneTimeWorkRequestBuilder<VocabularyGenerationWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        workManager.enqueueUniqueWork(
            WORK_NAME_GENERATION,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun cancelAll() {
        val intent = Intent(context, VocabularyNotificationReceiver::class.java).apply {
            action = VocabularyNotificationReceiver.ACTION_VOCAB_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val immediatePendingIntent = PendingIntent.getBroadcast(
            context,
            IMMEDIATE_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val screenOffPendingIntent = PendingIntent.getBroadcast(
            context,
            SCREEN_OFF_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val testPendingIntent = PendingIntent.getBroadcast(
            context,
            TEST_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        alarmManager.cancel(immediatePendingIntent)
        alarmManager.cancel(screenOffPendingIntent)
        alarmManager.cancel(testPendingIntent)
        workManager.cancelUniqueWork(WORK_NAME_GENERATION)
    }

    override fun testNotification(delaySeconds: Long) {
        scheduleAlarm(
            delayMillis = delaySeconds * 1000L,
            requestCode = TEST_ALARM_REQUEST_CODE,
            forceShow = true
        )
    }

    companion object {
        const val WORK_NAME_GENERATION = "vocabulary_generation_work"
        const val ALARM_REQUEST_CODE = 3000
        const val IMMEDIATE_ALARM_REQUEST_CODE = 3001
        const val TEST_ALARM_REQUEST_CODE = 3002
        const val SCREEN_OFF_ALARM_REQUEST_CODE = 3003
    }
}
