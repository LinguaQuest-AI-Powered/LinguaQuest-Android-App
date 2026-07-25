package com.iti.linguaquest.features.home.domain.model

import com.iti.linguaquest.features.all_worlds.domain.model.World

data class HomeSummary(
    val xp: Int,
    val coins: Int,
    val streakDays: Int,
    val activeLanguage: ActiveLanguage?,
    val exploreWorlds: List<World>
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