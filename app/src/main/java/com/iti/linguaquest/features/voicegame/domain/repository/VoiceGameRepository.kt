package com.iti.linguaquest.features.voicegame.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.domain.model.VoiceEvaluation

interface VoiceGameRepository {
    suspend fun evaluatePronunciation(
        targetSentence: String,
        targetLanguage: String,
        audioBytes: ByteArray
    ): LinguaQuestResult<VoiceEvaluation, LinguaQuestDataError>
}
