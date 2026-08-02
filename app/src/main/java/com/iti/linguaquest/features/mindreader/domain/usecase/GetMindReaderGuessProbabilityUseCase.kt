package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.engine
import javax.inject.Inject

class GetMindReaderGuessProbabilityUseCase @Inject constructor() {

    operator fun invoke(
        dataset: MindReaderDataset,
        state: MindReaderGameState
    ): Double {
        return dataset.engine().guessProbability(state)
    }
}
