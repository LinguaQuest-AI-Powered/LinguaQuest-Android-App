package com.iti.linguaquest.core.appicon.domain.usecase

import javax.inject.Inject

class LessonCompletedUseCase @Inject constructor(
    private val appIconSyncUseCase: AppIconSyncUseCase
) {
    suspend operator fun invoke() {
        appIconSyncUseCase.onLessonCompleted()
    }
}
