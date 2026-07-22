package com.iti.linguaquest.features.lockscreen.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VocabularyWorkSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : VocabularyWorkScheduler {

    private val workManager by lazy { WorkManager.getInstance(context) }

    override fun scheduleNotificationWork() {
        val request = PeriodicWorkRequestBuilder<VocabularyNotificationWorker>(2, TimeUnit.HOURS).build()
        workManager.enqueueUniquePeriodicWork(
            WORK_NAME_NOTIFICATION,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
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
        workManager.cancelUniqueWork(WORK_NAME_NOTIFICATION)
        workManager.cancelUniqueWork(WORK_NAME_GENERATION)
    }

    companion object {
        const val WORK_NAME_NOTIFICATION = "lockscreen_vocabulary_notification_work"
        const val WORK_NAME_GENERATION = "lockscreen_vocabulary_generation_work"
    }
}
