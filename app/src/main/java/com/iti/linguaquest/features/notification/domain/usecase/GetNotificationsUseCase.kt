package com.iti.linguaquest.features.notification.domain.usecase

import androidx.paging.PagingData
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(): Flow<PagingData<Notification>> {
        return notificationRepository.getNotificationsPagingData()
    }
}
