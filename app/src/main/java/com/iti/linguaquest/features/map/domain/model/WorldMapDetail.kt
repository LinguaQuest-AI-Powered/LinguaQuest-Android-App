package com.iti.linguaquest.features.map.domain.model

data class WorldMapDetail(
    val id: Int,
    val name: String,
    val difficulty: String,
    val levels: List<MapLevel>
)
