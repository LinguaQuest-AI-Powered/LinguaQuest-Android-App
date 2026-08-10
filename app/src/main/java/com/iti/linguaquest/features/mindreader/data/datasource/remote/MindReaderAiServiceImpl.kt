package com.iti.linguaquest.features.mindreader.data.datasource.remote

import com.google.gson.Gson
import com.iti.linguaquest.core.ai.network.GeminiRestClient
import com.iti.linguaquest.core.ai.network.model.GeminiContentDto
import com.iti.linguaquest.core.ai.network.model.GeminiGenerationConfigDto
import com.iti.linguaquest.core.ai.network.model.GeminiPartDto
import com.iti.linguaquest.core.ai.network.model.GeminiRequestDto
import com.iti.linguaquest.features.mindreader.data.datasource.remote.mdoel.MindReaderHonestyResponse
import com.iti.linguaquest.features.mindreader.data.datasource.remote.mdoel.MindReaderNextStepResponse
import com.iti.linguaquest.features.mindreader.data.datasource.remote.mdoel.MindReaderQuizResponse
import javax.inject.Inject

class MindReaderAiServiceImpl @Inject constructor(
    private val gson: Gson,
    private val geminiRestClient: GeminiRestClient
) : MindReaderAiService {

    private suspend fun generateJson(prompt: String): String? {
        val requestPayload = GeminiRequestDto(
            contents = listOf(
                GeminiContentDto(
                    parts = listOf(GeminiPartDto(text = prompt))
                )
            ),
            generationConfig = GeminiGenerationConfigDto(
                temperature = 0.5f,
                responseMimeType = "application/json"
            )
        )
        return geminiRestClient.executeGeminiRequest(requestPayload)
    }

    override suspend fun getNextTurn(
        categoryContext: String,
        targetLanguage: String,
        nativeLanguage: String,
        historyPrompt: String
    ): MindReaderNextStepResponse? {
        val maxTurns = 20
        val prompt = """
        You are the engine behind "Lingo's Mind Reader", an Akinator-style guessing game for language learners. The user is thinking of ONE specific word that belongs to this category:

        Category context: "$categoryContext"

        Conversation so far (question asked in the target language, and the user's answer):
        $historyPrompt

        (If historyPrompt is empty, this is the very first question.)

        Your job: 
        1. Analyze the conversation so far carefully. Every new question MUST be logical and strictly build upon the previous answers. Do not ask random questions.
        2. Pick a yes/no-style question that splits the remaining possibilities roughly in half based on the user's previous answers.
        3. Never repeat a question already asked.
        4. If this is the first question, randomly pick an interesting property to ask about (e.g., size, location, usage) so the game feels fresh every time. DO NOT always start with the same question.
        5. If you are highly confident about the word before reaching $maxTurns questions, STOP asking immediately and make your best guess! You do NOT need to reach $maxTurns questions. $maxTurns is only a maximum limit.
        6. If the conversation reaches $maxTurns turns, you MUST make a guess.

        Target language: $targetLanguage
        Native language: $nativeLanguage

        CRITICAL RULE 1: If asking a question, "questionTargetText" MUST be written ONLY in $targetLanguage, and "questionNativeText" MUST be its accurate translation in $nativeLanguage.
        CRITICAL RULE 2: Only set type to "guess" when you are actually naming a specific concrete word/object, never a category or vague guess.
        CRITICAL RULE 3: The guessed word must plausibly belong to the given category context.
        CRITICAL RULE 4: The "guessEmoji" MUST be the exact, most highly relevant single system emoji that directly represents the guessed object visually. Do NOT use generic or loosely related emojis (e.g., if the word is 'Stethoscope', you MUST use 🩺, do NOT use 🩼 or 🏥. If the word is 'Apple', use 🍎). If no exact emoji exists, pick the closest visual match.

        Respond STRICTLY in the following JSON format (no markdown, no backticks, just raw JSON):
        {
          "type": "question" | "guess",
          "questionTargetText": "string or null",
          "questionNativeText": "string or null",
          "guessWord": "string or null, in target language",
          "guessTranslation": "string or null, in native language",
          "guessEmoji": "single system emoji accurately representing the word, or null"
        }
        """.trimIndent()

        val jsonString = generateJson(prompt) ?: return null
        return try {
            gson.fromJson(jsonString, MindReaderNextStepResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun generateQuizChoices(
        categoryContext: String,
        correctWord: String,
        nativeLanguage: String,
        targetLanguage: String
    ): MindReaderQuizResponse? {
        val prompt = """
        The user just correctly identified the word "$correctWord" (in $targetLanguage) from this category: "$categoryContext".

        Generate exactly 3 answer options for a vocabulary quiz: one is the correct word "$correctWord", and two are plausible-but-wrong words from the same category (in $targetLanguage). Shuffle the order.

        CRITICAL RULE: Exactly one option must have "isCorrect": true.

        Respond STRICTLY in the following JSON format (no markdown, no backticks, just raw JSON):
        {
          "choices": [
            {"translationText": "string", "isCorrect": true|false},
            {"translationText": "string", "isCorrect": true|false},
            {"translationText": "string", "isCorrect": true|false}
          ]
        }
        """.trimIndent()

        val jsonString = generateJson(prompt) ?: return null
        return try {
            gson.fromJson(jsonString, MindReaderQuizResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun verifyUserWord(
        categoryContext: String,
        historyPrompt: String,
        claimedWord: String,
        feedbackLanguage: String
    ): MindReaderHonestyResponse? {
        val prompt = """
        The user played a guessing game and, when the AI failed to guess, claimed they were thinking of the word "$claimedWord" (category: "$categoryContext").

        Here is the full history of questions asked and the user's answers:
        $historyPrompt

        Check whether "$claimedWord" is logically consistent with EVERY answer the user gave. A real-world word/object should reasonably match yes/no/sometimes answers about its typical properties. If there is a clear contradiction (e.g. user said "no" to a property that is obviously true for "$claimedWord", or vice versa), the user was not honest.

        CRITICAL RULE 1: Be reasonably lenient — "sometimes" and "probably not" allow for ambiguity, only flag CLEAR contradictions, not borderline cases.
        CRITICAL RULE 2: "explanation" MUST be written in $feedbackLanguage, must be short (max 2 sentences), friendly if honest, and clearly point out the contradiction if not honest.

        Respond STRICTLY in the following JSON format (no markdown, no backticks, just raw JSON):
        {
          "isHonest": true|false,
          "explanation": "short message in $feedbackLanguage"
        }
        """.trimIndent()

        val jsonString = generateJson(prompt) ?: return null
        return try {
            gson.fromJson(jsonString, MindReaderHonestyResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }
}