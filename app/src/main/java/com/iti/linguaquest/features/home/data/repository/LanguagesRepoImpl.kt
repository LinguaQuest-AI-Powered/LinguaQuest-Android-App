package com.iti.linguaquest.features.home.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.mapper.toDomain
import com.iti.linguaquest.features.home.data.dataSource.remote.LanguagesRemoteDataSource
import com.iti.linguaquest.features.home.domain.model.UserLanguage
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import com.iti.linguaquest.features.home.domain.repository.LanguagesRepo
import javax.inject.Inject

class LanguagesRepoImpl @Inject constructor(
    private val remoteDataSource: LanguagesRemoteDataSource
) : LanguagesRepo {

    override suspend fun getMyLanguages(): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError> {
        val result = remoteDataSource.getMyLanguages()
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(
                result.data.map { it.toDomain() }
            )
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun getAvailableLanguages(): LinguaQuestResult<List<LanguageOption>, LinguaQuestDataError> {
        val result = remoteDataSource.getAvailableLanguages()
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(
                result.data.map { it.toDomain() }
            )
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun addLanguages(languageIds: List<Int>): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError> {
        val result = remoteDataSource.addLanguages(languageIds)
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(
                result.data.map { it.toDomain() }
            )
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun setActiveLanguage(languageId: Int): LinguaQuestResult<UserLanguage, LinguaQuestDataError> {
        val result = remoteDataSource.setActiveLanguage(languageId)
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(
                result.data.toDomain()
            )
            is LinguaQuestResult.Failure -> result
        }
    }
}
