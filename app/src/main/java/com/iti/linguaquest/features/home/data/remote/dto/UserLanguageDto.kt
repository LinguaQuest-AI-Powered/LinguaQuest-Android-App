package com.iti.linguaquest.features.home.data.remote.dto

data class UserLanguageDto(
    val id: Int,
    val name: String,
    val code: String,
    val imageUrl: String,
    val level: Int,
    val isActive: Boolean,
    val progressPercent: Int
)

data class LanguageOptionDto(
    val id: Int,
    val name: String,
    val code: String,
    val imageUrl: String,
    val isAdded: Boolean
)

data class MyLanguagesResponseDto(
    val languages: List<UserLanguageDto>
)

data class AvailableLanguagesResponseDto(
    val languages: List<LanguageOptionDto>
)

data class AddLanguagesRequestDto(
    val languageIds: List<Int>
)

data class SetActiveLanguageRequestDto(
    val languageId: Int
)

data class SetActiveLanguageResponseDto(
    val activeLanguage: UserLanguageDto
)
