package com.iti.linguaquest.features.home.data.dataSource.remote

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.AddLanguagesRequestDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.RemoveLanguagesRequestDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.SetActiveLanguageRequestDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.SetNativeLanguageRequestDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.UserLanguageDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.LanguageOptionDto
import javax.inject.Inject

class LanguagesRemoteDataSourceImpl @Inject constructor(
    private val api: LanguagesApiService,
    private  val save :UserPreferencesRepository
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

    override suspend fun removeLanguages(languageIds: List<Int>): LinguaQuestResult<List<UserLanguageDto>, LinguaQuestDataError> {
        val result = safeApiCall { api.removeLanguages(RemoveLanguagesRequestDto(languageIds)) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.languages)
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun setActiveLanguage(languageId: Int): LinguaQuestResult<UserLanguageDto, LinguaQuestDataError> {
            val result = safeApiCall {
                api.setActiveLanguage(SetActiveLanguageRequestDto(languageId))
            }
            return when (result) {
                is LinguaQuestResult.Success -> {
                    val activeLanguage = result.data.data.activeLanguage
                    save.saveTargetLanguage(
                        languageId = activeLanguage.id,
                        name = activeLanguage.name,
                        code = activeLanguage.code
                    )
                    LinguaQuestResult.Success(activeLanguage)
                }
                is LinguaQuestResult.Failure -> result
            }
        }

    override suspend fun setNativeLanguage(languageId: Int): LinguaQuestResult<UserLanguageDto, LinguaQuestDataError> {
        val result = safeApiCall {
            api.setNativeLanguage(SetNativeLanguageRequestDto(languageId))
        }
        return when (result) {
            is LinguaQuestResult.Success -> {
                val activeLanguage = result.data.data

                LinguaQuestResult.Success(activeLanguage)
            }
            is LinguaQuestResult.Failure -> result
        }
    }
}
