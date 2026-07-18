package com.iti.linguaquest.features.achivement.model

data class AchievementItem(
    val id: Int,
    val title: String,
    val dateEarned: String?,
    val iconRes: Int,
    val isEarned: Boolean
)
