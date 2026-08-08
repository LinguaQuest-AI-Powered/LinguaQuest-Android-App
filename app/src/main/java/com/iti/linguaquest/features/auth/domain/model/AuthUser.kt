package com.iti.linguaquest.features.auth.domain.model

data class AuthUser(
    val id: Int,
    val username: String?,
    val photo: String?,
    val nativeLanguage: String?,
    val isVerified: Boolean,
    val targetLanguages: List<String>
)

