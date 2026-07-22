package com.iti.linguaquest.features.voicegame.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.data.remote.VoiceEvaluationService
import com.iti.linguaquest.features.voicegame.domain.model.VoiceEvaluation
import com.iti.linguaquest.features.voicegame.domain.repository.VoiceGameRepository
import javax.inject.Inject

class VoiceGameRepositoryImpl @Inject constructor(
    private val service: VoiceEvaluationService
) : VoiceGameRepository {

    override suspend fun evaluatePronunciation(
        targetSentence: String,
        targetLanguage: String,
        audioBytes: ByteArray
    ): LinguaQuestResult<VoiceEvaluation, LinguaQuestDataError> {
        return try {
            val response = service.evaluatePronunciation(targetSentence, targetLanguage, audioBytes)
            val domainModel = VoiceEvaluation(
                rating = response.rating,
                correctWords = response.correctWords,
                wrongWords = response.wrongWords,
                advice = response.advice
            )
            LinguaQuestResult.Success(domainModel)
        } catch (e: Exception) {
            LinguaQuestResult.Failure(
                LinguaQuestDataError.CustomServerMessage(
                    e.message ?: "Voice evaluation failed"
                )
            )
        }
    }
}
