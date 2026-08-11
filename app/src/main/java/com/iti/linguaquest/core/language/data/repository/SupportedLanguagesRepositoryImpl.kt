package com.iti.linguaquest.core.language.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iti.linguaquest.core.language.data.datasource.local.SupportedLanguagesCacheDataSource
import com.iti.linguaquest.core.language.data.datasource.remote.LanguageRemoteDataSource
import com.iti.linguaquest.core.language.domain.repository.SupportedLanguagesRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.map
import com.iti.linguaquest.core.result.onSuccess
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SupportedLanguagesRepositoryImpl @Inject constructor(
    private val remoteDataSource: LanguageRemoteDataSource,
    private val cacheDataSource: SupportedLanguagesCacheDataSource
) : SupportedLanguagesRepository {

    override suspend fun getSupportedLanguages(): LinguaQuestResult<List<LanguageOption>, LinguaQuestDataError> {
        val cachedJson = cacheDataSource.cachedSupportedLanguages.first()
        if (!cachedJson.isNullOrEmpty()) {
            try {
                val type = object : TypeToken<List<LanguageOption>>() {}.type
                val cached: List<LanguageOption> = Gson().fromJson(cachedJson, type)
                if (cached.isNotEmpty()) {
                    return LinguaQuestResult.Success(cached)
                }
            } catch (e: Exception) {
            }
        }

        return remoteDataSource.getSupportedLanguages()
            .map { response ->
                response.languages.map { dto ->
                    LanguageOption(
                        id = dto.id,
                        name = dto.name,
                        code = dto.code,
                        imageUrl = dto.imageUrl,
                        isAdded = dto.isAdded
                    )
                }
            }
            .onSuccess { languages ->
                try {
                    val json = Gson().toJson(languages)
                    cacheDataSource.saveCachedSupportedLanguages(json)
                } catch (e: Exception) {
                }
            }
    }
}
