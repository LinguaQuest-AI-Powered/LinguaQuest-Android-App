package com.iti.linguaquest.features.home.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.SessionManagerRepository
import javax.inject.Inject

class ClearLanguageCacheUseCase @Inject constructor(
    private val sessionManagerRepository: SessionManagerRepository
) {
    suspend operator fun invoke() {
        sessionManagerRepository.clearLanguageDependentData()
    }
}
