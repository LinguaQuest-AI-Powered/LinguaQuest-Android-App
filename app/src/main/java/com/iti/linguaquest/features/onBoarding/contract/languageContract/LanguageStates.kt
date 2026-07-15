package com.iti.linguaquest.features.onBoarding.contract.languageContract
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

val defaultLanguages = listOf(
    LanguageOption("es", "Spanish", R.drawable.flag_spain),
    LanguageOption("fr", "French", R.drawable.flag_france),
    LanguageOption("ge", "German", R.drawable.flag_germany),
    LanguageOption("ja", "Japanese", R.drawable.flag_japan)
)