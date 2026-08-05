package com.iti.linguaquest.features.notification.data.mapper

import com.iti.linguaquest.features.notification.data.datasource.remote.dto.NotificationItemDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.NotificationResponseDto
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.domain.model.NotificationResponse
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun NotificationResponseDto.toDomain(): NotificationResponse {
    return NotificationResponse(status = status)
}

fun NotificationItemDto.toDomain(): Notification {

    return Notification(
        id = id,
        type = type ?: "INFO",
        title = title ?: "",
        body = body ?: "",
        isRead = isRead ?: false,
        createdAt = parseAndFormatDate(createdAt ?: "")
    )
}

private fun parseAndFormatDate(dateString: String): String {
    if (dateString.isBlank()) return ""
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val cleanDateString = dateString.substringBefore(".").substringBefore("Z")
        val date: Date? = inputFormat.parse(cleanDateString)
        if (date != null) {
            val outputFormat = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault())
            outputFormat.format(date)
        } else {
            dateString
        }
    } catch (e: Exception) {
        Timber.w(e, "Failed to parse date string: %s", dateString)
        dateString
    }
}
