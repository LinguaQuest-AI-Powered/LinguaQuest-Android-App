package com.iti.linguaquest.features.home.data.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.remote.dto.AddLanguagesRequestDto
import com.iti.linguaquest.features.home.data.remote.dto.SetActiveLanguageRequestDto
import com.iti.linguaquest.features.home.data.remote.dto.UserLanguageDto
import com.iti.linguaquest.features.home.data.remote.dto.LanguageOptionDto
import javax.inject.Inject

class LanguagesRemoteDataSourceImpl @Inject constructor(
    private val api: LanguagesApiService
) : LanguagesRemoteDataSource {

    override suspend fun getMyLanguages(): LinguaQuestResult<List<UserLanguageDto>, LinguaQuestDataError> {
        val result = safeApiCall { api.getMyLanguages() }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.languages)
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun getAvailableLanguages(): LinguaQuestResult<List<LanguageOptionDto>, LinguaQuestDataError> {
        val result = safeApiCall { api.getAvailableLanguages() }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.languages)
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun addLanguages(languageIds: List<Int>): LinguaQuestResult<List<UserLanguageDto>, LinguaQuestDataError> {
        val result = safeApiCall { api.addLanguages(AddLanguagesRequestDto(languageIds)) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.languages)
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun setActiveLanguage(languageId: Int): LinguaQuestResult<UserLanguageDto, LinguaQuestDataError> {
        val result = safeApiCall { api.setActiveLanguage(SetActiveLanguageRequestDto(languageId)) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.activeLanguage)
            is LinguaQuestResult.Failure -> result
        }
    }
}
