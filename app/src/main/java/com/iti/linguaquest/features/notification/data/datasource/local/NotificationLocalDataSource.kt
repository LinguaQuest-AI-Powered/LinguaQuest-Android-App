package com.iti.linguaquest.features.notification.data.datasource.local

import com.iti.linguaquest.core.database.notification.NotificationEntity
import com.iti.linguaquest.features.notification.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationLocalDataSource {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun getAllNotificationsOnce(): List<NotificationEntity>
    suspend fun getNotificationById(id: Long): NotificationEntity?
    suspend fun upsertNotifications(notifications: List<NotificationEntity>)
    suspend fun upsertNotification(notification: NotificationEntity)
    suspend fun markAsRead(id: Long)
    suspend fun markAsUnread(id: Long)
    suspend fun deleteNotification(id: Long)
    suspend fun deleteAllNotifications()
}
