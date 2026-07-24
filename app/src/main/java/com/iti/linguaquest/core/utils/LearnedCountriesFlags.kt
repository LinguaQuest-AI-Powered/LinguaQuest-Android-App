package com.iti.linguaquest.core.utils

fun String.toFlagEmoji(): String {
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