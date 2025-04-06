package com.example.advancedandroidcourse.data.repository

import com.example.advancedandroidcourse.data.model.Notification

interface NotificationRepository {
    suspend fun getNotificationsForUser(userId: String): List<Notification>
}