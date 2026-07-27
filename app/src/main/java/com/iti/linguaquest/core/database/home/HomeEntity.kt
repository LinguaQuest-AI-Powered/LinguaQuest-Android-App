package com.iti.linguaquest.core.database.home

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "home_summary")
data class HomeEntity(
    @PrimaryKey
    val id: Int = 1,
    val xp: Int,
    val coins: Int,
    val streakDays: Int,
    val activeLanguageId: Int?,
    val activeLanguageName: String?,
    val activeLanguageCode: String?,
    val activeLanguageImageUrl: String?,
    val activeLanguageLevel: Int?,
    val activeLanguageProgressPercent: Int?,
    val activeLanguageIsActive: Boolean?,
    val exploreWorldsJson: String
)
