package com.iti.linguaquest.features.achivement.presentation.mapper

import com.iti.linguaquest.R
import com.iti.linguaquest.features.achivement.domain.model.AchievementDomainModel
import com.iti.linguaquest.features.achivement.domain.model.AchievementStatus
import com.iti.linguaquest.features.achivement.presentation.view.model.AchievementItem
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun AchievementDomainModel.toUiModel(): AchievementItem {
    return AchievementItem(
        id = id,
        title = name,
        description = description,
        status = status,
        progressPercent = progressPercent,
        targetValue = targetValue,
        xpReward = xpReward,
        coinReward = coinReward,
        dateEarned = formatEarnedDate(earnedAt),
        icon = iconUrl.ifBlank { R.drawable.achievement_cup },
        isEarned = status == AchievementStatus.EARNED
    )
}

private fun formatEarnedDate(dateString: String?): String? {
    if (dateString.isNullOrBlank()) return null
    return runCatching {
        val parsed = ZonedDateTime.parse(dateString)
        parsed.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
    }.getOrElse {
        dateString.take(10)
    }
}
