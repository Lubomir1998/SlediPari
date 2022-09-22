package com.example.sledipari.api.models.pushnotifications

import kotlinx.serialization.Serializable

@Serializable
data class PushNotification(
    val data: NotificationData,
    val to: String
)

@Serializable
data class NotificationData(
    val title: String,
    val message: String
)