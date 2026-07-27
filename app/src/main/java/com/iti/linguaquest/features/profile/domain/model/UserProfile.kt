package com.iti.linguaquest.features.profile.domain.model

 data class UserProfile(
    val id: Int,
    val username: String
)

 data class ProfilePhoto(
    val photoUrl: String
)

 data class PasswordUpdateStatus(
    val status: String
)