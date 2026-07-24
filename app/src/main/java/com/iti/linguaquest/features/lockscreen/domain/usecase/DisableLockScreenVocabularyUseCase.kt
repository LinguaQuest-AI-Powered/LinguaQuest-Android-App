package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import javax.inject.Inject

class DisableLockScreenVocabularyUseCase @Inject constructor(
    private val repository: LockScreenRepository
) {
    suspend operator fun invoke() = repository.disable()
}
