package com.iti.linguaquest.core.appicon.worker


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters

import com.iti.linguaquest.core.appicon.domain.AppIconService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class AppIconRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val appIconService: AppIconService
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            appIconService.refresh()
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "AppIcon: refresh worker failed")
            Result.retry()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(NOTIFICATION_ID, createNotification())
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "App Icon",
                NotificationManager.IMPORTANCE_MIN
            )
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("")
            .setSmallIcon(android.R.drawable.ic_menu_gallery)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setSilent(true)
            .build()
    }

    private companion object {
        const val CHANNEL_ID = "app_icon_refresh"
        const val NOTIFICATION_ID = 9901
    }
}
