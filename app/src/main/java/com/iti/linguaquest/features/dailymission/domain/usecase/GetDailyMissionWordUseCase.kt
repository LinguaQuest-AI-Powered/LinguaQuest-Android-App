package com.iti.linguaquest.features.dailymission.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.features.dailymission.domain.model.DailyMission
import com.iti.linguaquest.features.dailymission.domain.repository.DailyMissionRepository
import javax.inject.Inject

class GetDailyMissionWordUseCase @Inject constructor(
    private val repository: DailyMissionRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): LinguaQuestResult<DailyMission, LinguaQuestDataError> {
        return repository.getDailyMission(forceRefresh)
    }
}
