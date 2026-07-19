package com.iti.linguaquest.features.voicechat.data.datasource

import android.util.Log
import com.google.firebase.ai.LiveGenerativeModel
import com.google.firebase.ai.type.InlineData
import com.google.firebase.ai.type.InlineDataPart
import com.google.firebase.ai.type.LiveServerContent
import com.google.firebase.ai.type.LiveServerGoAway
import com.google.firebase.ai.type.LiveSession
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.auth.FirebaseAuth
import com.iti.linguaquest.features.voicechat.domain.model.VoiceChatEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(PublicPreviewAPI::class)
class VoiceChatRemoteDataSourceImpl @Inject constructor(
    private val liveModel: LiveGenerativeModel
) : VoiceChatRemoteDataSource {

    private var session: LiveSession? = null

    override suspend fun connect() {

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }
        session = liveModel.connect()
    }

    override suspend fun close() {
        session?.close()
        session = null
    }

    override suspend fun sendText(text: String) {
        try {
            session?.send(
                text = text,
                turnComplete = true
            )
        } catch (e: Exception) {
            Log.e("VoiceChat", "sendText() failed", e)
        }
    }

    override suspend fun sendAudioChunk(chunk: ByteArray) {


        session?.sendAudioRealtime(
            InlineData(
                data = chunk,
                mimeType = "audio/pcm;rate=16000"
            )
        )
    }

    override fun observeServerEvents(): Flow<VoiceChatEvent> {

        val currentSession = session ?: run {
            return emptyFlow()
        }

        return currentSession
            .receive()
            .transform { serverMessage ->


                when (serverMessage) {

                    is LiveServerContent -> {

                        serverMessage.outputTranscription
                            ?.text
                            ?.let {
                                emit(VoiceChatEvent.TextChunk(it))
                            }

                        serverMessage.content
                            ?.parts
                            ?.forEach { part ->
                                if (part is InlineDataPart) {
                                    emit(
                                        VoiceChatEvent.AudioChunk(
                                            part.inlineData
                                        )
                                    )
                                }
                            }

                        if (serverMessage.turnComplete) {
                            emit(VoiceChatEvent.TurnComplete)
                        }
                    }

                    is LiveServerGoAway -> {
                        emit(
                            VoiceChatEvent.Error(
                                "Server ended the session (GoAway)"
                            )
                        )
                    }

                    else -> Unit
                }
            }
            .catch { e ->
                emit(
                    VoiceChatEvent.Error(
                        e.message ?: "Unknown Connection Error"
                    )
                )
            }
    }
}