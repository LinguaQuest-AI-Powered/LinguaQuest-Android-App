package com.iti.linguaquest.features.roleplay.data.repository

import com.iti.linguaquest.features.roleplay.data.audio.AudioPlayer
import com.iti.linguaquest.features.roleplay.data.audio.AudioRecorder
import com.iti.linguaquest.features.roleplay.data.remote.LiveRoleplayService
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoleplayRepositoryImpl @Inject constructor(
    private val liveService: LiveRoleplayService,
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer
) : RoleplayRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var recordingJob: Job? = null
    
    private val _events = MutableSharedFlow<RoleplayLiveEvent>()
    override val events: Flow<RoleplayLiveEvent> = _events

    override suspend fun connect(systemPrompt: String) {
        liveService.connect(systemPrompt)
        audioPlayer.start()
        listenForServerEvents()
    }
    
    override fun startMicrophone() {
        recordingJob = scope.launch {
            audioRecorder.startRecording().collect { chunk ->
                liveService.sendAudioChunk(chunk)
            }
        }
    }
    
    override fun stopMicrophone() {
        audioRecorder.stopRecording()
        recordingJob?.cancel()
    }

    override suspend fun disconnect() {
        stopMicrophone()
        audioPlayer.stop()
        liveService.close()
    }

    private fun listenForServerEvents() {
        scope.launch {
            liveService.observeServerEvents().collect { event ->
                if (event is RoleplayLiveEvent.AudioChunk) {
                    audioPlayer.write(event.bytes)
                }
                _events.emit(event)
            }
        }
    }
}
