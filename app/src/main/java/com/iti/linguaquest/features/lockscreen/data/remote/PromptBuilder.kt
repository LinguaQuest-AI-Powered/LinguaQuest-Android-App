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
You are a helpful language teacher. You strictly output valid JSON.

Generate exactly ${params.batchSize} unique vocabulary words in $safeTargetLanguage for a $safeLevel learner.

Native language:
$safeNativeLanguage

Avoid repeating any word from the following list:
$exclusions

Return the result as a JSON object with a single key "words" containing an array of exactly ${params.batchSize} objects.
Each object must have these string properties:
- "word": The word in $safeTargetLanguage.
- "meaning": A short definition of the word in $safeTargetLanguage.
- "translation": The translation in $safeNativeLanguage.
- "exampleSentence": An example sentence using the word in $safeTargetLanguage.
- "difficulty": Either "Easy", "Medium", or "Hard".

Do not include any other text, markdown formatting, or markdown code blocks. Just return the raw JSON string.
""".trimIndent()
    }
}
