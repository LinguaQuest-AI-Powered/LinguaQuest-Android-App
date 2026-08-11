package com.iti.linguaquest.features.voicegame.data.datasource.remote

import android.util.Base64
import com.google.gson.Gson
import com.iti.linguaquest.core.ai.network.GeminiRestClient
import com.iti.linguaquest.core.ai.network.model.GeminiContentDto
import com.iti.linguaquest.core.ai.network.model.GeminiGenerationConfigDto
import com.iti.linguaquest.core.ai.network.model.GeminiInlineDataDto
import com.iti.linguaquest.core.ai.network.model.GeminiPartDto
import com.iti.linguaquest.core.ai.network.model.GeminiRequestDto
import com.iti.linguaquest.features.voicegame.data.model.VoiceEvaluationResponse
import com.iti.linguaquest.features.voicegame.domain.prompt.VoiceGamePromptFactory
import java.text.Normalizer
import kotlin.math.roundToInt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceEvaluationService @Inject constructor(
    private val geminiRestClient: GeminiRestClient
) {

    private val gson = Gson()

    suspend fun evaluatePronunciation(
        targetSentence: String,
        targetLanguage: String,
        audioBytes: ByteArray,
        appLanguage: String = "English"
    ): VoiceEvaluationResponse {
        val promptText = VoiceGamePromptFactory.createEvaluationPrompt(
            targetSentence = targetSentence,
            targetLanguage = targetLanguage,
            appLanguage = appLanguage
        )

        val wavBytes = pcmToWav(audioBytes)
        val base64Audio = Base64.encodeToString(wavBytes, Base64.NO_WRAP)

        val requestPayload = GeminiRequestDto(
            contents = listOf(
                GeminiContentDto(
                    parts = listOf(
                        GeminiPartDto(
                            inlineData = GeminiInlineDataDto(
                                mimeType = "audio/wav",
                                data = base64Audio
                            )
                        ),
                        GeminiPartDto(text = promptText)
                    )
                )
            ),
            generationConfig = GeminiGenerationConfigDto(
                temperature = 0.1f,
                responseMimeType = "application/json"
            )
        )

        val rawText = geminiRestClient.executeGeminiRequest(requestPayload)
            ?: throw Exception("Empty or invalid response from model")

        val parsedResponse = try {
            gson.fromJson(rawText, VoiceEvaluationResponse::class.java)
        } catch (e: Exception) {
            throw e
        }

        val cleanTargetWords = targetSentence.split("\\s+".toRegex())
            .map { cleanWord(it) }
            .filter { it.isNotBlank() }

        val cleanCorrect = parsedResponse.correctWords
            .map { cleanWord(it) }
            .filter { it.isNotBlank() }

        val correctLowerSet = cleanCorrect.toSet()

        val finalWrongWords = cleanTargetWords.filter { targetWord ->
            !correctLowerSet.contains(cleanWord(targetWord))
        }

        val finalCorrectWords = cleanTargetWords.filter { targetWord ->
            correctLowerSet.contains(cleanWord(targetWord))
        }

        val calculatedRating = if (cleanTargetWords.isEmpty()) 0 else {
            val ratio = finalCorrectWords.size.toFloat() / cleanTargetWords.size.toFloat()
            (ratio * 10).roundToInt()
        }
        val finalRating = minOf(parsedResponse.rating, calculatedRating)

        return VoiceEvaluationResponse(
            rating = finalRating,
            correctWords = finalCorrectWords,
            wrongWords = finalWrongWords,
            advice = parsedResponse.advice,
            transcription = parsedResponse.transcription
        )
    }

    private fun cleanWord(word: String): String {
        val normalized = Normalizer.normalize(word, Normalizer.Form.NFD)
        val withoutDiacritics = normalized.replace("\\p{M}".toRegex(), "")
        return withoutDiacritics
            .replace("[^\\p{L}\\p{N}'-]".toRegex(), "")
            .lowercase()
    }

    private fun pcmToWav(pcmData: ByteArray, sampleRate: Int = 16000, channels: Int = 1, bitsPerSample: Int = 16): ByteArray {
        val totalDataLen = pcmData.size + 36
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val header = ByteArray(44)

        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        header[16] = 16
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (sampleRate and 0xff).toByte()
        header[25] = (sampleRate shr 8 and 0xff).toByte()
        header[26] = (sampleRate shr 16 and 0xff).toByte()
        header[27] = (sampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] = (channels * bitsPerSample / 8).toByte()
        header[33] = 0
        header[34] = bitsPerSample.toByte()
        header[35] = 0
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        header[40] = (pcmData.size and 0xff).toByte()
        header[41] = (pcmData.size shr 8 and 0xff).toByte()
        header[42] = (pcmData.size shr 16 and 0xff).toByte()
        header[43] = (pcmData.size shr 24 and 0xff).toByte()

        return header + pcmData
    }
}