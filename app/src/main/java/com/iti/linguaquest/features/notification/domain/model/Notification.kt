package com.iti.linguaquest.features.notification.domain.model

data class Notification(
    val id: Long,
    val type: String,
    val title: String,
    val body: String,
    val isRead: Boolean,
    val createdAt: String
)
