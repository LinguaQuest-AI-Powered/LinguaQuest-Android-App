package com.iti.linguaquest.features.home.data.remote.dto


data class HomeSummaryDto(
    val xp: Int,
    val coins: Int,
    val streakDays: Int,
    val activeLanguage: ActiveLanguageDto,
    val continueLesson: ContinueLessonDto?,
    val exploreWorlds: List<ExploreWorldDto>
)

data class ActiveLanguageDto(
    val id: Int,
    val name: String,
    val code: String,
    val level: Int,
    val levelProgressPercent: Int
)

data class ContinueLessonDto(
    val worldId: Int,
    val worldName: String,
    val levelId: Int,
    val word: String,
    val translation: String,
    val imageUrl: String,
    val sentence: String
)

data class ExploreWorldDto(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val difficulty: String,
    val status: String,
    val progressPercent: Int,
    val totalLevels: Int,
    val completedLevels: Int
)