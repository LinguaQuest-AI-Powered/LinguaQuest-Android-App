package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameLaunch
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import javax.inject.Inject

class StartMindReaderGameUseCase @Inject constructor(
    private val repository: MindReaderRepository,
    private val createMindReaderSessionUseCase: CreateMindReaderSessionUseCase,
    private val resolveMindReaderTargetLanguageCodeUseCase: ResolveMindReaderTargetLanguageCodeUseCase,
    private val resolveMindReaderNativeLanguageCodeUseCase: ResolveMindReaderNativeLanguageCodeUseCase
) {
    suspend operator fun invoke(worldKey: String? = null): MindReaderGameLaunch {
        val config = repository.getGameConfig()
        val languageCode = resolveMindReaderTargetLanguageCodeUseCase()
        val nativeLanguageCode = resolveMindReaderNativeLanguageCodeUseCase()
        val state = createMindReaderSessionUseCase(worldKey = worldKey)

        return MindReaderGameLaunch(
            languageCode = languageCode,
            nativeLanguageCode = nativeLanguageCode,
            config = config,
            state = state
        )
    }
}
