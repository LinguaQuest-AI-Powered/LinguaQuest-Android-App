package com.iti.linguaquest.features.notification.data.datasource.local

import com.iti.linguaquest.core.database.notification.NotificationDao
import com.iti.linguaquest.core.database.notification.NotificationEntity
import com.iti.linguaquest.features.notification.data.mapper.toDomainList
import com.iti.linguaquest.features.notification.domain.model.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationLocalDataSourceImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationLocalDataSource {

    override fun getNotifications(): Flow<List<Notification>> {
        return notificationDao.getNotifications().map { it.toDomainList() }
    }

    override suspend fun getAllNotificationsOnce(): List<NotificationEntity> {
        return notificationDao.getAllNotificationsOnce()
    }

    override suspend fun getNotificationById(id: Long): NotificationEntity? {
        return notificationDao.getNotificationById(id)
    }

    override suspend fun upsertNotifications(notifications: List<NotificationEntity>) {
        notificationDao.upsertNotifications(notifications)
    }

    override suspend fun upsertNotification(notification: NotificationEntity) {
        notificationDao.upsertNotification(notification)
    }

    override suspend fun markAsRead(id: Long) {
        notificationDao.markAsRead(id)
    }

    override suspend fun markAsUnread(id: Long) {
        notificationDao.markAsUnread(id)
    }

    override suspend fun deleteNotification(id: Long) {
        notificationDao.deleteNotification(id)
    }

    override suspend fun deleteAllNotifications() {
        notificationDao.deleteAllNotifications()
    }
}
