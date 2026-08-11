package com.iti.linguaquest.core.language.data.datasource.remote

import com.iti.linguaquest.core.language.data.datasource.remote.dto.SupportedLanguagesResponseDataDto
import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import javax.inject.Inject

interface LanguageRemoteDataSource {
    suspend fun getSupportedLanguages(): LinguaQuestResult<SupportedLanguagesResponseDataDto, LinguaQuestDataError>
}

class LanguageRemoteDataSourceImpl @Inject constructor(
    private val api: LanguageApiService
) : LanguageRemoteDataSource {
    override suspend fun getSupportedLanguages(): LinguaQuestResult<SupportedLanguagesResponseDataDto, LinguaQuestDataError> =
        safeApiCall { api.getSupportedLanguages().data }
}
