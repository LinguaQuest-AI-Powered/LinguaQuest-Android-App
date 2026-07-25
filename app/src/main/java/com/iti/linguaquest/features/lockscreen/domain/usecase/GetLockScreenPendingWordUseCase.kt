package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLockScreenPendingWordUseCase @Inject constructor(
    private val repository: LockScreenRepository
) {
    operator fun invoke(): Flow<LockScreenWord?> = repository.pendingWord
}
