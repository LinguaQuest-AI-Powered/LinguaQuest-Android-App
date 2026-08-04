package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.data.datasource.remote.MindReaderAiService
import com.iti.linguaquest.features.mindreader.domain.model.LocalizedText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGuessResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderQuestionCandidate
import java.util.UUID
import javax.inject.Inject

sealed interface MindReaderNextTurn {
    data class Question(val question: MindReaderQuestionCandidate) : MindReaderNextTurn
    data class Guess(
        val guess: MindReaderGuessResult,
        val popQuizOptions: List<MindReaderEntity>,
        val stumpOptions: List<MindReaderEntity>
    ) : MindReaderNextTurn
    data object Error : MindReaderNextTurn
}

class GetMindReaderNextTurnUseCase @Inject constructor(
    private val aiService: MindReaderAiService
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

        val aiResponse = aiService.getNextTurn(
            category = category,
            targetLanguage = targetLanguage,
            nativeLanguage = nativeLanguage,
            history = historyString
        ) ?: return MindReaderNextTurn.Error

        return if (aiResponse.isGuessing) {
            val entity = MindReaderEntity(
                id = UUID.randomUUID().toString(),
                worldKey = category,
                translations = LocalizedText(
                    mapOf(
                        targetLanguage to (aiResponse.guessWordTargetLang ?: ""),
                        nativeLanguage to (aiResponse.guessWordNativeLang ?: "")
                    )
                ),
                emoji = aiResponse.guessEmoji ?: "🤔",
                positiveAttributes = emptySet()
            )
            
            val popQuizEntities = aiResponse.popQuizWrongOptionsTargetLang?.mapIndexed { index, targetWord ->
                val nativeWord = aiResponse.popQuizWrongOptionsNativeLang?.getOrNull(index) ?: ""
                MindReaderEntity(
                    id = UUID.randomUUID().toString(),
                    worldKey = category,
                    translations = LocalizedText(mapOf(targetLanguage to targetWord, nativeLanguage to nativeWord)),
                    emoji = "🤔",
                    positiveAttributes = emptySet()
                )
            } ?: emptyList()
            
            val stumpEntities = aiResponse.stumpDropdownOptionsTargetLang?.mapIndexed { index, targetWord ->
                val nativeWord = aiResponse.stumpDropdownOptionsNativeLang?.getOrNull(index) ?: ""
                MindReaderEntity(
                    id = UUID.randomUUID().toString(),
                    worldKey = category,
                    translations = LocalizedText(mapOf(targetLanguage to targetWord, nativeLanguage to nativeWord)),
                    emoji = "🤔",
                    positiveAttributes = emptySet()
                )
            } ?: emptyList()

            MindReaderNextTurn.Guess(
                guess = MindReaderGuessResult(entity, 0.9),
                popQuizOptions = popQuizEntities,
                stumpOptions = stumpEntities
            )
        } else {
            val question = MindReaderQuestionCandidate(
                attributeId = UUID.randomUUID().toString(),
                question = LocalizedText(
                    mapOf(
                        targetLanguage to (aiResponse.questionTargetLang ?: ""),
                        nativeLanguage to (aiResponse.questionNativeLang ?: "")
                    )
                )
            )
            MindReaderNextTurn.Question(question)
        }
    }
}
