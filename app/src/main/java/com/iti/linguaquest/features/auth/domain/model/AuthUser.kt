package com.iti.linguaquest.features.auth.domain.model

data class AuthUser(
    val id: Long,
    val username: String?,
    val name: String,
    val photo: String?,
    val nativeLanguage: String?,
    val isVerified: Boolean,
    val targetLanguages: List<String> = emptyList(),
)
