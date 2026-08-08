package com.iti.linguaquest.features.home.presentation.contract

import com.iti.linguaquest.features.home.presentation.view.components.WorldItem
import androidx.compose.ui.geometry.Rect

sealed interface HomeIntent {
    data object LoadHome : HomeIntent
    data object Refresh : HomeIntent
    data object Retry : HomeIntent
    data class WorldClicked(val world: WorldItem) : HomeIntent
    data object SeeMoreWorldsClicked : HomeIntent
    data object FabClicked : HomeIntent
    data object DismissLanguageBottomSheet : HomeIntent
    data object AddNewLanguageClicked : HomeIntent
    data object DailyRewardBannerClicked : HomeIntent
    data object DismissDailyRewardBanner : HomeIntent
    data object DismissDailyRewardDialog : HomeIntent
    data object ClaimDailyRewardClicked : HomeIntent
    data class ContinueLevelClicked(val continueLevel: ContinueLevelUi, val rect: Rect) : HomeIntent
    data object TriggerDailyMission : HomeIntent
    data object DismissDailyMissionDialog : HomeIntent
    data class StartDailyMissionCamera(val word: String) : HomeIntent
}