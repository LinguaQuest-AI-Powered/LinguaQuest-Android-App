package com.iti.linguaquest.core.notification.data.mapper

import com.iti.linguaquest.core.notification.data.datasource.remote.dto.NotificationResponseDto
import com.iti.linguaquest.core.notification.domain.model.NotificationResponse

fun NotificationResponseDto.toDomain(): NotificationResponse {
    return NotificationResponse(
        status = status
    )
}
