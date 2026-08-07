package com.iti.linguaquest.features.profile.presentation.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.profile.presentation.model.ProfileState

data class ProfileUiState(

    val isLoading: Boolean = false,
    val profile: ProfileState = ProfileState(),
    val hasCachedData: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: UiText? = null,
    val isAvatarUploading: Boolean = false,
    val isOffline: Boolean = false,
    val isRefreshing: Boolean = false
)