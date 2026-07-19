package com.iti.linguaquest.features.all_worlds.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WorldsDataDto(
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("worlds") val worlds: List<WorldDto>
)

data class WorldDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("status") val status: String,
    @SerializedName("progressPercent") val progressPercent: Int,
    @SerializedName("totalLevels") val totalLevels: Int,
    @SerializedName("completedLevels") val completedLevels: Int
)
