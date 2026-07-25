package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import javax.inject.Inject

class ConnectToBossStageUseCase @Inject constructor(
    private val repository: RoleplayRepository
) {
    suspend operator fun invoke(scenario: BossScenario) {
        repository.connectToBossStage(scenario)
    }
}
