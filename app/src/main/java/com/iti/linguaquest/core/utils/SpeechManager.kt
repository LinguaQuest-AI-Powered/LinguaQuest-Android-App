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
                "arabic", "ar" -> Locale.forLanguageTag("ar")
                "spanish", "español", "es" -> Locale.forLanguageTag("es")
                "japanese", "日本語", "ja" -> Locale.JAPANESE
                "german", "deutsch", "de" -> Locale.GERMAN
                "french", "français", "fr" -> Locale.FRENCH
                "chinese", "中文", "zh" -> Locale.CHINESE
                "italian", "italiano", "it" -> Locale.ITALIAN
                "portuguese", "português", "pt" -> Locale.forLanguageTag("pt")
                "korean", "한국어", "ko" -> Locale.KOREAN
                "english", "en" -> Locale.ENGLISH
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
