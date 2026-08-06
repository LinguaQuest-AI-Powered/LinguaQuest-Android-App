package com.iti.linguaquest.features.home.domain.model

import com.iti.linguaquest.features.all_worlds.domain.model.World

data class HomeSummary(
    val xp: Int,
    val coins: Int,
    val streakDays: Int,
    val activeLanguage: ActiveLanguage?,
    val exploreWorlds: List<World>,
    val continueLevel: ContinueLevel?
)

data class ActiveLanguage(
    val id: Int,
    val name: String,
    val code: String,
    val imageUrl: String? = null,
    val level: Int,
    val levelProgressPercent: Int,
    val isActive: Boolean = true
)

data class ContinueLevel(
    val worldId: Int,
    val worldName: String,
    val levelId: Int,
    val levelOrder: Int,
    val word: String
)