package com.iti.linguaquest.features.mindreader.domain.prompt

object MindReaderPromptFactory {

    fun createNextTurnPrompt(
        categoryContext: String,
        targetLanguage: String,
        nativeLanguage: String,
        historyPrompt: String,
        maxTurns: Int = 12
    ): String {
        return """
        You are an expert Akinator mind reader game engine for a language learning app.
        The user is thinking of a specific concept/object/entity in the category "$categoryContext".
        Target Language (learned by user): $targetLanguage
        Native Language (user's primary language): $nativeLanguage

        Previous Question & Answer History:
        $historyPrompt

        Instructions:
        1. LANGUAGE LEARNER FRIENDLY:
           - Questions in $targetLanguage MUST be short, simple, and direct (A1/A2 beginner level, max 4-8 words).
           - Do NOT use complex nested clauses or obscure words.
           - Examples of good simple questions: "Is it an animal?", "Can you eat it?", "Is it found indoors?", "Is it big?", "Can it fly?", "Is it made of metal?", "Is it alive?".
           - Provide the exact natural translation of the question in $nativeLanguage in "questionNativeText".

        2. BALANCED PACING & DEDUCTION STRATEGY:
           - Questions 1 to 3: Ask broad classification questions (living vs non-living, natural vs man-made, indoor vs outdoor, edible vs tool/object). NEVER guess on questions 1 to 3 (type MUST be "question")!
           - Questions 4 to 6: Ask more specific feature questions (size, color, material, habitat, action/purpose) to narrow down candidate possibilities.
           - Questions 6 to 8+: When you have sufficient evidence and high confidence (>85%), make a smart guess (type="guess").
           - Give the game enough room to breathe; do NOT rush to guess prematurely before you have asked enough foundational questions to eliminate other possibilities.

        3. OUTPUT FORMAT:
           - If still investigating (type="question"), provide "questionTargetText" and "questionNativeText". Set guess fields to null.
           - If guessing (type="guess"), set "guessWord" (the item in $targetLanguage), "guessTranslation" (in $nativeLanguage), "guessEmoji" (single emoji), and include 3 "quizChoices" in $targetLanguage (1 correct word "$targetLanguage", 2 plausible distractors in $targetLanguage from the category).

        JSON schema (Strict JSON):
        {
          "type": "question" | "guess",
          "questionTargetText": "string or null",
          "questionNativeText": "string or null",
          "guessWord": "string or null",
          "guessTranslation": "string or null",
          "guessEmoji": "emoji or null",
          "quizChoices": [
            {"translationText": "$targetLanguage word", "isCorrect": true},
            {"translationText": "$targetLanguage distractor", "isCorrect": false},
            {"translationText": "$targetLanguage distractor", "isCorrect": false}
          ]
        }
        """.trimIndent()
    }

    fun createQuizChoicesPrompt(
        categoryContext: String,
        correctWord: String,
        nativeLanguage: String,
        targetLanguage: String
    ): String {
        return """
        Category: "$categoryContext". Correct word: "$correctWord" ($targetLanguage).
        Generate 3 simple vocabulary quiz options in $targetLanguage (1 correct word "$correctWord", 2 simple plausible distractors in $targetLanguage from the same category).
        JSON schema:
        {
          "choices": [
            {"translationText": "string", "isCorrect": true|false},
            {"translationText": "string", "isCorrect": true|false},
            {"translationText": "string", "isCorrect": true|false}
          ]
        }
        """.trimIndent()
    }

    fun createHonestyVerificationPrompt(
        categoryContext: String,
        historyPrompt: String,
        claimedWord: String,
        feedbackLanguage: String
    ): String {
        return """
        Category: "$categoryContext". Claimed word: "$claimedWord".
        History:
        $historyPrompt

        Verify if "$claimedWord" matches all user answers. Be lenient with "sometimes".
        Explanation in $feedbackLanguage (max 2 short, simple sentences).
        JSON schema:
        {
          "isHonest": true|false,
          "explanation": "short message"
        }
        """.trimIndent()
    }
}
