package com.iti.linguaquest.features.dailymission.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.features.dailymission.domain.model.VerifyMissionResult
import com.iti.linguaquest.features.dailymission.domain.repository.DailyMissionRepository
import java.io.File
import javax.inject.Inject

class VerifyDailyMissionUseCase @Inject constructor(
    private val repository: DailyMissionRepository
) {
    suspend operator fun invoke(
        imageFile: File,
        word: String
    ): LinguaQuestResult<VerifyMissionResult, LinguaQuestDataError> {
        return repository.verifyMission(imageFile, word)
    }
}
