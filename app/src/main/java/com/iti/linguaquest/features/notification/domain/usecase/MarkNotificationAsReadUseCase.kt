package com.iti.linguaquest.features.notification.domain.usecase

import com.iti.linguaquest.features.notification.domain.repository.NotificationRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import javax.inject.Inject

class MarkNotificationAsReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return notificationRepository.markNotificationAsRead(id)
    }
}
