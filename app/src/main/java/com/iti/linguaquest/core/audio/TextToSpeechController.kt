package com.iti.linguaquest.core.audio

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextToSpeechController @Inject constructor(
    @ApplicationContext context: Context
) {
    private var tts: TextToSpeech? = null
    private var isReady = false
    private val completionCallbacks = ConcurrentHashMap<String, () -> Unit>()

    init {
        tts = TextToSpeech(context) { status ->
            isReady = status == TextToSpeech.SUCCESS
            if (isReady) {
                tts?.language = Locale.US
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        utteranceId?.let { id ->
                            completionCallbacks.remove(id)?.invoke()
                        }
                    }
                    override fun onError(utteranceId: String?) {
                        utteranceId?.let { id ->
                            completionCallbacks.remove(id)?.invoke()
                        }
                    }
                })
            }
        }
    }

    fun speak(
        text: String,
        language: String? = null,
        onDone: (() -> Unit)? = null
    ) {
        if (!isReady || tts == null) {
            onDone?.invoke()
            return
        }

        if (!language.isNullOrBlank()) {
            val locale = getLocaleForLanguage(language)
            tts?.language = locale
        }

        val utteranceId = UUID.randomUUID().toString()
        if (onDone != null) {
            completionCallbacks[utteranceId] = onDone
        }

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun stop() {
        completionCallbacks.clear()
        if (isReady) tts?.stop()
    }

    private fun getLocaleForLanguage(language: String): Locale {
        return when (language.trim().lowercase(Locale.ROOT)) {
            "arabic", "ar", "العربية" -> Locale.forLanguageTag("ar")
            "french", "français", "fr" -> Locale.FRENCH
            "spanish", "español", "es" -> Locale.forLanguageTag("es")
            "german", "deutsch", "de" -> Locale.GERMAN
            "italian", "italiano", "it" -> Locale.ITALIAN
            "japanese", "日本語", "ja" -> Locale.JAPANESE
            "chinese", "中文", "zh" -> Locale.CHINESE
            "korean", "한국어", "ko" -> Locale.KOREAN
            "portuguese", "português", "pt" -> Locale.forLanguageTag("pt")
            "english", "en" -> Locale.ENGLISH
            else -> Locale.forLanguageTag(language)
        }
    }
}