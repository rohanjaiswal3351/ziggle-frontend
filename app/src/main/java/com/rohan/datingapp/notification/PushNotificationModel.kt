package com.rohan.datingapp.notification


data class PushNotificationModel(
    val data: NotificationModel,
    val to: String? = ""
)
