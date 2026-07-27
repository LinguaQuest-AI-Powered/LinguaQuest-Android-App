package com.iti.linguaquest.features.review.data.datasource

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.review.domain.model.AIReviewResponse
import javax.inject.Inject

class ReviewRemoteDataSource @Inject constructor() {

    private val model: GenerativeModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel("gemini-3.1-flash-lite")
    }
     suspend fun getAIReview(
        word: WordEntity
    ): LinguaQuestResult<AIReviewResponse, LinguaQuestDataError> {
        return try {
            val prompt = buildPrompt(word)
            val response = model.generateContent(prompt)
            val text = response.text ?: return LinguaQuestResult.Failure(LinguaQuestDataError.Remote.EMPTY_RESULT)
            val parsed = parseResponse(text)
            LinguaQuestResult.Success(parsed)
        } catch (e: Exception) {
            LinguaQuestResult.Failure(LinguaQuestDataError.CustomServerMessage(e.message ?: "Unknown Error: ${e.javaClass.simpleName}"))
        }
    }

   
    private fun parseResponse(raw: String): AIReviewResponse {
        val cleaned = raw
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        val json = org.json.JSONObject(cleaned)

        val sentence    = json.optString("sentence", "")
        val translation = json.optString("translation", "")
        val tip         = json.optString("tip", "")
        val fact        = json.optString("fact", "")

        val fullText = buildString {
            if (sentence.isNotBlank())    append("Example sentence. $sentence. ")
            if (translation.isNotBlank()) append("Translation. $translation. ")
            if (tip.isNotBlank())         append("Memory tip. $tip. ")
            if (fact.isNotBlank())        append("Fun fact. $fact.")
        }.trim()

        return AIReviewResponse(
            exampleSentence    = sentence,
            sentenceTranslation = translation,
            memoryTip          = tip,
            funFact            = fact,
            fullText           = fullText
        )
    }
}
