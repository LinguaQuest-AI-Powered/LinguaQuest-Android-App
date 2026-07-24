package com.iti.linguaquest.features.lockscreen.data.remote

 class PromptBuilder {

    fun build(
        nativeLanguage: String?,
        targetLanguage: String?,
        proficiencyLevel: String?,
        batchSize: Int,
        excludeWords: List<String>
    ): String {

        val safeNativeLanguage = nativeLanguage ?: "Arabic"
        val safeTargetLanguage = targetLanguage ?: "English"
        val safeLevel = proficiencyLevel ?: "Beginner"

        val exclusions =
            if (excludeWords.isEmpty()) {
                "None"
            } else {
                excludeWords.joinToString(separator = "\n")
            }

        return """
Generate exactly $batchSize unique vocabulary words.

Target language:
$safeTargetLanguage

Native language:
$safeNativeLanguage

Current proficiency:
$safeLevel

Requirements:

The vocabulary word must be written in the target language.
The translation must be written in the native language.
The example sentence must be written in the target language.
The response must be a single JSON array with exactly $batchSize objects.
Do not wrap the array in any other object.

Avoid repeating any word from the following list:

$exclusions

Return JSON only.
Do not return markdown.
Do not return explanations.

Each object must contain:
word
translation
example_sentence

Example response shape:
[
  {
    "word": "...",
    "translation": "...",
    "example_sentence": "..."
  }
]
""".trimIndent()
    }
}
