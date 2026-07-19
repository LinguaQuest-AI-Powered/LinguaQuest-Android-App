package com.iti.linguaquest.features.map.data.remote.dto

data class WorldMapDetailDto(
    val id: Int,
    val name: String,
    val difficulty: String,
    val levels: List<MapLevelDto>
)
