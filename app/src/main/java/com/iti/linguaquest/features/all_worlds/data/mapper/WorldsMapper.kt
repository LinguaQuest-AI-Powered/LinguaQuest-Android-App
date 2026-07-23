package com.iti.linguaquest.features.all_worlds.data.mapper

import com.iti.linguaquest.features.all_worlds.data.remote.dto.WorldDto
import com.iti.linguaquest.features.all_worlds.data.remote.dto.WorldsDataDto
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty
import com.iti.linguaquest.features.all_worlds.domain.model.WorldStatus
import com.iti.linguaquest.features.all_worlds.domain.model.WorldsData

fun WorldsDataDto.toDomain(): WorldsData = WorldsData(
    totalCount = totalCount ?: 0,
    worlds = worlds?.map { it.toDomain() }.orEmpty()
)

fun WorldDto.toDomain(): World = World(
    id = id ?: 0,
    name = name.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    difficulty = difficulty?.uppercase()?.let { diff ->
        runCatching { WorldDifficulty.valueOf(diff) }.getOrNull()
    } ?: WorldDifficulty.EASY,
    status = status?.uppercase()?.let { st ->
        runCatching { WorldStatus.valueOf(st) }.getOrNull()
    } ?: WorldStatus.IN_PROGRESS,
    progressPercent = progressPercent ?: 0,
    totalLevels = totalLevels ?: 0,
    completedLevels = completedLevels ?: 0
)
