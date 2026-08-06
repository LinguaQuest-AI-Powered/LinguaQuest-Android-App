package com.iti.linguaquest.features.notification.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.features.notification.data.datasource.paging.NotificationPagingSource
import com.iti.linguaquest.features.notification.data.datasource.remote.NotificationApiService
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.RegisterDeviceRequestDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.UnregisterDeviceRequestDto
import com.iti.linguaquest.features.notification.data.mapper.toDomain
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.domain.model.NotificationResponse
import com.iti.linguaquest.features.notification.domain.provider.FcmTokenProvider
import com.iti.linguaquest.features.notification.domain.repository.NotificationRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.map
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val apiService: NotificationApiService,
    private val fcmTokenProvider: FcmTokenProvider
) : NotificationRepository {

    override suspend fun registerDeviceToken(platform: String): LinguaQuestResult<NotificationResponse, LinguaQuestDataError> {
        val token = fcmTokenProvider.getToken()
        if (token.isNullOrBlank()) {
            return LinguaQuestResult.Failure(LinguaQuestDataError.CustomServerMessage("Device token is blank or unavailable"))
        }

        return safeApiCall {
            apiService.registerDeviceToken(
                RegisterDeviceRequestDto(
                    token = token,
                    platform = platform
                )
            ).data
        }.map { it.toDomain() }
    }

    override suspend fun unregisterDeviceToken(): LinguaQuestResult<NotificationResponse, LinguaQuestDataError> {
        val token = fcmTokenProvider.getToken()
        if (token.isNullOrBlank()) {
            return LinguaQuestResult.Failure(LinguaQuestDataError.CustomServerMessage("Device token is blank or unavailable"))
        }

        return safeApiCall {
            apiService.unregisterDeviceToken(
                UnregisterDeviceRequestDto(
                    token = token
                )
            ).data
        }.map { it.toDomain() }
    }

    override fun getNotificationsPagingData(): Flow<PagingData<Notification>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NotificationPagingSource(apiService) }
        ).flow
    }

    override suspend fun getUnreadNotificationCount(): LinguaQuestResult<Int, LinguaQuestDataError> {
        return safeApiCall {
            apiService.getUnreadCount().data?.count ?: 0
        }
    }

    override suspend fun deleteAllNotifications(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return safeApiCall {
            apiService.deleteAllNotifications()
            Unit
        }
    }

    override suspend fun deleteNotification(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return safeApiCall {
            apiService.deleteNotification(id)
            Unit
        }
    }

    override suspend fun markNotificationAsRead(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return safeApiCall {
            apiService.markAsRead(id)
            Unit
        }
    }
}

