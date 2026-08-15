package com.iti.linguaquest.core.ai.roleplay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.iti.linguaquest.core.ai.client.AiClient
import com.iti.linguaquest.core.audio.TextToSpeechController
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepSeekWalkieTalkieService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val aiClient: AiClient,
    private val textToSpeechController: TextToSpeechController
) : LiveRoleplayRemoteDataSource {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mainHandler = Handler(Looper.getMainLooper())

    private val _events = MutableSharedFlow<RoleplayLiveEvent>()
    override fun observeServerEvents(): Flow<RoleplayLiveEvent> = _events

    private val conversationHistory = mutableListOf<Pair<String, String>>()
    private var activeSystemPrompt: String = ""
    private var activeLanguage: String = "English"

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening: Boolean = false

    override suspend fun connect(systemPrompt: String, voiceName: String, targetLanguage: String) {
        activeSystemPrompt = systemPrompt
        activeLanguage = targetLanguage
        conversationHistory.clear()

        mainHandler.post {
            ensureSpeechRecognizer()
        }
    }

    override suspend fun sendAudioChunk(chunk: ByteArray) {}

    override fun startMicrophone() {
        mainHandler.post {
            ensureSpeechRecognizer()
            if (!isListening && speechRecognizer != null) {
                try {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, getLanguageTag(activeLanguage))
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, getLanguageTag(activeLanguage))
                        putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, getLanguageTag(activeLanguage))
                        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
                    }
                    isListening = true
                    speechRecognizer?.startListening(intent)
                    Timber.d("SpeechRecognizer startListening initiated for language: %s", activeLanguage)
                } catch (e: Exception) {
                    isListening = false
                    Timber.e(e, "Failed to start speech recognition")
                }
            }
        }
    }

    override fun stopMicrophone() {
        mainHandler.post {
            if (isListening) {
                isListening = false
                try {
                    speechRecognizer?.stopListening()
                    Timber.d("SpeechRecognizer stopListening called")
                } catch (e: Exception) {
                    Timber.e(e, "Failed to stop speech recognition")
                }
            }
        }
    }

    override suspend fun close() {
        mainHandler.post {
            try {
                speechRecognizer?.destroy()
            } catch (e: Exception) {
                Timber.e(e, "Failed to destroy speech recognizer")
            }
            speechRecognizer = null
            isListening = false
        }
        textToSpeechController.stop()
        conversationHistory.clear()
    }

    private fun ensureSpeechRecognizer() {
        if (speechRecognizer == null) {
            try {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(createRecognitionListener())
                }
                Timber.d("SpeechRecognizer created successfully")
            } catch (e: Exception) {
                Timber.e(e, "Failed to create SpeechRecognizer")
            }
        }
    }

    private fun createRecognitionListener(): RecognitionListener {
        return object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Timber.d("SpeechRecognizer: onReadyForSpeech")
            }
            override fun onBeginningOfSpeech() {
                Timber.d("SpeechRecognizer: onBeginningOfSpeech")
            }
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                Timber.d("SpeechRecognizer: onEndOfSpeech")
                isListening = false
            }

            override fun onError(error: Int) {
                isListening = false
                Timber.w("SpeechRecognizer onError: %d", error)
            }

            override fun onResults(results: Bundle?) {
                isListening = false
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val userText = matches?.firstOrNull()?.trim()
                Timber.d("SpeechRecognizer onResults: %s", userText)
                if (!userText.isNullOrBlank()) {
                    scope.launch {
                        handleUserTurn(userText)
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                Timber.d("SpeechRecognizer onPartialResults: %s", matches?.firstOrNull())
            }
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    private suspend fun handleUserTurn(userText: String) {
        _events.emit(RoleplayLiveEvent.Transcription(userText, isUser = true))
        conversationHistory.add("User" to userText)

        val promptBuilder = StringBuilder()
        promptBuilder.append(activeSystemPrompt).append("\n\nDialogue History:\n")
        conversationHistory.forEach { (speaker, text) ->
            promptBuilder.append("$speaker: $text\n")
        }
        promptBuilder.append("Boss: ")

        try {
            Timber.d("DeepSeek sending turn prompt: %s", promptBuilder.toString())
            val rawReply = aiClient.generateText(promptBuilder.toString(), temperature = 0.7f)
            val bossReply = rawReply
                ?.removePrefix("Boss:")
                ?.removePrefix("AI:")
                ?.removePrefix("Assistant:")
                ?.trim()

            Timber.d("DeepSeek received turn reply: %s", bossReply)
            if (!bossReply.isNullOrBlank()) {
                conversationHistory.add("Boss" to bossReply)
                _events.emit(RoleplayLiveEvent.Transcription(bossReply, isUser = false))

                textToSpeechController.speak(bossReply, activeLanguage) {
                    scope.launch {
                        _events.emit(RoleplayLiveEvent.TurnComplete)
                    }
                }
            } else {
                _events.emit(RoleplayLiveEvent.Error("No response received from model"))
            }
        } catch (e: Exception) {
            Timber.e(e, "DeepSeekWalkieTalkie turn generation failed")
            _events.emit(RoleplayLiveEvent.Error(e.message ?: "Failed to generate turn"))
        }
    }

    private fun getLanguageTag(language: String): String {
        return when (language.trim().lowercase(Locale.ROOT)) {
            "arabic", "ar", "العربية" -> "ar-SA"
            "french", "français", "fr" -> "fr-FR"
            "spanish", "español", "es" -> "es-ES"
            "german", "deutsch", "de" -> "de-DE"
            "italian", "italiano", "it" -> "it-IT"
            "japanese", "日本語", "ja" -> "ja-JP"
            "chinese", "中文", "zh" -> "zh-CN"
            "korean", "한국어", "ko" -> "ko-KR"
            "portuguese", "português", "pt" -> "pt-PT"
            "english", "en" -> "en-US"
            else -> language
        }
    }
}
