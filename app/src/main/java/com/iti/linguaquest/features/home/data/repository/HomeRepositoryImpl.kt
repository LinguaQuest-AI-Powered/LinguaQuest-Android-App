package com.iti.linguaquest.features.home.data.repository


import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.mapper.toDomain
import com.iti.linguaquest.features.home.data.remote.HomeApiService
import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApiService
) : HomeRepository {

    override suspend fun getHomeSummary(): LinguaQuestResult<HomeSummary, LinguaQuestDataError> {
        val result = safeApiCall { api.getHomeSummary() }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.toDomain())
            is LinguaQuestResult.Failure -> result
        }
    }
}