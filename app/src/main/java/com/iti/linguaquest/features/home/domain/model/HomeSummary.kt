package com.iti.linguaquest.features.home.domain.model


data class HomeSummary(
    val xp: Int,
    val coins: Int,
    val streakDays: Int,
    val activeLanguage: ActiveLanguage,
    val continueLesson: ContinueLesson?,
    val exploreWorlds: List<ExploreWorld>
)

data class ActiveLanguage(
    val id: Int,
    val name: String,
    val code: String,
    val level: Int,
    val levelProgressPercent: Int
)

data class ContinueLesson(
    val worldId: Int,
    val worldName: String,
    val levelId: Int,
    val word: String,
    val translation: String,
    val imageUrl: String
)

enum class WorldDifficulty { EASY, MEDIUM, HARD }
enum class WorldStatus { LOCKED, IN_PROGRESS, COMPLETED }

data class ExploreWorld(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val difficulty: WorldDifficulty,
    val status: WorldStatus,
    val progressPercent: Int,
    val totalLevels: Int,
    val completedLevels: Int
)