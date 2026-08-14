package com.iti.linguaquest.features.mindreader.domain.model

import java.util.Locale

data class MindReaderCategory(
    val id: String,
    val displayName: String,
    val displayNames: Map<String, String> = emptyMap(),
    val emoji: String = "",
    val seedQuestions: List<Map<String, String>> = emptyList()
) {
    fun resolveDisplayName(languageCode: String? = null): String {
        val code = languageCode?.lowercase(Locale.ROOT)?.take(2)
            ?: Locale.getDefault().language.lowercase(Locale.ROOT).take(2)
        return displayNames[code] ?: displayNames["en"] ?: displayName
    }
}
