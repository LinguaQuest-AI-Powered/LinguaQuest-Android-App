package com.iti.linguaquest.features.dailymission.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.features.dailymission.domain.model.DailyMission
import com.iti.linguaquest.features.dailymission.domain.model.VerifyMissionResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface DailyMissionRepository {
    suspend fun getDailyMission(): LinguaQuestResult<DailyMission, LinguaQuestDataError>
    suspend fun verifyMission(
        image: MultipartBody.Part,
        word: RequestBody
    ): LinguaQuestResult<VerifyMissionResult, LinguaQuestDataError>
}
