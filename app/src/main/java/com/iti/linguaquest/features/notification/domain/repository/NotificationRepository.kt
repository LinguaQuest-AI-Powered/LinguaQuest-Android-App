package com.iti.linguaquest.features.notification.domain.repository

import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.domain.model.NotificationResponse
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun registerDeviceToken(platform: String = "ANDROID"): LinguaQuestResult<NotificationResponse, LinguaQuestDataError>

    suspend fun unregisterDeviceToken(): LinguaQuestResult<NotificationResponse, LinguaQuestDataError>

    fun getNotifications(): Flow<List<Notification>>

    suspend fun refreshNotifications(page: Int = 0, size: Int = 50): LinguaQuestResult<Unit, LinguaQuestDataError>

    suspend fun getUnreadNotificationCount(): LinguaQuestResult<Int, LinguaQuestDataError>

    suspend fun deleteAllNotifications(): LinguaQuestResult<Unit, LinguaQuestDataError>

    suspend fun deleteNotification(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError>

    suspend fun markNotificationAsRead(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError>
}
