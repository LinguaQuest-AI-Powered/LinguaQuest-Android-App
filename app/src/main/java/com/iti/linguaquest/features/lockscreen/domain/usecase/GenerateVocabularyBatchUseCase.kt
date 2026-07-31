package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import com.iti.linguaquest.features.lockscreen.domain.model.VocabularyBatchParams
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GenerateVocabularyBatchUseCase @Inject constructor(
    private val lockScreenRepository: LockScreenRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<Int, LinguaQuestDataError> {
        val params = VocabularyBatchParams(
            batchSize = lockScreenRepository.batchSize.first().takeIf { it > 0 } ?: 10,
            excludeWords = lockScreenRepository.recentGeneratedWords(100),
            nativeLanguage = userPreferencesRepository.nativeLanguageName.first().orEmpty().ifBlank { "Arabic" },
            targetLanguage = userPreferencesRepository.targetLanguageName.first().orEmpty().ifBlank { "English" },
            proficiencyLevel = userPreferencesRepository.proficiencyLevel.first().orEmpty().ifBlank { "Beginner" }
        )

        return when (val result = lockScreenRepository.generateBatch(params)) {
            is LinguaQuestResult.Success -> lockScreenRepository.saveGeneratedBatch(
                words = result.data,
                params = params
            )
            is LinguaQuestResult.Failure -> result
        }
    }
}
