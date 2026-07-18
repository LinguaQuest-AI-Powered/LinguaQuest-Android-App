package com.iti.linguaquest.features.home.domain.repository

import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult


interface HomeRepository {
    suspend fun getHomeSummary(): LinguaQuestResult<HomeSummary, LinguaQuestDataError>
}