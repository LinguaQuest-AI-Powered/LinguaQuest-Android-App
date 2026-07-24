package com.iti.linguaquest.features.roleplay.data.repository

import com.iti.linguaquest.core.cache.data.datasource.UserPreferencesLocalDataSource
import com.iti.linguaquest.features.roleplay.data.audio.AudioPlayer
import com.iti.linguaquest.features.roleplay.data.audio.AudioRecorder
import com.iti.linguaquest.features.roleplay.data.remote.GeminiRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.data.remote.LiveRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayAssessmentResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.json.JSONObject
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
    
    override suspend fun connectToFreePlay(targetLanguage: String) {
        val systemPrompt = """
            Persona: You are Lingo, a friendly native $targetLanguage language tutor. The user is an English speaker practicing conversational $targetLanguage at a A2 level. The scenario is ordering coffee in a cafe in Cairo.
            Rules: Keep sentences short and natural for spoken dialogue. Gently correct major grammatical mistakes, then continue the roleplay. 
            Guardrails: RESPOND UNMISTAKABLY IN $targetLanguage. 
            Initiation Command: To begin, greet the user immediately and ask what they would like to order.
        """.trimIndent()
        
        liveService.connect(systemPrompt)
        audioPlayer.start()
        listenForServerEvents()
    }
    
    override suspend fun connectToBossStage(scenario: BossScenario) {
        val targetLanguage = userPreferences.targetLanguageName.firstOrNull() ?: "English"
        val systemPrompt = """
            You are ${scenario.bossName}, ${scenario.roleDescription}. 
            Speak strictly in $targetLanguage.
        """.trimIndent()
        
        liveService.connect(systemPrompt)
        audioPlayer.start()
        listenForServerEvents()
    }

    override suspend fun evaluateBossStage(transcript: List<String>, scenario: BossScenario): Result<RoleplayAssessmentResult> {
        return try {
            val nativeLanguage = userPreferences.nativeLanguageName.firstOrNull() ?: "English"
            val jsonResponse = geminiService.evaluateBossStage(
                transcript = transcript,
                taskObjective = scenario.taskObjective,
                nativeLanguage = nativeLanguage
            )
            
            if (jsonResponse != null) {
                val jsonObject = JSONObject(jsonResponse)
                val result = RoleplayAssessmentResult(
                    isTaskCompleted = jsonObject.optBoolean("task_completed", false),
                    fluencyScore = jsonObject.optInt("fluency_score", 0),
                    feedbackMessage = jsonObject.optString("feedback_message", "No feedback provided.")
                )
                Result.success(result)
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
