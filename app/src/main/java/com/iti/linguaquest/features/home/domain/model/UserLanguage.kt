package com.iti.linguaquest.features.home.domain.model

data class UserLanguage(
    val id: Int,
    val name: String,
    val code: String,
    val imageUrl: String,
    val level: Int,
    val isActive: Boolean,
    val progressPercent: Int
)

data class LanguageOption(
    val id: Int,
    val name: String,
    val code: String,
    val imageUrl: String,
    val isAdded: Boolean
)
