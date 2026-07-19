package com.iti.linguaquest.features.profile.presentation.contract


sealed interface ProfileEffect {
    data object NavigateToSettings : ProfileEffect
    data object NavigateToChangeLanguage : ProfileEffect
    data object NavigateToAllAchievements : ProfileEffect
    data object NavigateToAllLeaderboard : ProfileEffect
}