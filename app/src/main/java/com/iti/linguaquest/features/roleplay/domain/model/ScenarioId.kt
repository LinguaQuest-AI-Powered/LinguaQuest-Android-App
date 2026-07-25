package com.iti.linguaquest.features.roleplay.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
enum class ScenarioId(val rawValue: String) {
    @SerializedName("scenario_market_01")
    SCENARIO_MARKET_01("scenario_market_01"),

    @SerializedName("scenario_cafe_01")
    SCENARIO_CAFE_01("scenario_cafe_01");

    companion object {
        fun fromString(value: String?): ScenarioId? {
            return entries.find { it.rawValue == value }
        }
    }
}
