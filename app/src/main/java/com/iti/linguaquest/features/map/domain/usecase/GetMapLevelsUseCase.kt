package com.iti.linguaquest.features.map.domain.usecase

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.map.domain.model.WorldMapDetail
import com.iti.linguaquest.features.map.domain.repository.MapRepository
import javax.inject.Inject

class GetMapLevelsUseCase @Inject constructor(
    private val repository: MapRepository
) {
    suspend operator fun invoke(worldId: Int): LinguaQuestResult<WorldMapDetail, AppError> {
        return repository.getWorldMapDetail(worldId)
    }
}
