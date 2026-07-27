package com.iti.linguaquest.features.roleplay.domain.model

data class RoleplayObjective(
    val targetLanguage: String,
    val setting: String,
    val taskDescription: String,
    val maxTurns: Int = 6
)
