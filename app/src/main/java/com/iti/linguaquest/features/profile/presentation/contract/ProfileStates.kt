package com.iti.linguaquest.features.profile.presentation.contract

import com.iti.linguaquest.features.profile.presentation.model.ProfileState

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: ProfileState = ProfileState(),
    val hasError: Boolean = false,
    val isAvatarUploading: Boolean = false
)