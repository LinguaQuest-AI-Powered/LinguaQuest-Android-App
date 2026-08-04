package com.iti.linguaquest.core.notification.domain.repository

import com.iti.linguaquest.core.notification.domain.model.NotificationResponse
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun registerDeviceToken(platform: String = "ANDROID"): Flow<LinguaQuestResult<NotificationResponse, LinguaQuestDataError>>

    fun unregisterDeviceToken(): Flow<LinguaQuestResult<NotificationResponse, LinguaQuestDataError>>
}
