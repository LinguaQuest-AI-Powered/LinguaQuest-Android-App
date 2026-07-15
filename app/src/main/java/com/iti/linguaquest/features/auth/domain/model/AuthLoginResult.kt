package com.iti.linguaquest.features.auth.domain.model

data class AuthLoginResult(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val user: AuthUser,
)
