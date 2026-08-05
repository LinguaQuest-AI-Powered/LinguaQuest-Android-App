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
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
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
        return try {
            val response = apiService.getUnreadCount()
            if (response.isSuccessful) {
                val count = response.body()?.data?.count ?: 0
                LinguaQuestResult.Success(count)
            } else {
                LinguaQuestResult.Failure(mapHttpCodeToError(response.code()))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching unread notification count")
            LinguaQuestResult.Failure(mapExceptionToError(e))
        }
    }

    override suspend fun deleteAllNotifications(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            val response = apiService.deleteAllNotifications()
            if (response.isSuccessful) {
                LinguaQuestResult.Success(Unit)
            } else {
                LinguaQuestResult.Failure(mapHttpCodeToError(response.code()))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error deleting all notifications")
            LinguaQuestResult.Failure(mapExceptionToError(e))
        }
    }

    override suspend fun deleteNotification(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            val response = apiService.deleteNotification(id)
            if (response.isSuccessful) {
                LinguaQuestResult.Success(Unit)
            } else {
                LinguaQuestResult.Failure(mapHttpCodeToError(response.code()))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error deleting notification with id %d", id)
            LinguaQuestResult.Failure(mapExceptionToError(e))
        }
    }

    override suspend fun markNotificationAsRead(id: Long): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            val response = apiService.markAsRead(id)
            if (response.isSuccessful) {
                LinguaQuestResult.Success(Unit)
            } else {
                LinguaQuestResult.Failure(mapHttpCodeToError(response.code()))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error marking notification as read for id %d", id)
            LinguaQuestResult.Failure(mapExceptionToError(e))
        }
    }

    private fun mapHttpCodeToError(code: Int): LinguaQuestDataError {
        return when (code) {
            400 -> LinguaQuestDataError.Remote.BAD_REQUEST
            401, 403 -> LinguaQuestDataError.Remote.UNAUTHORIZED
            408 -> LinguaQuestDataError.Remote.REQUEST_TIMEOUT
            429 -> LinguaQuestDataError.Remote.TOO_MANY_REQUESTS
            in 500..599 -> LinguaQuestDataError.Remote.SERVER
            else -> LinguaQuestDataError.Remote.UNKNOWN
        }
    }

    private fun mapExceptionToError(e: Exception): LinguaQuestDataError {
        return when (e) {
            is IOException -> LinguaQuestDataError.Remote.NO_INTERNET
            is HttpException -> LinguaQuestDataError.Remote.SERVER
            else -> LinguaQuestDataError.Remote.UNKNOWN
        }
    }
}

