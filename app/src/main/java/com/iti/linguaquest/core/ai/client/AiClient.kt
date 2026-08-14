package com.iti.linguaquest.core.ai.client

interface AiClient {
    suspend fun generateText(prompt: String, temperature: Float = 0.7f): String?
    suspend fun generateJson(prompt: String, temperature: Float = 0.1f): String?
    suspend fun generateFromAudio(
        prompt: String,
        audioBytes: ByteArray,
        mimeType: String = "audio/wav",
        temperature: Float = 0.1f
    ): String?
}
