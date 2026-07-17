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
        tts = TextToSpeech(context) { status ->
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
            tts?.language = Locale.forLanguageTag(languageCode)
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
