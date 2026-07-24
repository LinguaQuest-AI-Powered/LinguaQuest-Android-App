package com.iti.linguaquest.core.database.profile

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    val id: Int,
    val username: String,
    val photoUrl: String?,
    val level: Int,
    val coins: Int,
    val totalXp: Int,
    val streakDays: Int,
    val worldsCount: Int,
    val languageId: Int,
    val languageName: String,
    val languageCode: String,
    val languageLevel: Int,
    val journeyLabel: String,
    val currentXp: Int,
    val nextMilestoneXp: Int,
    val achievementsJson: String,
    val leaderboardJson: String,
    val lastUpdatedAt: Long
)
