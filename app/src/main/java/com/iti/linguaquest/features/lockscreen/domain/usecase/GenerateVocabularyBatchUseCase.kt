package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import javax.inject.Inject

class GenerateVocabularyBatchUseCase @Inject constructor(
    private val repository: LockScreenRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<Int, LinguaQuestDataError> {
        return repository.generateBatch()
    }
}
