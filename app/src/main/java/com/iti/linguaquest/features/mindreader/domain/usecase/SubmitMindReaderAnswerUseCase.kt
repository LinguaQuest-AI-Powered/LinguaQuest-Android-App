package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderHistoryEntry
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderQuestionCandidate
import javax.inject.Inject

class SubmitMindReaderAnswerUseCase @Inject constructor() {

    operator fun invoke(
        state: MindReaderGameState,
        question: MindReaderQuestionCandidate,
        answer: MindReaderAnswerOption
    ): MindReaderGameState {
        val entry = MindReaderHistoryEntry(
            attributeId = question.attributeId,
            question = question.question,
            answer = answer,
            confidenceAfterAnswer = 0.0
        )
        return state.copy(
            history = state.history.append(entry),
            askedAttributes = state.askedAttributes + question.attributeId,
            questionCount = state.questionCount + 1
        )
    }
}
