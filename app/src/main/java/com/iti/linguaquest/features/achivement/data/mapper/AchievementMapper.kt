package com.iti.linguaquest.features.achivement.data.mapper

import com.iti.linguaquest.features.achivement.data.datasource.remote.dto.AchievementDto
import com.iti.linguaquest.features.achivement.data.datasource.remote.dto.AchievementsResponseDataDto
import com.iti.linguaquest.features.achivement.domain.model.AchievementDomainModel
import com.iti.linguaquest.features.achivement.domain.model.AchievementStatus
import com.iti.linguaquest.features.achivement.domain.model.AchievementsData

fun AchievementsResponseDataDto.toDomain(): AchievementsData = AchievementsData(
    earnedCount = earnedCount,
    inProgressCount = inProgressCount,
    xpEarned = xpEarned,
    achievements = achievements.map { it.toDomain() }
)

fun AchievementDto.toDomain(): AchievementDomainModel = AchievementDomainModel(
    id = id,
    name = name,
    description = description,
    iconUrl = iconUrl,
    status = runCatching { AchievementStatus.valueOf(status) }.getOrDefault(AchievementStatus.LOCKED),
    progressPercent = progressPercent,
    targetValue = targetValue,
    xpReward = xpReward,
    coinReward = coinReward,
    earnedAt = earnedAt
)
