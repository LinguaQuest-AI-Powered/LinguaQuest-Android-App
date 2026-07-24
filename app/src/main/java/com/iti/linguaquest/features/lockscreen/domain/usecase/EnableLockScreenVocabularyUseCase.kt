package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import javax.inject.Inject

class EnableLockScreenVocabularyUseCase @Inject constructor(
    private val repository: LockScreenRepository
) {
    suspend operator fun invoke(operationId: String, amount: Int = 50): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return repository.deduceCoinsAndEnable(operationId = operationId, amount = amount)
    }
}
