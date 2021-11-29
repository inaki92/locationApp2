package com.inaki.locationapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

// This object will be a helper to wrap the two function needed for the notifications
// notification channel, and the notification
object Notification {

    fun createNotificationChannel(context: Context) = run {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("CHANNEL_ID", "channel_name", importance)
        channel.description = "location notification channel"
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = context.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotification(context: Context): Notification =
        NotificationCompat.Builder(context, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("This app is using you GPS")
            .setContentText("We are tracking your location")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
}