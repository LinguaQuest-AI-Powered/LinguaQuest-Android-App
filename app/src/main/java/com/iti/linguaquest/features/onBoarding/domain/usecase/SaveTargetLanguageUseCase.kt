package com.iti.linguaquest.features.onBoarding.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveTargetLanguageUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(languageId: Int, name: String, code: String) {
        repository.saveTargetLanguage(languageId, name, code)
    }
}
