package com.iti.linguaquest.features.home.domain.repository

import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun observeHomeSummary(): Flow<HomeSummary?>

    suspend fun refreshHomeSummary(): LinguaQuestResult<HomeSummary, LinguaQuestDataError>
}