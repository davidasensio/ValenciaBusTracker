package com.handysparksoft.valenciabustracker

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import timber.log.Timber
import java.io.Serializable

class BusStopTrackerService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val action = intent.action

        /** Stop the service if we receive the Stop action.
         *  START_NOT_STICKY is important here, we don't want the service to be relaunched.
         */
        if (action == BusStopTrackerAction.ActionStop.name) {
            // Stuff to do before stopping the service
            Timber.d("Action stop")
            stopService()
            return START_NOT_STICKY
        }

        startForegroundServiceWithNotification(this, intent)

        when (action) {
            BusStopTrackerAction.Action1.name -> {
                Timber.d("Action 1")
            }
            BusStopTrackerAction.Action2.name -> {
                Timber.d("Action 2")
            }
        }

        instance = this
        return START_STICKY
    }

    @Suppress("UNCHECKED_CAST")
    private fun startForegroundServiceWithNotification(context: Context, intent: Intent) {
        (intent.extras?.get(NOTIFICATION_DATA_ARG) as? NotificationData)?.let { notificationData ->
            val notification = getNotification(
                context = this,
                notificationData = notificationData
            )

            if (isMyServiceRunning()) {
                (context.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)?.notify(
                    NOTIFICATION_ID,
                    notification
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        }
    }

    /**
     * Remove the foreground notification and stop the service.
     */
    private fun stopService() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        Timber.d("Service stopped")
    }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }

    companion object {
        const val NOTIFICATION_DATA_ARG = "NotificationData"
        private const val NOTIFICATION_ID = 1
        private var instance: BusStopTrackerService? = null

        fun startTheService(context: Context, notificationData: NotificationData, action: BusStopTrackerAction? = null) {
            val serviceIntent = Intent(context, BusStopTrackerService::class.java)
            action?.let { serviceIntent.action = action.toString() }
            serviceIntent.putExtra(NOTIFICATION_DATA_ARG, notificationData)
            context.startForegroundService(serviceIntent)
        }

        fun stopTheService(context: Context) {
            val serviceIntent = Intent(context, BusStopTrackerService::class.java)
            serviceIntent.action = BusStopTrackerAction.ActionStop.toString()
            context.startService(serviceIntent)
        }

        private fun getNotification(
            context: Context,
            notificationData: NotificationData,
            isOngoing: Boolean = true,
            isOnlyAlertOnce: Boolean = true
        ): Notification? {
            val notificationChannel = BusTrackerNotificationChannel(
                name = "Bus Stop Tracker",
                description = "Schedule changes tracker",
                importance = NotificationManager.IMPORTANCE_HIGH
            )()

            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.let {
                Timber.d("Notifications are enabled: ${it.areNotificationsEnabled()}")

                it.createNotificationChannel(notificationChannel)

                val notificationBuilder = NotificationCompat.Builder(context, notificationChannel.id)
                val notificationIntent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                return notificationBuilder
                    .setOngoing(isOngoing)
                    .setOnlyAlertOnce(isOnlyAlertOnce)
                    .setContentTitle(notificationData.contentTitle)
                    .setContentText(notificationData.contentText)
                    .setSubText(notificationData.subText)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    // .setProgress(100, 50, false)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setContentIntent(pendingIntent)
                    .setStyle(
                        NotificationCompat.BigTextStyle().bigText(
                            notificationData.listItems.map { it }.joinToString(separator = "\n") { it }
                        )
                    )
                    .addAction(getAction1(context))
                    .addAction(getAction2(context))
                    .addAction(getActionStop(context))
                    .build()
            }
            return null
        }

        private fun isMyServiceRunning() = instance != null

        @Suppress("UnusedPrivateMember")
        private fun isNotificationAlreadyInPlace(context: Context): Boolean {
            (context.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)?.let { notificationManager ->
                return notificationManager.activeNotifications.any { it.id == NOTIFICATION_ID }
            }
            return false
        }

        /**
         * Notification Actions
         */
        private fun getAction1(context: Context) = getNotificationAction(
            context = context,
            action = BusStopTrackerAction.Action1,
            text = context.getString(R.string.foreground_notification_action_1)
        )

        private fun getAction2(context: Context) = getNotificationAction(
            context = context,
            action = BusStopTrackerAction.Action2,
            text = context.getString(R.string.foreground_notification_action_2)
        )

        private fun getActionStop(context: Context) = getNotificationAction(
            context = context,
            action = BusStopTrackerAction.ActionStop,
            text = context.getString(R.string.foreground_notification_action_stop)
        )

        private fun getNotificationAction(
            context: Context,
            action: BusStopTrackerAction,
            text: String,
            icon: Int = 0
        ): NotificationCompat.Action {
            val intent = Intent(context, BusStopTrackerService::class.java)
            intent.action = action.toString()
            val pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            return NotificationCompat.Action(icon, text, pendingIntent)
        }
    }
}

enum class BusStopTrackerAction { Action1, Action2, ActionStop }

data class NotificationData(
    val contentTitle: String,
    val contentText: String,
    val subText: String,
    val listItems: List<String> = emptyList()
) : Serializable
