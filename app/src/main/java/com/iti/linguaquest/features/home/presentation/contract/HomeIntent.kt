package com.iti.linguaquest.features.home.presentation.contract

import com.iti.linguaquest.features.home.presentation.view.components.WorldItem

sealed interface HomeIntent {
    data object LoadHome : HomeIntent
    data object Retry : HomeIntent
    data class WorldClicked(val world: WorldItem) : HomeIntent
    data object StartVoicePractiseClicked : HomeIntent
    data object SeeMoreWorldsClicked : HomeIntent
    data object FabClicked : HomeIntent
    data object DismissLanguageBottomSheet : HomeIntent
    data object AddNewLanguageClicked : HomeIntent
    data object DailyRewardBannerClicked : HomeIntent
    data object DismissDailyRewardBanner : HomeIntent
    data object DismissDailyRewardDialog : HomeIntent
    data object ClaimDailyRewardClicked : HomeIntent
    data object RoleplayCardClicked : HomeIntent
}