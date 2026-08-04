package com.iti.linguaquest.features.mindreader.data.datasource.remote

import com.google.gson.Gson
import com.iti.linguaquest.core.ai.GeminiAiService
import javax.inject.Inject

data class MindReaderAiResponse(
    val isGuessing: Boolean,
    val questionTargetLang: String?,
    val questionNativeLang: String?,
    val guessWordTargetLang: String?,
    val guessWordNativeLang: String?,
    val guessEmoji: String?,
    val popQuizWrongOptionsTargetLang: List<String>?,
    val popQuizWrongOptionsNativeLang: List<String>?,
    val stumpDropdownOptionsTargetLang: List<String>?,
    val stumpDropdownOptionsNativeLang: List<String>?
)

data class MindReaderStumpVerificationResponse(
    val isHonest: Boolean,
    val reason: String
)

open class MindReaderAiService @Inject constructor(
    private val geminiAiService: GeminiAiService,
    private val gson: Gson
) {
    open suspend fun getNextTurn(
        category: String,
        targetLanguage: String,
        nativeLanguage: String,
        history: String
    ): MindReaderAiResponse? {
        val prompt = """
            You are playing the role of a highly intelligent, professional Akinator-style Mind Reader.
            The user is thinking of a specific concept in the category: '$category'.
            Target Language: '$targetLanguage'. Native Language: '$nativeLanguage'.
            
            Game History:
            $history
            
            Instructions:
            1. Analyze the history logically. Use process of elimination to narrow down possibilities.
            2. If you need more information, ask a strategic YES/NO question.
            3. CRITICAL: DO NOT repeat any questions that have already been asked in the Game History. Ensure your questions are realistic, smart, and progressively narrow down the options.
            4. CRITICAL: If the Game History is empty, pick a completely RANDOM, unpredictable starting question to ensure each game feels unique. Do not always start with the same question.
            5. If you are highly confident, make a Guess. 
            6. If Guessing, you MUST provide 'popQuizWrongOptions' (3 incorrect plausible choices) and 'stumpDropdownOptions' (10 alternative plausible choices from the category in case you are wrong).
            
            Reply ONLY in this JSON format:
            {
              "isGuessing": boolean,
              "questionTargetLang": "string or null",
              "questionNativeLang": "string or null",
              "guessWordTargetLang": "string or null",
              "guessWordNativeLang": "string or null",
              "guessEmoji": "string or null",
              "popQuizWrongOptionsTargetLang": ["word1", "word2", "word3"] or null,
              "popQuizWrongOptionsNativeLang": ["word1", "word2", "word3"] or null,
              "stumpDropdownOptionsTargetLang": ["word1", "word2", ..., "word10"] or null,
              "stumpDropdownOptionsNativeLang": ["word1", "word2", ..., "word10"] or null
            }
        """.trimIndent()

        val jsonString = geminiAiService.generateJson(prompt) ?: return null
        return try {
            gson.fromJson(jsonString, MindReaderAiResponse::class.java)
        } catch(e: Exception) {
            null
        }
    }

    open suspend fun verifyUserWord(
        category: String,
        history: String,
        userWord: String
    ): MindReaderStumpVerificationResponse? {
        val prompt = """
            You are a strict game referee for a Mind Reader game (Category: '$category').
            The AI failed to guess the word, and the user claims they were thinking of the word: '$userWord'.
            
            Here is the history of the user's answers to the AI's questions:
            $history
            
            Analyze if the user's answers are logically consistent with the word '$userWord'.
            Allow for minor human errors or subjectivity, but if the answers fundamentally contradict the word (e.g. saying a Cat is a reptile), they are cheating.
            
            Reply ONLY in this JSON format:
            {
              "isHonest": boolean,
              "reason": "Short explanation of why they are honest or why they contradicted themselves."
            }
        """.trimIndent()

        val jsonString = geminiAiService.generateJson(prompt) ?: return null
        return try {
            gson.fromJson(jsonString, MindReaderStumpVerificationResponse::class.java)
        } catch(e: Exception) {
            null
        }
    }
}
