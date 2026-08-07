package com.iti.linguaquest.features.home.presentation.contract

import com.iti.linguaquest.features.home.presentation.mapper.DailyRewardUi
import com.iti.linguaquest.features.home.presentation.mapper.LanguageProgressUi
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem
import com.iti.linguaquest.core.sharedComponents.text.UiText

data class ContinueLevelUi(
    val worldId: Int,
    val levelId: Int,
    val worldName: UiText,
    val targetWord: UiText,
    val levelOrder: Int,
    val totalLevels: Int
)

data class HomeState(
    val isLoading: Boolean = false,
    val xp: Int = 0,
    val coins: Int = 0,
    val languageProgress: LanguageProgressUi? = null,
    val worlds: List<WorldItem> = emptyList(),
    val startVoicePractise: Boolean? = false,
    val hasError: Boolean = false,
    val errorMessage: UiText? = null,
    val isLanguageBottomSheetVisible: Boolean = false,
    val dailyReward: DailyRewardUi? = null,
    val isDailyRewardDialogVisible: Boolean = false,
    val isDailyRewardBannerVisible: Boolean = false,
    val isRefreshing: Boolean = false,
    val continueLevel: ContinueLevelUi? = null
)
