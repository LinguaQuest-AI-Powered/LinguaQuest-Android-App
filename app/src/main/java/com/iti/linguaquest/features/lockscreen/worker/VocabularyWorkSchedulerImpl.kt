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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VocabularyWorkSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : VocabularyWorkScheduler {

    private val workManager by lazy { WorkManager.getInstance(context) }
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleImmediateNotification() {
        scheduleAlarm(5000L, IMMEDIATE_ALARM_REQUEST_CODE)
    }

    override fun scheduleNotificationWork() {
         scheduleAlarm(15 * 60 * 1000L)
    }

    private fun scheduleAlarm(delayMillis: Long, requestCode: Int = ALARM_REQUEST_CODE) {
        val intent = Intent(context, VocabularyNotificationReceiver::class.java).apply {
            action = VocabularyNotificationReceiver.ACTION_VOCAB_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val triggerAt = System.currentTimeMillis() + delayMillis

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
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
        alarmManager.cancel(pendingIntent)
        alarmManager.cancel(immediatePendingIntent)
        workManager.cancelUniqueWork(WORK_NAME_GENERATION)
    }

    override fun testNotification(delaySeconds: Long) {
        scheduleAlarm(delaySeconds * 1000L)
    }

    companion object {
        const val WORK_NAME_GENERATION = "lockscreen_vocabulary_generation_work"
        const val ALARM_REQUEST_CODE = 3000
        const val IMMEDIATE_ALARM_REQUEST_CODE = 3001
    }
}
