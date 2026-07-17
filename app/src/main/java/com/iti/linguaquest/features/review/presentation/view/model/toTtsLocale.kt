package com.iti.linguaquest.features.review.presentation.view.model
import java.util.Locale

fun String.toTtsLocale(): Locale = when (this.trim().lowercase()) {
    "arabic", "ar" -> Locale.forLanguageTag("ar")
    "spanish", "español", "es" -> Locale.forLanguageTag("es")
    "japanese", "日本語", "ja" -> Locale.JAPANESE
    "german", "deutsch", "de" -> Locale.GERMAN
    "french", "français", "fr" -> Locale.FRENCH
    "chinese", "中文", "zh" -> Locale.CHINESE
    "italian", "italiano", "it" -> Locale.ITALIAN
    "portuguese", "português", "pt" -> Locale.forLanguageTag("pt")
    "korean", "한국어", "ko" -> Locale.KOREAN
    else -> Locale.ENGLISH
}
