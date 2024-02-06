package com.example.inkubator.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.inkubator.R
import com.example.inkubator.main.MainActivity

class NotificationSet(context:Context) {
    private var CHANNEL_ID = "channel_id"
    private val ctx = context
    val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    fun sendWaterLevelNotification(level:String){
        if (level <= "2"){
            val intent = Intent(ctx,MainActivity::class.java)
            val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            val pendingIntent = PendingIntent.getActivity(ctx, 0, intent, flags)
            val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setVibrate(longArrayOf(1000))
                .setSound(ringtone)
                .setContentText("Ketinggian air: $level cm")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val notificationChannel = NotificationChannel(
                    CHANNEL_ID,"water_level_notification",
                    NotificationManager.IMPORTANCE_DEFAULT)
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(1000)

                notification.setChannelId(CHANNEL_ID)
                manager.createNotificationChannel(notificationChannel)
            }
            notification.setAutoCancel(true)
            val notificationBuilder = notification.build()
            notificationBuilder.flags = Notification.FLAG_AUTO_CANCEL or Notification.FLAG_ONLY_ALERT_ONCE
            manager.notify(1, notificationBuilder)

        }
    }
    fun sendDetectionNotification(detection:String,confidence:Float) {
        if (detection == "person" && confidence > 0.5) {
            val intent = Intent(ctx,MainActivity::class.java)
            val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            val pendingIntent = PendingIntent.getActivity(ctx, 0, intent, flags)
            val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setVibrate(longArrayOf(1000))
                .setSound(ringtone)
                .setContentText("Deteksi: $detection")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val notificationChannel = NotificationChannel(CHANNEL_ID,"detection_notification",
                    NotificationManager.IMPORTANCE_DEFAULT)
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(1000)

                notification.setChannelId(CHANNEL_ID)
                manager.createNotificationChannel(notificationChannel)
            }
            notification.setAutoCancel(true)
            val notificationBuilder = notification.build()
            notificationBuilder.flags = Notification.FLAG_AUTO_CANCEL or Notification.FLAG_ONLY_ALERT_ONCE
            manager.notify(2, notificationBuilder)
        }
    }
}