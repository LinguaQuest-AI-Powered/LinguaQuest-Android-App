package com.iti.linguaquest.features.home.data.mapper

import com.iti.linguaquest.features.home.data.remote.dto.ActiveLanguageDto
import com.iti.linguaquest.features.home.data.remote.dto.ContinueLessonDto
import com.iti.linguaquest.features.home.data.remote.dto.ExploreWorldDto
import com.iti.linguaquest.features.home.data.remote.dto.HomeSummaryDto
import com.iti.linguaquest.features.home.domain.model.ActiveLanguage
import com.iti.linguaquest.features.home.domain.model.ContinueLesson
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty

fun HomeSummaryDto.toDomain(): HomeSummary = HomeSummary(
    xp = xp,
    coins = coins,
    streakDays = streakDays,
    activeLanguage = activeLanguage.toDomain(),
    continueLesson = continueLesson?.toDomain(),
    exploreWorlds = exploreWorlds.map { it.toDomain() }
)

private fun ActiveLanguageDto.toDomain() = ActiveLanguage(
    id = id,
    name = name,
    code = code,
    level = level,
    levelProgressPercent = levelProgressPercent
)

private fun ContinueLessonDto.toDomain() = ContinueLesson(
    worldId = worldId,
    worldName = worldName,
    levelId = levelId,
    word = word,
    translation = translation,
    imageUrl = imageUrl,
    sentence = sentence
)

private fun ExploreWorldDto.toDomain() = World(
    id = id,
    name = name,
    imageUrl = imageUrl,
    difficulty = runCatching { WorldDifficulty.valueOf(difficulty) }
        .getOrDefault(WorldDifficulty.EASY),
    progressPercent = progressPercent,
    totalLevels = totalLevels,
    completedLevels = completedLevels
)