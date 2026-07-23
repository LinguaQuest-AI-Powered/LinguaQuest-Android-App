package com.iti.linguaquest.features.voicegame.data.remote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import com.iti.linguaquest.core.ai.GeminiAiService
import javax.inject.Inject
import javax.inject.Singleton

data class GeneratedSentence(
    @SerializedName("sentence") val sentence: String,
    @SerializedName("difficulty") val difficulty: String = "Easy",
    @SerializedName("phonetic") val phonetic: String? = null,
    @SerializedName("translation") val translation: String? = null
)

@Singleton
class PronunciationSentenceGeneratorService @Inject constructor(
    private val geminiAiService: GeminiAiService
) {

    private val gson = Gson()

    private val fallbackSentences = listOf(
        GeneratedSentence("Hello, how are you today?", "Easy", "/həˈloʊ haʊ ɑːr juː təˈdeɪ/", "Hello, how are you today?"),
        GeneratedSentence("I love learning new languages.", "Easy", "/aɪ lʌv ˈlɜːrnɪŋ njuː ˈlæŋɡwɪdʒɪz/", "I love learning new languages."),
        GeneratedSentence("The sun is shining brightly.", "Easy", "/ðə sʌn ɪz ˈʃaɪnɪŋ ˈbraɪtli/", "The sun is shining brightly."),
        GeneratedSentence("Have a wonderful day!", "Easy", "/hæv ə ˈwʌndərfəl deɪ/", "Have a wonderful day!"),
        GeneratedSentence("I am happy to meet you.", "Easy", "/aɪ æm ˈhæpi tuː miːt juː/", "I am happy to meet you."),
        GeneratedSentence("Good morning, my friend.", "Easy", "/ɡʊd ˈmɔːrnɪŋ maɪ frɛnd/", "Good morning, my friend."),
        GeneratedSentence("Where is the nearest library?", "Easy", "/wɛər ɪz ðə ˈnɪərɪst ˈlaɪbrəri/", "Where is the nearest library?"),
        GeneratedSentence("I would like a fresh cup of tea.", "Easy", "/aɪ wʊd laɪk ə frɛʃ kʌp ɒv tiː/", "I would like a fresh cup of tea."),
        GeneratedSentence("What a beautiful morning!", "Easy", "/wɒt ə ˈbjuːtɪfʊl ˈmɔːnɪŋ/", "What a beautiful morning!"),
        GeneratedSentence("See you again tomorrow.", "Easy", "/siː juː əˈɡɛn təˈmɒroʊ/", "See you again tomorrow."),
        GeneratedSentence("Music makes me feel happy.", "Easy", "/ˈmjuːzɪk meɪks miː fiːl ˈhæpi/", "Music makes me feel happy."),
        GeneratedSentence("Let's go for a short walk.", "Easy", "/lɛts ɡoʊ fɔːr ə ʃɔːrt wɔːk/", "Let's go for a short walk.")
    )

    suspend fun generateSentences(
        targetLanguage: String = "English",
        level: String = "Beginner",
        topic: String = "General Conversation",
        count: Int = 5
    ): List<GeneratedSentence> {
        return try {
            val prompt = """
                You are a supportive language tutor for beginner language learners.
                Generate $count short, simple, and easy-to-pronounce practice sentences in $targetLanguage.
                Topic context: $topic.

                EASY SENTENCE RULES:
                1. Sentences MUST be short, simple, and very easy to pronounce.
                2. Sentence length MUST be between 3 and 6 words max.
                3. Use common everyday words (e.g. greetings, simple feelings, daily actions).
                4. NO tongue twisters, complex grammar, or difficult multi-syllable words.
                5. Include simple phonetic transcription (IPA) and translation.

                Return STRICTLY a JSON object with NO markdown code fences following this schema:
                {
                  "sentences": [
                    {
                      "sentence": "Hello, how are you?",
                      "difficulty": "Easy",
                      "phonetic": "/həˈloʊ haʊ ɑːr juː/",
                      "translation": "Hello, how are you?"
                    }
                  ]
                }
            """.trimIndent()

            val rawText = geminiAiService.generateJson(prompt)
            if (rawText == null) {
                return getRandomFallback(count)
            }


            val jsonElement = JsonParser.parseString(rawText)
            val results = mutableListOf<GeneratedSentence>()

            when {
                jsonElement.isJsonObject -> {
                    val obj = jsonElement.asJsonObject
                    if (obj.has("sentences") && obj.get("sentences").isJsonArray) {
                        obj.getAsJsonArray("sentences").forEach { elem ->
                            try {
                                results.add(gson.fromJson(elem, GeneratedSentence::class.java))
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed parsing JSON array item", e)
                            }
                        }
                    } else if (obj.has("sentence")) {
                        results.add(gson.fromJson(obj, GeneratedSentence::class.java))
                    }
                }
                jsonElement.isJsonArray -> {
                    jsonElement.asJsonArray.forEach { elem ->
                        try {
                            results.add(gson.fromJson(elem, GeneratedSentence::class.java))
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed parsing array item", e)
                        }
                    }
                }
            }

            results.ifEmpty {
                getRandomFallback(count)
            }
        } catch (e: Exception) {
            getRandomFallback(count)
        }
    }

    private fun getRandomFallback(count: Int): List<GeneratedSentence> {
        return fallbackSentences.shuffled().take(count)
    }

    private companion object {
        const val TAG = "GEMINI_DEBUG"
    }
}