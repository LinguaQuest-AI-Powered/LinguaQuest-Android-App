package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import javax.inject.Inject

class GetAuthLanguagesUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<List<LanguageOption>, AuthError> {
        return repository.getAuthLanguages()
    }
}
