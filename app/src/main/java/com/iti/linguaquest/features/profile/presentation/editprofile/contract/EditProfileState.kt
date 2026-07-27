package com.iti.linguaquest.features.profile.presentation.editprofile.contract

import com.iti.linguaquest.features.profile.presentation.editprofile.utils.FieldError


data class EditProfileState(
    val isLoading: Boolean = false,
    val avatarModel: Any? = null,
    val displayName: String = "",
    val displayNameError: FieldError = FieldError(),
    val isSavingName: Boolean = false,
    val isNameUpdateSuccess: Boolean = false,
    val oldPassword: String = "",
    val newPassword: String = "",
    val oldPasswordError: FieldError = FieldError(),
    val newPasswordError: FieldError = FieldError(),
    val isSavingPassword: Boolean = false,
    val isPasswordUpdateSuccess: Boolean = false
)