package com.iti.linguaquest.features.setting.presentation

import com.iti.linguaquest.features.home.domain.model.LanguageOption

data class  LanguagesUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val languages: List<LanguageOption> = emptyList()
)
