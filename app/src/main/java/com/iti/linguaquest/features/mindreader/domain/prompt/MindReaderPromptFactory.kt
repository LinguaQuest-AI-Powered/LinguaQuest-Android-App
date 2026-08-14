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
        Akinator engine for "$categoryContext". Target lang: $targetLanguage, Native lang: $nativeLanguage.
        History:
        $historyPrompt

        Task:
        1. Ask high-entropy yes/no questions splitting remaining possibilities in half (~50%).
        2. Converge quickly. Aim to guess within 6-8 questions as confidence reaches 80%.
        3. When guessing (type="guess"), guessWord must be in $targetLanguage, guessTranslation in $nativeLanguage, guessEmoji exact emoji, and include 3 quizChoices in $targetLanguage (1 correct word "$targetLanguage", 2 plausible distractors in $targetLanguage from the same category).

        JSON schema:
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
        Generate 3 vocabulary quiz options in $targetLanguage (1 correct word "$correctWord", 2 plausible distractors in $targetLanguage).
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
        Explanation in $feedbackLanguage (max 2 sentences).
        JSON schema:
        {
          "isHonest": true|false,
          "explanation": "short message"
        }
        """.trimIndent()
    }
}
