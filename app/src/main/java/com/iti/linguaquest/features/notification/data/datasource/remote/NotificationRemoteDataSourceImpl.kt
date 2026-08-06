package com.iti.linguaquest.features.notification.data.datasource.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.NotificationItemDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.NotificationResponseDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.RegisterDeviceRequestDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.UnregisterDeviceRequestDto
import javax.inject.Inject

class NotificationRemoteDataSourceImpl @Inject constructor(
    private val apiService: NotificationApiService
) : NotificationRemoteDataSource {

    override suspend fun registerDeviceToken(request: RegisterDeviceRequestDto): LinguaQuestResult<NotificationResponseDto, LinguaQuestDataError> {
        return safeApiCall {
            apiService.registerDeviceToken(request).data
        }
    }

    override suspend fun unregisterDeviceToken(request: UnregisterDeviceRequestDto): LinguaQuestResult<NotificationResponseDto, LinguaQuestDataError> {
        return safeApiCall {
            apiService.unregisterDeviceToken(request).data
        }
    }

    override suspend fun getNotifications(page: Int, size: Int): LinguaQuestResult<List<NotificationItemDto>, LinguaQuestDataError> {
        return safeApiCall {
            apiService.getNotifications(page, size).data?.notifications ?: emptyList()
        }
    }

    override suspend fun getUnreadCount(): LinguaQuestResult<Int, LinguaQuestDataError> {
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

    override suspend fun markAsRead(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return safeApiCall {
            apiService.markAsRead(id)
            Unit
        }
    }
}
