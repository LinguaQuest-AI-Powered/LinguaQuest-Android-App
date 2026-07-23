package com.iti.linguaquest.features.onBoarding.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.SessionManagerRepository
import jakarta.inject.Inject

class SetIsFirstTimeUseCase @Inject constructor(
    private val sessionManagerRepository: SessionManagerRepository,
) {
    suspend operator fun invoke(isFirstTime: Boolean) {
        sessionManagerRepository.saveFirstTime(isFirstTime)
    }
}