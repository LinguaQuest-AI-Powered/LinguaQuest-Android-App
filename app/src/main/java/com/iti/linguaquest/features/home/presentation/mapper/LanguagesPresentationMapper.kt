package com.iti.linguaquest.features.home.presentation.mapper

import com.iti.linguaquest.features.home.domain.model.UserLanguage
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import com.iti.linguaquest.features.home.presentation.languages.contract.MyLanguageUiModel
import com.iti.linguaquest.features.home.presentation.languages.contract.LanguageUiItem

fun UserLanguage.toUiModel(): MyLanguageUiModel = MyLanguageUiModel(
    id = id,
    name = name,
    level = level,
    isCurrent = isActive,
    flagEmoji = code.toFlagEmoji()
)

fun LanguageOption.toUiItem(): LanguageUiItem = LanguageUiItem(
    id = id,
    name = name,
    flagEmoji = code.toFlagEmoji()
)

private fun String.toFlagEmoji(): String {
    val countryCode = when (this.lowercase()) {
        "en" -> "GB"
        "es" -> "ES"
        "fr" -> "FR"
        "ja" -> "JP"
        "de" -> "DE"
        "it" -> "IT"
        "ko" -> "KR"
        "pt" -> "PT"
        "ar" -> "SA"
        "zh" -> "CN"
        "ru" -> "RU"
        else -> this.uppercase()
    }
    if (countryCode.length != 2) return "🏳️"
    val firstChar = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6
    val secondChar = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}
