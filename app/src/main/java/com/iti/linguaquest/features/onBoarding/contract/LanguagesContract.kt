package com.iti.linguaquest.features.onBoarding.contract


import com.iti.linguaquest.R

data class LanguagesState(
    val nativeLanguage: String = "English",
    val targetLanguage: String? = null,
    val availableLanguages: List<LanguageOption> = defaultLanguages,
    val isNativeDropdownExpanded: Boolean = false,
    val isTargetDropdownExpanded: Boolean = false,
    val isContinueEnabled: Boolean = false
)

data class LanguageOption(
    val code: String,
    val displayName: String,
    val flagRes: Int
)

sealed interface LanguagesIntent {
    data class SelectNativeLanguage(val language: LanguageOption) : LanguagesIntent
    data class SelectTargetLanguage(val language: LanguageOption) : LanguagesIntent
    data object ToggleNativeDropdown : LanguagesIntent
    data object ToggleTargetDropdown : LanguagesIntent
    data object ContinueClicked : LanguagesIntent
}

sealed interface LanguagesEffect {
    data object NavigateToLevelScreen : LanguagesEffect
}

val defaultLanguages = listOf(
    LanguageOption("es", "Spanish", R.drawable.flag_spain),
    LanguageOption("fr", "French", R.drawable.flag_france),
    LanguageOption("ge", "German", R.drawable.flag_germany),
    LanguageOption("ja", "Japanese", R.drawable.flag_japan)
)