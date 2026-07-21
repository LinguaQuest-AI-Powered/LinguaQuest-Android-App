package com.iti.linguaquest.core.audio


import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextToSpeechController @Inject constructor(
    @ApplicationContext context: Context
) {
    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context) { status ->
            isReady = status == TextToSpeech.SUCCESS
            if (isReady) tts?.language = Locale.US
        }
    }

    fun speak(text: String) {
        if (isReady) tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "voicegame_sentence")
    }
}