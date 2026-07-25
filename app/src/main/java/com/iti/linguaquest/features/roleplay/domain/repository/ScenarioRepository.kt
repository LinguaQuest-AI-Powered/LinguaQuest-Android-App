package com.iti.linguaquest.features.roleplay.domain.repository

import com.iti.linguaquest.features.roleplay.domain.model.BossScenario

interface ScenarioRepository {
    suspend fun getBossScenarios(languageCode: String): List<BossScenario>
}
