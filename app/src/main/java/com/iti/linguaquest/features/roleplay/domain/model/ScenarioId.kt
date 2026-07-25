package com.iti.linguaquest.features.roleplay.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
enum class ScenarioId(val rawValue: String) {
    @SerializedName("scenario_market_01")
    SCENARIO_MARKET_01("scenario_market_01"),

    @SerializedName("scenario_cafe_01")
    SCENARIO_CAFE_01("scenario_cafe_01"),

    @SerializedName("scenario_library_01")
    SCENARIO_LIBRARY_01("scenario_library_01"),

    @SerializedName("scenario_school_01")
    SCENARIO_SCHOOL_01("scenario_school_01"),

    @SerializedName("scenario_taxi_01")
    SCENARIO_TAXI_01("scenario_taxi_01"),

    @SerializedName("scenario_apartment_01")
    SCENARIO_APARTMENT_01("scenario_apartment_01");

    companion object {
        fun fromString(value: String?): ScenarioId? {
            return entries.find { it.rawValue == value }
        }
    }
}
