package com.iti.linguaquest.features.roleplay.domain.model

data class BossScenario(
    val id: String,
    val worldId: String,
    val bossName: String,
    val roleDescription: String,
    val taskObjective: String
)
