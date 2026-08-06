package com.iti.linguaquest.features.roleplay.presentation.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface RoleplayEffect {
    data object NavigateToHome : RoleplayEffect
    data class ShowSnackbarAndNavigateBack(val message: UiText) : RoleplayEffect
}
