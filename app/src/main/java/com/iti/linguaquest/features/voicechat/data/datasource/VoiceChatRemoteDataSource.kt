package com.iti.linguaquest.features.voicechat.data.datasource

import com.google.firebase.ai.LiveGenerativeModel
import com.google.firebase.ai.type.InlineData
import com.google.firebase.ai.type.InlineDataPart
import com.google.firebase.ai.type.LiveServerContent
import com.google.firebase.ai.type.LiveSession
import com.google.firebase.ai.type.PublicPreviewAPI
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicechat.domain.model.VoiceChatResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface VoiceChatRemoteDataSource {
    suspend fun connect(systemInstructions: String): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun sendAudioChunk(audioBytes: ByteArray): LinguaQuestResult<Unit, LinguaQuestDataError>
    fun receiveChatStream(): Flow<VoiceChatResponse>
    suspend fun disconnect(): LinguaQuestResult<Unit, LinguaQuestDataError>
}

@OptIn(PublicPreviewAPI::class)
class VoiceChatRemoteDataSourceImpl @Inject constructor(
    private val liveModel: LiveGenerativeModel
) : VoiceChatRemoteDataSource {

    companion object {
        private const val TAG = "VoiceChatRemoteDS"
        private const val AUDIO_MIME_TYPE = "audio/pcm;rate=16000"
    }

    private var session: LiveSession? = null
    private var receiveScope: CoroutineScope? = null

    private val _chatStream = MutableSharedFlow<VoiceChatResponse>(extraBufferCapacity = 64)

    override suspend fun connect(systemInstructions: String): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            withContext(Dispatchers.IO) {
                session?.close()
                receiveScope?.cancel()

                val newSession = liveModel.connect()
                session = newSession

                val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
                receiveScope = scope
                scope.launch {
                    receiveLoop(newSession)
                }

                LinguaQuestResult.Success(Unit)
            }
        } catch (e: Exception) {
            LinguaQuestResult.Failure(
                LinguaQuestDataError.CustomServerMessage(
                    e.message ?: "Failed to connect to voice chat"
                )
            )
        }
    }

    override suspend fun sendAudioChunk(audioBytes: ByteArray): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            val currentSession = session
                ?: return LinguaQuestResult.Failure(
                    LinguaQuestDataError.CustomServerMessage("Session not connected")
                )

            withContext(Dispatchers.IO) {
                val audioData = InlineData(audioBytes, AUDIO_MIME_TYPE)
                currentSession.sendAudioRealtime(audioData)
            }
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
            LinguaQuestResult.Failure(
                LinguaQuestDataError.CustomServerMessage(
                    e.message ?: "Failed to send audio"
                )
            )
        }
    }

    override fun receiveChatStream(): Flow<VoiceChatResponse> {
        return _chatStream.asSharedFlow()
    }

    override suspend fun disconnect(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            withContext(Dispatchers.IO) {
                receiveScope?.cancel()
                receiveScope = null
                session?.close()
                session = null
                LinguaQuestResult.Success(Unit)
            }
        } catch (e: Exception) {
            receiveScope = null
            session = null
            LinguaQuestResult.Failure(
                LinguaQuestDataError.CustomServerMessage(
                    e.message ?: "Failed to disconnect"
                )
            )
        }
    }

    /**
     * Continuously receives messages from the Live API session and emits
     * them as [VoiceChatResponse] objects into the shared flow.
     */
    private suspend fun receiveLoop(liveSession: LiveSession) {
        try {
            liveSession.receive().collect { message ->
                // Only process LiveServerContent messages (contains audio/text/turn info)
                if (message is LiveServerContent) {
                    val serverContent = message

                    // Extract transcription text (model's output transcription)
                    val transcriptionText = serverContent.outputTranscription?.text

                    // Extract audio data from the content parts
                    var audioData: ByteArray? = null
                    serverContent.content?.parts?.forEach { part ->
                        if (part is InlineDataPart) {
                            audioData = part.inlineData
                        }
                    }

                    // Check if this is the end of the model's turn
                    val isEndOfTurn = serverContent.turnComplete

                    // Only emit if there's something meaningful
                    if (transcriptionText != null || audioData != null || isEndOfTurn) {
                        _chatStream.emit(
                            VoiceChatResponse(
                                text = transcriptionText,
                                audioBytes = audioData,
                                isEndOfTurn = isEndOfTurn
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            _chatStream.emit(
                VoiceChatResponse(
                    text = null,
                    audioBytes = null,
                    isEndOfTurn = true
                )
            )
        }
    }
}
