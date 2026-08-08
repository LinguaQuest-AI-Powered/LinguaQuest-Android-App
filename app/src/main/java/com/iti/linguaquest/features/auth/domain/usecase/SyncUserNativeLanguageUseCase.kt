package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.language.domain.manager.LanguageManager
import com.iti.linguaquest.core.result.LinguaQuestResult
import javax.inject.Inject

class SyncUserNativeLanguageUseCase @Inject constructor(
    private val getAuthLanguagesUseCase: GetAuthLanguagesUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val languageManager: LanguageManager
) {
    suspend operator fun invoke(nativeLanguageName: String? = null, nativeLanguageId: Int? = null) {
        if (nativeLanguageName.isNullOrBlank() && nativeLanguageId == null) return

        val result = getAuthLanguagesUseCase()
        if (result is LinguaQuestResult.Success) {
            val languages = result.data
            val matchedLang = if (nativeLanguageId != null) {
                languages.find { it.id == nativeLanguageId }
            } else {
                languages.find { it.name.equals(nativeLanguageName, ignoreCase = true) }
            }

            if (matchedLang != null) {
                userPreferencesRepository.saveNativeLanguage(matchedLang.id, matchedLang.name)
                userPreferencesRepository.saveAppLanguage(matchedLang.code)
                languageManager.changeLanguage(matchedLang.code)
            }
        }
    }
}

