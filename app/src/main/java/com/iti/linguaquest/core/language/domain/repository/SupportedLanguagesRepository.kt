package com.iti.linguaquest.core.language.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.LanguageOption

interface SupportedLanguagesRepository {
    suspend fun getSupportedLanguages(): LinguaQuestResult<List<LanguageOption>, LinguaQuestDataError>
}
