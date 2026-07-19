package com.iti.linguaquest.features.voicechat.data.repository

import com.iti.linguaquest.features.voicechat.data.audio.AudioPlayer
import com.iti.linguaquest.features.voicechat.data.audio.AudioRecorder
import com.iti.linguaquest.features.voicechat.data.datasource.VoiceChatRemoteDataSource
import com.iti.linguaquest.features.voicechat.domain.model.VoiceChatEvent
import com.iti.linguaquest.features.voicechat.domain.repository.VoiceChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class VoiceChatRepositoryImpl @Inject constructor(
    private val remoteDataSource: VoiceChatRemoteDataSource,
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer
) : VoiceChatRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var recordingJob: Job? = null
    private val _events = MutableSharedFlow<VoiceChatEvent>()
    override val events: Flow<VoiceChatEvent> = _events

    override suspend fun connect() {
        remoteDataSource.connect()
        audioPlayer.start()
        listenForServerEvents()
    }

    override fun disconnect() {
        stopListening()
        audioPlayer.stop()
            scope.launch {
            remoteDataSource.close()
        }
    }

    override suspend fun sendText(text: String) {
        remoteDataSource.sendText(text)
    }

    override fun startListening() {
        recordingJob = scope.launch {
            audioRecorder.start().collect { chunk ->
                remoteDataSource.sendAudioChunk(chunk)
            }
        }
    }

    override fun stopListening() {
        audioRecorder.stop()
        recordingJob?.cancel()
    }

    private fun listenForServerEvents() {
        scope.launch {
            remoteDataSource.observeServerEvents().collect { event ->
                if (event is VoiceChatEvent.AudioChunk) {
                    audioPlayer.write(event.pcmData)
                }
                _events.emit(event)
            }
        }
    }
}