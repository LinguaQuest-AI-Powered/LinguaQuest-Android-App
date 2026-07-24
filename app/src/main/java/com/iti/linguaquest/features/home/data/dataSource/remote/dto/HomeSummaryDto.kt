package com.iti.linguaquest.features.home.data.dataSource.remote.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import java.lang.reflect.Type

data class HomeSummaryDto(
    val xp: Int? = null,
    val coins: Int? = null,
    val streakDays: Int? = null,
    val activeLanguage: ActiveLanguageDto? = null,
    val exploreWorlds: ExploreWorldsContainerDto? = null
)

data class ActiveLanguageDto(
    val id: Int? = null,
    val name: String? = null,
    val code: String? = null,
    val imageUrl: String? = null,
    val level: Int? = null,
    val isActive: Boolean? = null,
    val levelProgressPercent: Int? = null,
    val progressPercent: Int? = null
)

@JsonAdapter(ExploreWorldsDeserializer::class)
data class ExploreWorldsContainerDto(
    val totalCount: Int? = null,
    val worlds: List<ExploreWorldDto>? = null
)

data class ExploreWorldDto(
    val id: Int? = null,
    val name: String? = null,
    val imageUrl: String? = null,
    val difficulty: String? = null,
    val status: String? = null,
    val progressPercent: Int? = null,
    val totalLevels: Int? = null,
    val completedLevels: Int? = null
)

class ExploreWorldsDeserializer : JsonDeserializer<ExploreWorldsContainerDto> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ExploreWorldsContainerDto {
        if (json == null || json.isJsonNull) return ExploreWorldsContainerDto()

        return if (json.isJsonArray) {
            val worldsList = mutableListOf<ExploreWorldDto>()
            json.asJsonArray.forEach { elem ->
                context?.deserialize<ExploreWorldDto>(elem, ExploreWorldDto::class.java)?.let {
                    worldsList.add(it)
                }
            }
            ExploreWorldsContainerDto(totalCount = worldsList.size, worlds = worldsList)
        } else if (json.isJsonObject) {
            val obj = json.asJsonObject
            val totalCount = if (obj.has("totalCount") && !obj.get("totalCount").isJsonNull) obj.get("totalCount").asInt else null
            val worldsList = mutableListOf<ExploreWorldDto>()
            if (obj.has("worlds") && obj.get("worlds").isJsonArray) {
                obj.getAsJsonArray("worlds").forEach { elem ->
                    context?.deserialize<ExploreWorldDto>(elem, ExploreWorldDto::class.java)?.let {
                        worldsList.add(it)
                    }
                }
            }
            ExploreWorldsContainerDto(totalCount = totalCount ?: worldsList.size, worlds = worldsList)
        } else {
            ExploreWorldsContainerDto()
        }
    }
}