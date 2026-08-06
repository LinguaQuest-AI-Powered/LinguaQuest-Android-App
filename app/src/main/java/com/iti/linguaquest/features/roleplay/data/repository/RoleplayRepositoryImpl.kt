package com.iti.linguaquest.features.roleplay.data.repository

import com.iti.linguaquest.core.cache.data.datasource.UserPreferencesLocalDataSource
import com.iti.linguaquest.features.roleplay.data.audio.AudioPlayer
import com.iti.linguaquest.features.roleplay.data.audio.AudioRecorder
import com.iti.linguaquest.features.roleplay.data.datasource.remote.GeminiRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.data.datasource.remote.LiveRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import com.iti.linguaquest.features.roleplay.domain.prompt.PromptFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoleplayRepositoryImpl @Inject constructor(
    private val liveService: LiveRoleplayRemoteDataSource,
    private val geminiService: GeminiRoleplayRemoteDataSource,
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer,
    private val userPreferences: UserPreferencesLocalDataSource
) : RoleplayRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var recordingJob: Job? = null
    private var listeningJob: Job? = null
    
    private val _events = MutableSharedFlow<RoleplayLiveEvent>()
    override val events: Flow<RoleplayLiveEvent> = _events

    override suspend fun connect(systemPrompt: String, voiceName: String) {

        liveService.connect(systemPrompt, voiceName)
        audioPlayer.start()
        startContinuousRecording()
        listenForServerEvents()

    }

    override suspend fun connectToBossStage(scenario: BossScenario) {
        val targetLanguage = userPreferences.targetLanguageName.firstOrNull() ?: "English"
        val systemPrompt = PromptFactory.createLiveSessionPrompt(
            bossName = scenario.bossName,
            roleDescription = scenario.roleDescription,
            objective = scenario.objective,
            targetLanguage = targetLanguage
        )
        
        liveService.connect(systemPrompt, scenario.voiceName)
        audioPlayer.start()
        startContinuousRecording()
        listenForServerEvents()

    }

    override suspend fun evaluateBossStage(transcript: List<String>, scenario: BossScenario): Result<BossEvaluationResult> {

        return try {
            val nativeLanguage = userPreferences.nativeLanguageName.firstOrNull() ?: "English"
            val targetLanguage = userPreferences.targetLanguageName.firstOrNull() ?: "English"
            val evaluationResult = geminiService.evaluateBossStage(
                transcript = transcript,
                taskObjective = scenario.objective,
                nativeLanguage = nativeLanguage,
                targetLanguage = targetLanguage
            )
            
            if (evaluationResult != null) {

                Result.success(evaluationResult)
            } else {
                Timber.w("[Repo] evaluateBossStage() — null result from Gemini")
                Result.failure(IllegalStateException("Failed to generate assessment JSON from Gemini"))
            }
        } catch (e: Exception) {
            Timber.e(e, "[Repo] evaluateBossStage() failed")
            Result.failure(e)
        }
    }
    
    override fun startMicrophone() {

        audioRecorder.resumeSending()
    }
    
    override fun stopMicrophone() {

        audioRecorder.pauseSending()
        sendSilenceTail()
    }

    override suspend fun disconnect() {

        audioRecorder.pauseSending()
        recordingJob?.cancel()
        recordingJob = null
        audioRecorder.stopRecording()
        listeningJob?.cancel()
        listeningJob = null
        audioPlayer.stop()
        liveService.close()

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
                liveService.sendAudioChunk(chunk)
                sent += currentChunkSize
            }

        }
    }

    private fun startContinuousRecording() {
        recordingJob?.cancel()
        recordingJob = scope.launch {

            audioRecorder.startRecording().collect { chunk ->
                liveService.sendAudioChunk(chunk)
            }

        }
    }

    private fun listenForServerEvents() {
        listeningJob?.cancel()
        listeningJob = scope.launch {

            liveService.observeServerEvents().collect { event ->
                when (event) {
                    is RoleplayLiveEvent.AudioChunk -> audioPlayer.write(event.bytes)
                    is RoleplayLiveEvent.Transcription -> {}
                    is RoleplayLiveEvent.TurnComplete -> {}
                    is RoleplayLiveEvent.Error -> Timber.e("[Repo] Live error: %s", event.message)
                }
                _events.emit(event)
            }
        }
    }
}


