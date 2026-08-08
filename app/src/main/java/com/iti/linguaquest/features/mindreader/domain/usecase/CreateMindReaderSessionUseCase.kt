package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import javax.inject.Inject

class CreateMindReaderSessionUseCase @Inject constructor() {

    operator fun invoke(
        dataset: MindReaderDataset,
        worldKey: String? = null
    ): MindReaderGameState {
        return MindReaderGameState(
            worldKey = worldKey
        )
    }
}
