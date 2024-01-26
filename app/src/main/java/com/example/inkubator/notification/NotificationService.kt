package com.example.inkubator.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.inkubator.R
import com.example.inkubator.main.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NotificationService:Service() {
    private lateinit var database : FirebaseDatabase
    private lateinit var waterLevelNotification: WaterLevelNotification

    override fun onCreate() {
        super.onCreate()
        database = FirebaseDatabase.getInstance()
        waterLevelNotification = WaterLevelNotification(this)
        // Menambahkan ValueEventListener untuk mengetahui prubshsn pada node REPTIL
        val reference = database.getReference("TEST/message")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val level = snapshot.value.toString()
                if (level != null){
                    waterLevelNotification.sendNotification(level)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, R.string.load_post_onCancelled.toString(),error.toException())
            }

        })
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}