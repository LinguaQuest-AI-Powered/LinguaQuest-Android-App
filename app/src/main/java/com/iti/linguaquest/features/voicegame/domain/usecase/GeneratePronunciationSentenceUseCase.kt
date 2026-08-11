package com.iti.linguaquest.features.voicegame.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.domain.model.PronunciationSentence
import com.iti.linguaquest.features.voicegame.domain.repository.VoiceGameRepository
import javax.inject.Inject

class GeneratePronunciationSentenceUseCase @Inject constructor(
    private val repository: VoiceGameRepository
) {
    suspend operator fun invoke(
        targetLanguage: String = "English",
        level: String = "Beginner",
        topic: String = "General Conversation",
        wordOfTheDay: String? = null,
        excludeSentences: List<String> = emptyList()
    ): LinguaQuestResult<PronunciationSentence, LinguaQuestDataError> {
        return repository.generatePronunciationSentence(targetLanguage, level, topic, wordOfTheDay, excludeSentences)
    }
}
