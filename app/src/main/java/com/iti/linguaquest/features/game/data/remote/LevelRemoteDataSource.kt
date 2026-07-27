package com.iti.linguaquest.features.game.data.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.data.remote.dto.StartLevelDto
import com.iti.linguaquest.features.game.data.remote.dto.VerifyLevelDto
import java.io.File

interface LevelRemoteDataSource {
    suspend fun startLevel(worldId: Int, levelId: Int): LinguaQuestResult<StartLevelDto, LinguaQuestDataError>
    suspend fun changeWord(worldId: Int, levelId: Int): LinguaQuestResult<StartLevelDto, LinguaQuestDataError>
    suspend fun verifyLevel(worldId: Int, levelId: Int, imageFile: File): LinguaQuestResult<VerifyLevelDto, LinguaQuestDataError>
    suspend fun getHint(worldId: Int, levelId: Int): LinguaQuestResult<com.iti.linguaquest.features.game.data.remote.dto.HintDto, LinguaQuestDataError>
}
