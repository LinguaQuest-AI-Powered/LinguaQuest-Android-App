package com.iti.linguaquest.features.roleplay.domain.model

data class BossScenario(
    val id: ScenarioId,
    val worldId: String,
    val bossName: String,
    val roleDescription: String,
    val objective: String,
    val voiceName: String
)
