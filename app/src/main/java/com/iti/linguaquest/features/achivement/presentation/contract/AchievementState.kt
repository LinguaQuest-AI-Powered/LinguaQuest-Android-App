package com.iti.linguaquest.features.achivement.presentation.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.achivement.domain.model.AchievementFilter
import com.iti.linguaquest.features.achivement.presentation.view.model.AchievementItem

import com.iti.linguaquest.core.sharedComponents.state.DataStatus

data class AchievementState(
    val dataStatus: DataStatus = DataStatus.Loading,
    val filter: AchievementFilter = AchievementFilter.ALL,
    val earnedCount: Int = 0,
    val inProgressCount: Int = 0,
    val xpEarned: Int = 0,
    val achievements: List<AchievementItem> = emptyList()
)
