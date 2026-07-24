package com.iti.linguaquest.features.all_worlds.domain.repository

import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty
import com.iti.linguaquest.features.all_worlds.domain.model.WorldsData
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult

interface WorldsRepository {
    suspend fun getWorlds(
        difficulty: WorldDifficulty? = null
    ): LinguaQuestResult<WorldsData, LinguaQuestDataError>
}
