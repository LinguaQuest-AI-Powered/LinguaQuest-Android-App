package com.iti.linguaquest.features.all_worlds.data.mapper

import com.iti.linguaquest.features.all_worlds.data.remote.dto.WorldDto
import com.iti.linguaquest.features.all_worlds.data.remote.dto.WorldsDataDto
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty
import com.iti.linguaquest.features.all_worlds.domain.model.WorldStatus
import com.iti.linguaquest.features.all_worlds.domain.model.WorldsData

fun WorldsDataDto.toDomain(): WorldsData = WorldsData(
    totalCount = totalCount ?: worlds?.size ?: 0,
    worlds = worlds?.map { it.toDomain() }.orEmpty()
)

fun WorldDto.toDomain(): World = World(
    id = id ?: 0,
    name = name.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    difficulty = safeWorldDifficulty(difficulty),
    status = safeWorldStatus(status),
    progressPercent = progressPercent ?: 0,
    totalLevels = totalLevels ?: 0,
    completedLevels = completedLevels ?: 0
)

private fun safeWorldDifficulty(value: String?): WorldDifficulty {
    if (value == null) return WorldDifficulty.EASY
    return try {
        WorldDifficulty.valueOf(value.trim().uppercase().replace("-", "_"))
    } catch (e: Exception) {
        WorldDifficulty.EASY
    }
}

private fun safeWorldStatus(value: String?): WorldStatus {
    if (value == null) return WorldStatus.LOCKED
    return try {
        val sanitized = value.trim().uppercase().replace("-", "_").replace(" ", "_")
        WorldStatus.valueOf(sanitized)
    } catch (e: Exception) {
        WorldStatus.LOCKED
    }
}
