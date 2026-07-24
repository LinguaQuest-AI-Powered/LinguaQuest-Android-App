package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class LockScreenMetadata(
    val featureEnabled: Boolean,
    val pendingGeneration: Boolean,
    val batchSize: Int,
    val lastGenerationTime: Long?,
    val lastNativeLanguage: String?,
    val lastTargetLanguage: String?,
    val lastProficiencyLevel: String?,
    val pendingCount: Int,
    val pendingOperationId: String?
)

class ObserveLockScreenSettingsMetadataUseCase @Inject constructor(
    private val repository: LockScreenRepository
) {
    operator fun invoke(): Flow<LockScreenMetadata> {
        return combine(
            repository.featureEnabled,
            repository.pendingGeneration,
            repository.batchSize,
            repository.lastGenerationTime,
            repository.lastNativeLanguage,
            repository.lastTargetLanguage,
            repository.lastProficiencyLevel,
            repository.pendingCount,
            repository.pendingOperationId
        ) { values ->
            LockScreenMetadata(
                featureEnabled = values[0] as Boolean,
                pendingGeneration = values[1] as Boolean,
                batchSize = values[2] as Int,
                lastGenerationTime = values[3] as Long?,
                lastNativeLanguage = values[4] as String?,
                lastTargetLanguage = values[5] as String?,
                lastProficiencyLevel = values[6] as String?,
                pendingCount = values[7] as Int,
                pendingOperationId = values[8] as String?
            )
        }
    }
}
