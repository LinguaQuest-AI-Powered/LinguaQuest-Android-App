package com.iti.linguaquest.features.home.presentation.contract

import com.iti.linguaquest.features.home.presentation.mapper.DailyRewardUi
import com.iti.linguaquest.features.home.presentation.mapper.LanguageProgressUi
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem

data class HomeState(
    val isLoading: Boolean = true,
    val xp: Int = 0,
    val coins: Int = 0,
    val languageProgress: LanguageProgressUi? = null,
    val worlds: List<WorldItem> = emptyList(),
    val startVoicePractise: Boolean? = false,
    val hasError: Boolean = false,
    val isLanguageBottomSheetVisible: Boolean = false,
    val dailyReward: DailyRewardUi? = null,
    val isDailyRewardDialogVisible: Boolean = false,
    val isDailyRewardBannerVisible: Boolean = false
)
