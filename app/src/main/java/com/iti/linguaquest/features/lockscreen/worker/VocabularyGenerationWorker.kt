package com.iti.linguaquest.features.lockscreen.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.domain.usecase.GenerateVocabularyBatchUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class VocabularyGenerationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val generateVocabularyBatchUseCase: GenerateVocabularyBatchUseCase,
    private val scheduler: VocabularyWorkScheduler
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return when (val result = generateVocabularyBatchUseCase()) {
            is LinguaQuestResult.Success -> {
                scheduler.scheduleImmediateNotification()
                scheduler.scheduleNotificationWork()
                Result.success()
            }
            is LinguaQuestResult.Failure -> {
                if (result.error.shouldRetryAutomatically()) {
                    Result.retry()
                } else {
                    Result.success()
                }
            }
        }
    }

    private fun LinguaQuestDataError.shouldRetryAutomatically(): Boolean {
        return when (this) {
            LinguaQuestDataError.Remote.REQUEST_TIMEOUT,
            LinguaQuestDataError.Remote.NO_INTERNET,
            LinguaQuestDataError.Remote.TOO_MANY_REQUESTS,
            LinguaQuestDataError.Remote.SERVER -> true
            LinguaQuestDataError.Remote.BAD_REQUEST,
            LinguaQuestDataError.Remote.UNAUTHORIZED,
            LinguaQuestDataError.Remote.SERIALIZATION,
            LinguaQuestDataError.Remote.EMPTY_RESULT,
            LinguaQuestDataError.Remote.UNKNOWN,
            LinguaQuestDataError.Local.NOT_FOUND,
            LinguaQuestDataError.Local.DISK_FULL,
            LinguaQuestDataError.Local.CONSTRAINT_VIOLATION,
            LinguaQuestDataError.Local.UNKNOWN,
            is LinguaQuestDataError.CustomServerMessage -> false
            else -> false
        }
    }
}
