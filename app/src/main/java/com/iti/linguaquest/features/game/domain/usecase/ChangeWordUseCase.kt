package com.iti.linguaquest.features.game.domain.usecase

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.session.SessionEvent
import com.iti.linguaquest.core.session.SessionEventBus
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import javax.inject.Inject

class ChangeWordUseCase @Inject constructor(
    private val repository: LevelRepository,
    private val sessionEventBus: SessionEventBus
) {
    suspend operator fun invoke(worldId: Int, levelId: Int): LinguaQuestResult<String, AppError> {
        val result = repository.changeWord(worldId, levelId)
        if (result is LinguaQuestResult.Success) {
            sessionEventBus.emit(SessionEvent.WordChanged)
        }
        return result
    }
}
