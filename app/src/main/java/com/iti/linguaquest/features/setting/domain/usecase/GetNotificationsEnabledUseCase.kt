package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.core.preferences.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userPreferencesRepository.notificationsEnabled
    }
}
