package com.iti.linguaquest.features.auth.domain.model

import com.iti.linguaquest.features.home.domain.model.LanguageOption
data class AuthUser(
    val id: Int,
    val username: String?,
    val photo: String?,
    val nativeLanguage: LanguageOption?,
    val isVerified: Boolean,
    val targetLanguages: List<String>
)

