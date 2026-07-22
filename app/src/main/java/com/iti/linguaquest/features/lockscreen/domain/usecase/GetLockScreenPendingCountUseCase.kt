package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLockScreenPendingCountUseCase @Inject constructor(
    private val repository: LockScreenRepository
) {
    operator fun invoke(): Flow<Int> = repository.pendingCount
}
