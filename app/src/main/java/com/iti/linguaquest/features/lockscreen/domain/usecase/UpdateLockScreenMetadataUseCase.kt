package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureMetadata
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import javax.inject.Inject

class UpdateLockScreenMetadataUseCase @Inject constructor(
    private val repository: LockScreenRepository
) {
    suspend operator fun invoke(metadata: LockScreenFeatureMetadata) {
        repository.updateFeatureMetadata(metadata)
    }
}
