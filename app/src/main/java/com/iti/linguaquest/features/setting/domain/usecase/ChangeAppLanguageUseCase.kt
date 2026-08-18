package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.features.home.domain.repository.LanguagesRepo
import com.iti.linguaquest.core.language.domain.manager.LanguageManager
import javax.inject.Inject

class ChangeAppLanguageUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val languageManager: LanguageManager,
    private val languagesRepo: LanguagesRepo
) {
    suspend operator fun invoke(languageId: Int, languageCode: String, languageName: String) {
        userPreferencesRepository.saveAppLanguage(languageCode)
        userPreferencesRepository.saveNativeLanguage(languageId, languageName, languageCode)
        languageManager.changeLanguage(languageCode)
        languagesRepo.setNativeLanguage(languageId)
    }
}
