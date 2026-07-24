package com.iti.linguaquest.features.onBoarding.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.SessionManagerRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class CheckIsFirstTimeUseCase @Inject constructor(
    private val sessionManagerRepository: SessionManagerRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return sessionManagerRepository.firstTime
    }
}
