package com.iti.linguaquest.features.profile.presentation.contract

sealed interface ProfileIntent {
    data object LoadProfile : ProfileIntent
    data object Retry : ProfileIntent
    data object SettingsClicked : ProfileIntent
    data object EditAvatarClicked : ProfileIntent
    data object ChangeLanguageClicked : ProfileIntent
    data object ViewAllAchievementsClicked : ProfileIntent
    data object ViewAllLeaderboardClicked : ProfileIntent
}