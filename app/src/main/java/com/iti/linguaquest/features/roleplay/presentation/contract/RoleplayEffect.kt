package com.iti.linguaquest.features.roleplay.presentation.contract

sealed interface RoleplayEffect {
    data object NavigateToHome : RoleplayEffect
}
