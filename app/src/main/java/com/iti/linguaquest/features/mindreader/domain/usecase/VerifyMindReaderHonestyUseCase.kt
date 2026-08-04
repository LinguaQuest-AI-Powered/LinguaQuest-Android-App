package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.data.datasource.remote.MindReaderAiService
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderContradictionResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory
import javax.inject.Inject

class VerifyMindReaderHonestyUseCase @Inject constructor(
    private val aiService: MindReaderAiService
) {
    suspend operator fun invoke(
        category: String,
        targetLanguage: String,
        history: MindReaderGameHistory,
        userWord: MindReaderEntity
    ): MindReaderContradictionResult {
        val historyString = history.turns.joinToString("\n") { turn ->
            "Q: ${turn.question.resolve(targetLanguage)}\nA: ${turn.answer.rawId}"
        }

        val wordTargetLang = userWord.resolveTranslation(targetLanguage)
        
        val aiResponse = aiService.verifyUserWord(
            category = category,
            history = historyString,
            userWord = wordTargetLang
        )

        return if (aiResponse != null) {
            MindReaderContradictionResult(
                evaluatedEntity = userWord,
                details = emptyList(),
                contradictionCount = if (aiResponse.isHonest) 0 else 1,
                matchedCount = history.turns.size,
                totalCount = history.turns.size,
                reason = aiResponse.reason
            )
        } else {
             MindReaderContradictionResult(
                evaluatedEntity = userWord,
                details = emptyList(),
                contradictionCount = 0,
                matchedCount = history.turns.size,
                totalCount = history.turns.size,
                reason = "AI Verification failed, assuming honest."
            )
        }
    }
}
