package com.iti.linguaquest.features.onBoarding.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import jakarta.inject.Inject

class SaveProficiencyLevelUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(level: String) {
        repository.saveProficiencyLevel(level)
    }
}
