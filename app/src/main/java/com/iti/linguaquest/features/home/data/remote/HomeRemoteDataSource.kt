package com.iti.linguaquest.features.home.data.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.remote.dto.HomeSummaryDto

interface HomeRemoteDataSource {
    suspend fun getHomeSummary(): LinguaQuestResult<HomeSummaryDto, LinguaQuestDataError>
}
