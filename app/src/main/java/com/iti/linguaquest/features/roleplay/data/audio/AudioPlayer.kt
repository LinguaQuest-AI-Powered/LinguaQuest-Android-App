package com.iti.linguaquest.features.roleplay.data.audio

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private var tempFile: File? = null

    init {
        tts = TextToSpeech(context) { status ->
            isTtsInitialized = status == TextToSpeech.SUCCESS
        }
    }

    suspend fun playAudio(bytes: ByteArray?, textToSpeak: String, targetLanguage: String, onCompletion: () -> Unit) {
        withContext(Dispatchers.Main) {
            if (bytes != null && bytes.isNotEmpty()) {
                playBytes(bytes, onCompletion)
            } else {
                playTts(textToSpeak, targetLanguage, onCompletion)
            }
        }
    }

    private fun playBytes(bytes: ByteArray, onCompletion: () -> Unit) {
        try {
            tempFile = File.createTempFile("roleplay_response", ".mp3", context.cacheDir)
            FileOutputStream(tempFile).use { it.write(bytes) }

            mediaPlayer = MediaPlayer().apply {
                setDataSource(tempFile!!.absolutePath)
                setOnCompletionListener { 
                    it.release()
                    mediaPlayer = null
                    tempFile?.delete()
                    onCompletion()
                }
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onCompletion()
        }
    }

    private fun playTts(text: String, targetLanguage: String, onCompletion: () -> Unit) {
        if (!isTtsInitialized || tts == null) {
            onCompletion()
            return
        }

        val locale = when (targetLanguage.lowercase(Locale.ROOT)) {
            "spanish" -> Locale("es", "ES")
            "french" -> Locale.FRANCE
            "german" -> Locale.GERMANY
            "italian" -> Locale.ITALY
            "japanese" -> Locale.JAPAN
            "korean" -> Locale.KOREA
            "chinese" -> Locale.CHINA
            "arabic" -> Locale("ar")
            else -> Locale.ENGLISH
        }
        
        tts?.language = locale

        val utteranceId = "ROLEPLAY_TTS_${System.currentTimeMillis()}"
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) {
                onCompletion()
            }
            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                onCompletion()
            }
        })

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }
}
