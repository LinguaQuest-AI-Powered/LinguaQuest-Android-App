package com.iti.linguaquest.core.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class SpeechManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var isReady = false
    private var queuedWord: String? = null
    private var queuedLang: String? = null

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isReady = true
                queuedWord?.let { word ->
                    queuedLang?.let { lang ->
                        speak(word, lang)
                    }
                }
            }
        }
    }

    fun speak(word: String, languageCode: String) {
        if (isReady) {
            val locale = when (languageCode.trim().lowercase()) {
                "arabic", "ar", "العربية" -> Locale.forLanguageTag("ar")
                "spanish", "español", "es", "الإسبانية", "الاسبانية" -> Locale.forLanguageTag("es")
                "japanese", "日本語", "ja", "اليابانية" -> Locale.JAPANESE
                "german", "deutsch", "de", "الألمانية", "الالمانية" -> Locale.GERMAN
                "french", "français", "fr", "الفرنسية" -> Locale.FRENCH
                "chinese", "中文", "zh", "الصينية" -> Locale.CHINESE
                "italian", "italiano", "it", "الإيطالية", "الايطالية" -> Locale.ITALIAN
                "portuguese", "português", "pt", "البرتغالية" -> Locale.forLanguageTag("pt")
                "korean", "한국어", "ko", "الكورية" -> Locale.KOREAN
                "english", "en", "الإنجليزية", "الانجليزية" -> Locale.ENGLISH
                else -> Locale.forLanguageTag(languageCode)
            }
            tts?.language = locale
            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
            // Clear queue
            queuedWord = null
            queuedLang = null
        } else {
            // Queue it to play as soon as the TTS engine is ready
            queuedWord = word
            queuedLang = languageCode
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
