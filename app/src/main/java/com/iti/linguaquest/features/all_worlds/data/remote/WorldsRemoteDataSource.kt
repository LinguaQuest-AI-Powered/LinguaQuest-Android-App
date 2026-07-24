package com.iti.linguaquest.features.all_worlds.data.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.all_worlds.data.remote.dto.WorldsDataDto

interface WorldsRemoteDataSource {
    suspend fun getWorlds(
        languageId: Int?,
        difficulty: String?
    ): LinguaQuestResult<WorldsDataDto, LinguaQuestDataError>
}
