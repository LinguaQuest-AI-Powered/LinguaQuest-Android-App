package com.iti.linguaquest.core.notification.data.repository

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.notification.data.datasource.remote.NotificationApiService
import com.iti.linguaquest.core.notification.data.datasource.remote.dto.RegisterDeviceRequestDto
import com.iti.linguaquest.core.notification.data.datasource.remote.dto.UnregisterDeviceRequestDto
import com.iti.linguaquest.core.notification.data.mapper.toDomain
import com.iti.linguaquest.core.notification.domain.model.NotificationResponse
import com.iti.linguaquest.core.notification.domain.provider.FcmTokenProvider
import com.iti.linguaquest.core.notification.domain.repository.NotificationRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val apiService: NotificationApiService,
    private val fcmTokenProvider: FcmTokenProvider
) : NotificationRepository {

    override fun registerDeviceToken(platform: String): Flow<LinguaQuestResult<NotificationResponse, LinguaQuestDataError>> =
        flow {
            val token = fcmTokenProvider.getToken()
            if (token.isNullOrBlank()) {
                emit(LinguaQuestResult.Failure(LinguaQuestDataError.CustomServerMessage("Device token is blank or unavailable")))
                return@flow
            }

            val result = safeApiCall {
                apiService.registerDeviceToken(
                    RegisterDeviceRequestDto(
                        token = token,
                        platform = platform
                    )
                ).data
            }.map { it.toDomain() }

            emit(result)
        }

    override fun unregisterDeviceToken(): Flow<LinguaQuestResult<NotificationResponse, LinguaQuestDataError>> =
        flow {
            val token = fcmTokenProvider.getToken()
            if (token.isNullOrBlank()) {
                emit(LinguaQuestResult.Failure(LinguaQuestDataError.CustomServerMessage("Device token is blank or unavailable")))
                return@flow
            }

            val result = safeApiCall {
                apiService.unregisterDeviceToken(
                    UnregisterDeviceRequestDto(
                        token = token
                    )
                ).data
            }.map { it.toDomain() }

            emit(result)
        }
}
