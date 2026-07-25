package com.iti.linguaquest.features.profile.presentation.editprofile.contract

import android.net.Uri




sealed interface EditProfileIntent {
    data class OnDisplayNameChanged(val name: String) : EditProfileIntent
    data class OnOldPasswordChanged(val password: String) : EditProfileIntent
    data class OnNewPasswordChanged(val password: String) : EditProfileIntent
    data class UploadPhoto(val uri: Uri) : EditProfileIntent

     data object SaveNameChanges : EditProfileIntent
    data object SavePasswordChanges : EditProfileIntent
}