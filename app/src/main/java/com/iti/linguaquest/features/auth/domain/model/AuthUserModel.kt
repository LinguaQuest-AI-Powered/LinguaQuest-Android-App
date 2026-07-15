package com.iti.linguaquest.features.auth.domain.model

data class AuthUserModel(
    val id: Int,
    val username: String,
    val email: String,
    val image: String,
    val nativeLanguage: String,
    val targetLanguages: List<String>,
)