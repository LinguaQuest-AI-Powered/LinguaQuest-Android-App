package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.core.preferences.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReminderEnabledUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.reminderEnabled
}
