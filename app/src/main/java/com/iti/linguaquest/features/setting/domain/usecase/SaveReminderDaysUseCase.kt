package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveReminderDaysUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(days: String) {
        repository.saveReminderDays(days)
    }
}
