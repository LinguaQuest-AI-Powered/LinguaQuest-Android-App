package com.iti.linguaquest.core.language.domain.usecase

import com.iti.linguaquest.core.language.domain.repository.SupportedLanguagesRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import javax.inject.Inject

class GetSupportedLanguagesUseCase @Inject constructor(
    private val repository: SupportedLanguagesRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<List<LanguageOption>, LinguaQuestDataError> {
        return repository.getSupportedLanguages()
    }
}
