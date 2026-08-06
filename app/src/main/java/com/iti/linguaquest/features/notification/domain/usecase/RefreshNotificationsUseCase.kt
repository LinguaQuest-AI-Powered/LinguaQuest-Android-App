package com.iti.linguaquest.features.notification.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class RefreshNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(page: Int = 0, size: Int = 50): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return notificationRepository.refreshNotifications(page, size)
    }
}
