package com.iti.linguaquest.core.appicon.worker


import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iti.linguaquest.core.appicon.domain.AppIconRefreshSource
import com.iti.linguaquest.core.appicon.domain.AppIconService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AppIconRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val appIconService: AppIconService
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        runCatching {
            appIconService.refresh(AppIconRefreshSource.WORKER)
        }
        return Result.success()
    }
}
