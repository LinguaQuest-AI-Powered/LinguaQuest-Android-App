package com.iti.linguaquest.features.onBoarding.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTargetLanguageUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Int?> {
        return repository.targetLanguage
    }
}
