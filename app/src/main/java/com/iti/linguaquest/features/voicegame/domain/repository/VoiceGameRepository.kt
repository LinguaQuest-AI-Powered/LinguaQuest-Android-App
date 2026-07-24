package com.iti.linguaquest.features.voicegame.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.domain.model.PronunciationSentence
import com.iti.linguaquest.features.voicegame.domain.model.VoiceEvaluation

interface VoiceGameRepository {
    suspend fun evaluatePronunciation(
        targetSentence: String,
        targetLanguage: String,
        audioBytes: ByteArray
    ): LinguaQuestResult<VoiceEvaluation, LinguaQuestDataError>

    suspend fun generatePronunciationSentence(
        targetLanguage: String = "English",
        level: String = "Beginner",
        topic: String = "General Conversation"
    ): LinguaQuestResult<PronunciationSentence, LinguaQuestDataError>
}
