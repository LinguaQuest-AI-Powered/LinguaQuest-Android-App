package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.core.preferences.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveReminderTimeUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(time: String) {
        repository.saveReminderTime(time)
    }
}
