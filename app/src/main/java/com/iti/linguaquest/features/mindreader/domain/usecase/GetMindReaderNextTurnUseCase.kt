package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiNextTurn
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGuessResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderNextTurn
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderQuestionCandidate
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import java.util.UUID
import javax.inject.Inject

class GetMindReaderNextTurnUseCase @Inject constructor(
    private val repository: MindReaderRepository
) {
    suspend operator fun invoke(
        category: String,
        targetLanguage: String,
        nativeLanguage: String,
        state: MindReaderGameState
    ): MindReaderNextTurn {
        val turnCount = state.history.turns.size
        val historyString = if (turnCount == 0) {
            "None (First question, Turn 1)"
        } else {
            val list = state.history.turns.mapIndexed { index, turn ->
                "${index + 1}. Q: ${turn.questionTargetText} (${turn.questionNativeText}) -> Answer: ${turn.answer.rawId}"
            }.joinToString("\n")
            "$list\n(Total questions asked so far: $turnCount)"
        }

        val aiResponse = repository.getNextTurn(
            categoryContext = category,
            targetLanguage = targetLanguage,
            nativeLanguage = nativeLanguage,
            historyPrompt = historyString
        )

        return when (aiResponse) {
            is MindReaderAiNextTurn.Guess -> {
                val entity = MindReaderEntity(
                    id = UUID.randomUUID().toString(),
                    worldKey = category,
                    targetText = aiResponse.word,
                    nativeText = aiResponse.translation,
                    emoji = aiResponse.emoji
                )

                MindReaderNextTurn.Guess(
                    guess = MindReaderGuessResult(
                        entity = entity,
                        confidence = 0.9,
                        quizChoices = aiResponse.quizChoices
                    )
                )
            }
            is MindReaderAiNextTurn.Question -> {
                val question = MindReaderQuestionCandidate(
                    attributeId = UUID.randomUUID().toString(),
                    targetText = aiResponse.targetText,
                    nativeText = aiResponse.nativeText
                )
                MindReaderNextTurn.Question(question)
            }
            MindReaderAiNextTurn.Error -> {
                MindReaderNextTurn.Error
            }
        }
    }
}
