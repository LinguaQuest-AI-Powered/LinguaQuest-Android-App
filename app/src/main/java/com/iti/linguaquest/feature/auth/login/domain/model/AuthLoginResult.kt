package com.iti.linguaquest.features.auth.login.domain.model

data class AuthLoginResult(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val user: AuthUser,
)
