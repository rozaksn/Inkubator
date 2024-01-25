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

class WaterLevelNotification(context:Context) {
    private val CHANNEL_ID = "channel_id"
    private val context = context
    val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    fun sendNotification(level:String){
        if (level <= "2"){
            val intent = Intent(context,MainActivity::class.java)
            val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, flags)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setVibrate(longArrayOf(1000,1000,1000))
                .setSound(ringtone)
                .setAutoCancel(true)
                .setContentText("Level air $level cm")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val notificationChannel = NotificationChannel(CHANNEL_ID,"notify_water_level",NotificationManager.IMPORTANCE_DEFAULT)
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(1000,1000,1000)

                notification.setChannelId(CHANNEL_ID)
                manager.createNotificationChannel(notificationChannel)
            }
            notification.setAutoCancel(true)
            val notificationBuilder = notification.build()
            notificationBuilder.flags = Notification.FLAG_AUTO_CANCEL or Notification.FLAG_ONGOING_EVENT
            manager.notify(1, notificationBuilder)
        }
    }
}