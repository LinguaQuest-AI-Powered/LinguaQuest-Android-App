package com.iti.linguaquest.features.voicegame.data.datasource.remote

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import com.iti.linguaquest.core.ai.client.AiClient
import com.iti.linguaquest.features.voicegame.domain.prompt.VoiceGamePromptFactory
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
    private val aiClient: AiClient
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
        count: Int = 5,
        wordOfTheDay: String? = null
    ): List<GeneratedSentence> {
        return try {
            val prompt = VoiceGamePromptFactory.createSentenceGeneratorPrompt(
                targetLanguage = targetLanguage,
                level = level,
                topic = topic,
                count = count,
                wordOfTheDay = wordOfTheDay
            )

            val rawText = aiClient.generateJson(prompt)
            if (rawText == null) {
                return getRandomFallback(targetLanguage, count)
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
                        }
                    }
                }
            }

            results.ifEmpty {
                getRandomFallback(targetLanguage, count)
            }
        } catch (e: Exception) {
            getRandomFallback(targetLanguage, count)
        }
    }

    private fun getRandomFallback(targetLanguage: String, count: Int): List<GeneratedSentence> {
        val list = when (targetLanguage.trim().lowercase()) {
            "spanish", "es" -> listOf(
                GeneratedSentence("Hola, ¿cómo estás hoy?", "Easy", "/ˈo.la ˈko.mo esˈtas oi/", "Hello, how are you today?"),
                GeneratedSentence("Me encanta aprender idiomas.", "Easy", "/me enˈkan.ta a.prenˈder iˈðjo.mas/", "I love learning new languages."),
                GeneratedSentence("¡Que tengas un buen día!", "Easy", "/ke ˈten.ɡas un bwen ˈdi.a/", "Have a nice day!"),
                GeneratedSentence("Mucho gusto en conocerte.", "Easy", "/ˈmu.tʃo ˈɣus.to en ko.noˈser.te/", "Nice to meet you.")
            )
            "french", "fr" -> listOf(
                GeneratedSentence("Bonjour, comment allez-vous?", "Easy", "/bɔ̃ʒuʁ kɔmɑ̃t ale vu/", "Hello, how are you?"),
                GeneratedSentence("J'aime apprendre des langues.", "Easy", "/ʒɛm apʁɑ̃dʁ de lɑ̃ɡ/", "I love learning languages."),
                GeneratedSentence("Passez une excellente journée!", "Easy", "/pase yn ɛksɛlɑ̃t ʒuʁne/", "Have a great day!"),
                GeneratedSentence("Ravi de vous rencontrer.", "Easy", "/ʁavi də vu ʁɑ̃kɔ̃tʁe/", "Nice to meet you.")
            )
            "german", "de" -> listOf(
                GeneratedSentence("Hallo, wie geht es dir?", "Easy", "/haˈloː viː ɡeːt ɛs diːɐ̯/", "Hello, how are you?"),
                GeneratedSentence("Ich lerne gerne Sprachen.", "Easy", "/ɪç ˈlɛʁnə ˈɡɛʁnə ˈʃpʁaːxn̩/", "I like learning languages."),
                GeneratedSentence("Einen schönen Tag noch!", "Easy", "/ˈaɪ̯nən ˈʃøːnən taːk nɔx/", "Have a nice day!"),
                GeneratedSentence("Schön dich kennenzulernen.", "Easy", "/ʃøːn dɪç ˈkɛnəntsuːˌlɛʁnən/", "Nice to meet you.")
            )
            "italian", "it" -> listOf(
                GeneratedSentence("Ciao, come stai oggi?", "Easy", "/ˈtʃa.o ˈko.me stai ˈod.dʒi/", "Hello, how are you today?"),
                GeneratedSentence("Mi piace imparare le lingue.", "Easy", "/mi ˈpja.tʃe im.paˈra.re le ˈliŋ.ɡwe/", "I like learning languages."),
                GeneratedSentence("Buona giornata a te!", "Easy", "/ˈbwɔ.na dʒorˈna.ta a te/", "Have a nice day!"),
                GeneratedSentence("Piacere di conoscerti.", "Easy", "/pjaˈtʃe.re di koˈno.ʃer.ti/", "Nice to meet you.")
            )
            "arabic", "ar" -> listOf(
                GeneratedSentence("مرحباً، كيف حالك اليوم؟", "Easy", "/marħaban kayfa ħaːluka l-yawm/", "Hello, how are you today?"),
                GeneratedSentence("أنا أحب تعلم اللغات.", "Easy", "/ʔanaː ʔuħibbu taʕalluma l-luɣaːt/", "I love learning languages."),
                GeneratedSentence("أتمنى لك يوماً سعيداً!", "Easy", "/ʔatamannaː laka yawman saʕiːdan/", "Have a nice day!"),
                GeneratedSentence("سعدت بلقائك كثيراً.", "Easy", "/suʕidtu biliqaːʔika kaθiːran/", "Nice to meet you.")
            )
            else -> fallbackSentences
        }
        return list.shuffled().take(count)
    }
}