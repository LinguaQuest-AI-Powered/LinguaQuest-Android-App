package com.iti.linguaquest.core.utils

import java.lang.Character.UnicodeBlock

object TranscriptSanitizer {

    private val devanagariWordMap = mapOf(
        "हेलो" to "Hello",
        "हेल्لو" to "Hello",
        "हाय" to "Hi",
        "नमस्ते" to "Hello",
        "ओके" to "Okay",
        "ओक" to "Okay",
        "बाय" to "Bye",
        "थैंक यू" to "Thank you",
        "शुक्रिया" to "Thank you",
        "प्लीज" to "Please",
        "यस" to "Yes",
        "नो" to "No"
    )

    private val arabicWordMap = mapOf(
        "نو" to "No",
        "يس" to "Yes",
        "أوكيه" to "Okay",
        "اوكيه" to "Okay",
        "أوكي" to "Okay",
        "اوكي" to "Okay",
        "اوك" to "Okay",
        "هيلو" to "Hello",
        "هالو" to "Hello",
        "هاي" to "Hi",
        "ثانك يو" to "Thank you",
        "ثانكس" to "Thanks",
        "بليز" to "Please",
        "جود" to "Good",
        "مورنينج" to "Morning",
        "باي" to "Bye"
    )

    private val accidentalAsrKeywords = listOf(
        "hindi",
        "devanagari",
        "الهندية",
        "هندي",
        "ديفاناغاري"
    )

    fun isArabicLanguage(language: String): Boolean {
        val normalized = language.trim().lowercase()
        return normalized == "arabic" || normalized == "ar" || normalized == "العربية"
    }

    fun sanitize(text: String, targetLanguage: String = "English"): String {
        if (text.isEmpty()) return text
        var result = text

        if (isArabicLanguage(targetLanguage)) {
            result = result.replace(Regex("[\\u0900-\\u097F]+"), "")
            return result
        }

        for ((devWord, engReplacement) in devanagariWordMap) {
            result = result.replace(devWord, engReplacement)
        }

        for ((arWord, engReplacement) in arabicWordMap) {
            val pattern = Regex("(^|\\s)${Regex.escape(arWord)}(\\s|$|\\.|,|!)")
            result = result.replace(pattern) { matchResult ->
                val prefix = matchResult.groupValues[1]
                val suffix = matchResult.groupValues[2]
                "$prefix$engReplacement$suffix"
            }
        }

        result = result.replace(Regex("[\\u0900-\\u097F]+"), "")

        return result
    }

    fun calculateTargetLanguagePercentage(userUtterances: List<String>, targetLanguage: String = "English"): Int {
        if (userUtterances.isEmpty()) return 100

        var targetChars = 0
        var nonTargetChars = 0
        val isArabicTarget = isArabicLanguage(targetLanguage)

        for (utterance in userUtterances) {
            val sanitized = sanitize(utterance, targetLanguage)
            for (char in sanitized) {
                val block = UnicodeBlock.of(char)
                if (isArabicTarget) {
                    when {
                        block == UnicodeBlock.ARABIC || block == UnicodeBlock.ARABIC_SUPPLEMENT || block == UnicodeBlock.ARABIC_EXTENDED_A -> targetChars++
                        char in 'a'..'z' || char in 'A'..'Z' -> nonTargetChars++
                    }
                } else {
                    when {
                        char in 'a'..'z' || char in 'A'..'Z' || block == UnicodeBlock.LATIN_1_SUPPLEMENT || block == UnicodeBlock.LATIN_EXTENDED_A -> targetChars++
                        block == UnicodeBlock.ARABIC || block == UnicodeBlock.ARABIC_SUPPLEMENT || block == UnicodeBlock.ARABIC_EXTENDED_A -> nonTargetChars++
                    }
                }
            }
        }

        val total = targetChars + nonTargetChars
        if (total == 0) return 100

        return ((targetChars.toDouble() / total) * 100).toInt().coerceIn(0, 100)
    }

    fun filterImprovements(improvements: List<String>): List<String> {
        return improvements.filterNot { item ->
            val lower = item.lowercase()
            accidentalAsrKeywords.any { keyword ->
                lower.contains(keyword)
            }
        }
    }
}
