package com.iti.linguaquest.core.notification.domain.repository

import com.iti.linguaquest.core.notification.domain.model.NotificationResponse
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult

interface NotificationRepository {
    suspend fun registerDeviceToken(platform: String = "ANDROID"): LinguaQuestResult<NotificationResponse, LinguaQuestDataError>

    suspend fun unregisterDeviceToken(): LinguaQuestResult<NotificationResponse, LinguaQuestDataError>
}
