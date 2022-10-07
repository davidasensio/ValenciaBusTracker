package com.handysparksoft.valenciabustracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class BusTrackerBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        BusStopTrackerService.startTheService(
            context = context,
            notificationData = NotificationData(
                contentTitle = context.getString(R.string.app_name),
                contentText = context.getString(R.string.foreground_notification_on_boot_info),
                subText = context.getString(R.string.foreground_notification_on_boot_completed)
            )
        )
        Timber.d("BroadcastReceiver triggered by $action")
    }
}
