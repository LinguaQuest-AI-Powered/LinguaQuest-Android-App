package com.iti.linguaquest.features.map.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.features.map.domain.model.WorldMapDetail

interface MapRepository {
    suspend fun getWorldMapDetail(worldId: Int): LinguaQuestResult<WorldMapDetail, AppError>
}
