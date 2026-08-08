package com.iti.linguaquest.features.mindreader.domain.usecase


import com.iti.linguaquest.features.mindreader.domain.model.LocalizedText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGuessResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderQuestionCandidate
import java.util.UUID
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import javax.inject.Inject

sealed interface MindReaderNextTurn {
    data class Question(val question: MindReaderQuestionCandidate) : MindReaderNextTurn
    data class Guess(val guess: MindReaderGuessResult) : MindReaderNextTurn
    data object Error : MindReaderNextTurn
}

class GetMindReaderNextTurnUseCase @Inject constructor(
    private val repository: MindReaderRepository
) {
    suspend operator fun invoke(
        category: String,
        targetLanguage: String,
        nativeLanguage: String,
        state: MindReaderGameState
    ): MindReaderNextTurn {
        val historyString = state.history.turns.joinToString("\n") { turn ->
            "Q: ${turn.question.resolve(targetLanguage)}\nA: ${turn.answer.rawId}"
        }

        val aiResponse = repository.getNextTurn(
            categoryContext = category,
            targetLanguage = targetLanguage,
            nativeLanguage = nativeLanguage,
            historyPrompt = historyString
        )

        return if (aiResponse is com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiNextTurn.Guess) {
            val entity = MindReaderEntity(
                id = UUID.randomUUID().toString(),
                worldKey = category,
                translations = LocalizedText(
                    mapOf(
                        targetLanguage to aiResponse.word,
                        nativeLanguage to aiResponse.translation
                    )
                ),
                emoji = aiResponse.emoji,
                positiveAttributes = emptySet()
            )

            MindReaderNextTurn.Guess(
                guess = MindReaderGuessResult(entity, 0.9)
            )
        } else if (aiResponse is com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiNextTurn.Question) {
            val question = MindReaderQuestionCandidate(
                attributeId = UUID.randomUUID().toString(),
                question = LocalizedText(
                    mapOf(
                        targetLanguage to aiResponse.targetText,
                        nativeLanguage to aiResponse.nativeText
                    )
                )
            )
            MindReaderNextTurn.Question(question)
        } else {
            MindReaderNextTurn.Error
        }
    }
}
