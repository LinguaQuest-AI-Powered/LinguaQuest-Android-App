package com.iti.linguaquest.features.map.domain.model

data class MapLevel(
    val levelNumber: Int,
    val isCompleted: Boolean,
    val isCurrent: Boolean,
    val stars: Int
)
