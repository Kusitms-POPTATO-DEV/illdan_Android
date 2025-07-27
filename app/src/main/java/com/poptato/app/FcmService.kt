package com.poptato.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import com.poptato.design_system.R

class FcmService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Timber.d("[FCM] FcmService -> token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Timber.d("[FCM] FcmService -> data: ${message.data}")

        val title = message.data["title"] ?: message.notification?.title
        val body = message.data["todolist"] ?: message.notification?.body

        if (message.data.isNotEmpty()) {
            sendNotification(title, body)
        } else {
            Timber.d("[FCM] FcmService -> empty data")
        }

    }

    private fun sendNotification(title: String?, body: String?) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(body)
            .setSmallIcon(R.drawable.ic_app)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY)
            .build()

        val summaryNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("일단")
            .setSmallIcon(R.drawable.ic_app)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine(body)
                    .setSummaryText("오늘 마감 할 일: ${notificationManager.activeNotifications.size}")
            )
            .setGroup(GROUP_KEY)
            .setGroupSummary(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        notificationManager.notify(SUMMARY_ID, summaryNotification)
    }

    companion object {
        private const val GROUP_KEY = "TODO_GROUP"
        private const val CHANNEL_ID = "TODO_CHANNEL"
        private const val CHANNEL_NAME = "TODO"
        private const val SUMMARY_ID = 0
    }
}