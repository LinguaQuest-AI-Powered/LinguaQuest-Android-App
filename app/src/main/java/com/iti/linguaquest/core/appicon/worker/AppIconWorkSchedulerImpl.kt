package com.iti.linguaquest.core.appicon.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.iti.linguaquest.core.appicon.domain.AppIconTiming
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class AppIconWorkSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppIconWorkScheduler {

    override fun scheduleDailyRefresh() {
        val request = PeriodicWorkRequestBuilder<AppIconRefreshWorker>(
            AppIconTiming.PERIODIC_REFRESH_INTERVAL_MINUTES, TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DAILY_REFRESH_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    override fun scheduleAngryWindowCheck() {
        val request = OneTimeWorkRequestBuilder<AppIconRefreshWorker>()
            .setInitialDelay(AppIconTiming.ANGRY_CHECK_DELAY_SECONDS, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            STATE_TRANSITION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun scheduleFollowUpCheck(delayMillis: Long) {
        val safeDelay = delayMillis.coerceAtLeast(0L)
        val request = if (safeDelay == 0L) {
            OneTimeWorkRequestBuilder<AppIconRefreshWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
        } else {
            OneTimeWorkRequestBuilder<AppIconRefreshWorker>()
                .setInitialDelay(safeDelay, TimeUnit.MILLISECONDS)
                .build()
        }
        WorkManager.getInstance(context).enqueueUniqueWork(
            STATE_TRANSITION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun scheduleBackgroundExitCheck() {
        Timber.d("AppIcon: Scheduling BACKGROUND_EXIT check in ${AppIconTiming.BACKGROUND_EXIT_DELAY_SECONDS}s")
        val request = OneTimeWorkRequestBuilder<AppIconRefreshWorker>()
            .setInitialDelay(AppIconTiming.BACKGROUND_EXIT_DELAY_SECONDS, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            BACKGROUND_EXIT_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun cancelBackgroundExitCheck() {
        WorkManager.getInstance(context).cancelUniqueWork(BACKGROUND_EXIT_WORK_NAME)
    }

    private companion object {
        const val DAILY_REFRESH_WORK_NAME = "app_icon_refresh_work"
        const val STATE_TRANSITION_WORK_NAME = "app_icon_state_transition_work"
        const val BACKGROUND_EXIT_WORK_NAME = "app_icon_background_exit_work"
    }
}
