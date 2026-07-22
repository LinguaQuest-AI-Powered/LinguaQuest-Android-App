package com.iti.linguaquest.features.voicegame.data.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import com.google.gson.Gson
import com.iti.linguaquest.features.voicegame.data.model.VoiceEvaluationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceEvaluationService @Inject constructor() {

    private val generativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = "gemini-3.5-flash-lite",
            generationConfig = generationConfig {
                temperature = 0.0f
                responseMimeType = "application/json"
            }
        )

    private val gson = Gson()

    suspend fun evaluatePronunciation(
        targetSentence: String,
        targetLanguage: String,
        audioBytes: ByteArray
    ): VoiceEvaluationResponse {
        return withContext(Dispatchers.IO) {
            val promptText = """
                You are a supportive language coach. The user is practicing speaking a sentence.
                Target Sentence: "$targetSentence"
                Target Language: $targetLanguage
                
                Analyze the provided audio recording.
                1. Compare what they actually said against the Target Sentence word by word.
                2. Identify correctly pronounced words and put them in `correct_words`.
                3. Identify words from the Target Sentence that were mispronounced, omitted, or substituted and put them in `wrong_words`.
                
                CRITICAL WORD-MATCHING RULES:
                - EVERY word in the Target Sentence MUST be categorized into EITHER `correct_words` OR `wrong_words`.
                - `correct_words` and `wrong_words` MUST contain ONLY words present in the Target Sentence.
                - Do NOT include punctuation marks (like '.', '?', ',', '!') attached to any word in `correct_words` or `wrong_words`.
                - A word belongs in `correct_words` ONLY if it was clearly spoken and recognizable.
                - If the audio is completely silent, incomprehensible, or you cannot hear any speech, set rating to 0, `correct_words` to [], put ALL words from the Target Sentence into `wrong_words`, and give advice "I couldn't hear you clearly. Please try speaking again."
                
                4. Provide a score out of 10 based on how many target words were spoken correctly.
                5. Give a short, encouraging piece of advice (max 2 sentences).
                
                Respond STRICTLY in the following JSON format (no markdown, no backticks, just raw JSON):
                {
                    "rating": <integer score between 0 and 10>,
                    "correct_words": ["word1", "word2"],
                    "wrong_words": ["word3"],
                    "advice": "a short, encouraging tip for improvement"
                }
            """.trimIndent()

            val wavBytes = pcmToWav(audioBytes)

            val inputContent = content {
                text(promptText)
                inlineData(wavBytes, "audio/wav")
            }

            val response = generativeModel.generateContent(inputContent)
            var rawText = response.text ?: throw Exception("Empty or invalid response from model")

            Log.d("VoiceEvaluation", "RAW GEMINI RESPONSE:\n$rawText")

            // Sanitize markdown if the model ignored our instructions
            rawText = rawText.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            if (rawText.isEmpty()) {
                throw Exception("Empty or invalid response from model")
            }

            val parsedResponse = try {
                gson.fromJson(rawText, VoiceEvaluationResponse::class.java)
            } catch (e: Exception) {
                Log.e("VoiceEvaluation", "JSON DECODING ERROR", e)
                throw e
            }

            // Post-process to ensure clean words and that every target word is accounted for
            val cleanTargetWords = targetSentence.split("\\s+".toRegex())
                .map { it.replace("[^a-zA-Z0-9'-]".toRegex(), "") }
                .filter { it.isNotBlank() }

            val cleanCorrect = parsedResponse.correctWords
                .map { it.replace("[^a-zA-Z0-9'-]".toRegex(), "") }
                .filter { it.isNotBlank() }

            val correctLowerSet = cleanCorrect.map { it.lowercase() }.toSet()

            // Filter wrong words to only include target sentence words that were not spoken correctly
            val finalWrongWords = cleanTargetWords.filter { targetWord ->
                !correctLowerSet.contains(targetWord.lowercase())
            }

            // Final clean correct words matching original casing from target sentence if possible
            val finalCorrectWords = cleanTargetWords.filter { targetWord ->
                correctLowerSet.contains(targetWord.lowercase())
            }

            return@withContext VoiceEvaluationResponse(
                rating = parsedResponse.rating,
                correctWords = finalCorrectWords,
                wrongWords = finalWrongWords,
                advice = parsedResponse.advice
            )
        }
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

