package com.iti.linguaquest.features.roleplay.data.remote

import com.google.firebase.Firebase
import com.google.firebase.ai.LiveGenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.InlineData
import com.google.firebase.ai.type.InlineDataPart
import com.google.firebase.ai.type.LiveServerContent
import com.google.firebase.ai.type.LiveServerGoAway
import com.google.firebase.ai.type.LiveSession
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.auth.FirebaseAuth
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(PublicPreviewAPI::class)
@Singleton
class LiveRoleplayService @Inject constructor() : LiveRoleplayRemoteDataSource {

    private var session: LiveSession? = null

    override suspend fun connect(systemPrompt: String) {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) auth.signInAnonymously().await()
        
        val liveModel = Firebase.ai(backend = GenerativeBackend.googleAI()).liveModel(
            modelName = "gemini-2.5-flash-native-audio-preview-12-2025",
            systemInstruction = com.google.firebase.ai.type.content { text(systemPrompt) },
            generationConfig = com.google.firebase.ai.type.liveGenerationConfig {
                responseModality = com.google.firebase.ai.type.ResponseModality.AUDIO
            }
        )
        session = liveModel.connect()
    }

    override suspend fun sendAudioChunk(chunk: ByteArray) {
        session?.sendAudioRealtime(
            InlineData(data = chunk, mimeType = "audio/pcm;rate=16000")
        )
    }

    override fun observeServerEvents(): Flow<RoleplayLiveEvent> {
        val currentSession = session ?: return emptyFlow()

        return currentSession.receive().transform { serverMessage ->
            when (serverMessage) {
                is LiveServerContent -> {
                    serverMessage.outputTranscription?.text?.let { emit(RoleplayLiveEvent.Transcription(it)) }
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
        }
    }

    override suspend fun close() {
        session?.close()
        session = null
    }
}
