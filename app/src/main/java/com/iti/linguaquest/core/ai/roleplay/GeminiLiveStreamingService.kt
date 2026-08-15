package com.iti.linguaquest.core.ai.roleplay

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.AudioTranscriptionConfig
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.InlineData
import com.google.firebase.ai.type.InlineDataPart
import com.google.firebase.ai.type.LiveServerContent
import com.google.firebase.ai.type.LiveServerGoAway
import com.google.firebase.ai.type.LiveSession
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.SpeechConfig
import com.google.firebase.ai.type.Voice
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.liveGenerationConfig
import com.google.firebase.auth.FirebaseAuth
import com.iti.linguaquest.features.roleplay.data.audio.AudioPlayer
import com.iti.linguaquest.features.roleplay.data.audio.AudioRecorder
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.CancellationException as ConcurrentCancellationException
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(PublicPreviewAPI::class)
@Singleton
class GeminiLiveStreamingService @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer
) : LiveRoleplayRemoteDataSource {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var session: LiveSession? = null
    private var sendChunkCount = 0
    private var recordingJob: Job? = null
    private var listeningJob: Job? = null
    private val _events = MutableSharedFlow<RoleplayLiveEvent>()

    override fun observeServerEvents(): Flow<RoleplayLiveEvent> = _events

    override suspend fun connect(systemPrompt: String, voiceName: String, targetLanguage: String) {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) auth.signInAnonymously().await()

        val liveModel = Firebase.ai(backend = GenerativeBackend.googleAI()).liveModel(
            modelName = "gemini-2.5-flash-native-audio-preview-12-2025",
            systemInstruction = content { text(systemPrompt) },
            generationConfig = liveGenerationConfig {
                responseModality = ResponseModality.AUDIO
                inputAudioTranscription = AudioTranscriptionConfig()
                outputAudioTranscription = AudioTranscriptionConfig()
                speechConfig = SpeechConfig(voice = Voice(voiceName))
            }
        )
        session = liveModel.connect()
        sendChunkCount = 0

        audioPlayer.start()
        startContinuousRecording()
        listenForServerEvents()
    }

    override suspend fun sendAudioChunk(chunk: ByteArray) {
        try {
            session?.sendAudioRealtime(
                InlineData(data = chunk, mimeType = "audio/pcm;rate=16000")
            )
            sendChunkCount++
        } catch (e: ConcurrentCancellationException) {
        } catch (e: CancellationException) {
        } catch (e: Exception) {
            Timber.e(e, "[LiveService] Failed to send audio chunk #%d", sendChunkCount)
        }
    }

    override fun startMicrophone() {
        audioRecorder.resumeSending()
    }

    override fun stopMicrophone() {
        audioRecorder.pauseSending()
        sendSilenceTail()
    }

    override suspend fun close() {
        audioRecorder.pauseSending()
        recordingJob?.cancel()
        recordingJob = null
        audioRecorder.stopRecording()
        listeningJob?.cancel()
        listeningJob = null
        audioPlayer.stop()
        session?.close()
        session = null
    }

    private fun sendSilenceTail() {
        scope.launch {
            val silenceDurationMs = 1000
            val sampleRate = 16000
            val bytesPerSample = 2
            val totalBytes = sampleRate * bytesPerSample * silenceDurationMs / 1000
            val chunkSize = 3200
            val silenceChunk = ByteArray(chunkSize)
            var sent = 0

            while (sent < totalBytes) {
                val remaining = totalBytes - sent
                val currentChunkSize = minOf(chunkSize, remaining)
                val chunk = if (currentChunkSize == chunkSize) silenceChunk else ByteArray(currentChunkSize)
                sendAudioChunk(chunk)
                sent += currentChunkSize
            }
        }
    }

    private fun startContinuousRecording() {
        recordingJob?.cancel()
        recordingJob = scope.launch {
            audioRecorder.startRecording().collect { chunk ->
                sendAudioChunk(chunk)
            }
        }
    }

    private fun listenForServerEvents() {
        listeningJob?.cancel()
        listeningJob = scope.launch {
            val currentSession = session ?: return@launch
            currentSession.receive().transform { serverMessage ->
                when (serverMessage) {
                    is LiveServerContent -> {
                        serverMessage.inputTranscription?.text?.let { emit(RoleplayLiveEvent.Transcription(it, isUser = true)) }
                        serverMessage.outputTranscription?.text?.let { emit(RoleplayLiveEvent.Transcription(it, isUser = false)) }
                        serverMessage.content?.parts?.forEach { part ->
                            if (part is InlineDataPart) emit(RoleplayLiveEvent.AudioChunk(part.inlineData))
                        }
                        if (serverMessage.turnComplete) emit(RoleplayLiveEvent.TurnComplete)
                    }
                    is LiveServerGoAway -> emit(RoleplayLiveEvent.Error("Server ended the session (GoAway)"))
                    else -> Unit
                }
            }.catch { e ->
                emit(RoleplayLiveEvent.Error(e.message ?: "Unknown Connection Error"))
            }.collect { event ->
                if (event is RoleplayLiveEvent.AudioChunk) {
                    audioPlayer.write(event.bytes)
                }
                _events.emit(event)
            }
        }
    }
}
