package com.iti.linguaquest.features.all_worlds.domain.model

data class World(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val difficulty: WorldDifficulty,
    val progressPercent: Int,
    val totalLevels: Int,
    val completedLevels: Int
)

enum class WorldDifficulty { EASY, MEDIUM, HARD }
