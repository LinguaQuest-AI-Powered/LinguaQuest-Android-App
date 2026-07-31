package com.iti.linguaquest.features.lockscreen.data.remote

import com.iti.linguaquest.features.lockscreen.domain.model.VocabularyBatchParams

class PromptBuilder {

    fun build(params: VocabularyBatchParams): String {

        val safeNativeLanguage = params.nativeLanguage
        val safeTargetLanguage = params.targetLanguage
        val safeLevel = params.proficiencyLevel

        val exclusions =
            if (params.excludeWords.isEmpty()) {
                "None"
            } else {
                params.excludeWords.joinToString(separator = "\n")
            }

        return """
Generate exactly ${params.batchSize} unique vocabulary words.

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
The response must be a single JSON array with exactly ${params.batchSize} objects.
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
