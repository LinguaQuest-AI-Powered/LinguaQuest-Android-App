package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.features.lockscreen.worker.VocabularyWorkScheduler
import javax.inject.Inject

class EnqueueGenerationWorkUseCase @Inject constructor(
    private val scheduler: VocabularyWorkScheduler
) {
    operator fun invoke() {
        scheduler.enqueueGenerationWork()
    }
}