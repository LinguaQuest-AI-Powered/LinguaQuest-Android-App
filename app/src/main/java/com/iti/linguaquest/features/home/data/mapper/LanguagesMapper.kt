package com.iti.linguaquest.features.home.data.mapper

import com.iti.linguaquest.features.home.data.remote.dto.UserLanguageDto
import com.iti.linguaquest.features.home.data.remote.dto.LanguageOptionDto
import com.iti.linguaquest.features.home.domain.model.UserLanguage
import com.iti.linguaquest.features.home.domain.model.LanguageOption

fun UserLanguageDto.toDomain() = UserLanguage(
    id = id,
    name = name,
    code = code,
    imageUrl = imageUrl,
    level = level,
    isActive = isActive,
    progressPercent = progressPercent
)

fun LanguageOptionDto.toDomain() = LanguageOption(
    id = id,
    name = name,
    code = code,
    imageUrl = imageUrl,
    isAdded = isAdded
)
