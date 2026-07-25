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
    
    private val _events = MutableSharedFlow<RoleplayLiveEvent>()
    override val events: Flow<RoleplayLiveEvent> = _events

    override suspend fun connect(systemPrompt: String) {
        liveService.connect(systemPrompt)
        audioPlayer.start()
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
        
        liveService.connect(systemPrompt)
        audioPlayer.start()
        listenForServerEvents()
    }

    override suspend fun evaluateBossStage(transcript: List<String>, scenario: BossScenario): Result<BossEvaluationResult> {
        return try {
            val nativeLanguage = userPreferences.nativeLanguageName.firstOrNull() ?: "English"
            val evaluationResult = geminiService.evaluateBossStage(
                transcript = transcript,
                taskObjective = scenario.objective,
                nativeLanguage = nativeLanguage
            )
            
            if (evaluationResult != null) {
                Result.success(evaluationResult)
            } else {
                Result.failure(Exception("Failed to generate assessment JSON from Gemini"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
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
