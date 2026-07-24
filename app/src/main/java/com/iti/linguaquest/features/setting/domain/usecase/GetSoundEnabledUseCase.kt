package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSoundEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userPreferencesRepository.soundEnabled
    }
}
