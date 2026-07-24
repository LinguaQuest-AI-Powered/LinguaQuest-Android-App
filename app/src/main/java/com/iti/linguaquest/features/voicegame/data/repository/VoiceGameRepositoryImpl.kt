package com.iti.linguaquest.features.voicegame.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.data.remote.PronunciationSentenceGeneratorService
import com.iti.linguaquest.features.voicegame.data.remote.VoiceEvaluationService
import com.iti.linguaquest.features.voicegame.domain.model.PronunciationSentence
import com.iti.linguaquest.features.voicegame.domain.model.VoiceEvaluation
import com.iti.linguaquest.features.voicegame.domain.repository.VoiceGameRepository
import javax.inject.Inject

class VoiceGameRepositoryImpl @Inject constructor(
    private val service: VoiceEvaluationService,
    private val generatorService: PronunciationSentenceGeneratorService
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

    override suspend fun generatePronunciationSentence(
        targetLanguage: String,
        level: String,
        topic: String
    ): LinguaQuestResult<PronunciationSentence, LinguaQuestDataError> {
        return try {
            val sentences = generatorService.generateSentences(targetLanguage, level, topic, count = 1)
            val generated = sentences.shuffled().firstOrNull()
                ?: throw IllegalStateException("No sentence generated from AI")
            LinguaQuestResult.Success(
                PronunciationSentence(
                    sentence = generated.sentence,
                    difficulty = generated.difficulty,
                    phonetic = generated.phonetic,
                    translation = generated.translation
                )
            )
        } catch (e: Exception) {
            LinguaQuestResult.Failure(
                LinguaQuestDataError.CustomServerMessage(
                    e.message ?: "Failed to generate sentence"
                )
            )
        }
    }
}
