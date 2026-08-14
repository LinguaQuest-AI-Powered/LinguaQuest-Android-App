package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderContradictionResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import java.util.UUID
import javax.inject.Inject

class VerifyMindReaderHonestyUseCase @Inject constructor(
    private val repository: MindReaderRepository
) {
    suspend operator fun invoke(
        categoryContext: String,
        targetLanguage: String,
        feedbackLanguage: String,
        history: MindReaderGameHistory,
        claimedWord: String
    ): MindReaderContradictionResult {
        val historyString = history.turns.joinToString("\n") { turn ->
            "Q: ${turn.questionTargetText}\nA: ${turn.answer.rawId}"
        }

        val aiResponse = repository.verifyUserWord(
            categoryContext = categoryContext,
            historyPrompt = historyString,
            claimedWord = claimedWord,
            feedbackLanguage = feedbackLanguage
        )

        val dummyEntity = MindReaderEntity(
            id = UUID.randomUUID().toString(),
            worldKey = categoryContext,
            targetText = claimedWord,
            nativeText = "",
            emoji = "🤔"
        )

        return if (aiResponse != null) {
            MindReaderContradictionResult(
                evaluatedEntity = dummyEntity,
                details = emptyList(),
                contradictionCount = if (aiResponse.isHonest) 0 else 1,
                matchedCount = history.turns.size,
                totalCount = history.turns.size,
                reason = aiResponse.explanation
            )
        } else {
            MindReaderContradictionResult(
                evaluatedEntity = dummyEntity,
                details = emptyList(),
                contradictionCount = 0,
                matchedCount = history.turns.size,
                totalCount = history.turns.size,
                reason = "AI Verification failed, assuming honest."
            )
        }
    }
}
