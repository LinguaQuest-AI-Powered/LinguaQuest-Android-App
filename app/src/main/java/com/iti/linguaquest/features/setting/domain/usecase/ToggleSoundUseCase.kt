package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class ToggleSoundUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        userPreferencesRepository.saveSoundEnabled(enabled)
    }
}
