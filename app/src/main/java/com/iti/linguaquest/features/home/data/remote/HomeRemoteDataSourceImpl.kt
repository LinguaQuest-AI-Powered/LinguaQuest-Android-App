package com.iti.linguaquest.features.home.data.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.remote.dto.HomeSummaryDto
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val api: HomeApiService
) : HomeRemoteDataSource {

    override suspend fun getHomeSummary(): LinguaQuestResult<HomeSummaryDto, LinguaQuestDataError> {
        val result = safeApiCall { api.getHomeSummary() }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }
}
