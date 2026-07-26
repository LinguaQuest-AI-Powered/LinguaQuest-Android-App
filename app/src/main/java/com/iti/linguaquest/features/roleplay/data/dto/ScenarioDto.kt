package com.iti.linguaquest.features.roleplay.data.dto

import com.google.gson.annotations.SerializedName

import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId

data class ScenarioDto(
    @SerializedName("id") val id: ScenarioId,
    @SerializedName("worldId") val worldId: String,
    @SerializedName("bossName") val bossName: String,
    @SerializedName("roleDescription") val roleDescription: String,
    @SerializedName("objective") val objective: String,
    @SerializedName("voiceName") val voiceName: String
)
