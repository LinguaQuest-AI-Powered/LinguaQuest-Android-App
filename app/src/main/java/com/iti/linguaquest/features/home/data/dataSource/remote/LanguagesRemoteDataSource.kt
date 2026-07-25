package com.iti.linguaquest.features.home.data.dataSource.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.UserLanguageDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.LanguageOptionDto

interface LanguagesRemoteDataSource {
    suspend fun getMyLanguages(): LinguaQuestResult<List<UserLanguageDto>, LinguaQuestDataError>
    suspend fun getAvailableLanguages(): LinguaQuestResult<List<LanguageOptionDto>, LinguaQuestDataError>
    suspend fun addLanguages(languageIds: List<Int>): LinguaQuestResult<List<UserLanguageDto>, LinguaQuestDataError>
    suspend fun setActiveLanguage(languageId: Int): LinguaQuestResult<UserLanguageDto, LinguaQuestDataError>
}
