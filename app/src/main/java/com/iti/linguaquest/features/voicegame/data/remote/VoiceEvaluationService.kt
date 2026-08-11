package com.iti.linguaquest.features.voicegame.data.remote

import android.util.Base64
import com.google.gson.Gson
import com.iti.linguaquest.core.ai.network.GeminiRestClient
import com.iti.linguaquest.core.ai.network.model.GeminiContentDto
import com.iti.linguaquest.core.ai.network.model.GeminiGenerationConfigDto
import com.iti.linguaquest.core.ai.network.model.GeminiInlineDataDto
import com.iti.linguaquest.core.ai.network.model.GeminiPartDto
import com.iti.linguaquest.core.ai.network.model.GeminiRequestDto
import com.iti.linguaquest.features.voicegame.data.model.VoiceEvaluationResponse
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
        val promptText = """
            AUDIO TRANSCRIPTION & PRONUNCIATION SCORING INSTRUCTIONS:
            
            STEP 1 - TRANSCRIBE THE AUDIO:
            Listen to the provided audio file carefully.
            Write down the EXACT words spoken in the audio file in the 'transcription' field of the JSON.
            - Transcribe ONLY what you hear in the audio file.
            - DO NOT guess, assume, or hallucinate words that were not spoken in the audio file.
            - If only a few words (e.g., 2 words out of 4) are spoken in the audio file, the 'transcription' field MUST contain ONLY those spoken words.
            - If the audio contains only background noise, silence, or no recognizable words, set 'transcription' to "".
            
            STEP 2 - EVALUATE AGAINST REFERENCE SENTENCE:
            Reference Sentence for practice: "$targetSentence"
            Target Language: $targetLanguage
            User's Application Language: $appLanguage
            
            Compare the 'transcription' from STEP 1 against the Reference Sentence:
            - A word from the Reference Sentence goes into 'correct_words' ONLY if it is present in 'transcription' AND clearly, correctly pronounced.
            - A word from the Reference Sentence goes into 'wrong_words' if it is missing from 'transcription' (omitted), mispronounced, or substituted.
            - EVERY single word from the Reference Sentence MUST be placed in either 'correct_words' or 'wrong_words'.
            - Do NOT include punctuation marks in 'correct_words' or 'wrong_words'.
            
            STEP 3 - RATING & ADVICE:
            - Calculate the score out of 10 based ONLY on the number of correct words spoken vs total reference words.
            - If 2 out of 4 reference words are in 'transcription', the rating MUST be 5 out of 10. Do NOT give 10/10 when words are missing.
            - If the audio is completely silent or no speech is heard, set rating to 0, 'transcription' to "", 'correct_words' to [], put ALL reference words into 'wrong_words', and give encouraging advice in $appLanguage.
            - Provide a short, encouraging piece of advice (max 2 sentences) written in $appLanguage.
            
            Return STRICTLY raw JSON (no markdown, no backticks):
            {
                "transcription": "exact spoken words from audio",
                "rating": <integer score between 0 and 10>,
                "correct_words": ["word1"],
                "wrong_words": ["word2"],
                "advice": "short tip in $appLanguage"
            }
        """.trimIndent()

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