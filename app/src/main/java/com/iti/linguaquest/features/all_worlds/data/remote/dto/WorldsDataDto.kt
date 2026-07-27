package com.iti.linguaquest.features.all_worlds.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WorldsDataDto(
    @SerializedName("totalCount") val totalCount: Int? = null,
    @SerializedName("worlds") val worlds: List<WorldDto>? = null
)

data class WorldDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("difficulty") val difficulty: String? = null,
    @SerializedName("progressPercent") val progressPercent: Int? = null,
    @SerializedName("totalLevels") val totalLevels: Int? = null,
    @SerializedName("completedLevels") val completedLevels: Int? = null
)
