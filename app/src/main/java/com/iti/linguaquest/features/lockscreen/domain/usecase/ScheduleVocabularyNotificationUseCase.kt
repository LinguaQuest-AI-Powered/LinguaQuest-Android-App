package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.features.lockscreen.worker.VocabularyWorkScheduler
import javax.inject.Inject

class ScheduleVocabularyNotificationUseCase @Inject constructor(
    private val scheduler: VocabularyWorkScheduler
) {
    operator fun invoke(immediate: Boolean = false) {
        if (immediate) {
            scheduler.scheduleImmediateNotification()
        }
        scheduler.scheduleNotificationWork()
    }
}