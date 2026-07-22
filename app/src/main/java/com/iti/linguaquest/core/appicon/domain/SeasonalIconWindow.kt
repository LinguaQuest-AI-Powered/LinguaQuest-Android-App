package com.iti.linguaquest.core.appicon.domain

data class SeasonalIconWindow(
    val iconType: AppIconType,
    val startMonth: Int,
    val startDay: Int,
    val endMonth: Int,
    val endDay: Int
)