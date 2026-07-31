package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameLaunch
import javax.inject.Inject

class StartMindReaderGameUseCase @Inject constructor(
    private val getMindReaderDatasetUseCase: GetMindReaderDatasetUseCase,
    private val createMindReaderSessionUseCase: CreateMindReaderSessionUseCase,
    private val resolveMindReaderTargetLanguageCodeUseCase: ResolveMindReaderTargetLanguageCodeUseCase
) {
    suspend operator fun invoke(worldKey: String? = null): MindReaderGameLaunch {
        val dataset = getMindReaderDatasetUseCase()
        val languageCode = resolveMindReaderTargetLanguageCodeUseCase()
        val state = createMindReaderSessionUseCase(
            dataset = dataset,
            worldKey = worldKey
        )

        return MindReaderGameLaunch(
            languageCode = languageCode,
            dataset = dataset,
            state = state
        )
    }
}
