package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.features.setting.domain.manager.LanguageManager
import javax.inject.Inject

class ChangeAppLanguageUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val languageManager: LanguageManager
) {
    suspend operator fun invoke(languageCode: String) {
        userPreferencesRepository.saveAppLanguage(languageCode)
        languageManager.changeLanguage(languageCode)
    }
}
