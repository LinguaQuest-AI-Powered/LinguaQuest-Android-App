package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.engine
import javax.inject.Inject

class SubmitMindReaderAnswerUseCase @Inject constructor() {

    operator fun invoke(
        dataset: MindReaderDataset,
        state: MindReaderGameState,
        attributeId: String,
        answer: MindReaderAnswerOption
    ): MindReaderGameState {
        return dataset.engine().applyAnswer(
            state = state,
            attributeId = attributeId,
            answer = answer
        )
    }
}
