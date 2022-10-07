package com.handysparksoft.valenciabustracker

import android.app.NotificationChannel
import android.app.NotificationManager

class BusTrackerNotificationChannel(
    private val name: String,
    private val description: String? = null,
    private val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
) {
    private val notificationChannelId = name.trim()
    private val notificationChannelName = name

    private fun getNotificationChannel(): NotificationChannel {
        val notificationChannel = NotificationChannel(
            notificationChannelId,
            notificationChannelName,
            importance
        )
        notificationChannel.description = description
        return notificationChannel
    }

    operator fun invoke() = getNotificationChannel()
}
