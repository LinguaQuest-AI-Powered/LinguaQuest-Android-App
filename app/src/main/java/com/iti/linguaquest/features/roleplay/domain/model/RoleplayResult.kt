package com.iti.linguaquest.features.roleplay.domain.model

data class RoleplayResult(
    val passed: Boolean,
    val coinsAwarded: Int,
    val feedback: String,
    val totalTurns: Int
)
