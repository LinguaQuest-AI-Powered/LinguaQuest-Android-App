package com.iti.linguaquest.core.database.notification

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: Long,
    val type: String,
    val title: String,
    val body: String,
    val isRead: Boolean,
    val createdAt: Long
)
