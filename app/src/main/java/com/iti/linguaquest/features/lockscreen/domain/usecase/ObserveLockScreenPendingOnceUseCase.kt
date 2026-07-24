package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import javax.inject.Inject

class ObserveLockScreenPendingOnceUseCase @Inject constructor(
    private val repository: LockScreenRepository
) {
    suspend operator fun invoke(): LockScreenWord? {
        return repository.observePendingOnce()
    }
}
