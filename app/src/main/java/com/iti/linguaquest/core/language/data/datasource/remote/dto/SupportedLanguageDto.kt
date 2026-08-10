package com.iti.linguaquest.core.language.data.datasource.remote.dto

data class SupportedLanguageOptionDto(
    val id: Int,
    val name: String,
    val code: String,
    val imageUrl: String,
    val isAdded: Boolean
)

data class SupportedLanguagesResponseDataDto(
    val languages: List<SupportedLanguageOptionDto>
)
