package com.iti.linguaquest.features.home.presentation.languages.contract

data class MyLanguagesState(
    val isLoading: Boolean = false,
    val languages: List<MyLanguageUiModel> = emptyList(),
    val isSettingActive: Boolean = false,
    val errorMessage: String? = null
)
