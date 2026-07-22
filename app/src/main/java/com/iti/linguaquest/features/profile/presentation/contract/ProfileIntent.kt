package com.iti.linguaquest.features.profile.presentation.contract

import android.net.Uri

sealed interface ProfileIntent {
    data object LoadProfile : ProfileIntent
    data object Retry : ProfileIntent
    data object SettingsClicked : ProfileIntent
    data class AvatarPicked(val uri: Uri) : ProfileIntent
    data object ChangeLanguageClicked : ProfileIntent
    data object ViewAllAchievementsClicked : ProfileIntent
    data object ViewAllLeaderboardClicked : ProfileIntent

}