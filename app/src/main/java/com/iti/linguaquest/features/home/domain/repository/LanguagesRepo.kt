package com.iti.linguaquest.features.home.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.UserLanguage
import com.iti.linguaquest.features.home.domain.model.LanguageOption

interface LanguagesRepo {
    suspend fun getMyLanguages(): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError>
    suspend fun getAvailableLanguages(): LinguaQuestResult<List<LanguageOption>, LinguaQuestDataError>
    suspend fun addLanguages(languageIds: List<Int>): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError>
    suspend fun setActiveLanguage(languageId: Int): LinguaQuestResult<UserLanguage, LinguaQuestDataError>
    suspend fun setNativeLanguage(languageId: Int): LinguaQuestResult<UserLanguage, LinguaQuestDataError>
}
