package com.iti.linguaquest.features.review.data.datasource

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
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

class ReviewRemoteDataSourceImpl @Inject constructor() : ReviewRemoteDataSource {

    private val model: GenerativeModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(modelName = "gemini-3.1-flash-lite")
    }

    override suspend fun getAIReview(
        word: WordEntity
    ): LinguaQuestResult<AIReviewResponse, LinguaQuestDataError> {
        return try {
            val prompt = buildPrompt(word)

            val response = model.generateContent(prompt)

            val text = response.text
                ?: return LinguaQuestResult.Failure(
                    LinguaQuestDataError.Remote.EMPTY_RESULT
                )

            val parsed = parseResponse(text)

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
        val cleaned = raw
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        val json = JSONObject(cleaned)

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
}
