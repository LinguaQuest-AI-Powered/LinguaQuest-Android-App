package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.repository.ScenarioRepository
import javax.inject.Inject

class GetBossScenariosUseCase @Inject constructor(
    private val repository: ScenarioRepository
) {
    suspend operator fun invoke(languageCode: String): List<BossScenario> {
        return repository.getBossScenarios(languageCode)
    }
}
