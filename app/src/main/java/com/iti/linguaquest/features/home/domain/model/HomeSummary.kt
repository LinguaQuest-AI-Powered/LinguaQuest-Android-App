package com.iti.linguaquest.features.home.domain.model

import com.iti.linguaquest.features.all_worlds.domain.model.World

data class HomeSummary(
    val xp: Int,
    val coins: Int,
    val streakDays: Int,
    val activeLanguage: ActiveLanguage,
    val continueLesson: ContinueLesson?,
    val exploreWorlds: List<World>
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
    val imageUrl: String,
    val sentence: String
)