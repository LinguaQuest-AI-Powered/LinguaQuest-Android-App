package com.iti.linguaquest.core.appicon.usecase

import com.iti.linguaquest.core.session.SessionEvent
import com.iti.linguaquest.core.session.SessionEventBus
import javax.inject.Inject

class LessonCompletedUseCase @Inject constructor(
    private val appIconSyncUseCase: AppIconSyncUseCase,
    private val sessionEventBus: SessionEventBus
) {
    suspend operator fun invoke() {
        appIconSyncUseCase.onLessonCompleted()
        sessionEventBus.emit(SessionEvent.LevelCompleted)
    }
}
