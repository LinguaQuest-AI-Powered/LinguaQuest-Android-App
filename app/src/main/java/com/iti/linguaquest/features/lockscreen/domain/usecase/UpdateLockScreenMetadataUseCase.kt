package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import javax.inject.Inject

class UpdateLockScreenMetadataUseCase @Inject constructor(
    private val repository: LockScreenRepository
) {
    suspend operator fun invoke(
        enabled: Boolean,
        pendingGeneration: Boolean,
        operationId: String? = null,
        batchSize: Int? = null,
        lastGenerationTime: Long? = null,
        lastNativeLanguage: String? = null,
        lastTargetLanguage: String? = null,
        lastProficiencyLevel: String? = null
    ) {
        repository.updateFeatureMetadata(
            enabled = enabled,
            pendingGeneration = pendingGeneration,
            operationId = operationId,
            batchSize = batchSize,
            lastGenerationTime = lastGenerationTime,
            lastNativeLanguage = lastNativeLanguage,
            lastTargetLanguage = lastTargetLanguage,
            lastProficiencyLevel = lastProficiencyLevel
        )
    }
}
