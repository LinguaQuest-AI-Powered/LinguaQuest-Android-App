package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.language.domain.manager.LanguageManager
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import javax.inject.Inject

class SyncUserNativeLanguageUseCase @Inject constructor(
    private val getAuthLanguagesUseCase: GetAuthLanguagesUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val languageManager: LanguageManager
) {
    suspend operator fun invoke(nativeLanguage: LanguageOption? = null, nativeLanguageId: Int? = null) {
        if (nativeLanguage == null && nativeLanguageId == null) return

        if (nativeLanguage != null) {
            userPreferencesRepository.saveNativeLanguage(nativeLanguage.id, nativeLanguage.name)
            userPreferencesRepository.saveAppLanguage(nativeLanguage.code)
            languageManager.changeLanguage(nativeLanguage.code)
            return
        }

        val result = getAuthLanguagesUseCase()
        if (result is LinguaQuestResult.Success) {
            val matchedLang = result.data.find { it.id == nativeLanguageId }

            if (matchedLang != null) {
                userPreferencesRepository.saveNativeLanguage(matchedLang.id, matchedLang.name)
                userPreferencesRepository.saveAppLanguage(matchedLang.code)
                languageManager.changeLanguage(matchedLang.code)
            }
        }
    }
}

