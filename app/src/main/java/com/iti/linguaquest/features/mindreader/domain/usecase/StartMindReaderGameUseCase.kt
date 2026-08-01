package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameLaunch
import javax.inject.Inject

class StartMindReaderGameUseCase @Inject constructor(
    private val getMindReaderDatasetUseCase: GetMindReaderDatasetUseCase,
    private val createMindReaderSessionUseCase: CreateMindReaderSessionUseCase,
    private val resolveMindReaderTargetLanguageCodeUseCase: ResolveMindReaderTargetLanguageCodeUseCase,
    private val resolveMindReaderNativeLanguageCodeUseCase: ResolveMindReaderNativeLanguageCodeUseCase
) {
    suspend operator fun invoke(worldKey: String? = null): MindReaderGameLaunch {
        val dataset = getMindReaderDatasetUseCase()
        val languageCode = resolveMindReaderTargetLanguageCodeUseCase()
        val nativeLanguageCode = resolveMindReaderNativeLanguageCodeUseCase()
        val state = createMindReaderSessionUseCase(
            dataset = dataset,
            worldKey = worldKey
        )

        return MindReaderGameLaunch(
            languageCode = languageCode,
            nativeLanguageCode = nativeLanguageCode,
            dataset = dataset,
            state = state
        )
    }
}
