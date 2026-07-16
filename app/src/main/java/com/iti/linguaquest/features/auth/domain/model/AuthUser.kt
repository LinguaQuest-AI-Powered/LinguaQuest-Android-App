package com.iti.linguaquest.features.auth.domain.model

data class AuthUser(
    val id: Int,
    val username: String?,
    val name: String,
    val photo: String?,
    val nativeLanguage: String?,
    val isVerified: Boolean,
    val targetLanguages: List<String>
)

 data class RegisteredAccount(
    val id: Int,
    val email: String,
    val username: String,
    val nativeLanguage: String,
    val targetLanguage: String,
    val isVerified: Boolean
)

 data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val user: AuthUser
)