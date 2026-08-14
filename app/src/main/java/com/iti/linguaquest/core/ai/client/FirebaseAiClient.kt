package com.iti.linguaquest.core.ai.client

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAiClient @Inject constructor() : AiClient {

    private val modelName = "gemini-3.5-flash-lite"

    private fun getModel(temperature: Float, isJson: Boolean): GenerativeModel {
        return Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = modelName,
                generationConfig = generationConfig {
                    this.temperature = temperature
                    if (isJson) {
                        responseMimeType = "application/json"
                    }
                }
            )
    }

    override suspend fun generateText(
        prompt: String,
        temperature: Float
    ): String? = withContext(Dispatchers.IO) {
        try {
            val response = getModel(temperature, isJson = false).generateContent(prompt)
            response.text?.trim()
        } catch (e: Exception) {
            Timber.e(e, "FirebaseAiClient generateText failed")
            null
        }
    }

    override suspend fun generateJson(
        prompt: String,
        temperature: Float
    ): String? = withContext(Dispatchers.IO) {
        try {
            val response = getModel(temperature, isJson = true).generateContent(prompt)
            cleanJson(response.text)
        } catch (e: Exception) {
            Timber.e(e, "FirebaseAiClient generateJson failed")
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
            val response = getModel(temperature, isJson = true).generateContent(
                content {
                    inlineData(audioBytes, mimeType)
                    text(prompt)
                }
            )
            cleanJson(response.text)
        } catch (e: Exception) {
            Timber.e(e, "FirebaseAiClient generateFromAudio failed")
            null
        }
    }

    private fun cleanJson(rawText: String?): String? {
        val cleaned = rawText
            ?.trim()
            ?.removePrefix("```json")
            ?.removePrefix("```")
            ?.removeSuffix("```")
            ?.trim()
        return cleaned?.takeIf { it.isNotEmpty() }
    }
}
