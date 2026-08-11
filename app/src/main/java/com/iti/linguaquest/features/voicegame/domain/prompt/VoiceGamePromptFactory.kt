package com.iti.linguaquest.features.voicegame.domain.prompt

object VoiceGamePromptFactory {

    fun createEvaluationPrompt(
        targetSentence: String,
        targetLanguage: String,
        appLanguage: String
    ): String {
        return """
            AUDIO TRANSCRIPTION & PRONUNCIATION SCORING INSTRUCTIONS:
            
            STEP 1 - TRANSCRIBE THE AUDIO:
            Listen to the provided audio file carefully.
            Write down the EXACT words spoken in the audio file in the 'transcription' field of the JSON.
            - Transcribe ONLY what you hear in the audio file.
            - DO NOT guess, assume, or hallucinate words that were not spoken in the audio file.
            - If only a few words (e.g., 2 words out of 4) are spoken in the audio file, the 'transcription' field MUST contain ONLY those spoken words.
            - If the audio contains only background noise, silence, or no recognizable words, set 'transcription' to "".
            
            STEP 2 - EVALUATE AGAINST REFERENCE SENTENCE:
            Reference Sentence for practice: "$targetSentence"
            Target Language: $targetLanguage
            User's Application Language: $appLanguage
            
            Compare the 'transcription' from STEP 1 against the Reference Sentence:
            - A word from the Reference Sentence goes into 'correct_words' ONLY if it is present in 'transcription' AND clearly, correctly pronounced.
            - A word from the Reference Sentence goes into 'wrong_words' if it is missing from 'transcription' (omitted), mispronounced, or substituted.
            - EVERY single word from the Reference Sentence MUST be placed in either 'correct_words' or 'wrong_words'.
            - Do NOT include punctuation marks in 'correct_words' or 'wrong_words'.
            
            STEP 3 - RATING & ADVICE:
            - Calculate the score out of 10 based ONLY on the number of correct words spoken vs total reference words.
            - If 2 out of 4 reference words are in 'transcription', the rating MUST be 5 out of 10. Do NOT give 10/10 when words are missing.
            - If the audio is completely silent or no speech is heard, set rating to 0, 'transcription' to "", 'correct_words' to [], put ALL reference words into 'wrong_words', and give encouraging advice in $appLanguage.
            - Provide a short, encouraging piece of advice (max 2 sentences) written in $appLanguage.
            
            Return STRICTLY raw JSON (no markdown, no backticks):
            {
                "transcription": "exact spoken words from audio",
                "rating": <integer score between 0 and 10>,
                "correct_words": ["word1"],
                "wrong_words": ["word2"],
                "advice": "short tip in $appLanguage"
            }
        """.trimIndent()
    }

    fun createSentenceGeneratorPrompt(
        targetLanguage: String,
        level: String,
        topic: String,
        count: Int,
        wordOfTheDay: String? = null
    ): String {
        val wordRule = if (!wordOfTheDay.isNullOrBlank()) {
            "\n6. The generated sentences MUST strictly contain the word: '$wordOfTheDay' (case-insensitive)."
        } else ""

        return """
            You are a supportive language tutor for beginner language learners.
            Generate $count short, simple, and easy-to-pronounce practice sentences in $targetLanguage.
            Topic context: $topic.

            EASY SENTENCE RULES:
            1. Sentences MUST be short, simple, and very easy to pronounce.
            2. Sentence length MUST be between 3 and 6 words max.
            3. Use common everyday words (e.g. greetings, simple feelings, daily actions).
            4. NO tongue twisters, complex grammar, or difficult multi-syllable words.
            5. Include simple phonetic transcription (IPA) and translation.$wordRule

            Return STRICTLY a JSON object with NO markdown code fences following this schema:
            {
              "sentences": [
                {
                  "sentence": "Hello, how are you?",
                  "difficulty": "Easy",
                  "phonetic": "/həˈloʊ haʊ ɑːr juː/",
                  "translation": "Hello, how are you?"
                }
              ]
            }
        """.trimIndent()
    }
}
