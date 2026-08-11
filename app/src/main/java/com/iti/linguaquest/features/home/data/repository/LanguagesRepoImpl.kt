package com.iti.linguaquest.features.home.data.repository

import com.iti.linguaquest.core.database.languages.LanguagesDao
import com.iti.linguaquest.core.database.languages.toEntity
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.mapper.toDomain
import com.iti.linguaquest.features.home.data.dataSource.remote.LanguagesRemoteDataSource
import com.iti.linguaquest.features.home.domain.model.UserLanguage
import com.iti.linguaquest.features.home.domain.repository.LanguagesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguagesRepoImpl @Inject constructor(
    private val remoteDataSource: LanguagesRemoteDataSource,
    private val languagesDao: LanguagesDao
) : LanguagesRepo {

    override fun getMyLanguages(): Flow<LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError>> = channelFlow {
        languagesDao.getMyLanguagesFlow().collect { cached ->
            send(LinguaQuestResult.Success(cached.map { it.toDomain() }))
            
            if (cached.isEmpty()) {
                val remoteResult = remoteDataSource.getMyLanguages()
                if (remoteResult is LinguaQuestResult.Success) {
                    val domainLanguages = remoteResult.data.map { it.toDomain() }
                    languagesDao.clearAndInsertMyLanguages(domainLanguages.map { it.toEntity() })
                } else if (remoteResult is LinguaQuestResult.Failure) {
                    send(remoteResult)
                }
            }
        }
    }

    override suspend fun refreshMyLanguages(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        val remoteResult = remoteDataSource.getMyLanguages()
        if (remoteResult is LinguaQuestResult.Success) {
            val domainLanguages = remoteResult.data.map { it.toDomain() }
            languagesDao.clearAndInsertMyLanguages(domainLanguages.map { it.toEntity() })
            return LinguaQuestResult.Success(Unit)
        }
        return remoteResult as LinguaQuestResult.Failure
    }

    override suspend fun addLanguages(languageIds: List<Int>): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError> {
        val result = remoteDataSource.addLanguages(languageIds)
        if (result is LinguaQuestResult.Success) {
            languagesDao.clearAndInsertMyLanguages(result.data.map { it.toDomain().toEntity() })
            return LinguaQuestResult.Success(result.data.map { it.toDomain() })
        } else {
            val myLanguages = remoteDataSource.getMyLanguages()
            if (myLanguages is LinguaQuestResult.Success) {
                languagesDao.clearAndInsertMyLanguages(myLanguages.data.map { it.toDomain().toEntity() })
            }
        }
        return result as LinguaQuestResult.Failure
    }

    override suspend fun removeLanguages(languageIds: List<Int>): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError> {
        val result = remoteDataSource.removeLanguages(languageIds)
        if (result is LinguaQuestResult.Success) {
            languagesDao.clearAndInsertMyLanguages(result.data.map { it.toDomain().toEntity() })
            return LinguaQuestResult.Success(result.data.map { it.toDomain() })
        } else {
            val myLanguages = remoteDataSource.getMyLanguages()
            if (myLanguages is LinguaQuestResult.Success) {
                languagesDao.clearAndInsertMyLanguages(myLanguages.data.map { it.toDomain().toEntity() })
            }
        }
        return result as LinguaQuestResult.Failure
    }

    override suspend fun setActiveLanguage(languageId: Int): LinguaQuestResult<UserLanguage, LinguaQuestDataError> {
        val result = remoteDataSource.setActiveLanguage(languageId)
        if (result is LinguaQuestResult.Success) {
            val myLanguages = remoteDataSource.getMyLanguages()
            if (myLanguages is LinguaQuestResult.Success) {
                languagesDao.clearAndInsertMyLanguages(myLanguages.data.map { it.toDomain().toEntity() })
            }
            return LinguaQuestResult.Success(result.data.toDomain())
        }
        return result as LinguaQuestResult.Failure
    }

    override suspend fun setNativeLanguage(languageId: Int): LinguaQuestResult<UserLanguage, LinguaQuestDataError> {
        val result = remoteDataSource.setNativeLanguage(languageId)
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(
                result.data.toDomain()
            )
            is LinguaQuestResult.Failure -> result
        }
    }
}
