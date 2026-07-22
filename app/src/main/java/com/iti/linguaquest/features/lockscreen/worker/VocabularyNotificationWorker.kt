package com.iti.linguaquest.features.lockscreen.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import com.iti.linguaquest.features.lockscreen.notification.VocabularyNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class VocabularyNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: LockScreenRepository,
    private val notificationManager: VocabularyNotificationManager,
    private val scheduler: VocabularyWorkScheduler
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val word = repository.observePendingOnce() ?: run {
            scheduler.enqueueGenerationWork()
            return Result.success()
        }

        return try {
            notificationManager.show(word)
            repository.markPosted(word.id)
            if (repository.pendingCountOnce() < MIN_PENDING_WORDS) {
                scheduler.enqueueGenerationWork()
            }
            Result.success()
        } catch (_: Exception) {
            try {
                repository.markFailed(word.id)
            } catch (_: Exception) {
                // ignore
            }
            Result.retry()
        }
    }

    companion object {
        const val MIN_PENDING_WORDS = 5
    }
}
