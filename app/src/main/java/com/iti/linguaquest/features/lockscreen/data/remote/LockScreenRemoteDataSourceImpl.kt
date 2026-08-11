package com.iti.linguaquest.features.lockscreen.data.remote

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.generationConfig
import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.data.remote.dto.DeductCoinsRequestDto
import com.iti.linguaquest.features.lockscreen.domain.model.GeneratedVocabularyWord
import com.iti.linguaquest.features.lockscreen.domain.model.VocabularyBatchParams
import jakarta.inject.Inject
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class LockScreenRemoteDataSourceImpl @Inject constructor(
    private val promptBuilder: PromptBuilder,
    private val coinsApiService: CoinsApiService
) : LockScreenRemoteDataSource {

    private val model: GenerativeModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName ="gemini-3.1-flash-lite",
                generationConfig = generationConfig {
                    responseMimeType = "application/json"
                }
            )
    }

    override suspend fun deductCoins(
        operationId: String,
        amount: Int,
        reason: String
    ): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return when (val result = safeApiCall {
            coinsApiService.deductCoins(
                idempotencyKey = operationId,
                request = DeductCoinsRequestDto(amount = amount, reason = reason)
            )
        }) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(Unit)
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun generateVocabulary(
        params: VocabularyBatchParams
    ): LinguaQuestResult<List<GeneratedVocabularyWord>, LinguaQuestDataError> {
        return try {
            val prompt = promptBuilder.build(params)
             val response = model.generateContent(prompt)
            val text = response.text
                ?: return LinguaQuestResult.Failure(LinguaQuestDataError.Remote.EMPTY_RESULT)
            val parsed = parseResponse(text)
             if (parsed.isEmpty()) {
                 LinguaQuestResult.Failure(LinguaQuestDataError.Remote.SERIALIZATION)
            } else {
                LinguaQuestResult.Success(parsed)
            }
        } catch (e: Exception) {
             LinguaQuestResult.Failure(e.toRemoteError())
        }
    }

    private fun parseResponse(raw: String): List<GeneratedVocabularyWord> {
        val candidates = buildList {
            val cleaned = raw
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            add(cleaned)
            extractJsonBlock(cleaned, '[', ']')?.let { add(it) }
            extractJsonBlock(cleaned, '{', '}')?.let { add(it) }
        }

        for (candidate in candidates.distinct()) {
            parseCandidate(candidate)?.let { parsed ->
                if (parsed.isNotEmpty()) {
                    return parsed
                }
            }
        }

        return emptyList()
    }

    private fun parseCandidate(candidate: String): List<GeneratedVocabularyWord>? {
        return runCatching {
            when (val parsed = JSONTokener(candidate).nextValue()) {
                is JSONArray -> parseArray(parsed)
                is JSONObject -> parseObject(parsed)
                else -> emptyList()
            }
        }.getOrNull()
    }

    private fun parseArray(array: JSONArray): List<GeneratedVocabularyWord> {
        val items = buildList {
            for (index in 0 until array.length()) {
                val obj = array.optJSONObject(index) ?: continue
                val word = obj.optString("word").trim()
                val translation = obj.optString("translation").trim()
                val exampleSentence = obj.optString("example_sentence").trim()
                if (word.isBlank() || translation.isBlank() || exampleSentence.isBlank()) continue
                add(
                    GeneratedVocabularyWord(
                        word = word,
                        translation = translation,
                        exampleSentence = exampleSentence
                    )
                )
            }
        }

        return items.distinctBy { it.word.lowercase() }
    }

    private fun parseObject(obj: JSONObject): List<GeneratedVocabularyWord> {
        val array = listOf("words", "items", "data", "vocabulary")
            .firstNotNullOfOrNull { key -> obj.optJSONArray(key) }
            ?: return emptyList()

        return parseArray(array)
    }

    private fun extractJsonBlock(raw: String, startToken: Char, endToken: Char): String? {
        val startIndex = raw.indexOf(startToken)
        val endIndex = raw.lastIndexOf(endToken)
        if (startIndex == -1 || endIndex == -1 || endIndex < startIndex) {
            return null
        }

        return raw.substring(startIndex, endIndex + 1)
    }

    private fun Throwable.toRemoteError(): LinguaQuestDataError {

        val message = message.orEmpty().lowercase()
        return when {
            message.contains("timeout") -> LinguaQuestDataError.Remote.REQUEST_TIMEOUT
            message.contains("network") || message.contains("host") || message.contains("connect") -> LinguaQuestDataError.Remote.NO_INTERNET
            message.contains("429") || message.contains("quota") || message.contains("too many") || message.contains("rate limit") -> LinguaQuestDataError.Remote.TOO_MANY_REQUESTS
            message.contains("400") || message.contains("bad request") -> LinguaQuestDataError.Remote.BAD_REQUEST
            message.contains("401") || message.contains("403") || message.contains("unauthor") || message.contains("permission denied") -> LinguaQuestDataError.Remote.UNAUTHORIZED
            message.contains("json") || message.contains("serialization") -> LinguaQuestDataError.Remote.SERIALIZATION
            else -> LinguaQuestDataError.CustomServerMessage(message.ifBlank { javaClass.simpleName })
        }
    }


}
