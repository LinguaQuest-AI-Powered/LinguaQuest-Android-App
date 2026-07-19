package com.iti.linguaquest.features.map.data.mapper

import com.iti.linguaquest.features.map.data.remote.dto.MapLevelDto
import com.iti.linguaquest.features.map.data.remote.dto.WorldMapDetailDto
import com.iti.linguaquest.features.map.domain.model.MapLevel
import com.iti.linguaquest.features.map.domain.model.WorldMapDetail

fun MapLevelDto.toDomain(): MapLevel {
    return MapLevel(
        id = id,
        order = order,
        status = status,
        word = word
    )
}

fun WorldMapDetailDto.toDomain(): WorldMapDetail {
    return WorldMapDetail(
        id = id,
        name = name,
        difficulty = difficulty,
        levels = levels.map { it.toDomain() }
    )
}
