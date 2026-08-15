package com.iti.linguaquest.core.ai.client

import com.iti.linguaquest.BuildConfig
import com.iti.linguaquest.core.ai.network.ItiGatewayApiService
import com.iti.linguaquest.core.ai.network.model.GatewayChatRequestDto
import com.iti.linguaquest.core.ai.network.model.GatewayMessageDto
import com.iti.linguaquest.core.ai.network.model.GatewayResponseFormatDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItiGatewayAiClient @Inject constructor(
    private val apiService: ItiGatewayApiService,
    private val geminiAiClient: GeminiAiClient
) : AiClient {

    private val jsonCandidateModels = listOf(
        GatewayModel.DEEPSEEK_V3_2.modelId,
        GatewayModel.LLAMA_3_3_70B.modelId,
        GatewayModel.GPT_OSS_120B.modelId
    )

    private val textCandidateModels = listOf(
        GatewayModel.DEEPSEEK_V3_2.modelId,
        GatewayModel.LLAMA_3_3_70B.modelId,
        GatewayModel.GPT_OSS_20B.modelId,
        GatewayModel.GPT_OSS_120B.modelId
    )

    private val invalidModels = Collections.synchronizedSet(mutableSetOf<String>())

    @Volatile
    private var activeJsonModel: String = jsonCandidateModels.first()

    @Volatile
    private var activeTextModel: String = textCandidateModels.first()

    override suspend fun generateText(
        prompt: String,
        temperature: Float
    ): String? = withContext(Dispatchers.IO) {
        try {
            val response = executeRequest(
                candidates = textCandidateModels,
                activeModelGetter = { activeTextModel },
                activeModelSetter = { activeTextModel = it }
            ) { model ->
                GatewayChatRequestDto(
                    modelId = model,
                    model = model,
                    messages = listOf(GatewayMessageDto(role = "user", content = prompt)),
                    temperature = temperature
                )
            }
            response?.trim()
        } catch (e: Exception) {
            Timber.e(e, "ItiGatewayAiClient generateText failed")
            null
        }
    }

    override suspend fun generateJson(
        prompt: String,
        temperature: Float
    ): String? = withContext(Dispatchers.IO) {
        try {
            val response = executeRequest(
                candidates = jsonCandidateModels,
                activeModelGetter = { activeJsonModel },
                activeModelSetter = { activeJsonModel = it }
            ) { model ->
                GatewayChatRequestDto(
                    modelId = model,
                    model = model,
                    messages = listOf(GatewayMessageDto(role = "user", content = prompt)),
                    temperature = temperature,
                    responseFormat = GatewayResponseFormatDto(type = "json_object")
                )
            }
            cleanJson(response)
        } catch (e: Exception) {
            Timber.e(e, "ItiGatewayAiClient generateJson failed")
            null
        }
    }

    override suspend fun generateFromAudio(
        prompt: String,
        audioBytes: ByteArray,
        mimeType: String,
        temperature: Float
    ): String? = withContext(Dispatchers.IO) {
        try {
            if (BuildConfig.GEMINI_API_KEY.isNotBlank()) {
                geminiAiClient.generateFromAudio(prompt, audioBytes, mimeType, temperature)
            } else {
                Timber.w("GEMINI_API_KEY is blank; audio evaluation requires Gemini multimodal capability")
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "ItiGatewayAiClient generateFromAudio failed")
            null
        }
    }

    private suspend fun executeRequest(
        candidates: List<String>,
        activeModelGetter: () -> String,
        activeModelSetter: (String) -> Unit,
        requestBuilder: (String) -> GatewayChatRequestDto
    ): String? {
        val apiKey = BuildConfig.AI_KEY
        if (apiKey.isBlank()) {
            Timber.e("AI_KEY is empty in BuildConfig")
            return null
        }

        val authHeader = "Bearer $apiKey"
        val currentActive = activeModelGetter()

        val modelsToTry = buildList {
            if (!invalidModels.contains(currentActive)) {
                add(currentActive)
            }
            candidates.forEach { model ->
                if (model != currentActive && !invalidModels.contains(model)) {
                    add(model)
                }
            }
        }

        var lastException: Throwable? = null
        var lastHttpCode: Int? = null

        for (model in modelsToTry) {
            try {
                val payload = requestBuilder(model)
                val response = apiService.createChatCompletion(
                    authorization = authHeader,
                    request = payload
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    val resultText = body?.outputText
                        ?: body?.choices?.firstOrNull()?.message?.content
                        ?: body?.content
                        ?: body?.response
                        ?: body?.output
                        ?: body?.text
                        ?: body?.message?.content

                    if (!resultText.isNullOrBlank()) {
                        activeModelSetter(model)
                        return resultText
                    }
                } else {
                    lastHttpCode = response.code()
                    if (response.code() == 404 || response.code() == 400) {
                        invalidModels.add(model)
                    }
                    Timber.w("Model $model returned HTTP ${response.code()}. Trying fallback...")
                }
            } catch (e: Exception) {
                lastException = e
                Timber.w(e, "Request to $model failed. Trying fallback...")
            }
        }

        Timber.e("All candidate models failed to generate response on ITI AI Gateway")
        if (lastHttpCode == 429 || lastException?.message?.contains("429") == true || lastException?.message?.contains("quota", ignoreCase = true) == true) {
            throw IllegalStateException("HTTP 429: Quota exceeded for ITI AI Gateway service", lastException)
        }
        if (lastException != null) {
            throw lastException
        }
        return null
    }

    private fun cleanJson(rawText: String?): String? {
        if (rawText.isNullOrBlank()) return null
        val trimmed = rawText.trim()
        val jsonBlockRegex = "```(?:json)?\\s*([\\s\\S]*?)\\s*```".toRegex()
        val match = jsonBlockRegex.find(trimmed)
        val extracted = if (match != null) {
            match.groupValues[1].trim()
        } else {
            val firstBrace = trimmed.indexOf('{')
            val firstBracket = trimmed.indexOf('[')
            val startIndex = when {
                firstBrace != -1 && firstBracket != -1 -> minOf(firstBrace, firstBracket)
                firstBrace != -1 -> firstBrace
                firstBracket != -1 -> firstBracket
                else -> -1
            }
            val lastBrace = trimmed.lastIndexOf('}')
            val lastBracket = trimmed.lastIndexOf(']')
            val endIndex = maxOf(lastBrace, lastBracket)
            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                trimmed.substring(startIndex, endIndex + 1).trim()
            } else {
                trimmed
            }
        }
        return extracted.takeIf { it.isNotEmpty() }
    }
}
