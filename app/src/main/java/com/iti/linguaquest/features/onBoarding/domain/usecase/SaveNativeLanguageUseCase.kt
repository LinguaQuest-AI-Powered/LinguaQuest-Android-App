package com.iti.linguaquest.features.onBoarding.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import jakarta.inject.Inject

class SaveNativeLanguageUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(languageId: Int) {
        repository.saveNativeLanguage(languageId)
    }
}
