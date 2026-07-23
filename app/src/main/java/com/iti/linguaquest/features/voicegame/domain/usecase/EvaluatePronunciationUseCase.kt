package com.iti.linguaquest.features.voicegame.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.domain.model.VoiceEvaluation
import com.iti.linguaquest.features.voicegame.domain.repository.VoiceGameRepository
import javax.inject.Inject

class EvaluatePronunciationUseCase @Inject constructor(
    private val repository: VoiceGameRepository
) {
    suspend operator fun invoke(
        targetSentence: String,
        targetLanguage: String,
        audioBytes: ByteArray
    ): LinguaQuestResult<VoiceEvaluation, LinguaQuestDataError> {
        return repository.evaluatePronunciation(targetSentence, targetLanguage, audioBytes)
    }
}
