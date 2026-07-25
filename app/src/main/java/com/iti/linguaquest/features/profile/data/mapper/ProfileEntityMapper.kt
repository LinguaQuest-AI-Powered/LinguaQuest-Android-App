package com.iti.linguaquest.features.profile.data.mapper

import com.google.gson.Gson
import com.iti.linguaquest.core.database.profile.ProfileEntity
import com.iti.linguaquest.features.profile.domain.model.AchievementsSummary
import com.iti.linguaquest.features.profile.domain.model.LanguageJourney
import com.iti.linguaquest.features.profile.domain.model.LeaderboardSummary
import com.iti.linguaquest.features.profile.domain.model.ProfileStats
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary

private val entityGson = Gson()

fun ProfileSummary.toEntity(): ProfileEntity = ProfileEntity(
    id = id,
    username = username,
    photoUrl = photoUrl,
    level = level,
    coins = stats.coins,
    totalXp = stats.totalXp,
    streakDays = stats.streakDays,
    worldsCount = stats.worldsCount,
    languageId = languageJourney.languageId,
    languageName = languageJourney.name,
    languageCode = languageJourney.code,
    languageLevel = languageJourney.level,
    journeyLabel = languageJourney.journeyLabel,
    currentXp = languageJourney.currentXp,
    nextMilestoneXp = languageJourney.nextMilestoneXp,
    achievementsJson = entityGson.toJson(achievementsSummary),
    leaderboardJson = entityGson.toJson(leaderboardSummary),
    lastUpdatedAt = System.currentTimeMillis()
)

fun ProfileEntity.toDomain(): ProfileSummary = ProfileSummary(
    id = id,
    username = username,
    photoUrl = photoUrl,
    level = level,
    stats = ProfileStats(
        coins = coins,
        totalXp = totalXp,
        streakDays = streakDays,
        worldsCount = worldsCount
    ),
    languageJourney = LanguageJourney(
        languageId = languageId,
        name = languageName,
        code = languageCode,
        level = languageLevel,
        journeyLabel = journeyLabel,
        currentXp = currentXp,
        nextMilestoneXp = nextMilestoneXp
    ),
    achievementsSummary = runCatching {
        entityGson.fromJson(achievementsJson, AchievementsSummary::class.java)
    }.getOrNull() ?: AchievementsSummary(earnedCount = 0, totalCount = 0, preview = emptyList()),
    leaderboardSummary = runCatching {
        entityGson.fromJson(leaderboardJson, LeaderboardSummary::class.java)
    }.getOrNull() ?: LeaderboardSummary(myRank = 0, preview = emptyList())
)
