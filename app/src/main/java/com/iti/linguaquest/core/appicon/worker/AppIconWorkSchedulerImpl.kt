package com.iti.linguaquest.core.appicon.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppIconWorkSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppIconWorkScheduler {

    override fun scheduleDailyRefresh() {
        val request = PeriodicWorkRequestBuilder<AppIconRefreshWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    private companion object {
        const val WORK_NAME = "app_icon_refresh_work"
    }
}
