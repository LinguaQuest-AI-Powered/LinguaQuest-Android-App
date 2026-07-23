package com.iti.linguaquest.features.all_worlds.domain.usecase

import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty
import com.iti.linguaquest.features.all_worlds.domain.model.WorldsData
import com.iti.linguaquest.features.all_worlds.domain.repository.WorldsRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import javax.inject.Inject

class GetWorldsUseCase @Inject constructor(
    private val repository: WorldsRepository
) {
    suspend operator fun invoke(
        difficulty: WorldDifficulty? = null
    ): LinguaQuestResult<WorldsData, LinguaQuestDataError> {
        return repository.getWorlds(difficulty)
    }
}
