package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import javax.inject.Inject

class EvaluateBossStageUseCase @Inject constructor(
    private val repository: RoleplayRepository
) {
    suspend operator fun invoke(transcript: List<String>, scenario: BossScenario): Result<BossEvaluationResult> {
        return repository.evaluateBossStage(transcript, scenario)
    }
}
