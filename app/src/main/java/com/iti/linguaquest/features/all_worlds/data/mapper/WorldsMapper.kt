package com.iti.linguaquest.features.all_worlds.data.mapper

import com.iti.linguaquest.features.all_worlds.data.remote.dto.WorldDto
import com.iti.linguaquest.features.all_worlds.data.remote.dto.WorldsDataDto
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty
import com.iti.linguaquest.features.all_worlds.domain.model.WorldStatus
import com.iti.linguaquest.features.all_worlds.domain.model.WorldsData

fun WorldsDataDto.toDomain(): WorldsData = WorldsData(
    totalCount = totalCount,
    worlds = worlds.map { it.toDomain() }
)

fun WorldDto.toDomain(): World = World(
    id = id,
    name = name,
    imageUrl = imageUrl,
    difficulty = WorldDifficulty.valueOf(difficulty),
    status = WorldStatus.valueOf(status),
    progressPercent = progressPercent,
    totalLevels = totalLevels,
    completedLevels = completedLevels
)
