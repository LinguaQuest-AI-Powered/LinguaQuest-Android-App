package com.iti.linguaquest.features.home.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.dataSource.local.HomeLocalDataSource
import com.iti.linguaquest.features.home.data.mapper.toDomain
import com.iti.linguaquest.features.home.data.dataSource.remote.HomeRemoteDataSource
import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
    private val localDataSource: HomeLocalDataSource
) : HomeRepository {

    override fun observeHomeSummary(): Flow<HomeSummary?> {
        return localDataSource.observeHomeSummary().map { dto ->
            dto?.toDomain()
        }
    }

    override suspend fun refreshHomeSummary(): LinguaQuestResult<HomeSummary, LinguaQuestDataError> {
        return when (val result = remoteDataSource.getHomeSummary()) {
            is LinguaQuestResult.Success -> {
                localDataSource.upsertHomeSummary(result.data)
                LinguaQuestResult.Success(result.data.toDomain())
            }
            is LinguaQuestResult.Failure -> result
        }
    }
}