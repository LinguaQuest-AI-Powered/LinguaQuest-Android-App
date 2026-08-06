package com.iti.linguaquest.features.notification.data.datasource.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.NotificationItemDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.NotificationResponseDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.RegisterDeviceRequestDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.UnregisterDeviceRequestDto

interface NotificationRemoteDataSource {
    suspend fun registerDeviceToken(request: RegisterDeviceRequestDto): LinguaQuestResult<NotificationResponseDto, LinguaQuestDataError>
    suspend fun unregisterDeviceToken(request: UnregisterDeviceRequestDto): LinguaQuestResult<NotificationResponseDto, LinguaQuestDataError>
    suspend fun getNotifications(page: Int, size: Int): LinguaQuestResult<List<NotificationItemDto>, LinguaQuestDataError>
    suspend fun getUnreadCount(): LinguaQuestResult<Int, LinguaQuestDataError>
    suspend fun deleteAllNotifications(): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun deleteNotification(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun markAsRead(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError>
}
