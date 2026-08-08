package com.iti.linguaquest.features.dailymission.data.repository

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.features.dailymission.data.remote.DailyMissionRemoteDataSource
import com.iti.linguaquest.features.dailymission.domain.model.DailyMission
import com.iti.linguaquest.features.dailymission.domain.model.VerifyMissionResult
import com.iti.linguaquest.features.dailymission.domain.repository.DailyMissionRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class DailyMissionRepositoryImpl @Inject constructor(
    private val remoteDataSource: DailyMissionRemoteDataSource
) : DailyMissionRepository {
    override suspend fun getDailyMission(): LinguaQuestResult<DailyMission, LinguaQuestDataError> {
        return when (val result = safeApiCall { remoteDataSource.getDailyMission() }) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(DailyMission(word = result.data.data.word))
            is LinguaQuestResult.Failure -> LinguaQuestResult.Failure(result.error)
        }
    }

    override suspend fun verifyMission(
        image: MultipartBody.Part,
        word: RequestBody
    ): LinguaQuestResult<VerifyMissionResult, LinguaQuestDataError> {
        return when (val result = safeApiCall { remoteDataSource.verifyMission(image, word) }) {
            is LinguaQuestResult.Success -> {
                LinguaQuestResult.Success(
                    VerifyMissionResult(
                        isMatch = result.data.data.isMatch,
                        xpEarned = result.data.data.xpEarned,
                        coinsEarned = result.data.data.coinsEarned
                    )
                )
            }
            is LinguaQuestResult.Failure -> LinguaQuestResult.Failure(result.error)
        }
    }
}
