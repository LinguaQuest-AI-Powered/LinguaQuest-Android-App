package com.iti.linguaquest.features.home.data.mapper

import com.iti.linguaquest.features.home.data.dataSource.remote.dto.ActiveLanguageDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.ExploreWorldDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.HomeSummaryDto
import com.iti.linguaquest.features.home.domain.model.ActiveLanguage
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.ContinueLevelDto
import com.iti.linguaquest.features.home.domain.model.ContinueLevel

fun HomeSummaryDto.toDomain(): HomeSummary = HomeSummary(
    xp = xp ?: 0,
    coins = coins ?: 0,
    streakDays = streakDays ?: 0,
    activeLanguage = activeLanguage?.toDomain(),
    exploreWorlds = exploreWorlds?.worlds?.map { it.toDomain() } ?: emptyList(),
    continueLevel = continueLevel?.toDomain()
)

private fun ContinueLevelDto.toDomain() =
    ContinueLevel(
        worldId = worldId ?: 0,
        worldName = worldName.orEmpty(),
        levelId = levelId ?: 0,
        levelOrder = levelOrder ?: 0,
        word = word.orEmpty()
    )

private fun ActiveLanguageDto.toDomain() = ActiveLanguage(
    id = id ?: 0,
    name = name.orEmpty(),
    code = code.orEmpty(),
    imageUrl = imageUrl,
    level = level ?: 1,
    levelProgressPercent = progressPercent ?: levelProgressPercent ?: 0,
    isActive = isActive ?: true
)

private fun ExploreWorldDto.toDomain() = World(
    id = id ?: 0,
    name = name.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    difficulty = difficulty?.let { diff ->
        runCatching { WorldDifficulty.valueOf(diff.uppercase()) }.getOrNull()
    } ?: WorldDifficulty.EASY,
    progressPercent = progressPercent ?: 0,
    totalLevels = totalLevels ?: 10,
    completedLevels = completedLevels ?: 0
)