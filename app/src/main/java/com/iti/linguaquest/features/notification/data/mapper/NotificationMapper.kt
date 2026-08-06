package com.iti.linguaquest.features.notification.data.mapper

import com.iti.linguaquest.core.database.notification.NotificationEntity
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

fun NotificationItemDto.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        type = type ?: "INFO",
        title = title ?: "",
        body = body ?: "",
        isRead = isRead ?: false,
        createdAt = parseDateToTimestamp(createdAt ?: "")
    )
}

fun NotificationEntity.toDomain(): Notification {
    return Notification(
        id = id,
        type = type,
        title = title,
        body = body,
        isRead = isRead,
        createdAt = formatTimestamp(createdAt)
    )
}

fun NotificationItemDto.toDomain(): Notification {
    return toEntity().toDomain()
}

fun List<NotificationEntity>.toDomainList(): List<Notification> {
    return map { it.toDomain() }
}

private fun parseDateToTimestamp(dateString: String): Long {
    if (dateString.isBlank()) return 0L
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val cleanDateString = dateString.substringBefore(".").substringBefore("Z")
        val date = inputFormat.parse(cleanDateString)
        date?.time ?: 0L
    } catch (e: Exception) {
        Timber.w(e, "Failed to parse date string to timestamp: %s", dateString)
        0L
    }
}

private fun formatTimestamp(timestamp: Long): String {
    if (timestamp <= 0L) return ""
    return try {
        val outputFormat = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault())
        outputFormat.format(Date(timestamp))
    } catch (e: Exception) {
        Timber.w(e, "Failed to format timestamp: %d", timestamp)
        ""
    }
}
