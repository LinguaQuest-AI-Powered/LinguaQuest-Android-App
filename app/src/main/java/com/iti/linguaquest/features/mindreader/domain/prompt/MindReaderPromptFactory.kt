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
        You are an elite Akinator-style AI mind reader engine for a language learning vocabulary game.
        The user is thinking of a specific word/concept in the category "$categoryContext".
        Target Language (learned by user): $targetLanguage
        Native Language (user's native language): $nativeLanguage

        Previous Question & Answer History:
        $historyPrompt

        Instructions for Superior Deduction (Think Like a Genius Akinator):
        1. STRICT YES/NO (BOOLEAN) QUESTIONS ONLY - CRITICAL:
           - The player can ONLY answer with: Yes, No, Sometimes, Probably, or Probably Not.
           - Every question MUST be a strict polar Yes/No question!
           - NEVER ask alternative/choice questions with "or" (e.g. NEVER ask "Is it eaten cold or hot?", "Is it a fruit or a vegetable?", "Is it big or small?", "Is it sweet or salty?").
           - INSTEAD, ask about one single binary attribute: "Is it usually eaten hot?", "Is it a fruit?", "Is it big?", "Is it sweet?".
           - NEVER ask open-ended questions (e.g. "What color is it?", "Where is it located?").
           - All questions MUST start with an auxiliary/modal verb suitable for a Yes/No answer: "Is it...", "Does it...", "Can it...", "Can you...", "Do you...", "Has it...", "Are they...".

        2. DEDUCTIVE REASONING & ACTIVE HYPOTHESIS TRACKING:
           - Maintain an internal set of candidate words in "$categoryContext" that strictly match ALL previous answers.
           - NEVER ask a question whose answer is already obvious from past answers.
           - NEVER contradict previous answers.
           - Calculate the highest information-gain question: ask about the single most discriminative binary trait that divides remaining candidates in half.

        3. BALANCED PACING & PROGRESSIVE ELIMINATION (AIM FOR 6 TO 8 QUESTIONS):
           - TURNS 1 to 3: Foundational classification. NEVER guess on turns 1, 2, or 3 (type MUST be "question")! Ask broad binary questions (e.g., "Is it alive?", "Is it man-made?", "Can you eat it?", "Is it found indoors?").
           - TURNS 4 to 5: Targeted feature narrowing (e.g., "Is it made of metal?", "Is it bigger than a dog?", "Is it yellow?", "Is it used for cooking?"). Do NOT rush to guess prematurely unless all other candidate entities in "$categoryContext" are completely eliminated.
           - TURNS 6 to 9: Optimal guessing window! When confidence is high (>85%) and 1 strong candidate emerges from the evidence, make the smart guess (type="guess").
           - TURNS 10+: If still not guessed, ask one final highly specific distinguishing question or make your best probable guess.

        4. LANGUAGE LEARNER ACCESSIBILITY:
           - Questions in $targetLanguage MUST be short, crystal-clear, and grammatically natural (beginner A1/A2, 4-8 words).
           - Examples of valid Yes/No questions: "Is it an animal?", "Is it yellow?", "Can you eat it raw?", "Is it found in the kitchen?", "Is it bigger than a cat?", "Is it made of wood?".
           - Provide the exact, natural translation in $nativeLanguage in "questionNativeText".

        5. OUTPUT FORMAT:
           - If asking a question: set type="question", populate "questionTargetText" and "questionNativeText", set guess fields to null.
           - If making a guess: set type="guess", set "guessWord" (the item in $targetLanguage), "guessTranslation" (in $nativeLanguage), "guessEmoji" (single emoji), and include 3 "quizChoices" in $targetLanguage (1 correct word "$targetLanguage", 2 plausible distractors in $targetLanguage from "$categoryContext").

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
