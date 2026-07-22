package com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract
import com.iti.linguaquest.features.home.domain.model.LanguageOption

data class LanguagesState(
    val nativeLanguage: LanguageOption? = null,
    val targetLanguage: LanguageOption? = null,
    val availableLanguages: List<LanguageOption> = emptyList(),
    val isNativeDropdownExpanded: Boolean = false,
    val isTargetDropdownExpanded: Boolean = false,
    val isContinueEnabled: Boolean = false,
    val isLoading: Boolean = true,
    val errorRes: Int? = null
)