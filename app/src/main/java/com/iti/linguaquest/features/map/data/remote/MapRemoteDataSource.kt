package com.iti.linguaquest.features.map.data.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.map.data.remote.dto.WorldMapDetailDto

interface MapRemoteDataSource {
    suspend fun getWorldMapDetail(worldId: Int): LinguaQuestResult<WorldMapDetailDto, LinguaQuestDataError>
}
