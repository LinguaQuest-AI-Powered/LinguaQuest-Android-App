package com.iti.linguaquest.features.review.data.datasource

import com.iti.linguaquest.core.ai.client.AiClient
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.review.domain.model.AIReviewResponse
import org.json.JSONObject
import javax.inject.Inject

interface ReviewRemoteDataSource {

    suspend fun getAIReview(
        word: WordEntity
    ): LinguaQuestResult<AIReviewResponse, LinguaQuestDataError>
}

class ReviewRemoteDataSourceImpl @Inject constructor(
    private val aiClient: AiClient
) : ReviewRemoteDataSource {

    override suspend fun getAIReview(
        word: WordEntity
    ): LinguaQuestResult<AIReviewResponse, LinguaQuestDataError> {
        return try {
            val prompt = buildPrompt(word)
            val jsonText = aiClient.generateJson(prompt)
                ?: return LinguaQuestResult.Failure(
                    LinguaQuestDataError.Remote.EMPTY_RESULT
                )

            val parsed = parseResponse(jsonText)
            LinguaQuestResult.Success(parsed)
        } catch (e: Exception) {
            LinguaQuestResult.Failure(
                LinguaQuestDataError.CustomServerMessage(
                    e.message ?: "Unknown Error: ${e.javaClass.simpleName}"
                )
            )
        }
    }

    private fun parseResponse(raw: String): AIReviewResponse {
        val json = JSONObject(raw)

        val sentence = json.optString("sentence", "")
        val translation = json.optString("translation", "")
        val tip = json.optString("tip", "")
        val fact = json.optString("fact", "")

        val fullText = buildString {
            if (sentence.isNotBlank()) append(sentence)
            if (translation.isNotBlank()) {
                if (isNotEmpty()) append("\n\n")
                append(translation)
            }
            if (tip.isNotBlank()) {
                if (isNotEmpty()) append("\n\n")
                append(tip)
            }
            if (fact.isNotBlank()) {
                if (isNotEmpty()) append("\n\n")
                append(fact)
            }
        }.trim()

        return AIReviewResponse(
            exampleSentence = sentence,
            sentenceTranslation = translation,
            memoryTip = tip,
            funFact = fact,
            fullText = fullText
        )
    }

    private fun buildPrompt(word: WordEntity): String {
        return """
            You are a helpful language learning assistant.
            Generate a helpful learning tip for the word "${word.translatedWord}" (which means "${word.sourceWord}") in ${word.targetLanguage}.
            Respond strictly in valid JSON format with keys:
            {
               "sentence": "An example sentence using the word",
               "translation": "English translation of the sentence",
               "tip": "A mnemonic or memory hook to remember it",
               "fact": "An interesting cultural or linguistic fun fact"
            }
        """.trimIndent()
    }
}
