package com.iti.linguaquest.features.auth.domain.model

data class GoogleSignInResult(
    val profileComplete: Boolean,
    val user: AuthUser
)

