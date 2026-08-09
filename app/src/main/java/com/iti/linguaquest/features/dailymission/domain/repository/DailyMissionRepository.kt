package com.iti.linguaquest.features.dailymission.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.features.dailymission.domain.model.DailyMission
import com.iti.linguaquest.features.dailymission.domain.model.VerifyMissionResult
import java.io.File

interface DailyMissionRepository {
    suspend fun getDailyMission(): LinguaQuestResult<DailyMission, LinguaQuestDataError>
    suspend fun verifyMission(
        imageFile: File,
        word: String
    ): LinguaQuestResult<VerifyMissionResult, LinguaQuestDataError>
}
