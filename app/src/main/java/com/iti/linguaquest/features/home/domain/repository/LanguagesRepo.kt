package com.iti.linguaquest.features.home.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.UserLanguage
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import kotlinx.coroutines.flow.Flow

interface LanguagesRepo {
    fun getMyLanguages(): Flow<LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError>>
    suspend fun refreshMyLanguages(): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun addLanguages(languageIds: List<Int>): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError>
    suspend fun removeLanguages(languageIds: List<Int>): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError>
    suspend fun setActiveLanguage(languageId: Int): LinguaQuestResult<UserLanguage, LinguaQuestDataError>
    suspend fun setNativeLanguage(languageId: Int): LinguaQuestResult<UserLanguage, LinguaQuestDataError>
}
