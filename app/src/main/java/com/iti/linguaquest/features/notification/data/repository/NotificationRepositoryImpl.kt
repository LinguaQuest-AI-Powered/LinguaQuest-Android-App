package com.iti.linguaquest.features.notification.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.map
import com.iti.linguaquest.features.notification.data.datasource.local.NotificationLocalDataSource
import com.iti.linguaquest.features.notification.data.datasource.remote.NotificationRemoteDataSource
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.RegisterDeviceRequestDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.UnregisterDeviceRequestDto
import com.iti.linguaquest.features.notification.data.mapper.toDomain
import com.iti.linguaquest.features.notification.data.mapper.toEntity
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.domain.model.NotificationResponse
import com.iti.linguaquest.features.notification.domain.provider.FcmTokenProvider
import com.iti.linguaquest.features.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val remoteDataSource: NotificationRemoteDataSource,
    private val localDataSource: NotificationLocalDataSource,
    private val fcmTokenProvider: FcmTokenProvider
) : NotificationRepository {

    override suspend fun registerDeviceToken(platform: String): LinguaQuestResult<NotificationResponse, LinguaQuestDataError> {
        val token = fcmTokenProvider.getToken()
        if (token.isNullOrBlank()) {
            return LinguaQuestResult.Failure(LinguaQuestDataError.CustomServerMessage("Device token is blank or unavailable"))
        }

        return remoteDataSource.registerDeviceToken(
            RegisterDeviceRequestDto(
                token = token,
                platform = platform
            )
        ).map { it.toDomain() }
    }

    override suspend fun unregisterDeviceToken(): LinguaQuestResult<NotificationResponse, LinguaQuestDataError> {
        val token = fcmTokenProvider.getToken()
        if (token.isNullOrBlank()) {
            return LinguaQuestResult.Failure(LinguaQuestDataError.CustomServerMessage("Device token is blank or unavailable"))
        }

        return remoteDataSource.unregisterDeviceToken(
            UnregisterDeviceRequestDto(token = token)
        ).map { it.toDomain() }
    }

    override fun getNotifications(): Flow<List<Notification>> {
        return localDataSource.getNotifications()
    }

    override suspend fun refreshNotifications(page: Int, size: Int): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return when (val result = remoteDataSource.getNotifications(page, size)) {
            is LinguaQuestResult.Success -> {
                val entities = result.data.map { it.toEntity() }
                localDataSource.upsertNotifications(entities)
                LinguaQuestResult.Success(Unit)
            }
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun getUnreadNotificationCount(): LinguaQuestResult<Int, LinguaQuestDataError> {
        return remoteDataSource.getUnreadCount()
    }

    override suspend fun deleteAllNotifications(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        val result = remoteDataSource.deleteAllNotifications()
        if (result is LinguaQuestResult.Success) {
            localDataSource.deleteAllNotifications()
        }
        return result
    }

    override suspend fun deleteNotification(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError> {
        val result = remoteDataSource.deleteNotification(id)
        if (result is LinguaQuestResult.Success) {
            localDataSource.deleteNotification(id)
        }
        return result
    }

    override suspend fun markNotificationAsRead(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError> {
        localDataSource.markAsRead(id)
        val result = remoteDataSource.markAsRead(id)
        if (result is LinguaQuestResult.Failure) {
            localDataSource.markAsUnread(id)
        }
        return result
    }
}
