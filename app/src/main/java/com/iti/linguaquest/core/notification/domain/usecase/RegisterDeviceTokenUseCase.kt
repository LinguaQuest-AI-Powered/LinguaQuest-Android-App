package com.iti.linguaquest.core.notification.domain.usecase

import com.iti.linguaquest.core.notification.domain.model.NotificationResponse
import com.iti.linguaquest.core.notification.domain.repository.NotificationRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import javax.inject.Inject

class RegisterDeviceTokenUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(platform: String = "ANDROID"): LinguaQuestResult<NotificationResponse, LinguaQuestDataError> {
        return repository.registerDeviceToken(platform)
    }
}
