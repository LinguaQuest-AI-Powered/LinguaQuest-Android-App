package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class ResolveMindReaderNativeLanguageCodeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(fallbackLanguageCode: String = "en"): String {
        val nativeLanguageId = userPreferencesRepository.nativeLanguage.first()
        val nativeLanguageName = userPreferencesRepository.nativeLanguageName.first()
        val appLanguage = userPreferencesRepository.appLanguage.first()
        val availableLanguages = loadAvailableLanguages()

        nativeLanguageId
            ?.let { id -> availableLanguages.firstOrNull { it.id == id }?.code }
            ?.let { return it }

        nativeLanguageName
            ?.let { name -> availableLanguages.firstOrNull { it.name.equals(name, ignoreCase = true) }?.code }
            ?.let { return it }

        appLanguage
            .takeIf { it.isNotBlank() }
            ?.let { code -> availableLanguages.firstOrNull { it.code.equals(code, ignoreCase = true) }?.code }
            ?.let { return it }

        return fallbackLanguageCode
    }

    private suspend fun loadAvailableLanguages(): List<LanguageOption> {
        return when (val result = authRepository.getAuthLanguages()) {
            is LinguaQuestResult.Success -> result.data
            is LinguaQuestResult.Failure -> emptyList()
        }
    }
}
